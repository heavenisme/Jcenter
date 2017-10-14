package com.heaven.model.ifilm;

/**
 * Created by LeoLu on 2017/1/19.
 */

public class WxAccessToken {


    /**
     * access_token : uW7TgnMiPnwgV4T3xopO58rRNVYArWItQGXAlnElODG87w2jAs4uN6JPnE6HQSdZlbj7Rj_IxhSlom5Te4X-eLd-DGyp30Le8YTqnzdRS-s
     * expires_in : 7200
     * refresh_token : arBK1iSSFDmF-HJCQVA-zFruqXrvZUF6WYJoBFKs1vlNIpGOqzdVUPeb4cLiUrG9ntwtwUHhc_DSbjRUksxHQMvaFo1-ZTqI7On5iKno9DM
     * openid : o9nsDwASyJZh4akRCSUiayJOuZdI
     * scope : snsapi_userinfo
     * unionid : oDC81wxAcUHgUx4flm-hvkHdw7cI
     */

    private String access_token;
    private int expires_in;
    private String refresh_token;
    private String openid;
    private String scope;
    private String unionid;

    public String getAccess_token() {
        return access_token;
    }

    public void setAccess_token(String access_token) {
        this.access_token = access_token;
    }

    public int getExpires_in() {
        return expires_in;
    }

    public void setExpires_in(int expires_in) {
        this.expires_in = expires_in;
    }

    public String getRefresh_token() {
        return refresh_token;
    }

    public void setRefresh_token(String refresh_token) {
        this.refresh_token = refresh_token;
    }

    public String getOpenid() {
        return openid;
    }

    public void setOpenid(String openid) {
        this.openid = openid;
    }

    public String getScope() {
        return scope;
    }

    public void setScope(String scope) {
        this.scope = scope;
    }

    public String getUnionid() {
        return unionid;
    }

    public void setUnionid(String unionid) {
        this.unionid = unionid;
    }
}
