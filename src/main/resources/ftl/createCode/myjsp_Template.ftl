<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%
    String path = request.getContextPath();
    String basePath = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + path + "/";
    pageContext.setAttribute("APP_PATH", path);
    pageContext.setAttribute("basePath", basePath);
%>
<html>
<head>
    <base href="<%=basePath%>"><!-- jsp文件头和头部 -->
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <title>${prefixName}View</title>
    <link href="static/css/bootstrap.css" rel="stylesheet">
    <link href="static/css/datepicker.css" rel="stylesheet">
    <link href="static/css/font-awesome.min.css" rel="stylesheet">
    <script type="text/javascript" src="static/js/jquery-2.1.1.min.js"></script>
    <script type="text/javascript" src="static/bootstrap-3.3.7-dist/js/bootstrap.min.js"></script>
    <script type="text/javascript" src="static/js/bootstrap-datepicker.min.js"></script>
    <style type="text/css">
        table tr td,th{
            text-align: center;
            white-space: nowrap; /*设置内容不换行*/
        }
        #selectInfo *{ display: inline-block; }
    </style>
</head>
<body>
<!-- 修改的模态框 -->
<div class="modal fade" id="${prefixName}UpdateModal" tabindex="-1" role="dialog" aria-labelledby="myModalLabel">
    <div class="modal-dialog" role="document">
        <div class="modal-content">
            <div class="modal-header">
                <button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">&times;</span></button>
                <h4 class="modal-title">修改</h4>
            </div>
            <div class="modal-body">
                <form class="form-horizontal">
                    <div class="form-group" style="display: none;">
                        <label class="col-sm-2 control-label">${keyFiled.filed}</label>
                        <div class="col-sm-10">
                            <input type="text" class="form-control" name="${keyFiled.filed}" id="${keyFiled.filed}_update_input">
                            <span class="help-block"></span>
                        </div>
                    </div>
        <#list fieldList as var>
            <#if var[1] == 'date' || var[1] == 'timestamp' || var[1] == 'datetime'>
                <div class="form-group">
                    <label class="col-sm-2 control-label">${var[3]!var[0]}</label>
                    <div class="col-sm-10">
                        <input class="span10 date-picker form-control" type="text" name="${var[0]}" id="${var[0]}_update_input"
                               readonly="readonly" placeholder="${var[3]!var[0]}" data-date-format="yyyy-mm-dd" style="width: 120px;">
                        <span class="help-block"></span>
                    </div>
                </div>
            <#elseif var[1] == 'int' || var[1] == 'bigint' || var[1] == 'decimal' || var[1] == 'double' || var[1] == 'float'>
                <div class="form-group">
                    <label class="col-sm-2 control-label">${var[3]!var[0]}</label>
                    <div class="col-sm-10">
                        <input type="number" class="form-control" name="${var[0]}" id="${var[0]}_update_input" placeholder="${var[3]!var[0]}">
                        <span class="help-block"></span>
                    </div>
                </div>
            <#elseif var[1] == 'text'>
                <div class="form-group">
                    <label class="col-sm-2 control-label">${var[3]!var[0]}</label>
                    <div class="col-sm-10">
                        <textarea id="${var[0]}_update_input" name="${var[0]}" class="form-control" cols="75" rows="5"></textarea>
                        <span class="help-block"></span>
                    </div>
                </div>
            <#else>
                <div class="form-group">
                    <label class="col-sm-2 control-label">${var[3]!var[0]}</label>
                    <div class="col-sm-10">
                        <input type="text" class="form-control" name="${var[0]}" id="${var[0]}_update_input" placeholder="${var[3]!var[0]}">
                        <span class="help-block"></span>
                    </div>
                </div>
            </#if>
        </#list>
                </form>
            </div>
            <div class="modal-footer">
                <button type="button" class="btn btn-default" data-dismiss="modal">关闭</button>
                <button type="button" class="btn btn-primary" id="${prefixName}_update_btn">更新</button>
            </div>
        </div>
    </div>
</div>

