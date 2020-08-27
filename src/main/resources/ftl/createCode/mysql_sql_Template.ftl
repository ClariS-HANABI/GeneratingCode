-- ----------------------------
-- Table structure for `${objectNameUpper}`
-- ----------------------------
DROP TABLE IF EXISTS `${tableName}`;
CREATE TABLE `${tableName}` (
	${keyFiled.name} ${keyFiled.mysqlType} AUTO_INCREMENT,
<#list fieldList as var>
	<#if var[1] == 'int' || var[1] == 'bigint'>
	`${var[5]}` ${var[1]}(${var[2]!11}) <#if var[4] == '1'>not null </#if>COMMENT '${var[3]!''}',
	<#elseif var[1] == 'varchar' || var[1] == 'char'>
	`${var[5]}` ${var[1]}(${var[2]!255}) <#if var[4] == '1'>not null </#if>COMMENT '${var[3]!''}',
	<#elseif var[1] == 'float' || var[1] == 'double' || var[1] == 'decimal'>
	`${var[5]}` ${var[1]}(15, 2) <#if var[4] == '1'>not null </#if>COMMENT '${var[3]!''}',
	<#elseif var[1] == 'date'>
	`${var[5]}` ${var[1]} <#if var[4] == '1'>not null </#if>COMMENT '${var[3]!''}',
	<#elseif var[1] == 'timestamp'>
	`${var[5]}` ${var[1]} <#if var[4] == '1'>not null </#if>COMMENT '${var[3]!''}',
	<#else>
	`${var[5]}` ${var[1]} <#if var[4] == '1'>not null </#if>COMMENT '${var[3]!''}',
	</#if>
</#list>
	PRIMARY KEY (`${keyFiled.name}`)
) AUTO_INCREMENT=1;

