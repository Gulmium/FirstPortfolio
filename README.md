# Aluka
在庫を管理し、座標とともに公開するAndroidアプリ
** **

## 本プロジェクトの動機

2025年7月時点、職業訓練校でJava・Servlet・JSPを中心に学んでいます。  
Webアプリの基礎は習得できたため技術の幅を広げるべく、未習得の技術を用いたプロジェクト制作に挑戦しました。

プロジェクト開始時は、将来のキャリア像が定まっていなかったこともあり、  
まずは自力でアプリを完成させることで、開発工程全体を経験することを目的としました。

## 使用技術

### 言語
- Python
- Kotlin

### 開発環境・ツール
- Android Studio（IDE）
- Docker

### バックエンド技術
- Django REST Framework
- MySQL

### 外部API
- Maps SDK for Android
** **

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
** **

### 使い方
ここから、本アプリケーションの使い方を説明していきます。
まず、seller1を登録し、店舗の座標と在庫を登録していきます。
### ログイン画面
![ログイン画面](https://github.com/Gulmium/FirstPortfolio/blob/master/Aluka/Android/seller1/02_login.png?raw=true)


### ログイン後のマイページ画面
sellerユーザーのマイページには、「店舗を追加」ボタンがあります。
![ログイン後画面](https://github.com/Gulmium/FirstPortfolio/blob/master/Aluka/Android/seller1/04_seller_activity.png?raw=true)
