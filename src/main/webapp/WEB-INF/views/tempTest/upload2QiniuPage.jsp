<%@ page contentType="text/html; charset=UTF-8"%>
<%@ include file="/resources/common/taglibs.jsp"%>
<!DOCTYPE html  >
<html >
<head>
<!-- 七牛的javascript sdk太扯淡，浪费时间，干脆直接上传到服务端再上传到七牛 -->
<title>七牛云上传视频等大文件演示</title>

    <!-- bootstrapValidator -->
	  <link rel="stylesheet" href="${resources_static}/plugins/projectSpecific/bootstrapValidator/css/bootstrapValidator.min.css">
<link rel="stylesheet" href="${resources_static}/plupload/jquery.plupload.queue/css/jquery.plupload.queue.css">

</head>

<body>
			<div id="uploader">
			    <p>Your browser doesn't have Flash, Silverlight or HTML5 support.</p>
			</div>
			          
			<button id="toStop">暂停一下</button>
			<button id="toStart">再次开始</button>
          <!-- /.box -->
          <%@ include file="/resources/common/required_js.jsp"%>
		 <%@ include file="/resources/common/bootstrap_alert.jsp"%>
		 <script src="${resources_static}/plupload/plupload.full.min.js"></script>
		 <!-- 
		 <script src="//cdn.bootcss.com/plupload/2.1.9/moxie.min.js"></script>
		  -->
		 <script src="${resources_static}/plupload/zh_CN.js"></script>
		 <script src="${resources_static}/plupload/moxie.js"></script>
		  <script src="${resources_static}/plugins/jQueryUI/jquery-ui.js"></script>
		  
		  <script src="${resources_static}/plupload/jquery.plupload.queue/jquery.plupload.queue.js"></script>
		  
        <script type="text/javascript">
 		/**
						var uploader = new plupload.Uploader({
						  browse_button: 'browse', // this can be an id of a DOM element or the DOM element itself
						  url: '${ctx}/tempTest/uploadFile?',
						  chunk_size: '4096kb',
						  max_retries: 3
						});
						 
						uploader.init();
						// 绑定事件
						uploader.bind('FilesAdded', function(up, files) {// 列表新增上传文件
						  var html = '';
						  plupload.each(files, function(file) {
						    html += '<li id="' + file.id + '">' + file.name + ' (' + plupload.formatSize(file.size) + ') <b></b></li>';
						  });
						  document.getElementById('filelist').innerHTML += html;
						});
						 
						uploader.bind('UploadProgress', function(up, file) {//进度条事件：控制进度条显示
						  document.getElementById(file.id).getElementsByTagName('b')[0].innerHTML = '<span>' + file.percent + "%</span>";
						});
						 
						uploader.bind('Error', function(up, err) {// 错误消息显示
						  document.getElementById('console').innerHTML += "\nError #" + err.code + ": " + err.message;
						});
						 
						document.getElementById('start-upload').onclick = function() { // 上传按钮点击事件
						  uploader.start();
						};
						document.getElementById('toStop').onclick = function() { // 上传按钮点击事件
							  uploader.stop();
							};
 
			**/				
							
						$(function(){
						 	 $("#uploader").pluploadQueue({
						        // General settings
						        runtimes : 'html5,flash,silverlight,html4',
						        url: '${ctx}/tempTest/uploadFile?lastModifiedTime=',
						 
						        // Maximum file size
						        // max_file_size : '5000mb',
						 
						        chunk_size:  '${blockSize}b',
						 
						        // Resize images on clientside if we can
						        resize : {
						            width : 200,
						            height : 200,
						            quality : 90,
						            crop: true // crop to exact dimensions
						        },
						 
						        // Specify what files to browse for
						        filters : {
						            mime_types : [
						                { title : "视频文件", extensions : "mp4,avi,jpg,png" }
						              ],
						              max_file_size: "6000mb",
						              prevent_duplicates: true
						        },
						 
						        // Rename files by clicking on their titles
						        rename: true,
						         
						        // Sort files
						        sortable: true,
						 
						        // Enable ability to drag'n'drop files onto the widget (currently only HTML5 supports that)
						        dragdrop: true,
						 
						        // Views to activate
						        views: {
						            list: true,
						            thumbs: true, // Show thumbs
						            active: 'thumbs'
						        },
						 
						        // Flash settings
						        flash_swf_url : '${resources_static}/plupload/Moxie.swf',
						     
						        // Silverlight settings
						        silverlight_xap_url : '${resources_static}/plupload/Moxie.xap',
						        
						        
						        
						        
						        
						        
						       /** UploadComplete :  function(up, files) {
							 			      $('filelist').innerHTML = "您选择的文件已经全部上传，总计共" + files.length + "个文件";
							 	});**/
							 	
							 	 init : {
							            PostInit: function() {
							            },
							 
							            Browse: function(up) {
							                // Called when file picker is clicked
							                console.log('[Browse]');
							            },
							 
							            Refresh: function(up) {
							                // Called when the position or dimensions of the picker change
							                console.log('[Refresh]');
							            },
							  
							            StateChanged: function(up) {
							                // Called when the state of the queue is changed
							                console.log('[StateChanged]', up.state == plupload.STARTED ? "STARTED" : "STOPPED");
							            },
							  
							            QueueChanged: function(up) {
							                // Called when queue is changed by adding or removing files
							                console.log('[QueueChanged]');
							            },
							 
							            OptionChanged: function(up, name, value, oldValue) {
							                // Called when one of the configuration options is changed
							                console.log('[OptionChanged]', 'Option Name: ', name, 'Value: ', value, 'Old Value: ', oldValue);
							            },
							 
							            BeforeUpload: function(up, file) {
							                // Called right before the upload for a given file starts, can be used to cancel it if required
							                console.log(file)
							                up.settings.url += file.lastModifiedDate.getTime() + "&fileTotalSize="+file.origSize;
							                
							            },
							  
							            UploadProgress: function(up, file) {
							                // Called while file is being uploaded
							                console.log('[UploadProgress]', 'File:', file, "Total:", up.total);
							            },
							 
							            FileFiltered: function(up, file) {
							                // Called when file successfully files all the filters
							                console.log('[FileFiltered]', 'File:', file);
							            },
							  
							            FilesAdded: function(up, files) {
							                // Called when files are added to queue
							                console.log('[FilesAdded]');
							  
							                plupload.each(files, function(file) {
							                    console.log('  File:', file);
							                });
							            },
							  
							            FilesRemoved: function(up, files) {
							                // Called when files are removed from queue
							                console.log('[FilesRemoved]');
							  
							                plupload.each(files, function(file) {
							                    console.log('  File:', file);
							                });
							            },
							  
							            FileUploaded: function(up, file, info) {
							                // Called when file has finished uploading
							                console.log('[FileUploaded] File:', file, "Info:", info);
							                console.log(info)
							            },
							  
							            ChunkUploaded: function(up, file, info) {
							                // Called when file chunk has finished uploading
							                console.log('[ChunkUploaded] File:', file, "Info:", info);
							            },
							 
							            UploadComplete: function(up, files) {
							                // Called when all files are either uploaded or failed
							                console.log('[UploadComplete]');
							            },
							 
							            Destroy: function(up) {
							                // Called when uploader is destroyed
							                console.log('[Destroy] ');
							            },
							  
							            Error: function(up, args) {
							                // Called when error occurs
							                console.log('[Error] ', args);
							            }
							        }
						 	 
						    });
						
						 	  $("#toStop").on('click', function () {
						 	      uploader.stop();
						 	    });

						 	    $("#toStart").on('click', function () {
						 	      uploader.start();
						 	    });
						 	    
						})	
			
</script>

</body>
</html>