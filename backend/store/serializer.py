from rest_framework import serializers
from .models import Store, Stock, Item

class StoreSerializer(serializers.ModelSerializer):
    class Meta:
        model = Store
        fields = ['id','storename', 'lat', 'lng']


class ItemSerializer(serializers.ModelSerializer):
    class Meta:
        model = Item
        fields = ['id','item_number', 'item_name', 'item_type']

class StockSerializer(serializers.ModelSerializer):
    store = StoreSerializer(read_only=True)
    item = ItemSerializer(read_only=True)

    class Meta:
        model = Stock
        fields = ['id','store', 'item', 'quantity']

class AddStockSerializer(serializers.ModelSerializer):
    store = serializers.PrimaryKeyRelatedField(queryset=Store.objects.all())
    item = ItemSerializer()

    class Meta:
        model = Stock
        fields = ['id', 'store', 'item', 'quantity']

    def create(self, validated_data):
        item_data = validated_data.pop('item')
        item, _ = Item.objects.get_or_create(
            item_number=item_data['item_number'],
            defaults={
                'item_name': item_data['item_name'],
                'item_type': item_data['item_type']
            }
        )
        stock, created = Stock.objects.get_or_create(
            store=validated_data['store'],
            item=item,
            defaults={'quantity': validated_data['quantity']}
        )
        if not created:
            # 在庫がすでにあるなら数量を加算
            stock.quantity += int(validated_data['quantity'])
            stock.save()

        return stock


class SearchItemSerializer(serializers.Serializer):
    item_name = serializers.CharField(required=True)
    lat = serializers.FloatField(required=True)
    lng = serializers.FloatField(required=True)
