<%@taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@include file="../../header.jsp"%> 

<html>
<head>
<title>Spring MVC Form Handling</title>
</head>
<body>
	%{status}
	<h2>Submitted Transfer Information</h2>
	<table>
		<tr>
			<td>Source :</td>
			<td>${Source}</td>
		</tr>
		<tr>
			<td>Destination :</td>
			<td>${Destination}</td>
		</tr>
		<tr>
			<td>Amount :</td>
			<td>${Value}</td>
		</tr>
		<tr>
			<td>ID :</td>
			<td>${Id}</td>
		</tr>
		<tr>
			<td>State :</td>
			<td>${State}</td>
		</tr>

	</table>


</body>
</html>