from . import models
import json
from django.core import serializers
from django.http import JsonResponse

''''
func: get news labeled 'comprehensive'
@return: data in json:
        [{"model":"xxx", "pk":xxx,"fields":{"title":"xxx", "author":"xxx", "date":"xxx", "digest":"xxx", "content":"xxx","label":"xx"}},
        {...},{...},...
        ]
'''
def get_news(self):
    reslist=models.NewsList.objects.filter(label='招生就业')
    if reslist.count()>0:
        json_res=serializers.serialize('json',reslist)
        json_res=json.loads(json_res)
        res=JsonResponse(json_res,safe=False,json_dumps_params={'ensure_ascii':False})
        return res
