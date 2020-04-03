from django.db import models


# Create your models here.
class NewsList(models.Model):
    title = models.CharField(max_length=500)
    author = models.CharField(max_length=50)
    date = models.CharField(max_length=100)
    digest = models.TextField()  # summary of the passage
    content = models.TextField()  # content of the passage
    label = models.CharField(max_length=20)# the label of the passage
