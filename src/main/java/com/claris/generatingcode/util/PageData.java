package com.claris.generatingcode.util;

import javax.servlet.http.HttpServletRequest;
import java.util.*;

public class PageData extends HashMap implements Map{
	
	private static final long serialVersionUID = 1L;
	
	Map map = null;
	HttpServletRequest request;
	
	public PageData(HttpServletRequest request){
		InitPageData(request,false);
	}

	public PageData(HttpServletRequest request, Boolean isToUpperCaseKey){
		InitPageData(request,true);
	}

	/**
	 * 将request封装到amp里
	 * @param request
	 * @param isToUpperCaseKey 参数名是否为大写
	 */
	public void InitPageData(HttpServletRequest request, Boolean isToUpperCaseKey){
		this.request = request;
		Map properties = request.getParameterMap(); //参数集合
		Map returnMap = new HashMap(); 
		Iterator entries = properties.entrySet().iterator(); 
		Entry entry;
		while (entries.hasNext()) {
			entry = (Entry) entries.next();
			String name = (String) entry.getKey();
			String value = "";
			Object valueObj = entry.getValue(); 
			if(null == valueObj){ //如果为null
				value = ""; 
			}else if(valueObj instanceof String[]){  //如果是字符串数组
				String[] values = (String[])valueObj;
				for(int i=0;i<values.length;i++){ 
					 value += values[i] + ",";
				}
				value = value.substring(0, value.length()-1); 
			}else{
				value = valueObj.toString(); 
			}
			if(isToUpperCaseKey){ //如果设置为大写
				name=name.toUpperCase();
			}
			returnMap.put(name, value); 
		}
		map = returnMap;
	}
	public PageData() {
		map = new HashMap();
	}
	
	public Map getMap(){
		return map;
	}

	/**
	 * 设置返回状态码和信息
	 * @param code
	 * @param message
	 * @return
	 */
	public PageData setResult(Integer code, String message){
		map.put("code", code);
		map.put("msg", message);
		return this;
	}

	@Override
	public Object get(Object key) {
		Object obj = null;
		if(map.get(key) instanceof Object[]) {
			Object[] arr = (Object[])map.get(key);
			obj = request == null ? arr:(request.getParameter((String)key) == null ? arr:arr[0]);
		} else {
			obj = map.get(key);
		}
		if(obj!=null){
			obj = String.valueOf(obj); //将对象字符串化
		}
		return obj;
	}

	/**
	 * 获取Object类型的对象
	 * @param key
	 * @return
	 */
	public Object getObject(Object key){
		return map.get(key);
	}

	
	public String getString(Object key) {
		Object obj = get(key);
		return obj == null? null: obj.toString() ;
	}
	
	public int getInt(Object key){
		try{
			Object obj = get(key);
			return obj == null? 0:Integer.parseInt(getString(key));
		}catch(NumberFormatException ex){
			return 0;
		}
	}

	@SuppressWarnings("unchecked")
	@Override
	public PageData put(Object key, Object value) {
		map.put(key, value);
		return this;
	}
	
	@Override
	public Object remove(Object key) {
		return map.remove(key);
	}

	@Override
	public void clear() {
		map.clear();
	}

	@Override
	public boolean containsKey(Object key) {
		// TODO Auto-generated method stub
		return map.containsKey(key);
	}

	@Override
	public boolean containsValue(Object value) {
		// TODO Auto-generated method stub
		return map.containsValue(value);
	}

	@Override
	public Set entrySet() {
		// TODO Auto-generated method stub
		return map.entrySet();
	}

	@Override
	public boolean isEmpty() {
		// TODO Auto-generated method stub
		return map.isEmpty();
	}

	@Override
	public Set keySet() {
		// TODO Auto-generated method stub
		return map.keySet();
	}

	@SuppressWarnings("unchecked")
	@Override
	public void putAll(Map t) {
		// TODO Auto-generated method stub
		map.putAll(t);
	}

	@Override
	public int size() {
		// TODO Auto-generated method stub
		return map.size();
	}

	@Override
	public Collection values() {
		// TODO Auto-generated method stub
		return map.values();
	}
	
}
