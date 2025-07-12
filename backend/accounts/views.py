from django.shortcuts import render, redirect
from django.contrib.auth import authenticate, views as auth_views
from django.views.generic.edit import CreateView
from django.views.generic import TemplateView
from django.contrib.auth.forms import UserCreationForm
from django.urls import reverse_lazy
from django.contrib.auth import login  
from django.contrib.auth.models import User, Group
from django.views import View
from django.http import HttpRequest, HttpResponse
from .serializer import UserSerializer,LoginSerializer
from .models import CustomUser
from rest_framework.views import APIView
from rest_framework.decorators import api_view
from rest_framework.response import Response
from rest_framework import status
from rest_framework.permissions import IsAuthenticated
from .models import CustomAuthenticationForm
from django.db import IntegrityError
from django.core.exceptions import ValidationError
from rest_framework_simplejwt.tokens import RefreshToken
from rest_framework import serializers
from rest_framework.viewsets import ReadOnlyModelViewSet
import json



class SignupView(APIView):
    def post(self, request):
        try:
            serializer = UserSerializer(data=request.data)
            if serializer.is_valid():
                user = CustomUser(
                    username=serializer.validated_data['username'],
                    role=serializer.validated_data['role'],
                    is_active=serializer.validated_data['is_active'],
                    is_staff=serializer.validated_data['is_staff'],
                    is_superuser=serializer.validated_data['is_superuser'],
                    last_login=serializer.validated_data.get('last_login', None)
                )
                user.set_password(serializer.validated_data['password'])
                user.save()
                return Response({"message": "user created successfully!"}, status=status.HTTP_201_CREATED)
            
            return Response(serializer.errors, status=status.HTTP_400_BAD_REQUEST)
        
        except IntegrityError as e:
            return Response({"error": "このユーザー名またはメールアドレスはすでに登録されています。"}, status=status.HTTP_400_BAD_REQUEST)
        
        except ValidationError as e:
            return Response({"error": "入力データが無効です。"}, status=status.HTTP_400_BAD_REQUEST)

        except Exception as e:
            return Response({"error": f"予期しないエラー: {str(e)}"}, status=status.HTTP_500_INTERNAL_SERVER_ERROR)



class LoginView(APIView):
    def post(self, request):
        serializer = LoginSerializer(data=request.data)
        if serializer.is_valid():
            username = serializer.validated_data['username']
            password = serializer.validated_data['password']

            user = authenticate(username=username, password=password)
            if user:
                refresh = RefreshToken.for_user(user)
                return Response({
                    "message": "ログイン成功",
                    "access": str(refresh.access_token),
                    "refresh": str(refresh)
                }, status=status.HTTP_200_OK)

            return Response({"error": "ユーザー名またはパスワードが間違っています。"}, status=status.HTTP_400_BAD_REQUEST)

        return Response(serializer.errors, status=status.HTTP_400_BAD_REQUEST)
    

class SignupSerializer(serializers.ModelSerializer):
    class Meta:
        model = CustomUser
        fields = ['username', 'password', 'role']
        extra_kwargs = {'password': {'write_only': True}}
 


class UserViewSet(ReadOnlyModelViewSet):
    serializer_class = UserSerializer
    permission_classes = [IsAuthenticated]

    def get_queryset(self):
        return CustomUser.objects.filter(id=self.request.user.id)




class UserDetailView(APIView):
    permission_classes = [IsAuthenticated]

    def get(self, request):
        serializer = UserSerializer(request.user)
        return Response(serializer.data)
    
    



class LogoutView(auth_views.LogoutView):
    next_page = reverse_lazy('accounts:login')
    def dispatch(self, request, *args, **kwargs):
        response = super().dispatch(request, *args, **kwargs)
        return redirect(reverse_lazy('accounts:login'))


class SuccessView(TemplateView):
    template_name = 'accounts/success.html'

success = SuccessView.as_view()

