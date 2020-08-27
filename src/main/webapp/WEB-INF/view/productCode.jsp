<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%
    String path = request.getContextPath();
    String basePath = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + path + "/";
%>
<!DOCTYPE html>
<html lang="en">
<head>
    <base href="<%=basePath%>">
    <title>生成代码</title>
    <!-- basic styles -->
    <link rel="stylesheet" href="static/css/bootstrap.min.css"/>
    <link rel="stylesheet" href="static/css/bootstrap-responsive.min.css"/>
    <link rel="stylesheet" href="static/css/font-awesome.min.css"/>
    <!-- ace styles -->
    <link rel="stylesheet" href="static/css/ace.min.css"/>
    <link rel="stylesheet" href="static/css/ace-responsive.min.css"/>
    <link rel="stylesheet" href="static/css/ace-skins.min.css"/>
    <script type="text/javascript" src="static/js/jquery-2.1.1.min.js"></script>
    <script src="static/bootstrap-3.3.7-dist/js/bootstrap.min.js"></script>
    <style type="text/css">
        .dialog-add {
            width: 100%;
            height: 100%;
            position: fixed;
            top: 0px;
            z-index: 10000;
            display: none;
        }

        .commitopacity {
            position: absolute;
            width: 100%;
            height: 500px;
            background: #7f7f7f;
            filter: alpha(opacity=50);
            -moz-opacity: 0.5;
            -khtml-opacity: 0.5;
            opacity: 0.5;
            top: 0px;
            z-index: 99999;
        }

        .commitbox {
            width: 55%;
            padding-left: 21%;
            padding-top: 6%;
            position: absolute;
            top: 0px;
            z-index: 99999;
        }

        .commitbox_inner {
            width: 96%;
            height: 235px;
            margin: 6px auto;
            background: #efefef;
            border-radius: 5px;
        }

        .commitbox_top {
            width: 100%;
            height: 230px;
            margin-bottom: 10px;
            padding-top: 10px;
            background: #FFF;
            border-radius: 5px;
            box-shadow: 1px 1px 3px #e8e8e8;
        }

        .commitbox_top textarea {
            width: 95%;
            height: 165px;
            display: block;
            margin: 0px auto;
            border: 0px;
        }

        .commitbox_cen {
            width: 95%;
            height: 40px;
            padding-top: 10px;
        }

        .commitbox_cen div.left {
            float: left;
            background-size: 15px;
            background-position: 0px 3px;
            padding-left: 18px;
            color: #f77500;
            font-size: 16px;
            line-height: 27px;
        }

        .commitbox_cen div.left img {
            width: 30px;
        }

        .commitbox_cen div.right {
            float: right;
            margin-top: 7px;
        }

        .commitbox_cen div.right span {
            cursor: pointer;
        }

        .commitbox_cen div.right span.save {
            border: solid 1px #c7c7c7;
            background: #6FB3E0;
            border-radius: 3px;
            color: #FFF;
            padding: 5px 10px;
        }

        .commitbox_cen div.right span.quxiao {
            border: solid 1px #f77400;
            background: #f77400;
            border-radius: 3px;
            color: #FFF;
            padding: 4px 9px;
        }

        .myClass td {
            margin-bottom: 10px;
        }

        .myClass input[type=radio], #all-table input[type=radio], input[type=checkbox] {
            opacity: initial;
            position: relative;
        }

        #all-table {
            display: none;
            width: 100%;
            height: 550px;
            border-top: 10px solid #DDD;
            border-bottom: 10px solid #DDD;
            background-color: #f9f9f9;
            overflow-x: auto;
        }

        #table-list {
            margin: 0px 20px 20px 5%;
            width: 100%;
        }

        .my_check {
            margin: 10px 5px 10px 5px;
        }
    </style>
