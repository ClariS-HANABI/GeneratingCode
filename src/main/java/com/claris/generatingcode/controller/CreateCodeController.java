package com.claris.generatingcode.controller;

import com.claris.generatingcode.service.SqlTableStructureService;
import com.claris.generatingcode.util.*;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/create-code")
public class CreateCodeController extends BaseController {

    private static Logger logger = Logger.getLogger(CreateCodeController.class);

    @Autowired
    private SqlTableStructureService sqlTableStructureService;

    /**
     * 判断表是否存在
     *
     * @return
     */
    @RequestMapping("/isExistTable")
    public Object isExistTable() {
        PageData pd = this.getPageData();
        try {
            int isExist = sqlTableStructureService.isExistTable(pd.getString("tableName"));
            if (isExist == 0) {
                pd.setResult(0, "该表不存在，请检查配置是否正确或表名是否正确！");
            } else {
                pd.setResult(1, "查询成功");
            }
        } catch (Exception e) {
            logger.error("查询表是否存在发生异常", e);
            pd.setResult(-1, "发生异常");
        }
        return pd;
    }

    /**
     * 查询数据库的所有表
     *
     * @return
     */
    @RequestMapping("/allTable")
    public Object allTable() {
        PageData pd = this.getPageData();
        try {
            List<PageData> list = sqlTableStructureService.getAllTable();
            pd.put("list", list);
        } catch (Exception e) {
            logger.error("获取所有表数据发生异常", e);
        }
        return pd;
    }

    /**
     * 根据字段等配置生成代码
     */
    @RequestMapping("/proCodeOnItems")
    public void proCodeOnItems(HttpServletResponse response) {
        PageData pd = this.getPageData();
        try {

            //创建数据模型
            Map<String, Object> params = new HashMap<>(10);
            //包路径
            String packagePath = pd.getString("packagePath");
            //类名
            String objectName = pd.getString("objectName");
            //mapper、service之类的名称前缀
            String prefixName = objectName.substring(0, 1).toLowerCase() + objectName.substring(1);
            //属性总数
            Integer zindex = pd.getInt("zindex");
            //获取dao类型
            Integer daoType = pd.getInt("daoType");
            params.put("daoType", daoType);
            //dao包名
            String daoPath = Tools.isEmpty(pd.getString("daoPath")) ? "dao" : pd.getString("daoPath");
            params.put("daoPath", daoPath);
            //获取实体类类型
            int entityType = pd.getInt("entityType");
            params.put("entityType", entityType);
            //实体类包名
            String entityPath = Tools.isEmpty(pd.getString("entityPath")) ? "entity" : pd.getString("entityPath");
            params.put("entityPath", entityPath);
            //属性集合
            List<String[]> fieldList = new ArrayList<String[]>();
            for (int i = 0; i < zindex; i++) {
                String field = pd.getString("field" + i);
                //属性放到集合里面
                fieldList.add(field.split(",fh,"));
            }
            //获取类名的大写字母下标
            int[] uppers = ProductCodeTool.getUpperCaseIndex(objectName);
            //获取表名称
            String tableName = ProductCodeTool.getTableNameOnArray(objectName, uppers);
            //添加数据库字段
            ProductCodeTool.setItemsNameOnList(fieldList);

            //设置主键信息
            PageData keyFiled = new PageData();
            keyFiled.put("name", "id").put("filed", "id").put("mysqlType", "int(18)").put("pgsqlType", "int8").put("type", "bigint");
            params.put("keyFiled", keyFiled);

            //封装参数
            SettingInfo settingInfo = new SettingInfo().setAddPackage(false).setDaoPath(daoPath).setDaoType(daoType).setEntityPath(entityPath)
                    .setEntityType(entityType).setFieldList(fieldList).setObjectName(objectName).setPackagePath(packagePath).setPrefixName(prefixName)
                    .setTableName(tableName).setZindex(zindex);

            //设置生成参数
            ProductCodeTool.setParamsInfo(params, settingInfo);

            //生成代码
            ProductCodeTool.printFileByObject(params, settingInfo);

            //生成的全部代码压缩成zip文件
            FileZip.zip(PathUtil.getClasspath() + "ftl/code", PathUtil.getClasspath() + "ftl/" + prefixName + "code.zip");
            //下载代码
            FileDownload.fileDownload(response, PathUtil.getClasspath() + "ftl/" + prefixName + "code.zip", prefixName + "code.zip");
            //最后清空之前生成的代码
            DelAllFile.delFolder(PathUtil.getClasspath() + "ftl");

        } catch (Exception e) {
            logger.error("生成代码发生异常", e);
        }
    }

