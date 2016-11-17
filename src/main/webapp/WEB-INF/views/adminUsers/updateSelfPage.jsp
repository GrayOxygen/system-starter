<%@ page contentType="text/html; charset=UTF-8"%>
<%@ include file="/resources/common/taglibs.jsp"%>
<!DOCTYPE html  >
<html >
<head>
<title>修改个人信息</title>
    <!-- bootstrapValidator -->
	  <link rel="stylesheet" href="${resources_static}/plugins/projectSpecific/bootstrapValidator/css/bootstrapValidator.min.css">
	  <link href="${resources_static}/plugins/bootstrap-fileinput/fileinput.min.css" media="all" rel="stylesheet" type="text/css" />
<style>
#currentBodyContent {
	width: 60%
}
</style>
</head>

<body>
           <div  id="currentBodyContent" class="box box-info">
            <div class="box-header with-border">
              <h3 class="box-title"></h3>
            </div>
            <!-- /.box-header -->
            <!-- form start -->
            <form class="form-horizontal"  action="javascript:;"  id="submitForm"  method="POST">
              <input type="hidden"   name="id" value="${model.id }">
              <input name="needUpdateNewFile" id="needUpdateNewFile" value="0" type="hidden">
              <div class="box-body">
              
             <div class="form-group">
              <label class="col-sm-2 control-label">头像 </label>

              <div class="col-md-4">
                <div class="input-icon right">
						<input id="fileID"  name="avatar" type="file" class="file" data-show-preview="true"  data-show-upload="false"  />
                </div>
              </div>
            </div>
                
           		
              </div>
              <!-- /.box-body -->
              <div class="box-footer">
                <button type="button" class="btn btn-info pull-right " id="submitBtn">提交</button>
              </div>
              <!-- /.box-footer -->
            </form>
          </div>
          
          
          
          
          <!-- /.box -->
          <%@ include file="/resources/common/required_js.jsp"%>
		 <%@ include file="/resources/common/bootstrap_alert.jsp"%>
		  <script type="text/javascript" src="${resources_static}/plugins/projectSpecific/bootstrapValidator/js/bootstrapValidator.min.js"></script>
           <!-- file input控件 -->
		  <script src="${resources_static}/plugins/bootstrap-fileinput/fileinput.min.js" type="text/javascript"></script>
		         
         <script>
		$(function(){
			$("#cancelBtn").click(function(){
				window.location.href="${ctx}/adminUsers/updateSelfPage"
			})
		  	// 点击之前聚焦校验
		  	$("#submitBtn").focus(function(){
					$("#submitForm").data('bootstrapValidator').validate();
			 });
			 // 提交点击事件
			$("#submitBtn").click(function(){
				// 校验通过
				if($("#submitForm").data('bootstrapValidator').isValid()){
				
				   var formData = new FormData($("#submitForm")[0]);
					$.ajax({
						url:"${ctx}/adminUsers/updateSelf" ,
						type:"POST",
						data: formData ,
						processData: false,  // 告诉jQuery不要去处理发送的数据
						contentType: false,   // 告诉jQuery不要去设置Content-Type请求头
						success:function(result){
							if(result&&result.success==true){
								openAlert(result.message);
								setTimeout(function(){
									top.location.href="${ctx}/adminUsers/index";
								},1000);
							}else{
								openAlert(result.message);
							}
						}
					});
					
				}
			});
			
			
			// 初始化图像信息
           var control = $('#fileID');

           //重要，需要更新控件的附加参数内容，以及图片初始化显示
           control.fileinput('refresh', {
               initialPreview: [ //预览图片的设置
                   "<img style='width:120px;height:120px' src='${model.avatar}' class='file-preview-image' alt='头像' title='头像'>",
               ] 
               
           });
		  
   			// 文件选择框点击事件:没改图片则使用原有的
	   		$('#fileID').on('filebrowse', function(event) {
				$("#needUpdateNewFile").val(1);
	   		});
			 
			// 加载校验器				    
		    $('#submitForm').bootstrapValidator({
		        fields: {
		            /**avatar: {
		                message: '当前值无效',
		                validators: {
		                    notEmpty: {
		                        message: 'avatar不能为空'
		                    }
		                }
		            }**/
		        }
		    });
		
		})
		
</script>

</body>
</html>