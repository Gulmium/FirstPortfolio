from django.db import models

from django.core.exceptions import ValidationError
from django.contrib.auth.models import AbstractBaseUser, BaseUserManager
from accounts.models import CustomUser

# Create your models here.


class Item(models.Model):
    item_number = models.CharField(max_length=13, unique=True,null=False)
    item_name = models.CharField(max_length=255, unique=True,null=False)
    item_type = models.CharField(max_length=50, choices=[('product', 'Product'), ('book', 'Book')])


class Store(models.Model):
    storename = models.CharField(max_length=255)
    lat = models.FloatField()
    lng = models.FloatField()

    seller = models.ForeignKey(CustomUser,
                               null=False,
                               on_delete=models.CASCADE,
                               related_name='stores'
                               )
    def clean(self):
        if self.seller.role != 'seller':
            raise ValidationError("Only sellers can create stores.")



class Stock(models.Model):
    store = models.ForeignKey(Store, on_delete=models.CASCADE, related_name='stocks', null=False)
    item = models.ForeignKey(Item, on_delete=models.CASCADE, related_name='stocks', null=False)
    quantity = models.IntegerField(null=False)

    class Meta:
        constraints = [
            models.UniqueConstraint(
                fields=['store', 'item'],
                name='unique_store_item'
            )
        ]


