<%@ page contentType="text/html; charset=UTF-8"%>
<%@ include file="/resources/common/taglibs.jsp"%>
<!DOCTYPE html  >
<html >
<head>
<title>修改用户</title>
    <!-- bootstrapValidator -->
	  <link rel="stylesheet" href="${resources_static}/plugins/projectSpecific/bootstrapValidator/css/bootstrapValidator.min.css">
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
              <div class="box-body">
              	 <div class="form-group">
                  <label for="userName" class="col-sm-2 control-label">用户名</label>

                  <div class="col-sm-5">
                    <input type="text" class="form-control" id="userName"  name="userName" placeholder="用户名"  value="${model.userName}">
                  </div>
                </div>
                <div class="form-group">
                  <label for="pwd" class="col-sm-2 control-label">密码</label>

                  <div class="col-sm-5">
                    <input type="password" class="form-control" id="pwd"  name="pwd"   placeholder="密码"  value="">
                  </div>
                </div>
                <div class="form-group">
                  <label for="email" class="col-sm-2 control-label">邮箱</label>

                  <div class="col-sm-5">
                    <input type="email" class="form-control" id="email"  name="email" placeholder="邮箱" value="${model.email }">
                  </div>
                </div>
                <div class="form-group">
                  <label for="phoneNum" class="col-sm-2 control-label">手机号码</label>

                  <div class="col-sm-5">
                    <input type="text" class="form-control" id="phoneNum"  name="phoneNum"   placeholder="手机号码" value="${model.phoneNum}">
                  </div>
                </div>
                
                <input type="hidden"  id="rolesID"  name="rolesID" />
                 <div class="form-group">
	                <label for="pwd" class="col-sm-2 control-label">角色列表</label>

          			<div class="col-sm-10" >
						<div class="input-icon right">
							<div class="panel panel-info"  >
							   <div class="panel-heading">
							      <h3 class="panel-title">勾选角色</h3>
							   </div>
							   <div class="panel-body">
								  <iframe src="${ctx}/adminUsers/rolesListPage?adminUserId=${model.id}"    id="iFrame"   style="height: 43em;width: 100%;border:0" ></iframe>
							   </div>
						    </div>   
						 </div>                     	
            		</div>
           		</div>
           		
              </div>
              <!-- /.box-body -->
              <div class="box-footer">
                <button type="button" class="btn btn-default" id="cancelBtn">取消</button>
                <button type="button" class="btn btn-info pull-right " id="submitBtn">提交</button>
              </div>
              <!-- /.box-footer -->
            </form>
          </div>
          
          
          
          
          <!-- /.box -->
          <%@ include file="/resources/common/required_js.jsp"%>
		 <%@ include file="/resources/common/bootstrap_alert.jsp"%>
		  <script type="text/javascript" src="${resources_static}/plugins/projectSpecific/bootstrapValidator/js/bootstrapValidator.min.js"></script>
          <script>
		$(function(){
			$("#cancelBtn").click(function(){
				window.location.href="${ctx}/adminUsers/listPage.do"
			})
		  	// 点击之前聚焦校验
		  	$("#submitBtn").focus(function(){
					 $("#submitForm").data('bootstrapValidator').validate();
			 });
			 // 提交点击事件
			$("#submitBtn").click(function(){
				// 校验 iframe 选择项  
				var iFrame = document.getElementById('iFrame');
				// 兼容：获取窗口对象
				var win = iFrame.window || iFrame.contentWindow;
				var checked = win.getCheckedFromIFrame();
				if( !(checked && checked.length>0) ){
					openAlert("请选择角色!");
					return ;
				}
				$("#rolesID").val(checked);
				
				// 校验通过
				if($("#submitForm").data('bootstrapValidator').isValid()){
				
				   var formData = new FormData($("#submitForm")[0]);
					$.ajax({
						url:"${ctx}/adminUsers/edit" ,
						type:"POST",
						data:formData ,
						processData: false,  // 告诉jQuery不要去处理发送的数据
						contentType: false,   // 告诉jQuery不要去设置Content-Type请求头
						success:function(result){
							if(result&&result.success==true){
								openAlert(result.message);
								setTimeout(function(){
									top.location.href="${ctx}/adminUsers/listPage";
								},1000);
							}else{
								openAlert(result.message);
							}
						}
					});
					
				}
			});
			 
			// 加载校验器				    
		    $('#submitForm').bootstrapValidator({
		        fields: {
		        	userName: {
		                message: '当前值无效',
		                validators: {
		                    notEmpty: {
		                        message: '账号不能为空'
		                    },
		                    stringLength: {
		                        min: 1,
		                        max: 25,
		                        message: '账号长度不能大于25个字符'
		                    }
		                }
		            },
		            pwd: {
		                message: '当前值无效',
		                validators: {
		                    notEmpty: {
		                        message: '密码不能为空'
		                    },
		                    stringLength: {
		                        min: 6,
		                        max: 12,
		                        message: '密码长度6到12位'
		                    }
		                }
		            },
		            phoneNum: {
		                message: '当前值无效',
		                validators: {
		                    notEmpty: {
		                        message: '手机号码不能为空'
		                    },
		                    regexp: {
		                        regexp: /^(1[2-9][0-9])[0-9]{8}$/,
		                        message: '手机号格式不合法'
		                    }
		                }
		            } 
		        }
		    });
		})
		
</script>
</body>
</html>