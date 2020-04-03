from django.urls import path
from . import views
from . import get_campus
from . import get_comprehensive
from . import get_cooperation
from . import get_media
from . import get_notice
from . import get_recruitement
from . import get_teaching

urlpatterns = [
    path('', views.main_spider),
    path('comprehensive/', get_comprehensive.get_news),
    path('campus/', get_campus.get_news),
    path('cooperation/', get_cooperation.get_news),
    path('media/', get_media.get_news),
    path('notification/', get_notice.get_news),
    path('recruitment/', get_recruitement.get_news),
    path('teaching/', get_teaching.get_news)
]
