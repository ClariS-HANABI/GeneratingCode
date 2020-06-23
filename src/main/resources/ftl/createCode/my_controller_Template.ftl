package ${packagePath}.controller;

import java.util.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
<#if entityType != 1>import ${packagePath}.controller.BaseController;</#if>
import com.github.pagehelper.*;
import javax.servlet.http.*;
import lombok.extern.slf4j.Slf4j;
import ${packagePath}.service.${objectName}Service;
<#if entityType == 1>
import ${packagePath}.${entityPath}.${objectName};
</#if>

/**
 * 创建时间：${nowDate?string("yyyy-MM-dd")}
 */
@Slf4j
@Controller
@RequestMapping(value = "/${prefixName}")
public class ${objectName}Controller<#if entityType != 1> extends BaseController </#if>{
	
	@Autowired
	private ${objectName}Service ${prefixName}Service;


	/**
	* 进入管理页面
	*/
	@RequestMapping(value="/view")
	public String ${prefixName}(){
		return "${prefixName}View";
	}

	/**
	* 查询详情
	* @param id
	* @return
	*/
	@RequestMapping(value = "/info/{id}", method = RequestMethod.GET, produces = "application/json;charset=UTF-8")
	@ResponseBody
	public Object getById(@PathVariable <#if keyFiled.type == 'int'>Integer<#elseif keyFiled.type == 'bigint'>Long<#else>String</#if> id){
	<#if keyFiled.type == 'int'>
		if(id == null || id.intValue() <= 0){
	<#elseif keyFiled.type == 'bigint'>
		if(id == null || id.longValue() <= 0){
	<#else>
		if(Tools.isEmpty(id)){
	</#if>
			return Tools.setResult(203, "id为空");
		}
	<#if entityType == 1>
		Map<String, Object> result = Tools.setResult(200, "查询成功");
		try {
			${objectName} ${objectNameLower} = ${prefixName}Service.findById(id);
			result.put("${objectNameLower}", ${objectNameLower});
		} catch (Exception e){
			result = Tools.setResult(500, "查询发生异常");
			log.error("根据id查询数据发生异常", e);
		}
		return result;
    <#else>
        PageData pd = this.getPageData();
        try{
			PageData result = ${prefixName}Service.findById(id);
			pd.setResult(200, "查询成功").put("${objectNameLower}", result);
        } catch(Exception e){
			log.error("根据id查询数据发生异常", e);
			pd.setResult(500, "查询发生异常");
        }
        return pd;
	</#if>
	}

	/**
	* 分页数据
	*/
	@RequestMapping(value = "/page", method = RequestMethod.GET, produces = "application/json;charset=UTF-8")
	@ResponseBody
	public Object page(@RequestParam(defaultValue = "1") Integer pn, @RequestParam(defaultValue = "10") Integer size,
		<#if entityType == 1> ${objectName} ${prefixName}, </#if>HttpServletResponse response){
	<#if entityType != 1>
		PageData pd = this.getPageData();
	<#else>
		Map<String, Object> map = null;
	</#if>
		try{
			response.setContentType("text/html;charset=utf-8");//设置页面的字符编码
			PageHelper.startPage(pn, size);
			List<${paramsType}> varList = ${prefixName}Service.listAll(<#if entityType == 1>${prefixName}<#else>pd</#if>);
			PageInfo page = new PageInfo(varList, 5);
		<#if entityType == 1>
			map = Tools.setResult(200, "查询分页数据成功");
			map.put("pageInfo",page);
		<#else>
			pd.setResult(200, "查询分页数据成功").pd.put("pageInfo",page);
		</#if>
		} catch(Exception e){
			log.error("查询分页数据发生异常", e);
		<#if entityType == 1>
			map = Tools.setResult(500, "查询分页数据发生异常");
		<#else>
			pd.setResult(500, "查询分页数据发生异常");
		</#if>
		}
    	return <#if entityType == 1>map<#else>pd</#if>;
    }

	/**
	* 获取数据集合
	*/
	@RequestMapping(value = "/list", method = RequestMethod.GET, produces = "application/json;charset=UTF-8")
	@ResponseBody
	public Object list(<#if entityType == 1>${objectName} ${prefixName}</#if>){
	<#if entityType != 1>
		PageData pd = this.getPageData();
	<#else>
		Map<String, Object> result = Tools.setResult(200, "查询列表成功");
	</#if>
		try{
			List<${paramsType}> list = ${prefixName}Service.listAll(<#if entityType == 1>${prefixName}<#else>pd</#if>);
		<#if entityType == 1>
			result.put("list", list);
		<#else>
			pd.setResult(200, "查询列表成功").pd.put("list", list);
		</#if>
		} catch(Exception e){
			log.error("查询列表发生异常", e);
		<#if entityType == 1>
			result = Tools.setResult(500, "查询列表发生异常");
		<#else>
			pd.setResult(500, "查询列表发生异常");
		</#if>
		}
		return <#if entityType == 1>result<#else>pd</#if>;
	}

    /**
    * 添加
    */
    @RequestMapping(value="/info",method=RequestMethod.POST)
    @ResponseBody
    public Object add(<#if entityType == 1>${objectName} ${prefixName}</#if>){
	<#if entityType != 1>
		PageData pd = this.getPageData();
		pd.setResult(200, "添加成功");
	<#else>
		Map<String, Object> result = Tools.setResult(200, "添加成功");
	</#if>
		try{
			Boolean isOk = ${prefixName}Service.add(<#if entityType == 1>${prefixName}<#else>pd</#if>);
			if(!isOk){
			<#if entityType == 1>
				result = Tools.setResult(206, "添加失败");
			<#else>
				pd.setResult(206, "添加失败");
			</#if>
			}
        }catch(Exception e){
			log.error("新增发生异常", e);
		<#if entityType == 1>
			result = Tools.setResult(500, "新增发生异常");
		<#else>
			pd.setResult(500, "新增发生异常");
		</#if>
        }
        return <#if entityType == 1>result<#else>pd</#if>;
	}


	/**
	* 更新
	*/
	@RequestMapping(value="/info",method=RequestMethod.PUT)
	@ResponseBody
	public Object update(<#if entityType == 1>${objectName} ${prefixName}</#if>){
	<#if entityType != 1>
		PageData pd = this.getPageData();
		pd.setResult(200, "修改成功");
	<#else>
		Map<String, Object> result = Tools.setResult(200, "修改成功");
	</#if>
		try{
			Boolean isOk = ${prefixName}Service.edit(<#if entityType == 1>${prefixName}<#else>pd</#if>);
			if(!isOk){
			<#if entityType == 1>
				result = Tools.setResult(206, "修改失败");
			<#else>
				pd.setResult(206, "修改失败");
			</#if>
			}
		}catch(Exception e){
			log.error("修改发生异常", e);
		<#if entityType == 1>
			result = Tools.setResult(500, "修改发生异常");
		<#else>
			pd.setResult(500, "修改发生异常");
		</#if>
		}
		return <#if entityType == 1>result<#else>pd</#if>;
	}


	/**
	* 删除
	*/
	@RequestMapping(value="/info/{id}",method=RequestMethod.DELETE)
    @ResponseBody
	public Object delete(@PathVariable <#if keyFiled.type == 'int'>Integer<#elseif keyFiled.type == 'bigint'>Long<#else>String</#if> id){
	<#if entityType != 1>
		PageData pd = this.getPageData();
		pd.setResult(200, "删除成功");
	<#else>
		Map<String, Object> result = Tools.setResult(200, "删除成功");
	</#if>
	<#if keyFiled.type == 'int'>
		if(id == null || id.intValue() <= 0){
	<#elseif keyFiled.type == 'bigint'>
		if(id == null || id.longValue() <= 0){
	<#else>
		if(Tools.isEmpty(id)){
	</#if>
			return Tools.setResult(203, "id为空");
		}
		try{
			Boolean isOk = ${prefixName}Service.delete(id);
			if(!isOk){
			<#if entityType == 1>
				result = Tools.setResult(206, "删除失败");
			<#else>
				pd.setResult(206, "删除失败");
			</#if>
			}
		} catch(Exception e){
			log.error("删除发生异常", e);
		<#if entityType == 1>
			result = Tools.setResult(500, "删除发生异常");
		<#else>
			pd.setResult(500, "删除发生异常");
		</#if>
		}
		return <#if entityType == 1>result<#else>pd</#if>;
	}


	/**
	* 批量删除
	*/
	@RequestMapping(value="/ids",method=RequestMethod.DELETE)
	@ResponseBody
	public Object delByIds(String str){
	<#if entityType != 1>
		PageData pd = new PageData();
	<#else>
		Map<String, Object> map = null;
	</#if>
		if(Tools.isEmpty(str)){
			return null;
		}
		try{
			String[] ids = str.split(",");
			if(ids != null && ids.length > 0) {
				int delCount = ${prefixName}Service.deleteAll(ids);
				if(delCount > 0){
				<#if entityType == 1>
					map = Tools.setResult(200, "批量删除成功");
				<#else>
					pd.setResult(200, "批量删除成功");
				</#if>
				}else{
				<#if entityType == 1>
					map = Tools.setResult(206, "批量删除失败");
				<#else>
					pd.setResult(206, "批量删除失败");
				</#if>
				}
			<#if entityType == 1>
				map.put("delCount", delCount);
			<#else>
				pd.put("delCount", delCount);
			</#if>
			}else{
				return null;
			}
		} catch(Exception e){
			log.error("批量删除发生异常", e);
		<#if entityType == 1>
			map = Tools.setResult(500, "批量删除发生异常");
		<#else>
			pd.setResult(500, "批量删除发生异常");
		</#if>
		}
		return <#if entityType == 1>map<#else>pd</#if>;
	}


}
