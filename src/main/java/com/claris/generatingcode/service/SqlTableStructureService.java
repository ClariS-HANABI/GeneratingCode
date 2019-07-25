package com.claris.generatingcode.service;

import com.claris.generatingcode.dao.DaoSupport;
import com.claris.generatingcode.util.PageData;
import com.claris.generatingcode.util.ProductCodeTool;
import com.claris.generatingcode.util.ProductCodeTool.DatabaseType;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

@Service
public class SqlTableStructureService {

    private static Logger logger = Logger.getLogger(SqlTableStructureService.class);

    @Resource(name = "daoSupport")
    private DaoSupport dao;

    @Value("${spring.datasource.url}")
    private String url;


    /**
     * 根据表名获取表结构  MySql
     *
     * @param pd
     * @return
     */
    public List<PageData> getMySqlStructureByName(PageData pd) {
        List<PageData> list = null;
        try {
            Object obj = dao.findForList("SqlTableStructureMapper.getMySqlStructureByName", pd);
            list = obj == null ? null : (List<PageData>) obj;
        } catch (Exception e) {
            logger.error("获取表结构发生异常", e);
        }
        return list;
    }

    /**
     * 根据表名获取表结构  PostgreSql
     *
     * @param pd
     * @return
     */
    public List<PageData> getPostgreSqlStructureByName(PageData pd) {
        List<PageData> list = null;
        try {
            Object obj = dao.findForList("SqlTableStructureMapper.getPostgreSqlStructureByName", pd);
            list = obj == null ? null : (List<PageData>) obj;
        } catch (Exception e) {
            logger.error("获取表结构发生异常", e);
        }
        return list;
    }

    /**
     * 查询表主键列相关信息  PostgreSql
     * 不排除有几个主键，默认取第一个，其它情况暂不进行处理
     *
     * @param pd
     * @return
     */
    public PageData getPrimaryKeyByName(PageData pd) {
        PageData result = null;
        try {
            Object obj = dao.findForObject("SqlTableStructureMapper.getPrimaryKeyByName", pd);
            result = obj == null ? null : (PageData) obj;
        } catch (Exception e) {
            logger.error("获取表主键列发生异常", e);
        }
        return result;
    }

    /**
     * 查询表是否存在
     *
     * @param tableName
     * @return
     */
    public int isExistTable(String tableName) {
        int isExist = 0;
        try {
            //获取数据库类型和数据库名称
            PageData pd = ProductCodeTool.getTableInfo(url);
            DatabaseType databaseType = DatabaseType.valueOf(pd.getString("databaseType"));
            //查询表是否存在
            pd.put("tableName", tableName);
            if (DatabaseType.POSTGRESQL.equals(databaseType)) {
                pd.put("selectType", 1);
            }
            Object obj = dao.findForObject("SqlTableStructureMapper.isExistTable", pd);
            isExist = obj == null ? 0 : (int) obj;
        } catch (Exception e) {
            logger.error("查询表是否存在发生异常", e);
        }
        return isExist;
    }

    /**
     * 根据数据库表名获取表信息等数据
     *
     * @param tableName 表名称
     * @return
     */
    public PageData getTableInfo(String tableName) {
        PageData pd = new PageData();
        try {
            //获取数据库类型和数据库名称
            PageData database = ProductCodeTool.getTableInfo(url);
            DatabaseType databaseType = DatabaseType.valueOf(database.getString("databaseType"));
            database.put("tableName", tableName);
            //获取表结构
            List<PageData> filedsInfo = null;
            if (DatabaseType.MYSQL.equals(databaseType)) {
                filedsInfo = getMySqlStructureByName(database);
                if (filedsInfo != null) {
                    //循环获取主键
                    for (PageData var : filedsInfo) {
                        if ("PRI".equals(var.getString("column_key"))) {
                            pd.put("pk", var);
                            break;
                        }
                    }
                }
            } else if (DatabaseType.POSTGRESQL.equals(databaseType)) {
                filedsInfo = getPostgreSqlStructureByName(database);
            }
            if (filedsInfo == null) {
                return pd.setResult(0, "该表无字段，请输入新的表名称！");
            }
            pd.put("databaseType", database.get("databaseType"));
            pd.put("databaseName", database.get("databaseName"));
            pd.put("filedsInfo", filedsInfo);
        } catch (Exception e) {
            logger.error("获取表信息等数据发生异常", e);
        }
        return pd;
    }

    /**
     * 获取数据库所有表
     * @return
     */
    public List<PageData> getAllTable(){
        List<PageData> list = null;
        try{
            //获取数据库类型和数据库名称
            PageData database = ProductCodeTool.getTableInfo(url);
            DatabaseType databaseType = DatabaseType.valueOf(database.getString("databaseType"));
            //如果是mysql
            String functionName = "";
            if (DatabaseType.MYSQL.equals(databaseType)) {
                functionName = "getMySqlAllTable";
            } //如果是pgsql
            else if (DatabaseType.POSTGRESQL.equals(databaseType)) {
                functionName = "getPostgreSqlAllTable";
            }
            Object obj = dao.findForList("SqlTableStructureMapper." +  functionName, database);
            list = obj != null ? (List<PageData>)obj : null;
        }catch(Exception e){
            logger.error("获取所有表数据发生异常", e);
        }
        return list;
    }

}
