package ${packagePath}.dao;

import java.util.*;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
<#if entityType == 1>
import ${packagePath}.${entityPath}.${objectName};
</#if>

@Mapper
public interface ${objectName}Mapper {

    int add(${paramsType} record);

    int delete(@Param("id") <#if keyFiled.type == 'int'>Integer id<#elseif keyFiled.type == 'bigint'>Long id<#else>String id</#if>);

    int edit(${paramsType} record);

    int saveList(<#if entityType == 1>Map<String, Object><#else>PageData</#if> params);

    int deleteAll(<#if entityType == 1>Map<String, Object><#else>PageData</#if> params);

    ${paramsType} findById(@Param("id") <#if keyFiled.type == 'int'>Integer id<#elseif keyFiled.type == 'bigint'>Long id<#else>String id</#if>);

    ${paramsType} findByInfo(${paramsType} param);

    List<${paramsType}> listAll(${paramsType} param);

    List<<#if entityType == 1>Map<String, Object><#else>PageData</#if>> getListByMap(<#if entityType == 1>Map<String, Object><#else>PageData</#if> params);

}