package com.claris.generatingcode.util;

import lombok.*;
import lombok.experimental.Accessors;

import java.util.List;

/**
 * 生成参数类
 */
@Data
@NoArgsConstructor
@Accessors(chain = true)
public class SettingInfo {

    /**
     * 属性集合
     */
    private List<String[]> fieldList;

    /**
     * 字段数量
     */
    private Integer zindex;

    /**
     * 包路径
     */
    private String packagePath;

    /**
     * 类名
     */
    private String objectName;

    /**
     * 表名
     */
    private String tableName;

    /**
     * 前缀名称
     */
    private String prefixName;

    /**
     * dao包名
     */
    private String daoPath;

    /**
     * entity包名
     */
    private String entityPath;

    /**
     * 实体类类型
     */
    private Integer entityType;

    /**
     * dao类型
     */
    private Integer daoType;

    /**
     * 是否在生成时加上层路径
     */
    private boolean addPackage;

}
