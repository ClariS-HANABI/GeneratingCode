function getWebPath() {
	//获取当前网址，如： http://localhost:8083/uimcardprj/share/meun.jsp
	var curWwwPath = window.document.location.href;
	//获取主机地址之后的目录，如： uimcardprj/share/meun.jsp
	var pathName = window.document.location.pathname;
	var pos = curWwwPath.indexOf(pathName);
	//获取主机地址，如： http://localhost:8083
	var localhostPaht = curWwwPath.substring(0, pos);
	//获取带"/"的项目名，如：/uimcardprj
	// var projectName = pathName.substring(0, pathName.substr(1).indexOf('/') + 1);
    var projectName = "";
	return {
		host:localhostPaht,
		projName:projectName
	};
}

function fileUpload(options) {
	var settings = {
			button_text: '上传文件',
			button_width: 100,
			button_height: 32,
			url: getWebPath().host + '/upload/uploadFiles',
			limit_size: "20 MB",
			fileType: "*.rar;*.zip;*.pdf;*.doc;*.docx;*.xls;*.xlsx",
			upload_limit: "0",
			queuedHandler: function (fileName) { },
			queueErrorHandler: function (code, message) {
				switch (code) {
				case SWFUpload.QUEUE_ERROR.QUEUE_LIMIT_EXCEEDED:
					alert('文件数超出限制');
					break;
				case SWFUpload.QUEUE_ERROR.FILE_EXCEEDS_SIZE_LIMIT:
					alert('文件大小超过限制');
					break;
				case SWFUpload.QUEUE_ERROR.ZERO_BYTE_FILE:
					alert('不能上传空文件');
					break;
				case SWFUpload.QUEUE_ERROR.INVALID_FILETYPE:
					alert('非法文件类型');
					break;
				}
			},
			uploadStartHandler: function () { },
			uploadProcessHandler: function (file, bytes, total) { },
			uploadErrorHandler: function (code, message) {
				switch (code) {
				case SWFUpload.UPLOAD_ERROR.HTTP_ERROR:
					alert('HTTP请求错误');
					break;
				case SWFUpload.UPLOAD_ERROR.MISSING_UPLOAD_URL:
					alert('缺失上传处理文件路径');
					break;
				case SWFUpload.UPLOAD_ERROR.IO_ERROR:
					alert('上传流错误');
					break;
				case SWFUpload.UPLOAD_ERROR.SECURITY_ERROR:
					alert('非法上传');
					break;
				case SWFUpload.UPLOAD_ERROR.UPLOAD_LIMIT_EXCEEDED:
					alert('超过上去限制');
					break;
				case SWFUpload.UPLOAD_ERROR.UPLOAD_FAILED:
					alert('上传失败');
					break;
				case SWFUpload.UPLOAD_ERROR.SPECIFIED_FILE_ID_NOT_FOUND:
					alert('缺失指定文件编号');
					break;
				case SWFUpload.UPLOAD_ERROR.FILE_VALIDATION_FAILED:
					alert('文件验证失败');
					break;
				case SWFUpload.UPLOAD_ERROR.FILE_CANCELLED:
					alert('文件取消上传');
					break;
				case SWFUpload.UPLOAD_ERROR.UPLOAD_STOPPED:
					alert('上传终止');
					break;
				}
			},
			uploadSuccessHandler: function (file, serverData, response) { },
			uploadCompletedHandler: function (file) { }
	};

	var self = this;
	var opt = $.extend({}, settings, options);

	this.swfUp = new SWFUpload({
		upload_url: opt.url,
		file_size_limit: opt.limit_size,
		file_types: opt.fileType,
		file_types_description: "Normal File",
		file_upload_limit: opt.upload_limit,
		file_queued_handler: function (file) {
			opt.queuedHandler.call(self, file.name);
		},
		file_queue_error_handler: function (file, code, message) {
			opt.queueErrorHandler.call(self, code, message);
		},
		upload_start_handler: function () {
			opt.uploadStartHandler.call(self);
		},
		upload_progress_handler: function (file, bytes, total) {
			opt.uploadProcessHandler.call(self, file, bytes, total);
		},
		upload_error_handler: function (file, code, message) {
			opt.uploadErrorHandler.call(self, code, message);
		},
		upload_success_handler: function (file, serverData, response) {
			opt.uploadSuccessHandler.call(self, file, serverData, response);
		},
		upload_complete_handler: function (file) {
			opt.uploadCompletedHandler.call(self, file);
		},
		post_params: opt.post_params,
		button_image_url: getWebPath().host + "/static/images/upimg.png",
		button_placeholder_id: opt.holdId,
		button_width: 100,//opt.button_width,
		button_height: 32,//opt.button_height,
		button_cursor: SWFUpload.CURSOR.HAND,
		//button_text: '<span class="swfUploadButton">' + opt.button_text + '</span>',
		//button_text_style: '.swfUploadButton { font-family: 宋体,Helvetica,Arial,sans-serif; font-size: 14px;color:#FFFFFF; font-weight:bold;text-align:center; }',
		//button_text_top_padding: 5,
		//button_text_left_padding: 0,
		//button_window_mode: SWFUpload.WINDOW_MODE.TRANSPARENT,http://project.haitun56.com.cn:2828/fscp/
		flash_url: getWebPath().host + "/static/swfupload/swfupload.swf",
		// flash_url: "http://project.haitun56.com.cn:1818/fscp/static/swfupload/swfupload.swf",
		debug: false
	});

	var that = this;

	$(window).unload(function () {
		that.swfUp.destroy();
	});
}

fileUpload.prototype.setPostParams = function (params) {
	this.swfUp.setPostParams(params);
};

fileUpload.prototype.start = function () {
	this.swfUp.startUpload();
};
