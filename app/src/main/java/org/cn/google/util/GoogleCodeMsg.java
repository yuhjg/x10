package org.cn.google.util;

public class GoogleCodeMsg {
    public static String getResultMsg(int i) {
        switch (i) {
            case -3:
                return "请求超时";
            case -2:
                return "当前请求无法被google识别";
            case -1:
                return "已断开google服务";
            case 0:
                return "请求成功";
            case 1:
                return "取消操作";
            case 2:
                return "网络连接不可用";
            case 3:
                return "插件支付版本需更新";
            case 4:
                return "所要求的产品无法购买";
            case 5:
                return "无效参数";
            case 6:
                return "Google服务发生错误";
            case 7:
                return "上一单未完成";
            case 8:
                return "此ID暂未获取此应用，无法购买";
            default:
                return "Google Play 未知错误";
        }
    }

}
