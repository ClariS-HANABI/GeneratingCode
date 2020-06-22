package com.claris.generatingcode.util;

import com.claris.generatingcode.service.SqlTableStructureService;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 生成代码工具类
 *
 * @author xieyueyang
 */
public class ProductCodeTool {

    //mysql类型对应的pgsql类型  sql脚本
    private final static Map<String, String> MysqlCastPgsql_SQL = new HashMap<String, String>() {{
        put("", "");
    }};

    //pgsql类型对应mysql的类型  sql脚本
    private final static Map<String, String> PgsqlCastMysql_SQL = new HashMap<String, String>() {{
        put("", "");
    }};

    //判断字符串是否为正数
    private static Pattern pattern = Pattern.compile("[1-9]+\\d*");


    /**
     * 根据类名和配置参数生成代码
     *
     * @param params 配置参数集合
     * @param info   生成参数封装
     * @throws Exception
     */
    public static void printFileByObject(Map<String, Object> params, SettingInfo info) throws Exception {
        //存放路径
        String filePath = "ftl/code/";
        //ftl路径
        String ftlPath = "createCode";
        //如果是实体类，生成entity
        if (info.getEntityType() == 1) {
            Freemarker.printFile("pojo_Template.ftl", params, (info.isAddPackage() ? "/" + info.getEntityPath() + "/" : "")
                    + info.getObjectName() + ".java", filePath, ftlPath);
        }
        //如果是普通DAO，生成DAO
        if (info.getDaoType() == 1) {
            Freemarker.printFile("dao_Template.ftl", params, (info.isAddPackage() ? "/"+ info.getDaoPath() + "/" : "")
                    + info.getObjectName() + "Mapper.java", filePath, ftlPath);
        }
        //生成controller
        Freemarker.printFile("my_controller_Template.ftl", params, (info.isAddPackage() ? "/controller/" : "")
                + info.getObjectName() + "Controller.java", filePath, ftlPath);
        //生成service和impl
        Freemarker.printFile("service_Template.ftl", params, "/service/" + info.getObjectName()
                + "Service.java", filePath, ftlPath);
        Freemarker.printFile("service_impl_Template.ftl", params, "/service/impl/" + info.getObjectName()
                + "ServiceImpl.java", filePath, ftlPath);
        //生成mybatis xml
        Freemarker.printFile("mapper_mySql_Template.ftl", params, "mapper/mySql/" + info.getObjectName()
                + "Mapper.xml", filePath, ftlPath);
        Freemarker.printFile("mapper_postgreSql_Template.ftl", params, "mapper/postgreSql/" + info.getObjectName()
                + "Mapper.xml", filePath, ftlPath);
        //生成SQL脚本
        Freemarker.printFile("mysql_sql_Template.ftl", params, "sql/mySql/" + info.getPrefixName() + ".sql", filePath, ftlPath);
        Freemarker.printFile("postgreSql_sql_Template.ftl", params, "sql/postgreSql/" + info.getPrefixName()
                + ".sql", filePath, ftlPath);
        //生成jsp页面
        Freemarker.printFile("myjsp_Template.ftl", params, (info.isAddPackage() ? "/view/" : "")
                + info.getPrefixName() + "View.jsp", filePath, ftlPath);
    }

    /**
     * 设置生成参数
     *
     * @param params 参数集合
     * @param info   生成参数封装
     */
    public static void setParamsInfo(Map<String, Object> params, SettingInfo info) {
        //字段属性集合
        params.put("fieldList", info.getFieldList());
        //字段数量
        params.put("zindex", info.getZindex());
        //包路径
        params.put("packagePath", info.getPackagePath());
        //类名
        params.put("objectName", info.getObjectName());
        //表名称
        params.put("tableName", info.getTableName());
        //类名(全小写)
        params.put("objectNameLower", info.getObjectName().toLowerCase());
        //类名(全大写)
        params.put("objectNameUpper", info.getObjectName().toUpperCase());
        //mapper、service之类的名称前缀
        params.put("prefixName", info.getPrefixName());
        //当前日期
        params.put("nowDate", new Date());
        //paramsType和result的类型
        String pojoName = info.getPackagePath() + "." + info.getEntityPath() + "." + info.getObjectName();
        params.put("entityName", info.getEntityType() == 1 ? pojoName : "pd");
        params.put("result", "resultType=" + (info.getEntityType() == 1 ? "\"" + pojoName + "\"" : "\"pd\""));
        params.put("entityClass", info.getEntityType() == 1 ? "java.util.Map" : "pd");
        params.put("paramsType", info.getEntityType() == 1 ? info.getObjectName() : "PageData");
    }

