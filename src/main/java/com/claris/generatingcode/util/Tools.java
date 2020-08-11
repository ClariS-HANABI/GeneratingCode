package com.claris.generatingcode.util;

import com.alibaba.fastjson.JSONObject;
import com.fasterxml.jackson.databind.util.JSONPObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

public class Tools {

    /**
     * 判断数值对象是否为null或者小于等于0
     *
     * @param value
     * @return false为大于0
     */
    public static <T extends Number> Boolean numberIsNull(T value) {
        return isEmpty(value) || value.longValue() <= 0;
    }


    /**
     * 判断数值对象是否不为null并且大于0
     *
     * @param value
     * @return true为大于0
     */
    public static <T extends Number> Boolean numberNotNull(T value) {
        return notEmpty(value) && value.longValue() > 0;
    }


    /**
     * 判断数组是否为空
     *
     * @param array
     * @param <T>
     * @return false为不为空
     */
    public static <T> Boolean isNull(T[] array) {
        return isEmpty(array) || array.length == 0;
    }

    /**
     * 判断集合是否为空
     *
     * @param collection
     * @param <T>
     * @return false为不为空
     */
    public static <T extends Collection> Boolean isNull(T collection) {
        return isEmpty(collection) || collection.size() == 0;
    }

    /**
     * 判断Map是否为空
     *
     * @param map
     * @param <T>
     * @return false为不为空
     */
    public static <T extends Map> Boolean isNull(T map) {
        return isEmpty(map) || map.keySet() == null || map.size() == 0;
    }


    /**
     * 判断数组是否不为空
     *
     * @param array
     * @param <T>
     * @return true不为空
     */
    public static <T> Boolean notNull(T[] array) {
        return notEmpty(array) && array.length > 0;
    }

    /**
     * 判断集合是否不为空
     *
     * @param collection
     * @param <T>
     * @return true不为空
     */
    public static <T extends Collection> Boolean notNull(T collection) {
        return notEmpty(collection) && collection.size() > 0;
    }

    /**
     * 判断Map是否不为空
     *
     * @param map
     * @param <T>
     * @return true不为空
     */
    public static <T extends Map> Boolean notNull(T map) {
        return notEmpty(map) && notEmpty(map.keySet()) && map.size() > 0;
    }


    /**
     * 类型转换
     *
     * @param obj 转换对象
     * @param <T> 泛型名称声明（可以有多个）
     * @return
     */
    public static <T> T typeCast(Object obj) {
        return isEmpty(obj) ? null : (T) obj;
    }

    /**
     * 类型转换
     *
     * @param obj    转换对象
     * @param tClass 转换的类型（泛型类型为T或者？都可以）
     * @param <T>    泛型名称声明（可以有多个）
     * @return
     */
    public static <T> T typeCast(Object obj, Class<T> tClass) {
        return isEmpty(obj) ? null : tClass.cast(obj);
    }

    /**
     * 将对象转换为字符串
     * @param value 转换对象
     * @return
     */
    public static String toString(Object value){
        return isEmpty(value) ? null : value.toString();
    }

    /**
     * 设置返回的状态码和消息
     *
     * @param code    状态码
     * @param message 消息
     * @return
     */
    public static Map<String, Object> setResult(Integer code, String message) {
        Map<String, Object> map = new HashMap<>(2);
        map.put("code", code);
        map.put("msg", message);
        return map;
    }


    /**
     * 根据传递的字符串数据以(',')拼接返回信息
     *
     * @param para 传入参数
     * @return 拼接后的字符串
     */
    public static Map<String, Object> getPageDataByStrArr(String[] para) {
        StringBuilder sb = new StringBuilder();
        sb.append("('");
        for (String item : para) {
            sb.append(item + "','");
        }
        String arrayStr = sb.substring(0, sb.lastIndexOf("','"))
                + "')";
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("arrayStr", arrayStr);
        return map;
    }


    /**
     * 获取随机生成的32位id
     */
    public static String get32UUID() {
        String facesibeId = UUID.randomUUID().toString().trim().replaceAll("-", "");
        return facesibeId;
    }


    /**
     * 获取随机生成的验证码
     *
     * @param codeLength 验证码长度(显只支持4位和6位)
     */
    public static String getVerificationCode(int codeLength) {
        String verificationCode = null;
        if (codeLength == 4) {
            verificationCode = String.format("%04d", new Object[]{Integer.valueOf(new Random().nextInt(10000))});
        } else if (codeLength == 6) {
            verificationCode = String.format("%06d", new Object[]{Integer.valueOf(new Random().nextInt(1000000))});
        }
        return verificationCode;
    }


