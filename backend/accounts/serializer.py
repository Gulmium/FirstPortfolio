from rest_framework import serializers
from .models import CustomUser


class UserSerializer(serializers.ModelSerializer):
	class Meta:
		model = CustomUser
		fields = ['id','password','username','role','is_active','is_staff','is_superuser','last_login']

		extra_kwargs = {'password': {'write_only': True}}


class LoginSerializer(serializers.Serializer):
	username = serializers.CharField()
	password = serializers.CharField(write_only=True)