    /**
     * 设置主键信息
     *
     * @param params                   参数集合
     * @param tableInfo                表结构等信息
     * @param fieldList                字段属性集合
     * @param tableName                表名
     * @param sqlTableStructureService 数据库查询对象
     */
    public static void setKeyFiled(Map<String, Object> params, PageData tableInfo, List<String[]> fieldList,
                                   String tableName, SqlTableStructureService sqlTableStructureService) {
        //主键属性
        PageData keyFiled = new PageData();
        //当前连接数据库类型
        DatabaseType databaseType = DatabaseType.valueOf(tableInfo.getString("databaseType"));
        //主键信息
        PageData pk = null;
        switch (databaseType) {
            //如果是mysql
            case MYSQL:
                //获取主键信息
                pk = (PageData) tableInfo.getObject("pk");
                if (pk != null) {
                    String data_type = pk.getString("data_type");
                    keyFiled.put("name", pk.getString("column_name")).put("mysqlType", pk.getString("column_type"));
                    if ("bigint".equals(data_type)) {
                        keyFiled.put("pgsqlType", "int8").put("type", "bigint");
                    } else if ("int".equals(data_type)) {
                        keyFiled.put("pgsqlType", "int4").put("type", "int");
                    } else {
                        keyFiled.put("pgsqlType", "varchar(55)").put("type", "varchar");
                    }
                    //删除主键列
                    for (int i = 0; i < fieldList.size(); i++) {
                        String[] str = fieldList.get(i);
                        if (str[5].equals(pk.getString("column_name"))) {
                            keyFiled.put("filed", str[0]);
                            fieldList.remove(i);
                            break;
                        }
                    }
                }
                //如果没有主键
                else {

                }
                break;
            //如果是pgsql
            case POSTGRESQL:
                //获取主键信息
                pk = sqlTableStructureService.getPrimaryKeyByName(new PageData().put("tableName", tableName));
                if (pk != null) {
                    for (int i = 0; i < fieldList.size(); i++) {
                        String[] str = fieldList.get(i);
                        if (str[5].equals(pk.getString("colname"))) {
                            //设置主键信息
                            keyFiled.put("name", pk.getString("colname")).put("pgsqlType", pk.getString("typename"));
                            if (str[1].equals("int")) {
                                keyFiled.put("mysqlType", "int(11)").put("type", "int");
                            } else if (str[1].equals("bigint")) {
                                keyFiled.put("mysqlType", "int(18)").put("type", "bigint");
                            } else {
                                keyFiled.put("mysqlType", "varchar(55)").put("type", "varchar");
                            }
                            keyFiled.put("filed", str[0]);
                            //删除主键列
                            fieldList.remove(i);
                            break;
                        }
                    }
                }
                //如果没有主键
                else {

                }
                break;
        }
        params.put("keyFiled", keyFiled);
    }

    /**
     * 获取类名大写字母下标
     *
     * @param objectName 类名
     * @return 大写字母下标数组
     * @throws Exception
     */
    public static int[] getUpperCaseIndex(String objectName) throws Exception {
        int[] uppers = new int[10];
        //大写字母数量
        int upperCount = 0;
        if (!Tools.isEmpty(objectName)) {
            for (int i = 0; i < objectName.length(); i++) {
                char ch = objectName.charAt(i);
                if (Character.isUpperCase(ch)) {
                    uppers[upperCount] = i;
                    upperCount++;
                }
            }
            if (upperCount == 0) {
                return null;
            } else {
                int[] array = new int[upperCount];
                for (int i = 0; i < upperCount; i++) {
                    array[i] = uppers[i];
                }
                return array;
            }
        } else {
            return null;
        }
    }

    /**
     * 获取表名的下划线下标数组
     *
     * @param tableName 表名
     * @return 下划线下标数组
     * @throws Exception
     */
    public static int[] getUnderlineIndex(String tableName) throws Exception {
        int[] lines = new int[10];
        //下划线数量
        int lineCount = 0;
        if (!Tools.isEmpty(tableName)) {
            int index = 0;
            do {
                index = tableName.indexOf("_", index == 0 ? 0 : index + 1);
                if (index > -1) {
                    lines[lineCount] = index;
                    lineCount++;
                }
            } while (index > -1);
            if (lineCount == 0) {
                return null;
            } else {
                int[] array = new int[lineCount];
                for (int i = 0; i < lineCount; i++) {
                    array[i] = lines[i];
                }
                return array;
            }
        } else {
            return null;
        }
    }

