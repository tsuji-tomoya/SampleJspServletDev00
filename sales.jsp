<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@taglib prefix = "c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title></title>
</head>
<body>


	<table border="3" >
	
		<tr>
			<th>従業員名</th>
			<th>売上金額</th>
		</tr>
<c:forEach var = "list" items="${empList2}">
		<tr>
			<th style="text-align: left;">${list.employeeName}</th>
			<th style="text-align: right;">${list.sales_amount}</th>
		</tr>
		
</c:forEach>
	</table>
	



</body>
</html>