    /**
     * 根据表名称等配置生成代码
     */
    @RequestMapping("/proCodeOnTable")
    public void proCodeOnTable(HttpServletResponse response) {
        PageData pd = this.getPageData();
        try {

            //创建数据模型
            Map<String, Object> params = new HashMap<>(10);
            //包路径
            String packagePath = pd.getString("packagePath");
            //表名
            String tableName = pd.getString("objectName");
            //获取表结构等信息
            PageData tableInfo = sqlTableStructureService.getTableInfo(tableName);
            //获取dao类型
            Integer daoType = pd.getInt("daoType");
            params.put("daoType", daoType);
            //dao包名
            String daoPath = Tools.isEmpty(pd.getString("daoPath")) ? "dao" : pd.getString("daoPath");
            params.put("daoPath", daoPath);
            //获取实体类类型
            int entityType = pd.getInt("entityType");
            params.put("entityType", entityType);
            //实体类包名
            String entityPath = Tools.isEmpty(pd.getString("entityPath")) ? "entity" : pd.getString("entityPath");
            params.put("entityPath", entityPath);
            //获取表名的下划线下标数组
            int[] lines = ProductCodeTool.getUnderlineIndex(tableName);
            //获取类名称
            String objectName = ProductCodeTool.getObjectNameOnArray(tableName, lines);
            //mapper、service之类的名称前缀
            String prefixName = objectName.substring(0, 1).toLowerCase() + objectName.substring(1);
            //根据表信息设置对应的字段信息
            PageData fieldInfo = ProductCodeTool.setItemsInfo(tableInfo);
            //属性集合
            List<String[]> fieldList = (List<String[]>) fieldInfo.getObject("fieldList");
            //属性总数
            Integer zindex = fieldInfo.getInt("zindex");

            //设置主键信息
            ProductCodeTool.setKeyFiled(params, tableInfo, fieldList, tableName, sqlTableStructureService);

            //封装参数
            SettingInfo settingInfo = new SettingInfo().setAddPackage(false).setDaoPath(daoPath).setDaoType(daoType).setEntityPath(entityPath)
                    .setEntityType(entityType).setFieldList(fieldList).setObjectName(objectName).setPackagePath(packagePath).setPrefixName(prefixName)
                    .setTableName(tableName).setZindex(zindex);

            //设置生成参数
            ProductCodeTool.setParamsInfo(params, settingInfo);

            //生成代码
            ProductCodeTool.printFileByObject(params, settingInfo);

            //生成的全部代码压缩成zip文件
            FileZip.zip(PathUtil.getClasspath() + "ftl/code", PathUtil.getClasspath() + "ftl/" + prefixName + "code.zip");
            //下载代码
            FileDownload.fileDownload(response, PathUtil.getClasspath() + "ftl/" + prefixName + "Code.zip", prefixName + "code.zip");
            //最后清空之前生成的代码
            DelAllFile.delFolder(PathUtil.getClasspath() + "ftl");

        } catch (Exception e) {
            logger.error("生成代码发生异常", e);
        }
    }

    /**
     * 根据选择的多表等配置生成代码
     */
    @RequestMapping("/proCodeOnAllTable")
    public void proCodeOnAllTable(HttpServletResponse response) {
        PageData pd = this.getPageData();
        try {

            //创建数据模型
            Map<String, Object> params = new HashMap<>(10);
            //包路径
            String packagePath = pd.getString("packagePath");
            //获取dao类型
            Integer daoType = pd.getInt("daoType");
            params.put("daoType", daoType);
            //dao包名
            String daoPath = Tools.isEmpty(pd.getString("daoPath")) ? "dao" : pd.getString("daoPath");
            params.put("daoPath", daoPath);
            //获取实体类类型
            int entityType = pd.getInt("entityType");
            params.put("entityType", entityType);
            //实体类包名
            String entityPath = Tools.isEmpty(pd.getString("entityPath")) ? "entity" : pd.getString("entityPath");
            params.put("entityPath", entityPath);
            //表名集合
            String tableNames = pd.getString("objectName");
            String[] tables = tableNames.split(",");
            //自动生成失败的表名
            String errorTables = "";
            //循环生成
            for (String tableName : tables) {
                try {
                    //获取表结构等信息
                    PageData tableInfo = sqlTableStructureService.getTableInfo(tableName);
                    //获取表名的下划线下标数组
                    int[] lines = ProductCodeTool.getUnderlineIndex(tableName);
                    //获取类名称
                    String objectName = ProductCodeTool.getObjectNameOnArray(tableName, lines);
                    //mapper、service之类的名称前缀
                    String prefixName = objectName.substring(0, 1).toLowerCase() + objectName.substring(1);
                    //根据表信息设置对应的字段信息
                    PageData fieldInfo = ProductCodeTool.setItemsInfo(tableInfo);
                    //属性集合
                    List<String[]> fieldList = (List<String[]>) fieldInfo.getObject("fieldList");
                    //属性总数
                    Integer zindex = fieldInfo.getInt("zindex");

                    //设置主键信息
                    ProductCodeTool.setKeyFiled(params, tableInfo, fieldList, tableName, sqlTableStructureService);

                    //封装参数
                    SettingInfo settingInfo = new SettingInfo().setAddPackage(true).setDaoPath(daoPath).setDaoType(daoType).setEntityPath(entityPath)
                            .setEntityType(entityType).setFieldList(fieldList).setObjectName(objectName).setPackagePath(packagePath)
                            .setPrefixName(prefixName).setTableName(tableName).setZindex(zindex);

                    //设置生成参数
                    ProductCodeTool.setParamsInfo(params, settingInfo);

                    //生成代码
                    ProductCodeTool.printFileByObject(params, settingInfo);
                } catch (Exception e) {
                    logger.error("自动生成代码失败", e);
                    errorTables += tableName + ",";
                }
            }
            //生成的全部代码压缩成zip文件
            String zipName = OfTime.getLongDetailTime() + ".zip";
            FileZip.zip(PathUtil.getClasspath() + "ftl/code",
                    PathUtil.getClasspath() + "ftl/" + zipName);
            //下载代码
            FileDownload.fileDownload(response, PathUtil.getClasspath() + "ftl/" + zipName, zipName);
            //最后清空之前生成的代码
            DelAllFile.delFolder(PathUtil.getClasspath() + "ftl");

            //如果有生成失败的 ，打印信息
            if (Tools.notEmpty(errorTables)) {
                logger.error("表" + errorTables.substring(0, errorTables.length() - 1) + "生成失败");
            }
        } catch (Exception e) {
            logger.error("生成代码发生异常", e);
            //清空之前生成的代码
            DelAllFile.delFolder(PathUtil.getClasspath() + "ftl");
        }
    }

}
