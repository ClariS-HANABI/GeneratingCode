package com.claris.generatingcode.util;

import com.alibaba.fastjson.JSONObject;
import com.fasterxml.jackson.databind.util.JSONPObject;

import javax.servlet.http.HttpServletRequest;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Tools {

	/**
	 * 类型转换
	 *
	 * @param obj 转换对象
	 * @param <T> 泛型名称声明（可以有多个）
	 * @return
	 */
	public static <T> T typeCast(Object obj) {
		return obj == null ? null : (T) obj;
	}

	/**
	 * 类型转换
	 *
	 * @param obj    转换对象
	 * @param tClass 转换的类型（泛型类型为T或者？都可以）
	 * @param <T>    泛型名称声明（可以有多个）
	 * @return
	 */
	public static <T> T typeCast(Object obj, Class<T> tClass) throws Exception {
		return obj == null ? null : (T) obj;
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
	 * 获取客户端真实IP地址
	 *
	 * @param request
	 * @return
	 */
	public static String getIpAddr(HttpServletRequest request) {
		String ip = request.getHeader("x-forwarded-for");
		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
			ip = request.getHeader("Proxy-Client-IP");
		}
		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
			ip = request.getHeader("WL-Proxy-Client-IP");
		}
		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
			ip = request.getRemoteAddr();
		}
		return ip;
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
	 * 验证中文姓名
	 *
	 * @param name
	 * @return
	 */
	public static boolean checkCHName(String name) {
		boolean flag = false;
		try {
			String check = "^[\u4e00-\u9fa5]{2,4}$";
			Pattern regex = Pattern.compile(check);
			Matcher matcher = regex.matcher(name);
			flag = matcher.matches();
		} catch (Exception e) {
			e.printStackTrace();
			flag = false;
		}
		return flag;
	}


	/**
	 * 验证邮箱
	 *
	 * @param email
	 * @return
	 */
	public static boolean checkEmail(String email) {
		boolean flag = false;
		try {
			String check = "^([a-z0-9A-Z]+[-|_|\\.]?)+[a-z0-9A-Z]@([a-z0-9A-Z]+(-[a-z0-9A-Z]+)?\\.)+[a-zA-Z]{2,}$";
			Pattern regex = Pattern.compile(check);
			Matcher matcher = regex.matcher(email);
			flag = matcher.matches();
		} catch (Exception e) {
			e.printStackTrace();
			flag = false;
		}
		return flag;
	}

	/**
	 * 验证手机号码
	 *
	 * @param
	 * @return
	 */
	public static boolean checkMobileNumber(String mobileNumber) {
		boolean flag = false;
		try {
			Pattern regex = Pattern.compile("^(0|86|17951)?(13[0-9]|15[012356789]|17[678]|18[0-9]|14[57])[0-9]{8}$");
			Matcher matcher = regex.matcher(mobileNumber);
			flag = matcher.matches();
		} catch (Exception e) {
			e.printStackTrace();
			flag = false;
		}
		return flag;
	}


	/**
	 * request参数乱码转换
	 *
	 * @param parameter
	 * @return
	 * @throws Exception
	 */
	public static String parameterTranscoding(String parameter) throws Exception {
		if (parameter != null) {
			byte[] b = parameter.getBytes("ISO-8859-1");//用tomcat的格式（iso-8859-1）方式去读。
			String string = new String(b, "utf-8");//采用utf-8去接string
			return string;
		}
		return null;
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
	 * 获取随机生成的随机码
	 *
	 * @param length 随机码的长度
	 */
	public static String getUUIDByLength(int length) {
		Random random = new Random(); //随机数对象
		if (length <= 0) { //如果长度小于1
			return null;
		}
		String uuid = UUID.randomUUID().toString().trim().replaceAll("-", ""); //UUID随机生成的随机数
		if (length < 32) { //如果小于32位，大于32位直接返回
			int number = 32 - length; //随机的区间
			int rundomNumber = random.nextInt(number); //随机数（字符串起始截取的位置）
			String subUUID = uuid.substring(rundomNumber, rundomNumber + length); //截取字符串
			return subUUID;
		}
		return uuid;
	}


	/**
	 * 检测字符串是否不为空(null,"","null")
	 *
	 * @param s
	 * @return 不为空则返回true，否则返回false
	 */
	public static boolean notEmpty(String s) {
		return s != null && !"".equals(s) && !"null".equals(s);
	}

	/**
	 * 检测字符串是否为空(null,"","null")
	 *
	 * @param s
	 * @return 为空则返回true，不否则返回false
	 */
	public static boolean isEmpty(String s) {
		return s==null || "".equals(s) || "''".equals(s) || "\"\"".equals(s) || "null".equals(s);
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
	 * 读取txt里的单行内容
	 *
	 * @param fileP 文件路径
	 */
	public static String readTxtFile(String fileP) {
		try {
			String filePath = String.valueOf(Thread.currentThread().getContextClassLoader().getResource("")) + "../../";    //项目路径
			filePath = filePath.replaceAll("file:/", "");
			filePath = filePath.replaceAll("%20", " ");
			filePath = filePath.trim() + fileP.trim();
			if (filePath.indexOf(":") != 1) {
				filePath = File.separator + filePath;
			}
			String encoding = "utf-8";
			filePath = new String(filePath.getBytes(), "utf-8");
			File file = new File(filePath);
			if (file.isFile() && file.exists()) {        // 判断文件是否存在
				InputStreamReader read = new InputStreamReader(
						new FileInputStream(file), encoding);    // 考虑到编码格式
				BufferedReader bufferedReader = new BufferedReader(read);
				String lineTxt = null;
				while ((lineTxt = bufferedReader.readLine()) != null) {
					return lineTxt;
				}
				read.close();
			} else {
				System.out.println("找不到指定的文件,查看此路径是否正确:" + filePath);
			}
		} catch (Exception e) {
			System.out.println("读取文件内容出错");
		}
		return "";
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
