<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
    <%@taglib prefix = "c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Insert title here</title>

</head>
<body>
<h1>商品登録画面</h1>
	<form action="newProduct" method="post">　
		<input type="hidden"  name = "max" value = "${MAX}"/>　
		<table>
		
			<tr>
				<td>商品ID</td>
				<td><input TYPE = "text" name="product" disabled/></td>
			</tr>
			
			<tr>
				<td>商品コード</td>
				<td><input TYPE = "text" name="item_id" value="${code}" /></td>
				<td>${error1}${error2}</td>
			</tr>
			
			<tr>
				<td>商品名</td>
				<td><input TYPE = "text" name="product_name" value="${name}"/></td>
				<td>${error}</td>
			<tr>
			
			<tr>
				<td>価格</td>
				<td><input TYPE = "text" name="price" value="${Price}"/></td>
				<td>${error3}${error4}</td>
			</tr>
			
			<tr>
				<td>カテゴリー</td>
				<td>
					<select NAME = "category" style="width: 125px;">
						<c:forEach var = "nelist" items = "${NPlist}">
							<option value="${nelist.categoryID}" 
							 <c:if test="${ID==nelist.categoryID}">selected</c:if>>${nelist.productName}</option>
						</c:forEach>
					</select>
				</td>
			</tr>
			
			<tr>
				<td></td>
				<td>
					<input type="submit" VALUE="登録" />
				</td>
			</tr>
			
		</table>
		
	</form>
	${message}
</body>
</html>