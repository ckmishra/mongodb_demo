<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@include file="header.jsp"%>
<html>
<head>
<title>Mongo Demo Project</title>
<style type="text/css">
body {
	background-image: url('http://crunchify.com/bg.png');
}

html,body {
	height: 100%;
	margin: 0;
	padding: 0;
	text-align: center;
}

div {
	height: 100%;
	float: left;
}

#left {
	width: 220px;
	margin: 5px;
}

#center {
	margin: 5px 0;
	width: calc(100% - 460px);
}

#right {
	margin: 5px;
	width: 220px;
}
</style>
</head>
<body>
	<br>
	<div id="main">
		<div id="left">
			<h3>Basic Operaiton</h3>
					${status}
			<form action="static/addAccount.html" method="get" id="form0"></form>
			<button type="submit" form="form0" value="Submit">Add New
				Account</button>
			<form action="transfer/start.jsp" method="get" id="form1"></form>
			<button type="submit" form="form1" value="Submit">Transfer
				Money</button>

			<form action="transfer/fail.jsp" method="get" id="form6"></form>
			<button type="submit" form="form6" value="Submit">Add New
				Transfer</button>
		</div>
		<div id="center">
			<h3>Recovery Operation</h3>

			<form action="transfer/recoverPending.jsp" method="post" id="form2"></form>
			<button type="submit" form="form2" value="Submit">Recover
				Pending</button>

			<form action="transfer/recoverApplied.jsp" method="post" id="form3"></form>
			<button type="submit" form="form3" value="Submit">Recover
				Applied Transaction</button>
		</div>

		<div id="right">

			<h3>Rollback Operation</h3>
			<form action="transfer/cancelPending.jsp" method="post" id="form4"></form>
			<button type="submit" form="form4" value="Submit">Cancel
				Transaction</button>
			<form action="transfer/rollbackApplied.jsp" method="post" id="form5"></form>
			<button type="submit" form="form5" value="Submit">Rollback
				Applied Transaction</button>
		</div>



	</div>
</body>
</html>