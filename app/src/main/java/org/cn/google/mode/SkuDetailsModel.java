package org.cn.google.mode;

import android.content.Intent;

public class SkuDetailsModel {

    private int id;//": 6,
    private String title;//": "140 水晶优惠组-CA (王国纪元 (Lords Mobile))",
    private String money;//": "$0.99",
    private String gold;//": "",
    private String sku_Json;//": "{\"productId\":\"23438\",\"type\":\"inapp\",\"price\":\"US$0.99\",\"price_amount_micros\":990000,\"price_currency_code\":\"USD\",\"title\":\"140 水晶优惠组-CA (王国纪元 (Lords Mobile))\",\"description\":\"140 水晶优惠组-CA\",\"skuDetailsToken\":\"AEuhp4L_5ZQbTsLWkeLdw9dV0mQnhTXxYKZgdemWbou--mbFPL0rzC7jWM6EhhhyM15c\"}",
    private String productId;//": 23438,
    private String type;//": "inapp",
    private String price_currency_code;//": "USD",
    private String skuDetailsToken;//": "AEuhp4L_5ZQbTsLWkeLdw9dV0mQnhTXxYKZgdemWbou--mbFPL0rzC7jWM6EhhhyM15c",
    private String description;//": "140 水晶优惠组-CA"

    @Override
    public String toString() {
        return "SkuDetailsModel{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", money='" + money + '\'' +
                ", gold='" + gold + '\'' +
                ", sku_Json='" + sku_Json + '\'' +
                ", productId=" + productId +
                ", type='" + type + '\'' +
                ", price_currency_code='" + price_currency_code + '\'' +
                ", skuDetailsToken='" + skuDetailsToken + '\'' +
                ", description='" + description + '\'' +
                '}';
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getMoney() {
        return money;
    }

    public void setMoney(String money) {
        this.money = money;
    }

    public String getGold() {
        return gold;
    }

    public void setGold(String gold) {
        this.gold = gold;
    }

    public String getSku_Json() {
        return sku_Json;
    }

    public void setSku_Json(String sku_Json) {
        this.sku_Json = sku_Json;
    }

    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getPrice_currency_code() {
        return price_currency_code;
    }

    public void setPrice_currency_code(String price_currency_code) {
        this.price_currency_code = price_currency_code;
    }

    public String getSkuDetailsToken() {
        return skuDetailsToken;
    }

    public void setSkuDetailsToken(String skuDetailsToken) {
        this.skuDetailsToken = skuDetailsToken;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
