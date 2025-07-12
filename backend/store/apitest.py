import requests
import json

API_URL = "https://shopping.yahooapis.jp/ShoppingWebService/V3/itemSearch"
API_KEY = "dj00aiZpPWpHc0Q2OVR2c1NhUCZzPWNvbnN1bWVyc2VjcmV0Jng9NDM-"  # APIキーを確認してください
JAN_CODE = "4901330577921"

params = {
    "appid": API_KEY,
    "query": JAN_CODE
}

response = requests.get(API_URL, params=params)

# ステータスコードとレスポンスの内容を確認
print("ステータスコード:", response.status_code)
print("レスポンスデータ:", response.text)

if response.status_code == 200:
    try:
        data = response.json()
        print("JSONデータ:", data)  # JSONデータを確認
        if "hits" in data:
            for item in data["hits"]:
                print("商品名:", item.get("name", "商品情報なし"))  # 商品名の表示
        else:
            print("商品情報が見つかりませんでした。")
    except json.JSONDecodeError:
        print("JSONの解析に失敗しました。")
else:
    print("APIリクエストに失敗しました:", response.status_code)


print("this file was executed successfully")