    /**
     * 生成数据库表名称
     *
     * @param objectName 类名
     * @param array      大写字母下标数组
     * @return 数据库表名称
     */
    public static String getTableNameOnArray(String objectName, int[] array) throws Exception {
        if (Tools.isEmpty(objectName)) {
            return null;
        }
        StringBuffer tableName = new StringBuffer(objectName);
        if (array != null && array.length > 0) {
            int len = array.length;
            if (len > 1) {
                //循环加入_符号, 因为加入后原来的索引会增加，所以在原来获取到的下标加上增加循环的变量
                for (int i = 1; i < len; i++) {
                    tableName.insert(i == 1 ? array[i] : array[i] + i - 1, "_");
                }
            }
        }
        //转换为小写
        return tableName.toString().toLowerCase();
    }

    /**
     * 生成类名称
     *
     * @param tableName 表名
     * @param array     下划线下标数组
     * @return 类名称
     */
    public static String getObjectNameOnArray(String tableName, int[] array) throws Exception {
        if (Tools.isEmpty(tableName)) {
            return null;
        }
        //把第一个字母变为大写
        StringBuffer objectName = new StringBuffer(tableName.substring(0, 1).toUpperCase() + tableName.substring(1));
        if (array != null && array.length > 0) {
            //循环把_后面的首字母变为大写
            for (int i = 0; i < array.length; i++) {
                char ch = objectName.charAt(array[i] + 1);
                String str = String.valueOf(ch).toUpperCase();
                objectName.setCharAt(array[i] + 1, str.charAt(0));
            }
        }
        //去除下划线
        return objectName.toString().replace("_", "");
    }

    /**
     * 根据字段名称添加数据库规范字段名称
     *
     * @param items 表字段属性集合
     * @return
     */
    public static void setItemsNameOnList(List<String[]> items) throws Exception {
        if (items != null && items.size() > 0) {
            //循环字段添加
            for (int i = 0; i < items.size(); i++) {
                String[] item = items.get(i);
                StringBuffer sqlItemName = null;
                //获取大写字母的下标
                int[] uppers = new int[10];
                int upperCount = 0;
                for (int j = 0; j < item[0].length(); j++) {
                    char ch = item[0].charAt(j);
                    if (Character.isUpperCase(ch)) {
                        uppers[upperCount] = j;
                        upperCount++;
                    }
                }
                if (upperCount == 0) {
                    //如果没有大写字母
                    sqlItemName = new StringBuffer(item[0]);
                } else {
                    //循环加入_符号, 因为加入后原来的索引会增加，所以在原来获取到的下标加上增加循环的变量
                    sqlItemName = new StringBuffer(item[0]);
                    for (int j = 0; j < uppers.length; j++) {
                        if (uppers[j] > 0) {
                            sqlItemName.insert(uppers[j] + j, "_");
                        }
                    }
                    //把大写字母变为小写
                    sqlItemName = new StringBuffer(sqlItemName.toString().toLowerCase());
                }
                //因为长度不够，所以重新建一个数组
                String[] str = new String[6];
                for (int s = 0; s < item.length; s++) {
                    str[s] = item[s];
                }
                str[5] = sqlItemName.toString();
                //填充
                items.set(i, str);
            }
        }
    }

    /**
     * 根据字段名称添加属性名称
     *
     * @param fileds 表字段属性集合
     * @return
     */
    public static void setFiledsNameOnList(List<String[]> fileds) throws Exception {
        if (fileds != null && fileds.size() > 0) {
            //循环字段添加
            for (int i = 0; i < fileds.size(); i++) {
                String[] filed = fileds.get(i);
                StringBuffer filedName = null;
                //获取下划线的下标
                int[] lines = new int[10];
                int lineCount = 0;
                int index = 0;
                do {
                    index = filed[5].indexOf("_", index == 0 ? 0 : index + 1);
                    if (index > -1) {
                        lines[lineCount] = index;
                        lineCount++;
                    }
                } while (index > -1);
                if (lineCount == 0) {
                    //如果没有下划线
                    filedName = new StringBuffer(filed[5]);
                } else {
                    //循环把_后面的首字母变为大写
                    filedName = new StringBuffer(filed[5]);
                    for (int j = 0; j < lines.length; j++) {
                        if (lines[j] > 0) {
                            char ch = filedName.charAt(lines[j] + 1);
                            String str = String.valueOf(ch).toUpperCase();
                            filedName.setCharAt(lines[j] + 1, str.charAt(0));
                        }
                    }
                    //把下划线去掉
                    filedName = new StringBuffer(filedName.toString().replace("_", ""));
                }
                //因为长度不够，所以重新建一个数组
                String[] str = new String[6];
                for (int s = 1; s < filed.length; s++) {
                    str[s] = filed[s];
                }
                str[0] = filedName.toString();
                //填充
                fileds.set(i, str);
            }
        }
    }

