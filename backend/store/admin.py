from django.contrib import admin
from .models import Store, Stock, Item

# Register your models here.
admin.site.register(Store)
admin.site.register(Stock)
admin.site.register(Item)