</head>
<body>
<!-- 添加属性  -->
<input type="hidden" name="hcItemName" id="hcItemName" value=""/>
<input type="hidden" name="msgIndex" id="msgIndex" value=""/>
<input type="hidden" name="itemType" value="varchar"/>
<input type="hidden" name="isNotNull" id="isNotNull" value="1"/>
<!-- 添加自定义字段窗口 -->
<div id="add-window" class="dialog-add">
    <div class="commitopacity" style="height: 100%;"></div>
    <div class="commitbox">
        <div class="commitbox_inner">
            <div class="commitbox_top" style="padding-bottom: 20px;">
                <br/>
                <table>
                    <tr>
                        <td style="padding-left: 30px;text-align: right;font-weight: bold;">属性名：</td>
                        <td><input name="itemName" id="itemName" type="text" value="" placeholder="属性名" title="属性名"/>
                        </td>
                        <td style="padding-left: 50px;text-align: right;font-weight: bold;">属性类型：</td>
                        <td style="padding-bottom: 6px;width: 300px;">
                            <select id="itemType" class="form-control" style="display: inline-block;width:150px;">
                                <option value="varchar">varchar</option>
                                <option value="int">int</option>
                                <option value="bigint">bigint</option>
                                <option value="char">char</option>
                                <option value="decimal">decimal</option>
                                <option value="float">float</option>
                                <option value="double">double</option>
                                <option value="date">date</option>
                                <option value="timestamp">timestamp</option>
                                <option value="text">text</option>
                            </select>
                            <input type="text" id="itemLenth" class="form-control" placeholder="长度"
                                   style="display: inline-block;width:100px;"/>
                        </td>
                    </tr>
                    <tr>
                        <td style="padding-left: 30px;text-align: right;font-weight: bold;">备注：</td>
                        <td>
                            <input name="remark" id="remark" type="text" value="" placeholder="字段备注" title="备注"/>
                        </td>
                        <td style="padding-left: 50px;text-align: right;font-weight: bold;">是否不为空：</td>
                        <td style="padding-bottom: 6px;">
                            <label style="float:left;padding-left: 20px;">
                                <input name="form-field-radioq" id="form-field-radio4" onclick="isNotNull('1');"
                                       type="radio" value="icon-edit" checked="checked">
                                <span class="lbl">是</span>
                            </label>
                            <label style="float:left;padding-left: 20px;">
                                <input name="form-field-radioq" id="form-field-radio5" onclick="isNotNull('0');"
                                       type="radio" value="icon-edit">
                                <span class="lbl">否</span>
                            </label>
                        </td>
                    </tr>
                    <tr>
                        <td style="padding-left: 36px;line-height: 35px;text-align: center;" colspan="100">
                            <font color="red" style="font-weight: bold;">
                                注意： 请不要添加主键，系统自动生成一个id自动作为主键
                            </font>
                        </td>
                    </tr>
                    <tr>
                        <td colspan="4">
                            <div class="commitbox_cen">
                                <div class="right" style="margin-right: 35%;">
                                    <span class="btn btn-app btn-mini btn-primary" onClick="save()">保存</span>&nbsp;&nbsp;
                                    <span class="btn btn-app btn-mini btn-warning" onClick="cancel()">取消</span>
                                </div>
                            </div>
                        </td>
                    </tr>
                </table>
            </div>
        </div>
    </div>
