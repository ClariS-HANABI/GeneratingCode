package com.claris.generatingcode.controller;

import com.claris.generatingcode.service.SqlTableStructureService;
import com.claris.generatingcode.util.ProductCodeTool.DatabaseType;
import com.claris.generatingcode.util.*;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;
import javax.servlet.http.HttpServletResponse;
import java.util.*;

@Controller
@RequestMapping(value="/createCode")
public class CreateCodeController extends BaseController {

	private static Logger logger = Logger.getLogger(CreateCodeController.class);

	@Autowired
	private SqlTableStructureService sqlTableStructureService;

	/**
	 * 去代码生成器页面
	 */
	@RequestMapping(value="/view")
	public ModelAndView goProductCode(){
		ModelAndView mv = new ModelAndView();
		mv.setViewName("productCode");
		return mv;
	}

	/**
	 * 判断表是否存在
	 * @return
	 */
	@RequestMapping(value = "/isExistTable", produces = "application/json;charset=UTF-8")
	@ResponseBody
	public Object isExistTable(){
		PageData pd = this.getPageData();
		try{
			int isExist = sqlTableStructureService.isExistTable(pd.getString("tableName"));
			if(isExist == 0){
				pd.setResult(0,"该表不存在，请检查配置是否正确或表名是否正确！");
			}else{
				pd.setResult(1, "查询成功");
			}
		}catch (Exception e){
			logger.error("查询表是否存在发生异常", e);
			pd.setResult(-1, "发生异常");
		}
		return pd;
	}
	
	/**
	 * 根据字段等配置生成代码
	 */
	@RequestMapping(value="/proCodeOnItems")
	public void proCodeOnItems(HttpServletResponse response){
		PageData pd = this.getPageData();
		try{
			/* ============================================================================================= */
			//创建数据模型
			Map<String,Object> root = new HashMap<>(10);
			//包路径
			String packagePath = pd.getString("packagePath");
			//类名
			String objectName = pd.getString("objectName");
			//属性总数
			Integer zindex = pd.getInt("zindex");
			//获取dao类型
			Integer daoType = pd.getInt("daoType");
			root.put("daoType", daoType);
			//属性集合
			List<String[]> fieldList = new ArrayList<String[]>();
			for(int i=0; i< zindex; i++){
				String field = pd.getString("field"+ i);
				//属性放到集合里面
				fieldList.add(field.split(",fh,"));
			}
			//获取类名的大写字母下标
			int[] uppers = ProductCodeTool.getUpperCaseIndex(objectName);
			//获取表名称
			String tableName = ProductCodeTool.getTableNameOnArray(objectName, uppers);
			//添加数据库字段
			ProductCodeTool.setItemsNameOnList(fieldList);
			//主键属性
			PageData keyFiled = new PageData();
			keyFiled.put("name", "id").put("filed", "id").put("mysqlType", "int(18)").put("pgsqlType", "int8").put("type", "bigint");
			root.put("keyFiled", keyFiled);
			//字段属性集合
			root.put("fieldList", fieldList);
			//字段数量
			root.put("zindex", zindex);
			//包路径
			root.put("packagePath", packagePath);
			//类名
			root.put("objectName", objectName);
			//表名称
			root.put("tableName", tableName);
			//类名(全小写)
			root.put("objectNameLower", objectName.toLowerCase());
			//类名(全大写)
			root.put("objectNameUpper", objectName.toUpperCase());
			//mapper、service之类的名称前缀
			String prefixName = objectName.substring(0, 1).toLowerCase() + objectName.substring(1);
			root.put("prefixName", prefixName);
			//当前日期
			root.put("nowDate", new Date());
			//获取实体类类型
			int entityType = pd.getInt("entityType");
			root.put("entityType", entityType);
			//paramsType和result的类型
			root.put("entityName", entityType==1?packagePath + ".entity."+ objectName:"pd");
			root.put("result", entityType==1?"resultMap='BaseResultMap'":"resultType='pd'");
			root.put("entityClass", entityType==1?"java.util.Map":"pd");
			root.put("paramsType", entityType==1?objectName:"PageData");

			/* ============================================================================================= */

			//存放路径
			String filePath = "ftl/code/";
			//ftl路径
			String ftlPath = "createCode";
			//如果是实体类
			if(entityType == 1){
				//生成entity
				Freemarker.printFile("entity_Template.ftl", root, objectName + ".java", filePath, ftlPath);
			}
			//如果是普通DAO
			if(daoType == 1){
				//生成DAO
				Freemarker.printFile("dao_Template.ftl", root, objectName + "Mapper.java", filePath, ftlPath);
			}
			//生成controller
			Freemarker.printFile("my_controller_Template.ftl", root, objectName + "Controller.java", filePath, ftlPath);
			//生成service
			Freemarker.printFile("service_Template.ftl", root, objectName + "Service.java", filePath, ftlPath);
			//生成mybatis xml
			Freemarker.printFile("mapper_mySql_Template.ftl", root, "mapper/mySql/"
					+ objectName + "Mapper.xml", filePath, ftlPath);
			Freemarker.printFile("mapper_postgreSql_Template.ftl", root, "mapper/postgreSql/"
					+ objectName + "Mapper.xml", filePath, ftlPath);
			//生成SQL脚本
			Freemarker.printFile("mysql_sql_Template.ftl", root, "sql/mySql/" + prefixName + ".sql", filePath, ftlPath);
			Freemarker.printFile("postgreSql_sql_Template.ftl", root, "sql/postgreSql/" + prefixName + ".sql", filePath, ftlPath);
			//生成jsp页面
			Freemarker.printFile("myjsp_Template.ftl", root, prefixName + "View.jsp", filePath, ftlPath);
			//生成的全部代码压缩成zip文件
			FileZip.zip(PathUtil.getClasspath() + "ftl/code", PathUtil.getClasspath() + "ftl/" + prefixName +"code.zip");
			//下载代码
			FileDownload.fileDownload(response, PathUtil.getClasspath() + "ftl/" + prefixName + "Code.zip", prefixName + "code.zip");
			//最后清空之前生成的代码
			DelAllFile.delFolder(PathUtil.getClasspath() + "ftl");

		}catch (Exception e){
			logger.error("生成代码处理发生异常", e);
		}
	}

