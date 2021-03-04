<?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE mapper PUBLIC "-//mybatis.org//DTD Mapper 3.0//EN" "http://mybatis.org/dtd/mybatis-3-mapper.dtd">
<mapper namespace=<#if entityType == 1>"${packagePath}.${daoPath}.${objectName}Mapper"<#else>"${objectName}Mapper"</#if>>

	<sql id="ColumnList">
		<#list fieldList as var>
			<#if var[1] == 'date'>
		date_format(a.${var[5]}, '%Y-%m-%d') as ${var[0]},
			<#elseif var[1] == 'datetime'>
		date_format(a.${var[5]},'%Y-%m-%d %H:%i:%s') as ${var[0]},
			<#elseif var[1] == 'year'>
		date_format(a.${var[5]},'%Y') as ${var[0]},
			<#elseif var[1] == 'time'>
		date_format(a.${var[5]},'%H:%i:%s') as ${var[0]},
			<#else>
		a.${var[5]} as ${var[0]},
			</#if>
		</#list>
		a.${keyFiled.name} as ${keyFiled.filed}
	</sql>

	<sql id="BaseColumnList">
		<#list fieldList as var>
			<#if var[1] == 'date'>
		date_format(a.${var[5]}, '%Y-%m-%d') as ${var[5]},
			<#elseif var[1] == 'datetime'>
		date_format(a.${var[5]},'%Y-%m-%d %H:%i:%s') as ${var[5]},
			<#elseif var[1] == 'year'>
		date_format(a.${var[5]},'%Y') as ${var[5]},
			<#elseif var[1] == 'time'>
		date_format(a.${var[5]},'%H:%i:%s') as ${var[5]},
			<#else>
		a.${var[5]},
			</#if>
		</#list>
		a.${keyFiled.name}
	</sql>

	<sql id="WhereTerm">
	<#list fieldList as var>
		<if test="${var[0]} != null and ${var[0]} != ''" >
			and a.${var[5]} = ${r"#{"}${var[0]}${r"}"}
		</if>
	</#list>
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
	<delete id="delete" parameterType="<#if keyFiled.type == 'int'>java.lang.Integer<#elseif keyFiled.type == 'bigint'>java.lang.Long<#else>java.lang.String</#if>">
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
	<select id="findById" parameterType="<#if keyFiled.type == 'int'>java.lang.Integer<#elseif keyFiled.type == 'bigint'>java.lang.Long<#else>java.lang.String</#if>" ${result}>
		select
		<include refid="ColumnList" />
		from ${tableName} a
		where a.${keyFiled.name} = ${r"#{"}${keyFiled.filed}${r"}"}
	</select>


    <!-- 根据条件获取单个数据 -->
    <select id="findByInfo" parameterType="${entityName}" ${result}>
        select
		<include refid="ColumnList" />
        from ${tableName} a
        <where>
			<include refid="WhereTerm"/>
        </where>
    </select>
	
	
	<!-- 列表 -->
	<select id="listAll" parameterType="${entityName}" ${result}>
		select
		<include refid="ColumnList" />
		from ${tableName} a
		<where>
			<include refid="WhereTerm"/>
		</where>
	</select>


	<!-- 根据参数列表查询 -->
	<select id="getListByMap" parameterType="${entityClass}" resultType="${entityClass}">
		select
		<include refid="ColumnList" />
		from ${tableName} a
		<where>
			<include refid="WhereTerm"/>
		</where>
	</select>

</mapper>