    /**
     * 根据数据库连接字符串返回数据库类型和数据库名称
     *
     * @param url 数据库连接字符
     * @return
     */
    public static PageData getTableInfo(String url) throws Exception {
        PageData pd = new PageData();
        String databaseName = "";
        if (url.indexOf("?") > -1) {
            databaseName = url.substring(url.lastIndexOf("/") + 1, url.indexOf("?"));
        } else {
            databaseName = url.substring(url.lastIndexOf("/") + 1);
        }
        String str = url.substring(url.indexOf("jdbc:") + 5, url.indexOf("://"));
        String databaseType = str.toUpperCase();
        pd.put("databaseName", databaseName);
        pd.put("databaseType", databaseType);
        return pd;
    }

    /**
     * 根据数据库获取到的表结构设置对应的字段信息
     *
     * @param pd
     * @return
     */
    public static PageData setItemsInfo(PageData pd) throws Exception {
        PageData result = new PageData();
        //根据表结构的各种信息进行设置
        List<PageData> filedsInfo = (List<PageData>) pd.getObject("filedsInfo");
        DatabaseType databaseType = DatabaseType.valueOf(pd.getString("databaseType"));
        List<String[]> fieldList = new ArrayList<>();
        switch (databaseType) {
            case MYSQL:
                for (PageData filed : filedsInfo) {
                    String[] info = new String[6];
                    //字段类型
                    info[1] = filed.getString("data_type");
                    //类型长度
                    info[2] = filed.getString("type_length");
                    //如果为长度为空（有部分列的长度为空）,截取可能存在的长度值
                    if (Tools.isEmpty(info[2])) {
                        String columnType = filed.getString("column_type");
                        int left = columnType.indexOf("(");
                        int right = columnType.indexOf(")");
                        if (left != -1 && right != -1) {
                            String length = columnType.substring(left + 1, right);
                            //判断是否为正整数
                            Matcher isNum = pattern.matcher(length);
                            if (isNum.find()) {
                                info[2] = isNum.group(0);
                            }
                        }
                    }
                    //字段注释
                    info[3] = filed.getString("column_comment");
                    //是否不为空
                    info[4] = "NO".equals(filed.getString("is_nullable")) ? "1" : "0";
                    //字段名
                    info[5] = filed.getString("column_name");
                    fieldList.add(info);
                }
                break;
            case POSTGRESQL:
                for (PageData filed : filedsInfo) {
                    String[] info = new String[6];
                    //字段类型
                    String udt_name = filed.getString("udt_name");
                    String type = null;
                    switch (udt_name) {
                        case "int2":
                        case "int4":
                            type = "int";
                            break;
                        case "int8":
                            type = "bigint";
                            break;
                        case "float4":
                            type = "float";
                            break;
                        case "float8":
                            type = "double";
                            break;
                        default:
                            type = udt_name;
                    }
                    info[1] = type;
                    //类型长度
                    String length = filed.getString("type_length");
                    //pgsql的一些类型没有长度，所以加上判断
                    if (type.equals("int")) {
                        length = "11";
                    } else if (type.equals("bigint")) {
                        length = "18";
                    }
                    info[2] = length;
                    //字段注释
                    info[3] = filed.getString("column_comment");
                    //是否不为空
                    info[4] = "NO".equals(filed.getString("is_nullable")) ? "1" : "0";
                    //字段名
                    info[5] = filed.getString("column_name");
                    fieldList.add(info);
                }
                break;
        }
        //添加属性名称
        setFiledsNameOnList(fieldList);
        //返回结果
        result.put("fieldList", fieldList);
        result.put("zindex", fieldList.size());
        return result;
    }

    public enum DatabaseType {
        /**
         * mysql数据库类型
         */
        MYSQL,
        /**
         * postgresql数据库类型
         */
        POSTGRESQL;
    }


}