<!-- 添加的模态框 -->
<div class="modal fade" id="${prefixName}AddModal" tabindex="-1" role="dialog" aria-labelledby="myModalLabel">
    <div class="modal-dialog" role="document">
        <div class="modal-content">
            <div class="modal-header">
                <button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">&times;</span></button>
                <h4 class="modal-title" id="myModalLabel">添加</h4>
            </div>
            <div class="modal-body">
                <form class="form-horizontal">
                    <div class="form-group" style="display: none;">
                        <label class="col-sm-2 control-label">${keyFiled.filed}</label>
                        <div class="col-sm-10">
                            <input type="text" class="form-control" name="${keyFiled.filed}" id="${keyFiled.filed}_add_input">
                            <span class="help-block"></span>
                        </div>
                    </div>
        <#list fieldList as var>
            <#if var[1] == 'date' || var[1] == 'timestamp' || var[1] == 'datetime'>
                <div class="form-group">
                    <label class="col-sm-2 control-label">${var[3]!var[0]}</label>
                    <div class="col-sm-10">
                        <input class="span10 date-picker form-control" type="text" name="${var[0]}" id="${var[0]}_add_input"
                               readonly="readonly" placeholder="${var[3]!var[0]}" data-date-format="yyyy-mm-dd" style="width: 120px;">
                        <span class="help-block"></span>
                    </div>
                </div>
            <#elseif var[1] == 'int' || var[1] == 'bigint' || var[1] == 'decimal' || var[1] == 'double' || var[1] == 'float'>
                <div class="form-group">
                    <label class="col-sm-2 control-label">${var[3]!var[0]}</label>
                    <div class="col-sm-10">
                        <input type="number" class="form-control" name="${var[0]}" id="${var[0]}_add_input" placeholder="${var[3]!var[0]}">
                        <span class="help-block"></span>
                    </div>
                </div>
            <#elseif var[1] == 'text'>
                <div class="form-group">
                    <label class="col-sm-2 control-label">${var[3]!var[0]}</label>
                    <div class="col-sm-10">
                        <textarea id="${var[0]}_add_input" name="${var[0]}" class="form-control" cols="75" rows="5"></textarea>
                        <span class="help-block"></span>
                    </div>
                </div>
            <#else>
                <div class="form-group">
                    <label class="col-sm-2 control-label">${var[3]!var[0]}</label>
                    <div class="col-sm-10">
                        <input type="text" class="form-control" name="${var[0]}" id="${var[0]}_add_input" placeholder="${var[3]!var[0]}">
                        <span class="help-block"></span>
                    </div>
                </div>
            </#if>
        </#list>
                </form>
            </div>
            <div class="modal-footer">
                <button type="button" class="btn btn-default" data-dismiss="modal">关闭</button>
                <button type="button" class="btn btn-primary" id="${prefixName}_add_btn">保存</button>
            </div>
        </div>
    </div>
</div>

<!-- 搭建显示页面 -->
<div class="container">
    <!-- 标题 -->
    <div class="row">
        <div class="col-md-12">
            <h1>${prefixName}列表</h1>
        </div>
    </div>
    <div class="row">
        <br>
        <div class="col-md-10" id="selectInfo">
            <input type="text" id="content" class="form-control" placeholder="输入查询信息" style="width: 160px;">
            <button type="button" id="inquire" class="btn btn-primary" style="margin-left: 20px;">查询</button>
        </div>
        <div class="col-md-2">
            <button class="btn btn-primary" id="${prefixName}_add_modal_btn">新增</button>
            <button class="btn btn-danger" id="${prefixName}_delete_all_btn">批量删除</button>
        </div>
    </div>
    <div class="row" style="margin-top: 30px;">
        <div class="col-md-12">
            <table class="table table-hover" id="${prefixName}_table">
                <thead>
                <tr>
                    <th>
                        <input type="checkbox" id="check_all"/>
                    </th>
                    <th style="display: none;">主键</th>
                <#list fieldList as var>
                    <th>${var[3]!var[0]}</th>
                </#list>
                    <th>操作</th>
                </tr>
                </thead>
                <tbody></tbody>
            </table>
        </div>
    </div>
    <!-- 显示分页信息 -->
    <div class="row">
        <!--分页文字信息  -->
        <div class="col-md-6" id="page_info_area"></div>
        <!-- 分页条信息 -->
        <div class="col-md-6" id="page_nav_area">

        </div>
    </div>
