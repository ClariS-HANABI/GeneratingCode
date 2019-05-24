-- ----------------------------
-- Table structure for `${objectNameUpper}`
-- ----------------------------
DROP TABLE IF EXISTS `${tableName}`;
CREATE TABLE `${tableName}` (
	${keyFiled.name} ${keyFiled.pgsqlType} primary key,
<#list fieldList as var>
	<#if var[1] == 'varchar' || var[1] == 'char'>
	${var[5]} ${var[1]}(${var[2]}) <#if var[4] == '1'>not null</#if> <#if var_index+1 < zindex >,</#if>
	<#elseif var[1] == 'decimal'>
	${var[5]} ${var[1]}(15, 2) <#if var[4] == '1'>not null</#if> <#if var_index+1 < zindex>,</#if>
	<#elseif var[1] == 'timestamp'>
	${var[5]} ${var[1]}(6) <#if var[4] == '1'>not null</#if> <#if var_index+1 < zindex>,</#if>
	<#elseif var[1] == 'int'>
	${var[5]} int4 <#if var[4] == '1'>not null</#if> <#if var_index+1 < zindex>,</#if>
	<#elseif var[1] == 'bigint'>
	${var[5]} int8 <#if var[4] == '1'>not null</#if> <#if var_index+1 < zindex>,</#if>
	<#elseif var[1] == 'float'>
	${var[5]} float4 <#if var[4] == '1'>not null</#if> <#if var_index+1 < zindex>,</#if>
	<#elseif var[1] == 'double'>
	${var[5]} float8 <#if var[4] == '1'>not null</#if> <#if var_index+1 < zindex>,</#if>
	<#else>
	${var[5]} ${var[1]} <#if var[4] == '1'>not null</#if> <#if var_index+1 <zindex>,</#if>
	</#if>
</#list>
);

<#list fieldList as var>
	<#if var[3]?? && var[3] != ''>
comment on column ${tableName}.${var[5]} is '${var[3]}';
	</#if>
</#list>
