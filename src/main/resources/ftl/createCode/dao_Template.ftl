package ${packagePath}.dao;

import java.util.*;
import org.apache.ibatis.annotations.Mapper;
import ${packagePath}.util.*;
<#if entityType == 1>
import ${packagePath}.pojo.${objectName};
</#if>

@Mapper
public interface ${objectName}Mapper{

    int add(${paramsType} record);

    int delete(${paramsType} record);

    int edit(${paramsType} record);

    int saveList(<#if entityType == 1>Map<String, Object><#else>PageData</#if> params);

    int deleteAll(<#if entityType == 1>Map<String, Object><#else>PageData</#if> params);

    ${paramsType} findById(${paramsType} param);

    ${paramsType} findByInfo(${paramsType} param);

    List<${paramsType}> listAll(${paramsType} param);

    List<<#if entityType == 1>Map<String, Object><#else>PageData</#if>> getListByMap(<#if entityType == 1>Map<String, Object><#else>PageData</#if> params);

}