</div>
<!-- 生成代码选项表单 -->
<form name="Form" id="Form" method="post">
    <input type="hidden" name="zindex" id="zindex" value="0">
    <div id="zhongxin" style="margin-top: 30px;">
        <!-- 自动生成配置 -->
        <table style="margin-top: 10px;margin-bottom: 20px;width: 100%;" class="myClass">
            <tr>
                <td style="width:7%;text-align: right;font-weight: bold;">包路径：</td>
                <td style="width: 400px;">
                    <input type="text" name="packagePath" id="packagePath" value=""
                           placeholder="这里输入包路径（例如：com.claris.hanabi）"
                           style="width:370px;padding-left: 10px;" title="包路径" class="form-control"/>
                </td>
                <td style="width:7%;text-align: right;font-weight: bold;">实体类类型：</td>
                <td style="width: 600px;">
                    <input type="radio" name="entityType" value="2" checked="checked"/>PageData
                    <input type="radio" name="entityType" value="1" style="margin-left: 20px;"/>对象实体类
                    <span style="margin-left: 30px;">包名：</span>
                    <input type="text" name="entityPath" id="entityPath" placeholder="不填默认为entity"
                           style="width:150px;padding-left: 10px;margin-bottom: 0px;" title="包名" class="form-control"/>
                </td>
                <td rowspan="2" style="width: 10%;">
                    <a class="btn btn-app btn-success btn-mini" onclick="commit();" id="productc"><i
                            class="icon-print"></i>生成</a>
                </td>
            </tr>
            <tr>
                <td style="width:7%;text-align: right;font-weight: bold;">DAO类型：</td>
                <td>
                    <input type="radio" name="daoType" value="2" checked="checked"/>公用DAO
                    <input type="radio" name="daoType" value="1" style="margin-left: 20px;"/>普通DAO
                    <span style="margin-left: 30px;">包名：</span>
                    <input type="text" name="daoPath" id="daoPath" placeholder="不填默认为dao"
                           style="width:150px;padding-left: 10px;margin-bottom: 0px;" title="包名" class="form-control"/>
                </td>
                <td style="width:7%;text-align: right;font-weight: bold;">实体类来源：</td>
                <td>
                    <input type="radio" id="onCreate" name="createType" value="2" checked="checked"/>自创建
                    <input type="radio" id="onGet" name="createType" value="1" style="margin-left: 20px;"/>数据库表
                    <input type="radio" id="onGetAll" name="createType" value="0" style="margin-left: 20px;"/>多表生成
                    <div style="margin-left: 30px;display: inline-block;">
                        <span id="createName">类名</span>：
                        <input type="text" name="objectName" id="objectName" value="" placeholder="请输入名称"
                               style="width:250px;padding-left: 10px;"/>
                    </div>
                </td>
            </tr>
        </table>
        <!-- 自定义字段列表 -->
        <table id="table_report" class="table table-striped table-bordered table-hover">
            <thead>
            <tr>
                <th class="center">属性名</th>
                <th class="center">类型</th>
                <th class="center">长度</th>
                <th class="center">备注</th>
                <th class="center" style="width: 150px;">是否不为空</th>
                <th class="center" style="width:150px;">操作</th>
            </tr>
            </thead>
            <tbody id="fields"></tbody>
        </table>
        <!-- 操作选项 -->
        <table id="table_foot" class="table table-striped table-bordered table-hover">
            <tr>
                <td style="text-align: center;" colspan="100">
                    <a class="btn btn-app btn-info btn-mini" onclick="dialog_open();"><i class="icon-ok"></i>新增</a>
                    <a class="btn btn-app btn-danger btn-mini" onclick="clean_add_filed();"><i
                            class="icon-remove-sign"></i>清空</a>
                </td>
            </tr>
        </table>
        <!-- 多表生成窗口 -->
        <div id="all-table">
            <div style="margin: 20px 5% 10px;">
                <input id='check_all' type='radio' value='all'/>
                <span style="font-weight: bold; font-size: 14px;">全选</span>
                <input id='cencel_all' type='radio' value='cancel' style="margin-left: 20px;"/>
                <span style="font-weight: bold; font-size: 14px;">取消全选</span>
            </div>
            <div id="table-list"></div>
        </div>
    </div>
    <div id="zhongxin2" class="center" style="display:none">
        <br/><br/><br/><br/><img src="static/images/jiazai.gif"/><br/><h4 class="lighter block green"></h4>
    </div>
