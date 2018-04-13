<%@page contentType="text/html; charset=utf-8"%>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c"%>
<!DOCTYPE html>
<html lang="zh-CN">
<head>
<meta charset="utf-8">
<meta http-equiv="X-UA-Compatible" content="IE=edge">
<meta name="viewport" content="width=device-width,initial-scale=1">
<title>服务接口</title>
<link href="../static/bootstrap/3.3.5/css/bootstrap.min.css" rel="stylesheet">
<script src="../static/jquery/1.11.3/jquery.min.js"></script>
<script src="../static/bootstrap/3.3.5/js/bootstrap.min.js"></script>
<style>
table {
    font-family: arial, sans-serif;
    border-collapse: collapse;
    width: 100%;
}

td, th {
    border: 1px solid #dddddd;
    text-align: left;
    padding: 8px;
}

tr:nth-child(even) {
    background-color: #dddddd;
}
</style>
</head>
<body class="home-template">
	<header class="site-header jumbotron">
		<div class="container">
			<div class="row">
				<div class="col-xs-12">
					<h1>服务接口</h1>
					<p> 业务接口，游戏接口....</p>
					<form class="" role="search">
						<div class="form-group">
							<input type="text" class="form-control search clearable"
								placeholder="搜索接口ID"> <i class="fa fa-search"></i>
						</div>
					</form>
				</div>
			</div>
		</div>
	</header>
	
	<main class="packages-list-container" id="all-packages">
	<div class="container">
		<div class="row">
			<div class="col-xs-12">
			   <p class="navbar-text"><span class="glyphicon glyphicon-search" aria-hidden="true"></span>接口列表</p>
			   <p>
						<table >
					        <tr>
						      <td style="width:150px;">ID</td>
						      <td style="width:250px;">名称</td>
						      <td style="width:250px;">请求参数</td>
						      <td style="width:250px;">请求参数中文</td>
						      <td style="width:250px;">响应参数</td>
						      <td style="width:250px;">响应中文参数</td>
						      
						      <td>说明</td>
						   </tr>  
						<c:forEach var="item" items="${interfaces}">   
						   <tr>
						      <td>${item.value.id}</td>
						      <td>${item.value.name}</td>
						      <td>${item.value.reqEn}</td>
						      <td>${item.value.reqCn}</td>
						      <td>${item.value.replyEn}</td>
						      <td>${item.value.replyCn}</td>
						      <td>${item.value.explain}</td>
						   </tr>  
					   </c:forEach>  
				    </table>
			    </p>
			   </div>
			</div>
		</div>

	</main>
	<footer id="footer" class="footer hidden-print">
		
	</footer>
</body>
</html>