from django.db import models
from django.contrib.auth.models import AbstractBaseUser,  BaseUserManager


# Create your models here.



    

class CustomUserManager(BaseUserManager):

    def create_user(self, username, password=None, **extra_fields):
        if not username:
            raise ValueError("Username is required")
        
        extra_fields.setdefault("role", 'buyer')
        extra_fields.setdefault("is_active", True)

        print(extra_fields)

        user = self.model(username=username, **extra_fields)

        user.set_password(password)
        user.save(using=self._db)
        return user

    def create_superuser(self, username, password, **extra_fields):
        extra_fields.setdefault("is_staff", True)
        extra_fields.setdefault("is_superuser", True)
        extra_fields.setdefault("role", 'admin')
        return self.create_user(username=username, password=password, **extra_fields)
    

    def save(self, *args, **kwargs):
        if not self.is_superuser and self.role == "admin":
            raise ValueError("一般ユーザーは 'admin' になれません！")
        super().save(*args, **kwargs)


class CustomUser(AbstractBaseUser):
    id = models.BigAutoField(primary_key=True)
    username = models.CharField(max_length=150, unique=True)

    role = models.CharField(
        max_length=10,
        choices=[('buyer', 'Buyer'), ('seller', 'Seller'), ('admin', 'Admin')],
        default='buyer'
    )

    is_active = models.BooleanField(default=True)
    is_staff = models.BooleanField(default=False)
    is_superuser = models.BooleanField(default=False) 
    last_login = models.DateTimeField(null=True, blank=True) 

    USERNAME_FIELD = 'username'
    
    REQUIRED_FIELDS = []  # emailを削除

    objects = CustomUserManager()

    def has_perm(self, perm, obj=None):
        return self.is_superuser

    def has_module_perms(self, app_label):
        return self.is_superuser


from django.contrib.auth.forms import AuthenticationForm

class CustomAuthenticationForm(AuthenticationForm):
    class Meta:
        model = CustomUser