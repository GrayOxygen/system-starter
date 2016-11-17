<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/resources/common/taglibs.jsp"%>
<!DOCTYPE html>
<!--
This is a starter template page. Use this page to start your new project from
scratch. This page gets rid of all links and provides the needed markup only.
-->
<html>
<head>
<title>首页</title>
  <%@ include file="/resources/common/required_style.jsp" %>
      <link href="http://vjs.zencdn.net/4.12/video-js.css" rel="stylesheet">
	<script src="http://vjs.zencdn.net/4.12/video.js"></script>
</head>
<body>
    <section class="content">

      <!-- Default box -->
      <div class="box">
        <div class="box-header with-border">
          <h3 class="box-title">欢迎来到${company_name}</h3>

          <div class="box-tools pull-right">
            <button type="button" class="btn btn-box-tool" data-widget="collapse" data-toggle="tooltip" title="Collapse">
              <i class="fa fa-minus"></i></button>
            <button type="button" class="btn btn-box-tool" data-widget="remove" data-toggle="tooltip" title="Remove">
              <i class="fa fa-times"></i></button>
          </div>
        </div>
        <div class="box-body">
           我们致力于向商家，用户提供便捷高效的服务
           
         <video id="MY_VIDEO_1" class="video-js vjs-default-skin" controls
		 preload="auto" width="640" height="264" poster="${ctx }/resources/video/Pierrot-Le-Fou.jpg"
		 data-setup="{}">
		 <source src="${ctx }/resources/video/纽约-廖逸君作品《Walking in My Shoes》.mp4"  type='video/mp4'>
		 <source src="${ctx }/resources/video/MY_VIDEO.webm" type='video/webm'>
		 <p class="vjs-no-js">浏览器需支持 <a href="http://videojs.com/html5-video-support/" target="_blank">HTML5视频</a></p>
		</video>

        </div>
        <!-- /.box-body
        <div class="box-footer">
          Footer
        </div>
         -->
        <!-- /.box-footer-->
      </div>
      <!-- /.box -->

    </section>
    <%@ include file="/resources/common/required_js.jsp"%>
    


</body>
</html>