</form>
<!-- 引入 -->
<script src="static/js/ace-elements.min.js" type="text/javascript"></script>
<script src="static/js/ace.min.js" type="text/javascript"></script>
<!--引入弹窗组件start-->
<script type="text/javascript" src="static/zDialog/zDrag.js"></script>
<script type="text/javascript" src="static/zDialog/zDialog.js"></script>
<!-- 确认窗口 -->
<script type="text/javascript" src="static/js/bootbox.js"></script>
<!--提示框-->
<script type="text/javascript" src="static/js/jquery.tips.js"></script>
<script type="text/javascript">

    //逻辑处理、事件等等
    var arField = new Array();
    var index = 0;

    //初始化数据
    allTable();

    $("#onCreate").click(function () {
        $("#table_report").show();
        $("#table_foot").show();
        $("#createName").text("类名");
        $("#objectName").parent().show();
        $("#all-table").hide();
    });

    $("#onGet").click(function () {
        $("#table_report").hide();
        $("#table_foot").hide();
        $("#createName").text("表名");
        $("#objectName").parent().show();
        $("#all-table").hide();
    });

    $("#onGetAll").click(function () {
        $("#table_report").hide();
        $("#table_foot").hide();
        $("#objectName").parent().hide();
        $("#all-table").show();
    });

    $("#itemType").change(function () {
        var value = this.value;
        if (value == "varchar" || value == "int" || value == "bigint" || value == "char") {
            $("#itemLenth").show();
        } else {
            $("#itemLenth").hide();
        }
        setType(value);
    });

    //全选事件
    $("#check_all").click(function () {
        //如果表集合不为空
        if ($("#table-list").html() != null && $("#table-list").html() != '') {
            $("#table-list input[type='checkbox']").prop("checked", true);
            $("#cencel_all").attr("checked", false);
        }
    });

    //取消全选事件
    $("#cencel_all").click(function () {
        //如果表集合不为空
        if ($("#table-list").html() != null && $("#table-list").html() != '') {
            $("#table-list input[type='checkbox']:checked").prop("checked", false).removeAttr("checked");
            $("#check_all").attr("checked", false);
        }
    });

    //获取数据库全部表
    function allTable() {
        $.ajax({
            type: "POST",
            url: "/create-code/allTable",
            dataType: "json",
            success: function (result) {
                if (result.list != null) {
                    var list = result.list;
                    if (list != null && list.length > 0) {
                        var div_row = 14;
                        //计算有多少个单元格
                        var div_count = list.length <= div_row ? 1 : list.length % div_row == 0 ?
                            parseInt(list.length / div_row) : parseInt(list.length / div_row) + 1;
                        if (div_count > 6) {
                            //如果超出宽度，根据单元格数量对宽度进行加长
                            var new_width = 100 + 20 * (div_count - 6);
                            $("#table-list").css("width", new_width + "%");
                        }
                        var div = $("<div style='width: 250px; display: inline-block;'></div>");
                        for (var i = 0; i < list.length; i++) {
                            var item = $("<div class='my_check'></div>").append("<input type='checkbox' value='" +
                                list[i].table_name + "' style='margin-right: 5px;'/>" + list[i].table_name);
                            //添加表到单元格中
                            div.append(item);
                            if ((i + 1) % div_row == 0) {
                                $("#table-list").append(div);
                                div = $("<div style='width: 250px; display: inline-block;'></div>");
                            } else if (i + 1 == list.length) {
                                $("#table-list").append(div);
                            }
                        }
                    }
                }
            }
        });
    }

    //生成
    function commit() {

        if ($("#packagePath").val() == "") {
            $("#packagePath").tips({
                side: 3,
                msg: '输入包路径',
                bg: '#AE81FF',
                time: 2
            });
            $("#packagePath").focus();
            return false;
        } else {
            var pat = new RegExp("^[A-Za-z|.]+$");
            if (!pat.test($("#packagePath").val())) {
                $("#packagePath").tips({
                    side: 3,
                    msg: '只能输入字母或者.',
                    bg: '#AE81FF',
                    time: 2
                });
                $("#packagePath").focus();
                return false;
            } else {
                pat = new RegExp("^([A-Za-z]+\\.)*[A-Za-z]+$");
                if (!pat.test($("#packagePath").val())) {
                    $("#packagePath").tips({
                        side: 3,
                        msg: '请输入格式正确的包路径',
                        bg: '#AE81FF',
                        time: 2
                    });
                    $("#packagePath").focus();
                    return false;
                }
            }
        }

        //如果不是多表生成
        if (!$("#onGetAll").is(":checked")) {
            if ($("#objectName").val() == "") {
                $("#objectName").tips({
                    side: 3,
                    msg: $("#onGet").is(":checked") ? "请输入表名" : "请输入类名",
                    bg: '#AE81FF',
                    time: 2
                });
                $("#objectName").focus();
                return false;
            }
        }

        //如果是自定义字段
        if ($("#onCreate").is(":checked")) {
            var objectName = $("#objectName").val();
            var pat = new RegExp("^[A-Z]");
            if (!pat.test(objectName)) {
                $("#objectName").tips({
                    side: 3,
                    msg: "类名首字母必须大写",
                    bg: '#AE81FF',
                    time: 2
                });
                $("#objectName").focus();
                return false;
            } else if ($("#fields").html() == '') {
                $("#table_report").tips({
                    side: 3,
                    msg: '请添加属性',
                    bg: '#AE81FF',
                    time: 2
                });
                return false;
            }
        } //如果是多表生成
        else if ($("#onGetAll").is(":checked")) {
            var len = $("#table-list input[type='checkbox']:checked").length;
            if (len == null || len == 0) {
                alert("未选择表！");
                return false;
            }
        }

        if (!confirm("确定要生成吗?")) {
            return false;
        }

        //如果是自创建的
        if ($("#onCreate").is(":checked")) {
            $("#Form").attr("action", "create-code/proCodeOnItems");
        }
        //如果是多表生成
        else if ($("#onGetAll").is(":checked")) {
            $("#Form").attr("action", "create-code/proCodeOnAllTable");
        }
        //如果是数据库表
        else {
            $("#Form").attr("action", "create-code/proCodeOnTable");
        }

        //如果使用数据库表，提示用户确认数据库连接配置
        if ($("#onGet").is(":checked")) {
            if (!confirm("请确认数据库连接配置为正确，已免发生不必要的错误！")) {
                return false;
            } else {
                $.ajax({
                    type: "POST",
                    url: "create-code/isExistTable",
                    data: {
                        tableName: $("#objectName").val()
                    },
                    dataType: "json",
                    success: function (result) {
                        if (result.code != 1) {
                            //弹框提示信息
                            bootbox.dialog({
                                message: "<span class='bigger-110'>" + result.msg + "</span>",
                                buttons: {"button": {"label": "确定", "className": "btn-sm btn-warning"}}
                            });
                            return false;
                        } else {
                            $("#Form").submit();
                            $("#productc").tips({
                                side: 3,
                                msg: '提交成功,等待下载',
                                bg: '#AE81FF',
                                time: 9
                            });
                            setTimeout("top.Dialog.close()", 10000);
                        }
                    }
                });
            }
        } //如果是多表生成
        else if ($("#onGetAll").is(":checked")) {
            var tableNames = "";
            $("#table-list input[type='checkbox']:checked").each(function () {
                tableNames += $(this).val() + ",";
            });
            $("#objectName").val(tableNames.substring(0, tableNames.length - 1));
            $("#Form").submit();
            $("#productc").tips({
                side: 3,
                msg: '提交成功,等待下载',
                bg: '#AE81FF',
                time: 9
            });
            setTimeout("top.Dialog.close()", 10000);
        } else {
            $("#Form").submit();
            $("#productc").tips({
                side: 3,
                msg: '提交成功,等待下载',
                bg: '#AE81FF',
                time: 9
            });
            setTimeout("top.Dialog.close()", 10000);
        }

    }

    //保存编辑属性
    function save() {
        var itemName = $("#itemName").val(); 	 		 //属性名
        var itemType = $("#itemType").val(); 	 		 //类型
        var itemLenth = $("#itemLenth").val(); 	 		//长度
        var remark = $("#remark").val();   	 		 //备注
        var isNotNull = $("#isNotNull").val(); 		 //是否不为空
        var msgIndex = $("#msgIndex").val();

        if (itemName == "") {
            $("#itemName").tips({
                side: 3,
                msg: '输入属性名',
                bg: '#AE81FF',
                time: 2
            });
            $("#itemName").focus();
            return false;
        } else {
            if (isSame(itemName)) {
                var headstr = itemName.substring(0, 1);
                var pat = new RegExp("^[0-9]+$");
                if (pat.test(headstr)) {
                    $("#itemName").tips({
                        side: 3,
                        msg: '属性名首字母必须为字母',
                        bg: '#AE81FF',
                        time: 2
                    });
                    $("#itemName").focus();
                    return false;
                }
            } else {
                if (msgIndex != '') {
                    var hcItemName = $("#hcItemName").val();
                    if (hcItemName != itemName) {
                        if (!isSame(itemName)) {
                            $("#itemName").tips({
                                side: 3,
                                msg: '属性名重复',
                                bg: '#AE81FF',
                                time: 2
                            });
                            $("#itemName").focus();
                            return false;
                        }
                    }
                } else {
                    $("#itemName").tips({
                        side: 3,
                        msg: '属性名重复',
                        bg: '#AE81FF',
                        time: 2
                    });
                    $("#itemName").focus();
                    return false;
                }
            }
        }

        if (itemType == "varchar" || itemType == "int" || itemType == "bigint" || itemType == "char") {
            if (itemLenth == null || itemLenth == '') {
                $("#itemLenth").tips({
                    side: 3,
                    msg: '长度不能为空',
                    bg: '#AE81FF',
                    time: 2
                });
                $("#itemLenth").focus();
                return false;
            } else {
                //如果不为数字
                if (isNaN(itemLenth)) {
                    $("#itemLenth").tips({
                        side: 3,
                        msg: '长度必须为数字',
                        bg: '#AE81FF',
                        time: 2
                    });
                    $("#itemLenth").focus();
                    return false;
                } else if (Number(itemLenth) < 1) {
                    $("#itemLenth").tips({
                        side: 3,
                        msg: '长度不能小于1',
                        bg: '#AE81FF',
                        time: 2
                    });
                    $("#itemLenth").focus();
                    return false;
                }
            }
        } else {
            itemLenth = '';
        }

        if (remark == "") {
            $("#remark").tips({
                side: 3,
                msg: '输入备注',
                bg: '#AE81FF',
                time: 2
            });
            $("#remark").focus();
            return false;
        }

        remark = remark == '' ? '' : remark;
        var fields = itemName + ',fh,' + itemType + ',fh,' + itemLenth + ',fh,' + remark + ',fh,' + isNotNull;

        //msgIndex不为空时是修改
        if (msgIndex == '') {
            arrayField(fields);
        } else {
            editArrayField(fields, msgIndex);
        }
        $("#msgIndex").val("");
        $("#add-window").css("display", "none");
    }

    //打开编辑属性(新增)
    function dialog_open() {
        //默认按上一次设置的属性
        $("#itemName").val('');
        $("#remark").val('');
        $("#add-window").css("display", "block");
    }

    //打开编辑属性(修改)
    function editField(value, msgIndex) {
        var efieldarray = value.split(',fh,');
        $("#itemName").val(efieldarray[0]);
        $("#hcItemName").val(efieldarray[0]);
        $("#itemType").val(efieldarray[1]);
        $("#itemLenth").val(efieldarray[2]);
        $("#remark").val(efieldarray[3]);
        $("#msgIndex").val(msgIndex);
        if (efieldarray[4] == '1') {
            $("#form-field-radio4").attr("checked", true);
        } else {
            $("#form-field-radio5").attr("checked", true);
        }
        $("#isNotNull").val(efieldarray[4]);
        $("#add-window").css("display", "block");
    }

    //关闭编辑属性
    function cancel() {
        $("#msgIndex").val("");
        $("#add-window").css("display", "none");
    }

    //清空自定义字段
    function clean_add_filed() {
        if (!confirm("确认要清空自定义字段吗？")) {
            return;
        }
        $("#fields").html("");
        index = 0;
        $("#zindex").val(index);
    }

    //赋值类型
    function setType(value) {
        $("input[name='itemType']").val(value);
    }

    //赋值是否不为空
    function isNotNull(value) {
        $("#isNotNull").val(value);
    }

    //追加属性列表
    function appendC(value) {
        var fieldarray = value.split(',fh,');
        var field = '<tr>' +
            '<td class="center">' + fieldarray[0] + '</td>' +
            '<td class="center">' + fieldarray[1] + '</td>' +
            '<td class="center">' + fieldarray[2] + '</td>' +
            '<td class="center">' + fieldarray[3] + '</td>' +
            '<td class="center">' + (fieldarray[4] == "1" ? "是" : "否") + '</td>' +
            '<td class="center">' +
            '<input type="hidden" name="field' + index + '" value="' + value + '">' +
            '<a class="btn btn-mini btn-info" title="编辑" onclick="editField(\'' + value
                + '\',\'' + index + '\')"><i class="icon-edit"></i></a>&nbsp;' +
            '<a class="btn btn-mini btn-danger" title="删除" onclick="removeField(\'' + index + '\')"><i class="icon-trash"></i></a>' +
            '</td>' +
            '</tr>';
        $("#fields").append(field);
        index++;
        $("#zindex").val(index);
    }

    //保存属性后往数组添加元素
    function arrayField(value) {
        arField[index] = value;
        appendC(value);
    }

    //修改属性
    function editArrayField(value, msgIndex) {
        arField[msgIndex] = value;
        index = 0;
        $("#fields").html('');
        for (var i = 0; i < arField.length; i++) {
            appendC(arField[i]);
        }
    }

    //删除数组添加元素并重组列表
    function removeField(value) {
        index = 0;
        $("#fields").html('');
        arField.splice(value, 1);
        for (var i = 0; i < arField.length; i++) {
            appendC(arField[i]);
        }
    }

    //判断属性名是否重复
    function isSame(value) {
        for (var i = 0; i < arField.length; i++) {
            var array0 = arField[i].split(',fh,')[0];
            if (array0 == value) {
                return false;
            }
        }
        return true;
    }

</script>
</body>
</html>