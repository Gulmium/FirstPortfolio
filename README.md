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
ボタンを押して店舗を追加してみましょう。
![ログイン後画面](https://github.com/Gulmium/FirstPortfolio/blob/master/Aluka/Android/seller1/04_seller_activity.png?raw=true)


### 店舗追加画面
「店舗を追加」ボタンを押すと、地図が開き現在地が表示されます。
任意の場所に店舗を追加し、名前を付けて保存します。
今回は、北千住駅に[seller1_store]を作ります。
![店舗追加画面](https://github.com/Gulmium/FirstPortfolio/blob/master/Aluka/Android/seller1/06_add_store.png?raw=true)


### 店舗追加後のマイページ
マイページに戻ると店舗が追加されているのが確認できます。
![ログイン後画面](https://github.com/Gulmium/FirstPortfolio/blob/master/Aluka/Android/seller1/07_seller_activity.png?raw=true)


### 商品追加カメラ
スクリーンショットを取り忘れてしまいましたが、店舗をタップすると今度は「商品を追加」ボタンがあります。
それを押すとカメラが立ち上がり、Janコードを読むと「商品を登録するか」と聞いてきます。
![カメラ画面](https://github.com/Gulmium/FirstPortfolio/blob/master/Aluka/Android/seller1/09_camera.png?raw=true)


### 商品追加の画面
「はい」を押すと、商品を追加できます。
先ほど「データベースにない」と書かれていました。どのユーザーも登録したことがない商品については、商品名も書かなければなりません。
一番上のJanコードは編集不可です。
商品名、商品の分類、個数はユーザーが入力します。
![商品追加画面](https://github.com/Gulmium/FirstPortfolio/blob/master/Aluka/Android/seller1/11_add_stock.png?raw=true)


### 商品追加後のseller1_store
先ほどの手順でいくつか商品を追加してみました。
これが「seller1」が持つ「seller1_store」が持つ在庫一覧です。
![商品追加後画面](https://github.com/Gulmium/FirstPortfolio/blob/master/Aluka/Android/seller1/13_stock_list.png?raw=true)


### seller2のseller2_storeの在庫登録
細かい説明は割愛しますが、同じ手順で比較用のseller2の、seller2_storeと在庫を登録していきます。
店舗2の座標は南千住駅です。

![店舗追加画面2](https://github.com/Gulmium/FirstPortfolio/blob/master/Aluka/Android/seller2/14_add_store2.png?raw=true)

![商品追加後画面2](https://github.com/Gulmium/FirstPortfolio/blob/master/Aluka/Android/seller2/15_stock_list.png?raw=true)


### buyer登録
次は買い手、buyerの登録です。
![サインアップ画面](https://github.com/Gulmium/FirstPortfolio/blob/master/Aluka/Android/buyer/17_signup_buyer.png?raw=true)


### 商品検索画面
buyerのマイページは商品検索の機能しかありません。任意の商品がある、一番近くの店舗を教えてくれるというシンプルな機能です。
北千住駅にある「seller1_store」にも、南千住駅にある「seller2_store」にもある「アーモンドチョコレート」を検索してみたいと思います。
私は北千住駅よりもっと北に住んでいるので、うまくいけば「seller1_store」が出てくるはずです。
![商品検索画面](https://github.com/Gulmium/FirstPortfolio/blob/master/Aluka/Android/buyer/19_search_almond.png?raw=true)


### アーモンドチョコレートの検索結果
実験成功です。画面下部にトーストで店舗名も表示されます。
![検索結果画面](https://github.com/Gulmium/FirstPortfolio/blob/master/Aluka/Android/buyer/20_search_almond_result.png?raw=true)


### アーモンドクランチの検索
次に、seller2_storeにしかないアーモンドクランチを検索してみます。
![検索画面](https://github.com/Gulmium/FirstPortfolio/blob/master/Aluka/Android/buyer/21_search_crunch.png?raw=true)


### アーモンドクランチの検索結果
seller2_storeとその座標が無事表示されました。
![検索画面](https://github.com/Gulmium/FirstPortfolio/blob/master/Aluka/Android/buyer/22_search_crunch_result.png?raw=true)


### まとめ
以上が、このアプリの機能となります。
ご覧いただきありがとうございました。
