package ${packagePath}.service.impl;

import java.util.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import lombok.extern.slf4j.Slf4j;
import ${packagePath}.service.${objectName}Service;
<#if entityType == 1>
import ${packagePath}.${entityPath}.${objectName};
</#if>
<#if daoType != 1>
import ${packagePath}.dao.DaoSupport;
<#else>
import ${packagePath}.${daoPath}.${objectName}Mapper;
</#if>

@Slf4j
@Service
public class ${objectName}ServiceImpl implements ${objectName}Service{

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
	@Override
	public Boolean add(${paramsType} record){
		int result = 0;
		try{
		<#if daoType != 1>
			Object obj = dao.save("${objectName}Mapper.add", record);
			result = obj == null? 0:(int)obj;
		<#else>
			result = ${prefixName}Mapper.add(record);
		</#if>
		}catch(Exception e){
			log.error("新增发生异常", e);
		}
		return result > 0;
	}

	/**
	* 删除
	*/
	@Override
	public Boolean delete(<#if keyFiled.type == 'int'>Integer id<#elseif keyFiled.type == 'bigint'>Long id<#else>String id</#if>){
		int result = 0;
		try{
		<#if daoType != 1>
			Object obj = dao.delete("${objectName}Mapper.delete", id);
			result = obj == null? 0:(int)obj;
		<#else>
			result = ${prefixName}Mapper.delete(id);
		</#if>
		}catch(Exception e){
			log.error("删除发生异常", e);
		}
		return result > 0;
	}

	/**
	* 修改
	*/
	@Override
	public Boolean edit(${paramsType} record){
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
		return result > 0;
	}

	/**
	* 批量新增
	*/
	@Override
	@Transactional(rollbackFor = Exception.class)
	public int saveList(List<${paramsType}> list) throws Exception{
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
	@Override
	@Transactional(rollbackFor = Exception.class)
	public int deleteAll(String[] array) throws Exception{
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
	@Override
	public ${paramsType} findById(<#if keyFiled.type == 'int'>Integer id<#elseif keyFiled.type == 'bigint'>Long id<#else>String id</#if>){
		${paramsType} result = null;
		try{
		<#if daoType != 1>
			Object obj = dao.findForObject("${objectName}Mapper.findById", id);
			result = obj == null? null:(${paramsType})obj;
		<#else>
			result = ${prefixName}Mapper.findById(id);
		</#if>
		}catch(Exception e){
			log.error("查询发生异常", e);
		}
		return result;
	}

	/**
	* 根据条件获取单个数据
	*/
	@Override
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
	@Override
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
	@Override
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