</div>
<!-- 确认窗口 -->
<script type="text/javascript" src="static/js/bootbox.js"></script>
<script type="text/javascript">
    var totalRecord,currentPage;

    function returnValue(value){
        return value==null||value==""?"":value;
    }

    function isNullValue(value){
        return value==null||value==""?true:false;
    }

    function countValueIsNull(count){
        return count!=null && count>0?false:true;
    }

    $(function(){
        //时间框初始化
        $('.date-picker').datepicker();
        to_page(1);
    });

    $(document).ready(function(){
        $("#inquire").click(function(){
            to_page(1);
        });
    });

    function to_page(pn){
        $.ajax({
            type:"GET",
            url:"${prefixName}/page",
            data:{
                pn: pn,
                size: 10,
                content: $("#content").val()
            },
            dataType:"json",
            success:function(result){
                //1、解析并显示数据
                build_roles_table(result);
                //2、解析并显示分页信息
                build_page_info(result);
                //3、解析显示分页条数据
                build_page_nav(result);
            }
        });
    }

    function build_roles_table(result){
        //清空table表格
        $("#${prefixName}_table tbody").empty();
        var menu = result.pageInfo.list;
        $.each(menu, function (index, item){
            var checkBoxTd = $("<td><input type='checkbox' class='check_item'/></td>");
            var id = $("<td style='display: none;'></td>").append(item.${keyFiled.filed});
        <#list fieldList as var>
            var ${var[0]} = $("<td></td>").append(item.${var[0]});
        </#list>
            var editBtn = $("<button></button>").addClass("btn btn-primary btn-sm edit_btn")
                    .append($("<span></span>").addClass("glyphicon glyphicon-pencil")).append("编辑");
            editBtn.attr("edit-id",item.${keyFiled.filed});
            var delBtn =  $("<button></button>").addClass("btn btn-danger btn-sm delete_btn")
                    .append($("<span></span>").addClass("glyphicon glyphicon-trash")).append("删除");
            delBtn.attr("del-id",item.${keyFiled.filed});
            var btnTd = $("<td></td>").append(editBtn).append(" ").append(delBtn);
            //append方法执行完成以后还是返回原来的元素
            var trElement = $("<tr></tr>").append(checkBoxTd);
            trElement.append(id);
        <#list fieldList as var>
            trElement.append(${var[0]});
        </#list>
            trElement.append(btnTd).appendTo("#${prefixName}_table tbody");
        });
    }
    //解析显示分页信息
    function build_page_info(result){
        $("#page_info_area").empty();
        $("#page_info_area").append("当前"+result.pageInfo.pageNum+"页,总"+
                result.pageInfo.pages+"页,总"+
                result.pageInfo.total+"条记录");
        totalRecord = result.pageInfo.total;
        currentPage = result.pageInfo.pageNum;
    }
    //解析显示分页条，点击分页要能去下一页....
    function build_page_nav(result){
        //page_nav_area
        $("#page_nav_area").empty();
        var ul = $("<ul></ul>").addClass("pagination");

        //构建元素
        var firstPageLi = $("<li></li>").append($("<a></a>").append("首页").click(function(){ to_page(1); }));
        var prePageLi = $("<li></li>").append($("<a></a>").append("&laquo;"));
        if(result.pageInfo.hasPreviousPage == false){
            firstPageLi.addClass("disabled");
            prePageLi.addClass("disabled");
        }else{
            //为元素添加点击翻页的事件
            firstPageLi.click(function(){
                to_page(1);
            });
            prePageLi.click(function(){
                to_page(result.pageInfo.pageNum -1);
            });
        }

        var nextPageLi = $("<li></li>").append($("<a></a>").append("&raquo;"));
        var lastPageLi = $("<li></li>").append($("<a></a>").append("末页").click(function(){ to_page(result.pageInfo.pages); }));
        if(result.pageInfo.hasNextPage == false){
            nextPageLi.addClass("disabled");
            lastPageLi.addClass("disabled");
        }else{
            nextPageLi.click(function(){
                to_page(result.pageInfo.pageNum +1);
            });
            lastPageLi.click(function(){
                to_page(result.pageInfo.pages);
            });
        }

        //添加首页和前一页 的提示
        ul.append(firstPageLi).append(prePageLi);
        //1,2，3遍历给ul中添加页码提示
        $.each(result.pageInfo.navigatepageNums,function(index,item){

            var numLi = $("<li></li>").append($("<a></a>").append(item));
            if(result.pageInfo.pageNum == item){
                numLi.addClass("active");
            }
            numLi.click(function(){
                to_page(item);
            });
            ul.append(numLi);
        });
        //添加下一页和末页 的提示
        ul.append(nextPageLi).append(lastPageLi);

        //把ul加入到nav
        var navEle = $("<nav></nav>").append(ul);
        navEle.appendTo("#page_nav_area");
    }
    //清空表单样式及内容
    function reset_form(ele){
        $(ele)[0].reset();
        //清空表单样式
        $(ele).find("*").removeClass("has-error has-success");
        $(ele).find(".help-block").text("");
    }
    //显示校验结果的提示信息
    function show_validate_msg(ele,status,msg){
        //清除当前元素的校验状态
        $(ele).parent().removeClass("has-success has-error");
        $(ele).nextAll("span").text("");
        if("success"==status){
            $(ele).parent().addClass("has-success");
            $(ele).nextAll("span").text(msg);
        }else if("error" == status){
            $(ele).parent().addClass("has-error");
            $(ele).nextAll("span").text(msg);
        }
    }

    //点击新增按钮弹出模态框。
    $("#${prefixName}_add_modal_btn").click(function(){
        reset_form("#${prefixName}AddModal form");
        $("#${prefixName}AddModal").modal({
            backdrop:"static"
        });
    });


    //添加时非空验证
    function SaveJudgmentisnotempty(){
        var isError = false;

    <#list fieldList as var>
        <#if var[4] == '1'>

        var ${var[0]} = $("#${var[0]}_add_input").val();
        if(isNullValue(${var[0]})){
            show_validate_msg("#${var[0]}_add_input","error","${var[3]!var[0]}不能为空");
            isError = true;
        }else{
            show_validate_msg("#${var[0]}_add_input","success","");
        }

        </#if>
    </#list>

        if(isError == true){
            $("#${prefixName}_add_btn").attr("ajax-va","error");
        }else{
            $("#${prefixName}_add_btn").attr("ajax-va","success");
        }
    }

    //修改时非空验证
    function UpdateJudgmentisnotempty(){
        var isError = false;

    <#list fieldList as var>
        <#if var[4] == '1'>

        var ${var[0]} = $("#${var[0]}_update_input").val();
        if(isNullValue(${var[0]})){
            show_validate_msg("#${var[0]}_update_input","error","${var[3]!var[0]}不能为空");
            isError = true;
        }else{
            show_validate_msg("#${var[0]}_update_input","success","");
        }

        </#if>
    </#list>

        if(isError == true){
            $("#${prefixName}_update_btn").attr("ajax-va","error");
        }else{
            $("#${prefixName}_update_btn").attr("ajax-va","success");
        }
    }


    //点击保存
    $("#${prefixName}_add_btn").click(function(){
        SaveJudgmentisnotempty();
        if($(this).attr("ajax-va") == "error"){
            return false;
        }
        //2、保存
        $.ajax({
            url: "${prefixName}/sud",
            type: "POST",
            data: $("#${prefixName}AddModal form").serialize(),
            dataType: "json",
            success: function(result){
                var btnClassName = "";
                if(result.code == 200){
                    btnClassName = " btn-success";
                }else if(result.code == 206){
                    btnClassName = " btn-warning";
                }else{
                    btnClassName = " btn-danger";
                }
                bootbox.dialog({
                    message: "<span class='bigger-110'>" + result.msg + "!</span>",
                    buttons: { "button":{ "label":"确定", "className":"btn-sm" + btnClassName }}
                });
                //1、关闭模态框
                $("#${prefixName}AddModal").modal('hide');
                //2、来到最后一页，显示刚才保存的数据
                to_page(1);
            }
        });
    });

    //点击修改按钮弹出模态框。
    $(document).on("click",".edit_btn",function(){
        reset_form("#${prefixName}UpdateModal form");
        getData($(this).attr("edit-id"));
        $("#${prefixName}_update_btn").attr("edit-id",$(this).attr("edit-id"));
        $("#${prefixName}UpdateModal").modal({
            backdrop:"static"
        });
    });

    function getData(id){
        $.ajax({
            type: "GET",
            url: "${prefixName}/info/" + id,
            dataType: "json",
            success: function(result){
                if(result.code == 200){
                    $("#${keyFiled.filed}_update_input").val(result.${keyFiled.filed});
                <#list fieldList as var>
                    $("#${var[0]}_update_input").val(result.${var[0]});
                </#list>
                }
            }
        });
    }

    //点击更新
    $("#${prefixName}_update_btn").click(function(){
        //非空验证
        UpdateJudgmentisnotempty();
        if($(this).attr("ajax-va")=="error"){
            return false;
        }
        //2、更新
        $.ajax({
            type: "PUT",
            url: "${prefixName}/sud",
            data: $("#${prefixName}UpdateModal form").serialize(),
            dataType: "json",
            success: function(result){
                var btnClassName = "";
                if(result.code == 200){
                    btnClassName = " btn-success";
                }else if(result.code == 206){
                    btnClassName = " btn-warning";
                }else{
                    btnClassName = " btn-danger";
                }
                bootbox.dialog({
                    message: "<span class='bigger-110'>" + result.msg + "!</span>",
                    buttons: { "button":{ "label":"确定", "className":"btn-sm" + btnClassName }}
                });
                //1、关闭对话框
                $("#${prefixName}UpdateModal").modal("hide");
                //2、回到本页面
                to_page(currentPage);
            }
        });
    });

    //单个删除
    $(document).on("click",".delete_btn",function(){
        //1、弹出是否确认删除对话框
        var id = $(this).attr("del-id");
        bootbox.confirm("确定要删除吗?", function(result) {
            if(result){
                //确认，发送ajax请求删除即可
                $.ajax({
                    type: "DELETE",
                    url: "${prefixName}/sud",
                    data: {
                        id: id
                    },
                    dataType: "json",
                    success: function(result){
                        var btnClassName = "";
                        if(result.code == 200){
                            btnClassName = " btn-success";
                        }else if(result.code == 206){
                            btnClassName = " btn-warning";
                        }else{
                            btnClassName = " btn-danger";
                        }
                        bootbox.dialog({
                            message: "<span class='bigger-110'>" + result.msg + "!</span>",
                            buttons: { "button":{ "label":"确定", "className":"btn-sm" + btnClassName }}
                        });
                        //回到本页
                        to_page(currentPage);
                    }
                });
            }
        });
    });

    //完成全选/全不选功能
    $("#check_all").click(function(){
        //attr获取checked是undefined;
        $(".check_item").prop("checked",$(this).prop("checked"));
    });

    //check_item
    $(document).on("click",".check_item",function(){
        //判断当前选择中的元素是否5个
        var flag = $(".check_item:checked").length==$(".check_item").length;
        $("#check_all").prop("checked",flag);
    });

    //点击全部删除，就批量删除
    $("#${prefixName}_delete_all_btn").click(function(){
        var del_idstr = "";
        $.each($(".check_item:checked"),function(){
            //组装id字符串
            del_idstr += $(this).parents("tr").find("td:eq(1)").text()+",";
        });
        //去除删除的id多余的-
        del_idstr = del_idstr.substring(0, del_idstr.length-1);
        bootbox.confirm("确定要删除所选数据吗?", function(result) {
            if(result){
                //发送ajax请求删除
                $.ajax({
                    type: "DELETE",
                    url: "${prefixName}/ids",
                    data: {
                        str: del_idstr
                    },
                    dataType: "json",
                    success: function(result){
                        var btnClassName = "";
                        if(result.code == 200){
                            btnClassName = " btn-success";
                        }else if(result.code == 206){
                            btnClassName = " btn-warning";
                        }else{
                            btnClassName = " btn-danger";
                        }
                        bootbox.dialog({
                            message: "<span class='bigger-110'>" + result.msg + "!</span>",
                            buttons: { "button":{ "label":"确定", "className":"btn-sm" + btnClassName }}
                        });
                        //回到当前页面
                        to_page(currentPage);
                    }
                });
            }
        });
    });
</script>
</body>
</html>