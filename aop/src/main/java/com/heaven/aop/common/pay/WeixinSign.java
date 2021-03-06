package com.heaven.aop.common.pay;

import com.google.gson.annotations.SerializedName;

/**
 * FileName: com.heaven.aop.common.pay.WeixinSign.java
 * author: Heaven
 * email: heavenisme@aliyun.com
 * date: 2019-08-31 16:22
 *
 * @version V1.0 TODO <描述当前版本功能>
 */
public class WeixinSign {

    /**
     * sign : 58C87A9AAAE8EE836A3BAF68A8913B4A
     * timestamp : 1567239657
     * partnerid : 1218208301
     * noncestr : bLry2tSvHS5c7Z0Q
     * prepayid : wx3116205689078492a8ace60c1890767700
     * package : Sign=WXPay
     * appid : wxd8bd97731f1fa499
     */

    private String sign;
    private String timestamp;
    private String partnerid;
    private String noncestr;
    private String prepayid;
    @SerializedName("package")
    private String packageX;
    private String appid;

    public String getSign() {
        return sign;
    }

    public void setSign(String sign) {
        this.sign = sign;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    public String getPartnerid() {
        return partnerid;
    }

    public void setPartnerid(String partnerid) {
        this.partnerid = partnerid;
    }

    public String getNoncestr() {
        return noncestr;
    }

    public void setNoncestr(String noncestr) {
        this.noncestr = noncestr;
    }

    public String getPrepayid() {
        return prepayid;
    }

    public void setPrepayid(String prepayid) {
        this.prepayid = prepayid;
    }

    public String getPackageX() {
        return packageX;
    }

    public void setPackageX(String packageX) {
        this.packageX = packageX;
    }

    public String getAppid() {
        return appid;
    }

    public void setAppid(String appid) {
        this.appid = appid;
    }
}
