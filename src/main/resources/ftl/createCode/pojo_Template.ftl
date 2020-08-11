package ${packagePath}.${entityPath};

import lombok.*;
import lombok.experimental.Accessors;
import com.alibaba.fastjson.annotation.JSONField;
import java.util.*;

@Data
@NoArgsConstructor
@Accessors(chain = true)
public class ${objectName} {

<#if keyFiled.type == 'int'>
    private Integer ${keyFiled.filed};
<#elseif keyFiled.type == 'bigint'>
    private Long ${keyFiled.filed};
<#else>
    private String ${keyFiled.filed};
</#if>

<#list fieldList as var>
    <#if var[1] == 'varchar' || var[1] == 'char' || var[1] == 'text'>
    private String ${var[0]};

    <#elseif var[1] == 'int'>
    private Integer ${var[0]};

    <#elseif var[1] == 'bigint'>
    private Long ${var[0]};

    <#elseif var[1] == 'decimal' || var[1] == 'float' || var[1] == 'double'>
    private Double ${var[0]};

    <#elseif var[1] == 'date'>
    @JSONField(format = "yyyy-MM-dd")
    private Date ${var[0]};

    <#elseif var[1] == 'timestamp'>
    @JSONField(format = "yyyy-MM-dd HH:mm:ss")
    private Date ${var[0]};

    <#else>
    private String ${var[0]};

    </#if>
</#list>

}