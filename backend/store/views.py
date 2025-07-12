from django.shortcuts import render
from rest_framework.permissions import IsAuthenticated
from .serializer import StoreSerializer, AddStockSerializer, StockSerializer, SearchItemSerializer
from rest_framework.response import Response
from rest_framework.views import APIView
from rest_framework import status
from .models import Item, Stock, Store
import logging


logger = logging.getLogger(__name__)


# Create your views here.

class StoresDetailView(APIView):
    permission_classes = [IsAuthenticated]

    def get(self, request):
        serializer = StoreSerializer(request.user.stores.all(), many=True)
        return Response(serializer.data, status=status.HTTP_200_OK)


class getStocksView(APIView):
    permission_classes = [IsAuthenticated]

    def get(self, request, store_id):
        try:
            store = request.user.stores.get(id=store_id)
            items = store.stocks.all()
            serializer = StockSerializer(items, many=True)
            return Response(serializer.data, status=status.HTTP_200_OK)
        except Store.DoesNotExist:
            return Response({"detail": "Store not found"}, status=404)
        except Exception as e:
            return Response({"error": str(e)}, status=500)


class addStoreView(APIView):
    permission_classes = [IsAuthenticated]
    def post(self, request):
        serializer = StoreSerializer(data=request.data)
        if serializer.is_valid():
            serializer.save(seller=request.user)
            return Response(serializer.data, status=status.HTTP_201_CREATED)
        

class checkItemView(APIView):
    permission_classes = [IsAuthenticated]

    def get(self,request, item_number):
        if item_number is None:
            return Response({"error": "item_number is required"}, status=status.HTTP_400_BAD_REQUEST)

        try:
            item = Item.objects.get(item_number=item_number)
            return Response({"result": True, "item_name": item.item_name, "item_type": item.item_type})
        except Item.DoesNotExist:
            return Response({"result": False, "item_name": None, "item_type": None})


class addStockView(APIView):
    permission_classes = [IsAuthenticated]

    def post(self, request):
        data = request.data.copy()
        item_number = data.get("item", {}).get("item_number")

        # 商品の存在確認
        item_qs = Item.objects.filter(item_number=item_number)
        if item_qs.exists():
            # 商品が既に存在する場合は手動で処理（バリデーション回避）
            item = item_qs.first()
            store_id = data.get("store")
            quantity = data.get("quantity")

            if store_id is None or quantity is None:
                return Response(
                    {"error": "store と quantity は必須です。"},
                    status=status.HTTP_400_BAD_REQUEST
                )

            # store を取得
            try:
                store = Store.objects.get(id=store_id)
            except Store.DoesNotExist:
                return Response({"error": "指定された store が存在しません。"}, status=status.HTTP_400_BAD_REQUEST)

            # 在庫確認と処理
            stock_qs = Stock.objects.filter(store=store, item=item)
            if stock_qs.exists():
                stock = stock_qs.first()
                stock.quantity += int(quantity)
                stock.save()
                message = "Stock updated successfully"
            else:
                stock = Stock(store=store, item=item, quantity=quantity)
                stock.save()
                message = "Stock added successfully"

            return Response({"message": message}, status=status.HTTP_201_CREATED)

        else:
            # 商品が存在しない場合は serializer を通して一括処理
            serializer = AddStockSerializer(data=data)
            if serializer.is_valid():
                store = serializer.validated_data["store"]
                item_data = serializer.validated_data["item"]
                quantity = serializer.validated_data["quantity"]

                # item 作成
                item = Item.objects.create(
                    item_number=item_data["item_number"],
                    item_name=item_data["item_name"],
                    item_type=item_data["item_type"]
                )

                stock = Stock.objects.create(store=store, item=item, quantity=quantity)
                return Response({"message": "Item and stock added successfully"}, status=status.HTTP_201_CREATED)

            else:
                logger.warning(f"Validation errors: {serializer.errors}")
                return Response(serializer.errors, status=status.HTTP_400_BAD_REQUEST)

class searchItemView(APIView):
    permission_classes = [IsAuthenticated]

    def post(self, request):
        serializer = SearchItemSerializer(data=request.data)
        if serializer.is_valid():
            itemname = serializer.validated_data['item_name']
            mylat = float(serializer.validated_data['lat'])
            mylng = float(serializer.validated_data['lng'])

            item_qs = Stock.objects.filter(item__item_name__iexact=itemname)
            if item_qs.exists():
                nearest_store = None
                distance = float('inf')
                store_data = [nearest_store, distance]
                for stock in item_qs:
                    store = stock.store
                    comparison = (mylat - store.lat) ** 2 + (mylng - store.lng) ** 2
                    if distance > comparison:
                        distance = comparison
                        nearest_store = store
                        store_data = [nearest_store, distance]
                    else:
                        continue
                
                serializer = StoreSerializer(store_data[0])
                return Response({"store": serializer.data}, status=status.HTTP_200_OK)
            else:
                return Response({"message": "Item not found"}, status=status.HTTP_404_NOT_FOUND)
        else:
            return Response(serializer.errors, status=status.HTTP_400_BAD_REQUEST)