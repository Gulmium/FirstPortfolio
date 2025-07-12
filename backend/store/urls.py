from django.urls import path
from . import views
from .views import StoresDetailView, addStockView, searchItemView
from django.views.generic import TemplateView
from django.contrib.auth import views as auth_views
from django.urls import reverse_lazy

app_name = 'store'

urlpatterns = [
    path('mine/', StoresDetailView.as_view(), name='store_detail'),
    path('<int:store_id>/stock/', views.getStocksView.as_view(), name='get_stocks'),
    path('add/', views.addStoreView.as_view(), name='add_store'),
    path('check_item/<item_number>/', views.checkItemView.as_view(), name='check_item'),
    path('add_stock/', views.addStockView.as_view(), name='add_stock'),
    path('search/item/', views.searchItemView.as_view(), name='search_item'),
]