    /**
     * 检测字符串是否不为空(null,"","null", "''", """")
     *
     * @param s
     * @return 不为空则返回true，否则返回false
     */
    public static boolean notEmpty(String s) {
        return s != null && !"".equals(s) && !"''".equals(s) && !"\"\"".equals(s) && !"null".equals(s);
    }

    /**
     * 检测对象不为空
     *
     * @param obj
     * @return
     */
    public static boolean notEmpty(Object obj) {
        if (obj == null) {
            return false;
        }
        return notEmpty(obj.toString());
    }

    /**
     * 检测字符串是否为空(null,"","null", "''", """")
     *
     * @param s
     * @return 为空则返回true，不否则返回false
     */
    public static boolean isEmpty(String s) {
        return s == null || "".equals(s) || "''".equals(s) || "\"\"".equals(s) || "null".equals(s);
    }

    /**
     * 检测对象是否为空
     *
     * @param obj
     * @return
     */
    public static boolean isEmpty(Object obj) {
        if (obj == null) {
            return true;
        }
        return isEmpty(obj.toString());
    }


    /**
     * 检测字符串是否为空(null,"","null",whitespace)
     *
     * @param s
     * @return 为空则返回true，不否则返回false
     */
    public static boolean IsNullOrWhiteSpace(String s) {
        return s == null || s.isEmpty();
    }


    /**
     * 按照yyyy-MM-dd HH:mm:ss的格式，日期转字符串
     *
     * @param date
     * @return yyyy-MM-dd HH:mm:ss
     */
    public static String date2Str(Date date) {
        return date2Str(date, "yyyy-MM-dd HH:mm:ss");
    }

    /**
     * 按照yyyy-MM-dd HH:mm:ss的格式，字符串转日期
     *
     * @param date
     * @return
     */
    public static Date str2Date(String date) {
        if (notEmpty(date)) {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            try {
                return sdf.parse(date);
            } catch (ParseException e) {
                e.printStackTrace();
            }
            return new Date();
        } else {
            return null;
        }
    }

    /**
     * 按照参数format的格式，日期转字符串
     *
     * @param date
     * @param format
     * @return
     */
    public static String date2Str(Date date, String format) {
        if (date != null) {
            SimpleDateFormat sdf = new SimpleDateFormat(format);
            return sdf.format(date);
        } else {
            return "";
        }
    }

    /**
     * 时间戳转string
     *
     * @param time
     * @return
     */
    public static String getString(Long time) {
        if (time != null) {
            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            String date = format.format(time);
            return date;
        } else {
            return "";
        }
    }

    /**
     * 把时间根据时、分、秒转换为时间段
     *
     * @param StrDate
     */
    public static String getTimes(String StrDate) {
        String resultTimes = "";

        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date now;

        try {
            now = new Date();
            Date date = df.parse(StrDate);
            long times = now.getTime() - date.getTime();
            long day = times / (24 * 60 * 60 * 1000);
            long hour = (times / (60 * 60 * 1000) - day * 24);
            long min = ((times / (60 * 1000)) - day * 24 * 60 - hour * 60);
            long sec = (times / 1000 - day * 24 * 60 * 60 - hour * 60 * 60 - min * 60);

            StringBuffer sb = new StringBuffer();
            //sb.append("发表于：");
            if (hour > 0) {
                sb.append(hour + "小时前");
            } else if (min > 0) {
                sb.append(min + "分钟前");
            } else {
                sb.append(sec + "秒前");
            }

            resultTimes = sb.toString();
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return resultTimes;
    }


    /**
     * 格式化阿里大鱼字符串参数
     *
     * @param dataArr 参数
     */
    public static String getJsonStr(String... dataArr) {
        Map<String, String> mapData = new HashMap<String, String>();
        if (dataArr == null) {
            return null;
        }
        int length = dataArr.length / 2;
        for (int i = 0; i < length; i++) {
            mapData.put(dataArr[i], dataArr[length + i]);
        }
        return JSONObject.toJSONString(mapData);
    }


    public static Object returnObject(PageData pd, Map map) {
        if (pd.containsKey("callback")) {
            String callback = pd.get("callback").toString();
            return new JSONPObject(callback, map);
        } else {
            return map;
        }
    }

}
