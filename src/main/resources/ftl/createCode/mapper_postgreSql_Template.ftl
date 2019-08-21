<?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE mapper PUBLIC "-//mybatis.org//DTD Mapper 3.0//EN" "http://mybatis.org/dtd/mybatis-3-mapper.dtd">
<mapper namespace=<#if entityType == 1>"${packagePath}.dao.${objectName}Mapper"<#else>"${objectName}Mapper"</#if>>
	<#if entityType == 1>
	<resultMap id="BaseResultMap" type="${entityName}">
			<#if keyFiled.type == 'int'>
		<id column="${keyFiled.name}" jdbcType="INTEGER" property="${keyFiled.filed}" />
			<#elseif keyFiled.type == 'bigint'>
		<id column="${keyFiled.name}" jdbcType="BIGINT" property="${keyFiled.filed}" />
			<#else>
		<id column="${keyFiled.name}" jdbcType="VARCHAR" property="${keyFiled.filed}" />
			</#if>
		<#list fieldList as var>
			<#if var[1] == 'int'>
		<id column="${var[5]}" jdbcType="INTEGER" property="${var[0]}" />
			<#elseif var[1] == 'bigint'>
		<id column="${var[5]}" jdbcType="BIGINT" property="${var[0]}" />
			<#elseif var[1] == 'varchar' || var[1] == 'char' || var[1] == 'text' >
		<id column="${var[5]}" jdbcType="VARCHAR" property="${var[0]}" />
			<#elseif var[1] == 'decimal' || var[1] == 'double' || var[1] == 'float'>
		<id column="${var[5]}" jdbcType="DOUBLE" property="${var[0]}" />
			<#elseif var[1] == 'date' || var[1] == 'timestamp'>
		<id column="${var[5]}" jdbcType="DATE" property="${var[0]}" />
			<#else>
		<id column="${var[5]}" jdbcType="${var[1]?upper_case}" property="${var[0]}" />
			</#if>
		</#list>
	</resultMap>
	</#if>

	<sql id="Column_List">
	<#list fieldList as var>
		a.${var[5]} as "${var[0]}",
	</#list>
		a.${keyFiled.name} as "${keyFiled.filed}"
	</sql>

	<sql id="Base_Column_List">
	<#list fieldList as var>
		a.${var[5]},
	</#list>
		a.${keyFiled.name}
	</sql>

	<!-- 新增 -->
	<insert id="add" parameterType="${entityName}">
		insert into ${tableName}(
		<#list fieldList as var>
			<if test="${var[0]} != null">
				${var[5]},
			</if>
		</#list>
			${keyFiled.name}
		) values (
		<#list fieldList as var>
			<if test="${var[0]} != null">
				${r"#{"}${var[0]}${r"}"},
			</if>
		</#list>
			${r"#{"}${keyFiled.filed}${r"}"}
		)
	</insert>


	<!-- 批量新增 -->
	<insert id="saveList" parameterType="${entityClass}">
		insert into ${tableName}(
		<#list fieldList as var>
			${var[5]},
		</#list>
			${keyFiled.name}
		)values
		<foreach collection="list" item="var" separator=",">
			(
		<#list fieldList as var>
			${r"#{var."}${var[0]}${r"}"},
		</#list>
			${r"#{var."}${keyFiled.filed}${r"}"}
			)
		</foreach>
	</insert>


	<!-- 删除 -->
	<delete id="delete" parameterType="${entityName}">
		delete from ${tableName}
		where ${keyFiled.name} = ${r"#{"}${keyFiled.filed}${r"}"}
	</delete>


	<!-- 批量删除 -->
	<delete id="deleteAll" parameterType="${entityClass}">
		delete from ${tableName}
		where ${keyFiled.name} in
		<foreach item="item" index="index" collection="array" open="(" separator="," close=")">
			${r"#{item}"}
		</foreach>
		<!-- id in ${r"${arrayStr}"} -->
	</delete>


	<!-- 修改 -->
	<update id="edit" parameterType="${entityName}">
		update ${tableName} set
	<#list fieldList as var>
		<if test="${var[0]} != null" >
			${var[5]} = ${r"#{"}${var[0]}${r"}"},
		</if>
	</#list>
		${keyFiled.name} = ${keyFiled.name}
		where ${keyFiled.name} = ${r"#{"}${keyFiled.filed}${r"}"}
	</update>


	<!-- 通过ID获取数据 -->
	<select id="findById" parameterType="${entityName}" ${result}>
		select
		<include refid=<#if entityType == 1>'Base_Column_List'<#else>'Column_List'</#if> />
		from ${tableName} a
		where a.${keyFiled.name} = ${r"#{"}${keyFiled.filed}${r"}"}
	</select>


	<!-- 根据条件获取单个数据 -->
	<select id="findByInfo" parameterType="${entityName}" ${result}>
		select
		<include refid=<#if entityType == 1>'Base_Column_List'<#else>'Column_List'</#if> />
		from ${tableName} a
		<where>
		<#list fieldList as var>
			<if test="${var[0]} != null and ${var[0]} != ''" >
				and a.${var[5]}= ${r"#{"}${var[0]}${r"}"}
			</if>
		</#list>
		</where>
	</select>


	<!-- 列表 -->
	<select id="listAll" parameterType="${entityName}" ${result}>
		select
		<include refid=<#if entityType == 1>'Base_Column_List'<#else>'Column_List'</#if> />
		from ${tableName} a
		<where>
		<#list fieldList as var>
			<if test="${var[0]} != null and ${var[0]} != ''" >
				and a.${var[5]}= ${r"#{"}${var[0]}${r"}"}
			</if>
		</#list>
		</where>
	</select>


	<!-- 根据参数列表查询 -->
	<select id="getListByMap" parameterType="${entityClass}" resultType="${entityClass}">
		select
		<include refid="Column_List" />
		from ${tableName} a
		<where>
			<#list fieldList as var>
				<if test="${var[0]} != null and ${var[0]} != ''" >
					and a.${var[5]}= ${r"#{"}${var[0]}${r"}"}
				</if>
			</#list>
		</where>
	</select>

</mapper>