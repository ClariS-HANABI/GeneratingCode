package com.claris.generatingcode.util;

import javax.net.ssl.HttpsURLConnection;
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;
import java.util.Map;

/**
 * http 工具类
 */
public class HttpUtil {

    /**
     * 默认编码格式
     */
    private static final String ENCODING = "UTF-8";


    /**
     * http的get请求
     * @param requestUrl 请求路径
     * @param encoding 编码格式
     * @param params 请求参数
     * @param heardParam 其他请求头参数
     * @return
     * @throws Exception
     */
    public static String get(String requestUrl, String encoding, String params, Map<String, String> heardParam) throws Exception {
        return HttpUtil.request(requestUrl, encoding, params,"GET", heardParam);
    }

    /**
     * http的post请求
     * @param requestUrl 请求路径
     * @param encoding 编码格式
     * @param params 请求参数
     * @param heardParam 其他请求头参数
     * @return
     * @throws Exception
     */
    public static String post(String requestUrl, String encoding, String params, Map<String, String> heardParam) throws Exception {
        return HttpUtil.request(requestUrl, encoding, params, "POST", heardParam);
    }

    /**
     * https请求
     * @param requestUrl 请求路径
     * @param params 请求参数
     * @param methodType 请求方式
     * @param encoding 编码格式
     * @param heardParam 其他请求头参数
     * @return
     * @throws Exception
     */
    public static String https(String requestUrl, String params, MethodType methodType,
                               String encoding, Map<String, String> heardParam) throws Exception {
        if(Tools.isEmpty(encoding)){
            encoding = ENCODING;
        }
        String type = "GET";
        if(methodType == MethodType.POST){
            type = "POST";
        } else if(methodType == MethodType.PUT){
            type = "PUT";
        } else if(methodType == MethodType.DELETE){
            type = "DELETE";
        }
        return HttpUtil.send(requestUrl + "?", params, encoding, type, heardParam, HttpType.HTTPS);
    }

    private static String request(String requestUrl, String encoding, String params,
                                  String methodType, Map<String, String> heardParam) throws Exception {
        if(Tools.isEmpty(encoding)){
            encoding = ENCODING;
        }
        return HttpUtil.send(requestUrl + "?", params, encoding, methodType, heardParam, HttpType.HTTP);
    }

    private static String send(String requestUrl, String params, String encoding, String methodType,
                               Map<String, String> heardParam, HttpType httpType) throws Exception {
        URL urlObj = new URL(requestUrl);
        // 打开和URL之间的连接
        if(httpType == HttpType.HTTP){
            HttpURLConnection connection = (HttpURLConnection) urlObj.openConnection();
            return HttpUtil.sendRequest(params, encoding, methodType, heardParam, connection);
        } else if(httpType == HttpType.HTTPS){
            HttpsURLConnection connection = (HttpsURLConnection) urlObj.openConnection();
            return HttpUtil.sendRequest(params, encoding, methodType, heardParam, connection);
        }
        return null;
    }


    /**
     * 发送请求
     * @param params 请求参数
     * @param encoding 编码类型
     * @param methodType 请求类型
     * @param heardParam 其他请求头参数
     * @param connection url连接对象
     * @return
     * @throws Exception
     */
    private static String sendRequest(String params, String encoding, String methodType,
                                     Map<String, String> heardParam, HttpURLConnection connection) throws Exception {
        //请求类型
        connection.setRequestMethod(methodType);
        // 设置通用的请求属性
        connection.setRequestProperty("Connection", "Keep-Alive");
        //循环设置请求头属性
        if(!Tools.isNull(heardParam)){
            for(String key : heardParam.keySet()){
                connection.setRequestProperty(key, heardParam.get(key));
            }
        }
        connection.setUseCaches(false);
        connection.setDoOutput(true);
        connection.setDoInput(true);

        // 得到请求的输出流对象
        DataOutputStream out = new DataOutputStream(connection.getOutputStream());
        out.write(params.getBytes(encoding));
        out.flush();
        out.close();

        // 建立实际的连接
        connection.connect();
        // 获取所有响应头字段
        Map<String, List<String>> headers = connection.getHeaderFields();
        // 遍历所有的响应头字段
        for (String key : headers.keySet()) {
            System.err.println(key + "--->" + headers.get(key));
        }
        // 定义 BufferedReader输入流来读取URL的响应
        BufferedReader in = null;
        in = new BufferedReader(new InputStreamReader(connection.getInputStream(), encoding));
        String result = "";
        String getLine;
        while ((getLine = in.readLine()) != null) {
            result += getLine;
        }
        in.close();
        System.err.println("result:" + result);
        return result;
    }

    /**
     * http类型
     */
    enum HttpType{
        HTTP,
        HTTPS
    }

    /**
     * 请求类型
     */
    public enum MethodType{
        GET,
        POST,
        PUT,
        DELETE
    }
}