	/**
	 * 根据表名称等配置生成代码
	 */
	@RequestMapping(value="/proCodeOnTable")
	public void proCodeOnTable(HttpServletResponse response){
		PageData pd = this.getPageData();
		try{
			/* ============================================================================================= */
			//创建数据模型
			Map<String,Object> root = new HashMap<>(10);
			//包路径
			String packagePath = pd.getString("packagePath");
			//表名
			String tableName = pd.getString("objectName");
			//获取表结构等信息
			PageData tableInfo = sqlTableStructureService.getTableInfo(tableName);
			//获取dao类型
			Integer daoType = pd.getInt("daoType");
			root.put("daoType", daoType);
			//获取表名的下划线下标数组
			int[] lines = ProductCodeTool.getUnderlineIndex(tableName);
			//获取表名称
			String objectName = ProductCodeTool.getObjectNameOnArray(tableName, lines);
			//根据表信息设置对应的字段信息
			PageData fieldInfo = ProductCodeTool.setItemsInfo(tableInfo);
			//属性集合
			List<String[]> fieldList = (List<String[]>)fieldInfo.getObject("fieldList");
			//属性总数
			Integer zindex = fieldInfo.getInt("zindex");
			//主键属性
			PageData keyFiled = new PageData();
			DatabaseType databaseType = DatabaseType.valueOf(tableInfo.getString("databaseType"));
			//如果是mysql
			if(DatabaseType.MYSQL.equals(databaseType)){
				//获取主键信息
				PageData pk = (PageData)tableInfo.getObject("pk");
				String data_type = pk.getString("data_type");
				keyFiled.put("name", pk.getString("column_name")).put("mysqlType", pk.getString("column_type"));
				if("bigint".equals(data_type)){
					keyFiled.put("pgsqlType", "int8").put("type", "bigint");
				}else if("int".equals(data_type)){
					keyFiled.put("pgsqlType", "int4").put("type", "int");
				}else{
					keyFiled.put("pgsqlType", "varchar(55)").put("type", "varchar");
				}
				//删除主键列
				for(int i = 0; i<fieldList.size(); i++){
					String[] str = fieldList.get(i);
					if(str[5].equals(pk.getString("column_name"))){
						keyFiled.put("filed", str[0]);
						fieldList.remove(i);
						break;
					}
				}
			}
			//如果是pgsql
			else if(DatabaseType.POSTGRESQL.equals(databaseType)){
				//获取主键信息
				PageData pk = sqlTableStructureService.getPrimaryKeyByName(new PageData().put("tableName", tableName));
				for(int i = 0; i<fieldList.size(); i++){
					String[] str = fieldList.get(i);
					if(str[5].equals(pk.getString("colname"))){
						//设置主键信息
						keyFiled.put("name", pk.getString("colname")).put("pgsqlType", pk.getString("typename"));
						if(str[1].equals("int")){
							keyFiled.put("mysqlType", "int(11)").put("type", "int");
						}else if(str[1].equals("bigint")){
							keyFiled.put("mysqlType", "int(18)").put("type", "bigint");
						}else{
							keyFiled.put("mysqlType", "varchar(55)").put("type", "varchar");
						}
						keyFiled.put("filed", str[0]);
						//删除主键列
						fieldList.remove(i);
						break;
					}
				}
			}
			root.put("keyFiled", keyFiled);
			//字段属性集合
			root.put("fieldList", fieldList);
			//字段数量
			root.put("zindex", zindex);
			//包路径
			root.put("packagePath", packagePath);
			//类名
			root.put("objectName", objectName);
			//表名称
			root.put("tableName", tableName);
			//类名(全小写)
			root.put("objectNameLower", objectName.toLowerCase());
			//类名(全大写)
			root.put("objectNameUpper", objectName.toUpperCase());
			//mapper、service之类的名称前缀
			String prefixName = objectName.substring(0, 1).toLowerCase() + objectName.substring(1);
			root.put("prefixName", prefixName);
			//当前日期
			root.put("nowDate", new Date());
			//获取实体类类型
			int entityType = pd.getInt("entityType");
			root.put("entityType", entityType);
			//paramsType和result的类型
			root.put("entityName", entityType==1?packagePath + ".entity."+ objectName:"pd");
			root.put("result", entityType==1?"resultMap='BaseResultMap'":"resultType='pd'");
			root.put("entityClass", entityType==1?"java.util.Map":"pd");
			root.put("paramsType", entityType==1?objectName:"PageData");

			/* ============================================================================================= */

			//存放路径
			String filePath = "ftl/code/";
			//ftl路径
			String ftlPath = "createCode";
			//如果是实体类
			if(entityType == 1){
				//生成entity
				Freemarker.printFile("entity_Template.ftl", root, objectName + ".java", filePath, ftlPath);
			}
			//如果是普通DAO
			if(daoType == 1){
				//生成DAO
				Freemarker.printFile("dao_Template.ftl", root, objectName + "Mapper.java", filePath, ftlPath);
			}
			//生成controller
			Freemarker.printFile("my_controller_Template.ftl", root, objectName + "Controller.java", filePath, ftlPath);
			//生成service
			Freemarker.printFile("service_Template.ftl", root, objectName + "Service.java", filePath, ftlPath);
			//生成mybatis xml
			Freemarker.printFile("mapper_mySql_Template.ftl", root, "mapper/mySql/"
					+ objectName + "Mapper.xml", filePath, ftlPath);
			Freemarker.printFile("mapper_postgreSql_Template.ftl", root, "mapper/postgreSql/"
					+ objectName + "Mapper.xml", filePath, ftlPath);
			//生成SQL脚本
			Freemarker.printFile("mysql_sql_Template.ftl", root, "sql/mySql/" + prefixName + ".sql", filePath, ftlPath);
			Freemarker.printFile("postgreSql_sql_Template.ftl", root, "sql/postgreSql/" + prefixName + ".sql", filePath, ftlPath);
			//生成jsp页面
			Freemarker.printFile("myjsp_Template.ftl", root, prefixName + "View.jsp", filePath, ftlPath);
			//生成的全部代码压缩成zip文件
			FileZip.zip(PathUtil.getClasspath() + "ftl/code", PathUtil.getClasspath() + "ftl/" + prefixName +"code.zip");
			//下载代码
			FileDownload.fileDownload(response, PathUtil.getClasspath() + "ftl/" + prefixName + "Code.zip", prefixName + "code.zip");
			//最后清空之前生成的代码
			DelAllFile.delFolder(PathUtil.getClasspath() + "ftl");

		}catch (Exception e){
			logger.error("生成代码处理发生异常", e);
		}
	}
	
}
