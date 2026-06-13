package org.cn.google.mode;

public class ProductDetails {

    private String productId;//": '1',
    private String jsonPurchaseInfo;//": {"orderId":"GPA.3315-6563-0607-34401","packageName":"com.igg.android.lordsmobile","productId":"23438","purchaseTime":1616596172087,"purchaseState":0,"purchaseToken":"hnlhndnbjpmaphckfgkipgie.AO-J1OxQ5o2fQvs5QfgtRP2ZPJ8vWupovB8LiAM91Mkas5nCiIl0SZZvjCyFkLa1RQOvof-5Ey8ZZE5bk1yNzB2afp6p6xG0TvyyNuvmarbUo0gB31pmij8","acknowledged":false},
    private String mSignature;//": '1',

//     "productId": "23438",
//                    "jsonPurchaseInfo": "{\"orderId\":\"GPA.3342-5687-8435-71371\",\"packageName\":\"com.igg.android.lordsmobile\",\"productId\":\"23438\",\"purchaseTime\":1616921634680,\"purchaseState\":0,\"purchaseToken\":\"epgmnaplpgpofdjkokifedhd.AO-J1OwUvepx6N8Pch-2-pzewYfB8uAwnOKTkSBvgjxxIwQ5CDCeosiq3VYn_F8_LGaXDPfksL33fpeXhF2UzH1jQZs-2wnwb-nIg2-V2O2HyyiZQaojocE\",\"acknowledged\":false}",
//                    "mSignature": "MmX+GKNjHsLZy0yV9pY/j/Cx/opJGFjheHDHd6HmoS10ZMC02W5FgcrRvrfFj5/3dEUL+yswQzVGsxYXXf+OK12TvJb1McWcixMKc3uPBwWeT6Z96YVOTVHH7QLXWqIJ7ecZyxHSYh9KLCNnE0fXuxyEpei0EiPoLzbi65a+LiT4/VaHsglg8+huGkUVYZ7ak+YS6Hax51r6RXh4u817ndfJtshQ/9f0eD2+E5PdoWdT/GI0BMy2j3EEN6AU9jZNF2EsV2pn3UKWvdGpe2kYJm2h6+REN+sdatnfEKUByQRJi5pzCfoGC0nHRPLub/bsERTuChfoA5mQohrfERb7Qg=="

    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }

    public String getJsonPurchaseInfo() {
        return jsonPurchaseInfo;
    }

    public void setJsonPurchaseInfo(String jsonPurchaseInfo) {
        this.jsonPurchaseInfo = jsonPurchaseInfo;
    }

    public String getmSignature() {
        return mSignature;
    }

    public void setmSignature(String mSignature) {
        this.mSignature = mSignature;
    }
}
