package ${packagePath}.service;

import java.util.*;
import javax.annotation.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import lombok.extern.log4j.*;
<#if entityType == 1>
import ${packagePath}.pojo.${objectName};
</#if>
<#if daoType != 1>
import ${packagePath}.dao.DaoSupport;
<#else>
import ${packagePath}.dao.${objectName}Mapper;
</#if>
import ${packagePath}.util.*;

@Log4j
@Service
public class ${objectName}Service{

<#if daoType != 1>
	@Resource(name = "daoSupport")
	private DaoSupport dao;
<#else>
	@Autowired
	private ${objectName}Mapper ${prefixName}Mapper;
</#if>


	/**
	* 新增
	*/
	public int save(${paramsType} record){
		int result = 0;
		try{
		<#if daoType != 1>
			Object obj = dao.save("${objectName}Mapper.save", record);
			result = obj == null? 0:(int)obj;
		<#else>
			result = ${prefixName}Mapper.save(record);
		</#if>
		}catch(Exception e){
			log.error("新增发生异常", e);
		}
		return result;
	}

	/**
	* 删除
	*/
	public int delete(<#if keyFiled.type == 'int'>Integer id<#elseif keyFiled.type == 'bigint'>Long id<#else>String id</#if>){
	    ${paramsType} record = new ${paramsType}();
		int result = 0;
		try{
		<#if entityType == 1>
		    record.set${(keyFiled.filed?substring(0, 1))?upper_case + keyFiled.filed?substring(1)}(id);
		<#else>
		    record.put("${keyFiled.filed}", id);
		</#if>
		<#if daoType != 1>
			Object obj = dao.delete("${objectName}Mapper.delete", record);
			result = obj == null? 0:(int)obj;
		<#else>
			result = ${prefixName}Mapper.delete(record);
		</#if>
		}catch(Exception e){
			log.error("删除发生异常", e);
		}
		return result;
	}

	/**
	* 修改
	*/
	public int edit(${paramsType} record){
		int result = 0;
		try{
		<#if daoType != 1>
			Object obj = dao.update("${objectName}Mapper.edit", record);
			result = obj == null? 0:(int)obj;
		<#else>
			result = ${prefixName}Mapper.edit(record);
		</#if>
		}catch(Exception e){
			log.error("修改发生异常", e);
		}
		return result;
	}

	/**
	* 批量新增
	*/
	@Transactional(rollbackFor = Exception.class)
	public int saveList(List<${paramsType}> list){
		int result = 0;
    <#if entityType == 1>
        Map<String, Object> map = new HashMap<>(10);
		map.put("list", list);
    <#else>
        PageData pd = new PageData();
		pd.put("list", list);
    </#if>
	<#if daoType != 1>
		Object obj = dao.save("${objectName}Mapper.saveList", <#if entityType == 1>map<#else>pd</#if>);
		result = obj == null? 0:(int)obj;
	<#else>
		result = ${prefixName}Mapper.saveList(<#if entityType == 1>map<#else>pd</#if>);
	</#if>
		return result;
	}

	/**
	* 批量删除
	*/
	@Transactional(rollbackFor = Exception.class)
	public int deleteAll(String[] array){
		int result = 0;
    <#if entityType == 1>
        Map<String, Object> map = new HashMap<>(10);
		map.put("array", array);
    <#else>
        PageData pd = new PageData();
		pd.put("array", array);
    </#if>
	<#if daoType != 1>
		Object obj = dao.delete("${objectName}Mapper.deleteAll", <#if entityType == 1>map<#else>pd</#if>);
		result = obj == null? 0:(int)obj;
	<#else>
		result = ${prefixName}Mapper.deleteAll(<#if entityType == 1>map<#else>pd</#if>);
	</#if>
		return result;
	}

	/**
	* 通过id获取数据
	*/
	public ${paramsType} findById(<#if keyFiled.type == 'int'>Integer id<#elseif keyFiled.type == 'bigint'>Long id<#else>String id</#if>){
		${paramsType} result = new ${paramsType}();
		try{
		<#if entityType == 1>
		    result.set${(keyFiled.filed?substring(0, 1))?upper_case + keyFiled.filed?substring(1)}(id);
		<#else>
		    result.put("${keyFiled.filed}", id);
		</#if>
		<#if daoType != 1>
			Object obj = dao.findForObject("${objectName}Mapper.findById", result);
			result = obj == null? null:(${paramsType})obj;
		<#else>
			result = ${prefixName}Mapper.findById(result);
		</#if>
		}catch(Exception e){
			log.error("查询发生异常", e);
		}
		return result;
	}

	/**
	* 根据条件获取单个数据
	*/
	public ${paramsType} findByInfo(${paramsType} param){
		${paramsType} result = new ${paramsType}();
		try{
		<#if daoType != 1>
			Object obj = dao.findForObject("${objectName}Mapper.findByInfo", param);
			result = obj == null? null:(${paramsType})obj;
		<#else>
			result = ${prefixName}Mapper.findByInfo(param);
		</#if>
		}catch(Exception e){
			log.error("查询发生异常", e);
		}
		return result;
	}

	/**
	* 列表
	*/
	public List<${paramsType}> listAll(${paramsType} param){
		List<${paramsType}> list = new ArrayList<>();
		try{
		<#if daoType != 1>
			Object obj = dao.findForList("${objectName}Mapper.listAll", param);
			list = obj == null? null:(List<${paramsType}>)obj;
		<#else>
			list = ${prefixName}Mapper.listAll(param);
		</#if>
		}catch(Exception e){
			log.error("查询列表发生异常", e);
		}
		return list;
	}

	/**
	* 根据参数列表查询
	*/
	public List<<#if entityType == 1>Map<String, Object><#else>PageData</#if>> getListByMap(<#if entityType == 1>Map<String, Object><#else>PageData</#if> params){
		List<<#if entityType == 1>Map<String, Object><#else>PageData</#if>> list = new ArrayList<>();
		try{
		<#if daoType != 1>
			Object obj = dao.findForList("${objectName}Mapper.getListByMap", params);
			list = obj == null? null:(List<<#if entityType == 1>Map<String, Object><#else>PageData</#if>>)obj;
		<#else>
			list = ${prefixName}Mapper.getListByMap(params);
		</#if>
		}catch(Exception e){
			log.error("查询列表发生异常", e);
		}
		return list;
	}

}
