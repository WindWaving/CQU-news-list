/**
 * the API of translation
 * use this by calling `getTransResult`
 */
package com.baidu.translate.demo;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class TransApi {
    private static final String TRANS_API_HOST = "http://api.fanyi.baidu.com/api/trans/vip/translate";

    private String appid;
    private String securityKey;

    public TransApi(String appid, String securityKey) {
        this.appid = appid;
        this.securityKey = securityKey;
    }

    public String getTransResult(String query, String from, String to) throws Exception {
        JSONObject params = buildParams(query, from, to);
        return HttpGet.post(TRANS_API_HOST, params);
    }

    /**
     * build strings to params of url, like ?id=4&name=she
     * @param query: string to be translated
     * @param from: from lang, we need 'zh' here
     * @param to: from lang, we need 'en' here
     * @return
     * @throws JSONException
     */
    private JSONObject buildParams(String query, String from, String to) throws JSONException {
        System.out.println(query);
//        Map<String, String> params = new HashMap<String, String>();
        JSONObject params=new JSONObject();
        params.put("q", query);
        params.put("from", from);
        params.put("to", to);

        params.put("appid", appid);

        // 随机数
        String salt = String.valueOf(System.currentTimeMillis());
        params.put("salt", salt);

        // 签名
        String src = appid + query + salt + securityKey; // 加密前的原文
        params.put("sign", com.baidu.translate.demo.MD5.md5(src));

        return params;
    }

}
