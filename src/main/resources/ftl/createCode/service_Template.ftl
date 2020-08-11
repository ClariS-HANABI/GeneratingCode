package ${packagePath}.service;

import java.util.*;
<#if entityType == 1>
import ${packagePath}.${entityPath}.${objectName};
</#if>

public interface ${objectName}Service {

    Boolean add(${paramsType} record);

    Boolean delete(<#if keyFiled.type == 'int'>Integer id<#elseif keyFiled.type == 'bigint'>Long id<#else>String id</#if>);

    Boolean edit(${paramsType} record);

    int saveList(<#if entityType == 1>List<${paramsType}> list<#else>PageData params</#if>) throws Exception;

    int deleteAll(<#if entityType == 1>String[] array<#else>PageData params</#if>) throws Exception;

    ${paramsType} findById(<#if keyFiled.type == 'int'>Integer id<#elseif keyFiled.type == 'bigint'>Long id<#else>String id</#if>);

    ${paramsType} findByInfo(${paramsType} param);

    List<${paramsType}> listAll(${paramsType} param);

    List<<#if entityType == 1>Map<String, Object><#else>PageData</#if>> getListByMap(<#if entityType == 1>Map<String, Object><#else>PageData</#if> params);

}