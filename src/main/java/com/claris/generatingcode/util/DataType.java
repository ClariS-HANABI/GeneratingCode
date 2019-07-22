package com.claris.generatingcode.util;

import lombok.*;
import java.io.Serializable;
import java.util.*;

@NoArgsConstructor
@Getter
@Setter
@ToString
@EqualsAndHashCode
public class DataType implements Serializable {

    //通用类型
    private String type;

    //长度
    private String length;

    //mapper的jdbc类型
    private String jdbcType;

    //mysql类型
    private String mysqlType;

    //pgsql类型
    private String pgsqlType;

    /**
     * 初始化构造参数
     * @param type
     * @param length
     * @param mysqlType
     * @param pgsqlType
     */
    public DataType(String type, String length, String jdbcType, String mysqlType, String pgsqlType){
        this.type = type;
        this.length = length;
        this.jdbcType = jdbcType;
        this.mysqlType = mysqlType;
        this.pgsqlType = pgsqlType;
    }

}
