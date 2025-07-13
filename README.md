# Aluka
在庫を管理し、座標とともに公開するAndroidアプリ


## 使用技術
- Android Studio
- Django REST Framework
- MySQL
- Docker



## ディレクトリ構成


### Android側
```
android-project/
└── app/
    └── src/
        └── main/
            └── java/com/example/aluka/
                ├── MainActivity.kt
                ├── ...（複数のActivity/Fragmentを省略）
                ├── data/...
                ├── network/...
                └── ui/
                    └── theme/...
```



### サーバー側(Django)
```
myproject/
├── manage.py
├── accounts/
│   ├── models.py
│   ├── views.py
│   ├── urls.py
│   └── serializer.py
├── store/
│   ├── models.py
│   ├── views.py
│   ├── urls.py
│   └── serializer.py
├── my_app/
│   ├── models.py
│   ├── views.py
│   ├── urls.py
│   └── serializer.py
├── myproject/
│   ├── settings.py
│   ├── urls.py
│   └── ...
└── ...（他の補助ファイルやキャッシュ類は省略）
```
構成はベストプラクティス通りです。



