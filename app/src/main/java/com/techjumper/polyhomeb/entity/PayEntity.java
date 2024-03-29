package com.techjumper.polyhomeb.entity;

import com.google.gson.annotations.SerializedName;

/**
 * * * * * * * * * * * * * * * * * * * * * * *
 * Created by lixin
 * Date: 2016/11/3
 * * * * * * * * * * * * * * * * * * * * * * *
 **/

public class PayEntity {


    /**
     * method : pay
     * params : {"url":{"type":2,"back_type":"","alipay":{"parms_str":"app_id=2016100902063373&biz_content=%7B%22body%22%3A%22%E5%95%86%E5%9F%8E%E6%94%AF%E4%BB%98%22%2C%22extra_common_param%22%3A8%2C%22out_trade_no%22%3A%223014781507436213%22%2C%22product_code%22%3A%22QUICK_MSECURITY_PAY%22%2C%22subject%22%3A%22%E5%95%86%E5%9F%8E%E6%94%AF%E4%BB%98%22%2C%22timeout_express%22%3A%2230m%22%2C%22total_amount%22%3A%220.01%22%7D&charset=utf-8&method=alipay.trade.app.pay&notify_url=http%3A%2F%2Fpoly.techjumper.com%2Fadmin%2Falipay_notify&sign_type=RSA&timestamp=2016-11-03%2013%3A25%3A44&version=1.0","sign":"ZhdxNPYFxr2TbSwrZPkMgp73qk3wOSXFLucfhcaSyXM+yO0H6T8TApvSQT6fsbQjYq8KHAntJfuhdWEkPSkanjHWcdcs2Z0ewH0nT4xfu8LJ8mXt4ED/LXrLgEVDAgS+3UBEH37hS1hMqBzC9XexFV5OZUlKCKjuvtJMGCbcutE="}}}
     */

    private String method;
    /**
     * url : {"type":2,"back_type":"","alipay":{"parms_str":"app_id=2016100902063373&biz_content=%7B%22body%22%3A%22%E5%95%86%E5%9F%8E%E6%94%AF%E4%BB%98%22%2C%22extra_common_param%22%3A8%2C%22out_trade_no%22%3A%223014781507436213%22%2C%22product_code%22%3A%22QUICK_MSECURITY_PAY%22%2C%22subject%22%3A%22%E5%95%86%E5%9F%8E%E6%94%AF%E4%BB%98%22%2C%22timeout_express%22%3A%2230m%22%2C%22total_amount%22%3A%220.01%22%7D&charset=utf-8&method=alipay.trade.app.pay&notify_url=http%3A%2F%2Fpoly.techjumper.com%2Fadmin%2Falipay_notify&sign_type=RSA&timestamp=2016-11-03%2013%3A25%3A44&version=1.0","sign":"ZhdxNPYFxr2TbSwrZPkMgp73qk3wOSXFLucfhcaSyXM+yO0H6T8TApvSQT6fsbQjYq8KHAntJfuhdWEkPSkanjHWcdcs2Z0ewH0nT4xfu8LJ8mXt4ED/LXrLgEVDAgS+3UBEH37hS1hMqBzC9XexFV5OZUlKCKjuvtJMGCbcutE="}}
     */

    private ParamsBean params;

    public String getMethod() {
        return method;
    }

    public void setMethod(String method) {
        this.method = method;
    }

    public ParamsBean getParams() {
        return params;
    }

    public void setParams(ParamsBean params) {
        this.params = params;
    }

    public static class ParamsBean {
        /**
         * type : 2
         * back_type :
         * alipay : {"parms_str":"app_id=2016100902063373&biz_content=%7B%22body%22%3A%22%E5%95%86%E5%9F%8E%E6%94%AF%E4%BB%98%22%2C%22extra_common_param%22%3A8%2C%22out_trade_no%22%3A%223014781507436213%22%2C%22product_code%22%3A%22QUICK_MSECURITY_PAY%22%2C%22subject%22%3A%22%E5%95%86%E5%9F%8E%E6%94%AF%E4%BB%98%22%2C%22timeout_express%22%3A%2230m%22%2C%22total_amount%22%3A%220.01%22%7D&charset=utf-8&method=alipay.trade.app.pay&notify_url=http%3A%2F%2Fpoly.techjumper.com%2Fadmin%2Falipay_notify&sign_type=RSA&timestamp=2016-11-03%2013%3A25%3A44&version=1.0","sign":"ZhdxNPYFxr2TbSwrZPkMgp73qk3wOSXFLucfhcaSyXM+yO0H6T8TApvSQT6fsbQjYq8KHAntJfuhdWEkPSkanjHWcdcs2Z0ewH0nT4xfu8LJ8mXt4ED/LXrLgEVDAgS+3UBEH37hS1hMqBzC9XexFV5OZUlKCKjuvtJMGCbcutE="}
         */

