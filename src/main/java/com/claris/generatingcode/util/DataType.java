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

    //类型
    private String type;

    //长度
    private String length;

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
    public DataType(String type, String length, String mysqlType, String pgsqlType){
        this.type = type;
        this.length = length;
        this.mysqlType = mysqlType;
        this.pgsqlType = pgsqlType;
    }

}
