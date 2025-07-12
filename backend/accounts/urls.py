from django.urls import path
from . import views
from .views import LoginView, SuccessView, LogoutView, SignupView, UserDetailView
from django.views.generic import TemplateView
from django.contrib.auth import views as auth_views
from django.urls import reverse_lazy

app_name = 'accounts' 

urlpatterns = [
    path('login/', LoginView.as_view(), name='login'),
    path('users/me/', UserDetailView.as_view(), name='user_detail'),
    path('logout/', LogoutView.as_view(), name='logout'),
    path('signup/', SignupView.as_view(), name='signup'),
    path('success/', SuccessView.as_view(), name='success'),
]