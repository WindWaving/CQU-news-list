# CQUnews
Android client for CQU news

# Development and Test Environment
> Windows 10
> Server: Python3.7 + Django 3.0.4
> Server Service: Pythonanywhere
> Client: SDK29
# Dependencies
- Client:
> 'com.android.support:design:28.0.0'  --for tabLayout
> 'com.android.support:viewpager:28.0.0' --for viewPager
> Baidu translation API(don't have to install yourself)
- Server packages:
> requests 2.23.0
> django 3.0.4
> lxml 4.5.0
> mysqlclient 1.4.6
# Table of contents
- [To-Use](#touse)
- [To-Test](#test)
- [Installation(for developers)](#install)
- [Done and Todo](#todo)
- [Contract](#Contract)
- [Bug Solution](#bug)

## <span id="touse">To-Use</span>
You can just download the apk we provided for you. 
Without expectations, it should work well like this:
![](https://pic.images.ac.cn/image/5e8835564aefe)
## <span id="test">To-Test</span>
Of course you can test the application directly, but I'm suggesting you using a local server to host, mine is hosted on `pythonanywhere`, it's a little slow when booted or updated.
## <span id="install">Installation</span>
#### First, let's begin with the directories:
- Client folder:
> CQUnews-android
- Server folders:
> -newsList
> ---index
> ---newsList
> -manage.py
> -virtualEnv

#### To install the server:
You can deploy on your server or a local one, when you do this, please do as following:
1.  Gather the server folders in your IDE
2.  Prepare your mysql database on your server or on your computer, you should create a database named 'newsdb'.
3. Before running the server, you have to modify some codes:
- newsList/settings.py:
   `DATABASES `
![](https://pic.images.ac.cn/image/5e8839145bd9d)
4. Enter the `VirtualEnv/Scripts` to activate the virtual environment. 
5. Get into the folder(`newsList`) with `manage.py` inside it, use `python manage.py make migrations`,`python manage.py migrate` in your terminal
6. Create superuser to avoid fill in the username and password all the time with `python manage.py createsuperuser`(this has to be the same in your settings.py/DATABASE)
6. Run `python runserver 0.0.0.0:8780(or other port) --noreload` in terminal
7. Then you can visit `http://localhost:8780/spider` to start crawling news from Cqu news website.(I haven't made it automatically, so you have to open the page yourself, when you see 'everything is ok', you can check your database now)

#### To install the client:
1. You can install the .apk file in your phone or emulator, or build `CQUnews-android` in your IDE
2. If you deploy the server yourself, you can modify some codes to test your server:
 `NewsHttpUtil.java`--`public static JSONArray reqNews`, modify the `baseUrl` to your server IP, or if you are using a local server, change it to your emulator/phone IP (use ipconfig, not the localhost or 127.0.0.1)
 for local server: ![](https://pic.images.ac.cn/image/5e88398f65cd7)
 for remote server: ![](https://pic.images.ac.cn/image/5e887fbaee08e)


## <span id="todo">Done and Todo</span>
Done:
>1. pull the news from CQU news website: http://news.cqu.edu.cn/newsv2/
>2. classify news in application
>3. update news manually        System.out.println("init done");
>4. translate between Chinese and English(the call of Baidu Translation API is limited with free users, both the speed and amount, so I'm not sure whether this function will be kept in future.)

To do:
>1. transfer the project on another server, it's using 'pythonanywhere' right now, but I found that the platform doesn't support crawling, so I'm manually changing the cloud database right now.
>2. fix the bug (some fragments mix up) when updating news
>3. automatically update every day

## <span id="bug">Bug Solution</span>
If you have trouble with the App, click 'update' in the menu on the right top corner,or just restart it.
## <span id="Contract">Contract</span>
Welcome to give me your feedback here or email me at 13833799573@163.com