        private UrlBean url;

        public UrlBean getUrl() {
            return url;
        }

        public void setUrl(UrlBean url) {
            this.url = url;
        }

        public static class UrlBean {
            private int type;
            private String back_type;
            private String order_number;
            /**
             * parms_str : app_id=2016100902063373&biz_content=%7B%22body%22%3A%22%E5%95%86%E5%9F%8E%E6%94%AF%E4%BB%98%22%2C%22extra_common_param%22%3A8%2C%22out_trade_no%22%3A%223014781507436213%22%2C%22product_code%22%3A%22QUICK_MSECURITY_PAY%22%2C%22subject%22%3A%22%E5%95%86%E5%9F%8E%E6%94%AF%E4%BB%98%22%2C%22timeout_express%22%3A%2230m%22%2C%22total_amount%22%3A%220.01%22%7D&charset=utf-8&method=alipay.trade.app.pay&notify_url=http%3A%2F%2Fpoly.techjumper.com%2Fadmin%2Falipay_notify&sign_type=RSA&timestamp=2016-11-03%2013%3A25%3A44&version=1.0
             * sign : ZhdxNPYFxr2TbSwrZPkMgp73qk3wOSXFLucfhcaSyXM+yO0H6T8TApvSQT6fsbQjYq8KHAntJfuhdWEkPSkanjHWcdcs2Z0ewH0nT4xfu8LJ8mXt4ED/LXrLgEVDAgS+3UBEH37hS1hMqBzC9XexFV5OZUlKCKjuvtJMGCbcutE=
             */

            private AlipayBean alipay;
            private WxPayBean wxpay;
            private UnionpayBean unionpay;

            public UnionpayBean getUnionpay() {
                return unionpay;
            }

            public void setUnionpay(UnionpayBean unionpay) {
                this.unionpay = unionpay;
            }

            public WxPayBean getWxpay() {
                return wxpay;
            }

            public void setWxpay(WxPayBean wxpay) {
                this.wxpay = wxpay;
            }

            public String getOrder_number() {
                return order_number;
            }

            public void setOrder_number(String order_number) {
                this.order_number = order_number;
            }

            public int getType() {
                return type;
            }

            public void setType(int type) {
                this.type = type;
            }

            public String getBack_type() {
                return back_type;
            }

            public void setBack_type(String back_type) {
                this.back_type = back_type;
            }

            public AlipayBean getAlipay() {
                return alipay;
            }

            public void setAlipay(AlipayBean alipay) {
                this.alipay = alipay;
            }

            public static class AlipayBean {
                private String parms_str;
                private String sign;

                public String getParms_str() {
                    return parms_str;
                }

                public void setParms_str(String parms_str) {
                    this.parms_str = parms_str;
                }

                public String getSign() {
                    return sign;
                }

                public void setSign(String sign) {
                    this.sign = sign;
                }
            }

            public static class WxPayBean {
                private String appid;
                private String partnerid;
                private String prepayid;
                @SerializedName("package")
                private String packageX;
                private String noncestr;
                private String timestamp;
                private String sign;

                public String getAppid() {
                    return appid;
                }

                public void setAppid(String appid) {
                    this.appid = appid;
                }

                public String getPartnerid() {
                    return partnerid;
                }

                public void setPartnerid(String partnerid) {
                    this.partnerid = partnerid;
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

                public String getNoncestr() {
                    return noncestr;
                }

                public void setNoncestr(String noncestr) {
                    this.noncestr = noncestr;
                }

                public String getTimestamp() {
                    return timestamp;
                }

                public void setTimestamp(String timestamp) {
                    this.timestamp = timestamp;
                }

                public String getSign() {
                    return sign;
                }

                public void setSign(String sign) {
                    this.sign = sign;
                }
            }

            public static class UnionpayBean {
                private String tn;

                public String getTn() {
                    return tn;
                }

                public void setTn(String tn) {
                    this.tn = tn;
                }
            }
        }
    }
}
