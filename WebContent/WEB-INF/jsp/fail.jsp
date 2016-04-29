<%@taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@include file="../../header.jsp"%> 

<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Insert title here</title>
<style type="text/css">
body {
	background-image: url('http://crunchify.com/bg.png');
}
</style>
</head>
<body>
	<h2>Transfer Information</h2>
	<form:form method="POST" action="../transfer/fail.jsp">
		<table>
			<tr>
				<td><form:label path="source">Source</form:label></td>
				<td><form:input path="source" /></td>
			</tr>
			<tr>
				<td><form:label path="destination">Destination</form:label></td>
				<td><form:input path="destination" /></td>
			</tr>
			<tr>
				<td><form:label path="value">Amount</form:label></td>
				<td><form:input path="value" /></td>
			</tr>
			<tr>
				<td><form:label path="state">State</form:label></td>
				<td><form:input path="state" /></td>
			</tr>
			<tr>
				<td colspan="2"><input type="submit" value="Submit" /></td>
			</tr>
		</table>
	</form:form>

</body>
</html>