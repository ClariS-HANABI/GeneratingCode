package com.claris.generatingcode.controller;

import com.claris.generatingcode.util.*;
import org.springframework.web.context.request.*;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;

public class BaseController {

	/**
	 * 得到PageData
	 */
	public PageData getPageData() {
		return new PageData(this.getRequest());
	}
	/**
	 * 得到PageData,转化参数为大写
	 */
	public PageData getRequestData() {
		return new PageData(this.getRequest(),true);
	}


	public Page getRequestPage(boolean isToUpperCaseKey){
		PageData pd = new PageData();
		if(isToUpperCaseKey)
		{
			pd=getRequestData();
			pd.put("ISDELETE", 0);
		}
		else
		{
			pd=getPageData();
			pd.put("isDelete", 0);
		}
		Page page=new Page();
		page.setPd(pd);

		if (!Tools.IsNullOrWhiteSpace(pd.getString("CURRENTPAGE"))) {
			page.setCurrentPage(pd.getInt("CURRENTPAGE"));
		} else if(!Tools.IsNullOrWhiteSpace(pd.getString("SHOWCOUNT"))) {
			page.setShowCount(pd.getInt("SHOWCOUNT"));
		}else {
			page.setShowCount(10);
		}
		return page;
	}


	/**
	 * 得到request对象
	 */
	public HttpServletRequest getRequest() {
		HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder
				.getRequestAttributes()).getRequest();
		return request;
	}

	/**
	 * 得到32位的uuid
	 *
	 * @return
	 */
	public String get32UUID() {

		return Tools.get32UUID();
	}


	/**
	 * 得到分页列表的信息
	 */
	public Page getPage() {

		return new Page();
	}

}
