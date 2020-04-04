from django.shortcuts import HttpResponse
from lxml import html
import requests
import re
from multiprocessing.dummy import Pool

# Create your views here.
from . import models

'''
'''
def main_spider(self):
    headers = {
        'User-Agent': 'Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/80.0.3987.149 Safari/537.36',
    }
    url = 'http://news.cqu.edu.cn/newsv2/'
    spider = NewsSpider(url, headers)
    labellist = spider.get_labels()  # get all the labels
    for label in labellist:
        pool = Pool(4)  # multiple process
        itemlist = spider.get_by_label(label, 3)  # links of news as list
        pool.map(spider.get_news_content, itemlist)  # get news content
        pool.close()
        pool.join()
    return HttpResponse('everything is ok')


class NewsSpider:
    ''''
    @param:
        url: the home page address of cqu news
        headers: to set the UA
    '''

    def __init__(self, url, headers):
        self.homepage = requests.get(url, headers=headers).content.decode()  # visit the home page to get html content
        self.headers = headers  # imitate the headers of browser
        self.cnt = 0  # not important for test, the total numbers of news

    '''
    func: get html of the url
    @param: 
        url: address of page
    @return: html
    '''

    def get_page(self, url):
        page = ''
        try:
            page = requests.get(url, headers=self.headers).content.decode()
        except Exception:
            print('page not found')
        return page

    '''
    funct: get all the labels on homepage
        a label: tuple(link,name)
    @return: 
        the list of all the labels
    '''

    def get_labels(self):
        labellist = []
        try:
            lis = html.etree.HTML(self.homepage).xpath('//ul[@class="nav"]/li[@class="shide"]')
            labellist = []
            for i in range(len(lis)):
                if i > 0:
                    li = lis[i]
                    href = li.xpath('a/@href')[0]
                    text = li.xpath('a/text()')[0]
                    item = (href, text)
                    labellist.append(item)
        except Exception:
            print('labels not found')
        return labellist

    '''
    func: get latest 100 news from a label
    @param: 
        label_item: a tuple(link,name) of a label
    @return:
        the 100 links of news as list
    '''

    def get_by_label(self, label_item, max_news):
        # print(label_item)
        itemlist = []  # the links of news
        try:
            page = self.get_page(label_item[0])
            div = html.etree.HTML(page).xpath('//div[@class="page"]')[0]
            total_news_cnts = div.xpath('a[@class="a1"]/text()')[0]
            total_news_cnts = int(re.findall('\d+', total_news_cnts)[0])  # the numbers of news
            page_urls = [label_item[0]]  # urls of each page
            tmp = len(div.xpath('a'))
            for i in range(tmp):
                if 2 <= i <= tmp - 2:
                    page_urls.append(div.xpath('a')[i].xpath('@href')[0])
            _min = min(max_news, total_news_cnts)  # get at most 100 news
            item_cnt = 0
            for cur_page in page_urls:
                page = self.get_page(cur_page)
                cur_news_cnts = len(
                    html.etree.HTML(page).xpath('//div[@class="item"]'))  # the numbers of news in current page
                for item in range(cur_news_cnts):
                    if item_cnt <= max_news:
                        div = html.etree.HTML(page).xpath('//div[@class="item"]')[item]
                        if len(div.xpath('div[@class="content"]')) == 0:
                            link = re.findall('<a href="(.*?)">', str(html.tostring(div)))[0]
                        else:
                            link = div.xpath('div[@class="content"]/div[@class="title"]/a[last()]/@href')[0]
                        itemlist.append(link)
                        item_cnt = item_cnt + 1
                    else:
                        return itemlist
        except Exception:
            print('news not found')
        return itemlist

    '''
    func: get a piece of news
    @param: link of the news
    @return: the data structure of a passage
    '''

    def get_news_content(self, link):
        try:
            page = self.get_page(link)
            data = {'title': '', 'author': '', 'label': '', 'date': '', 'digest': '', 'content': ''}
            data['title'] = html.etree.HTML(page).xpath('//h1[@class="dtitle"]/text()')[0]
            data['title']=data['title'].strip()
            data['author'] = html.etree.HTML(page).xpath('//div[@class="dinfo"]/div[@class="dinfoa"]')[0].xpath(
                'string(.)')
            data['author']=data['author'].strip()
            data['label'] = html.etree.HTML(page).xpath(('//div[@class="dnav"]/a[last()]/text()'))[0]
            data['label'] = data['label'].strip()  # some labels are wraped with \n or space
            data['date'] = html.etree.HTML(page).xpath('//div[@class="dinfob"]/div[@class="ibox"]/span[last()]/text()')[
                0]
            data['date']=data['date'].strip()
            data['digest'] = html.etree.HTML(page).xpath('//div[@class="abstract"]/div[@class="adetail"]/text()')[0]
            data['digest']=data['digest'].strip()
            data['content'] = html.etree.HTML(page).xpath('//div[@class="acontent"]')[0].xpath('string(.)')
            data['content']=data['content'].strip()
        except Exception as e:
            print('fail')
        else:
            self.cnt = self.cnt + 1
            print(self.cnt)
            self.save_db(data)

    def save_db(self, data):
        cur_news = models.NewsList.objects.filter(title=data['title'])
        if cur_news.count() == 0:
            news_item = models.NewsList()
            news_item.title = data['title']
            news_item.author = data['author']
            news_item.date = data['date']
            news_item.digest = data['digest']
            news_item.content = data['content']
            news_item.label = data['label']
            news_item.save()
