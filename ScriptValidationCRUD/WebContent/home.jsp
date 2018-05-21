<%@page import="java.util.ArrayList"%>
<%@page import="com.pravin.dblog4j.util.User"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/th/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Home Page</title>
</head>
<body>
	<table border="1">
		<thead>
			<tr>
				<th>User Id</th>
				<th>User Name</th>
				<th>Email</th>
				<th>Country</th>
				<th>Password</th>
				<th colspan="2">Action</th>
			</tr>
		</thead>
		<tbody>
			<c:forEach items="${users}" var="user">
				<tr>
					<td>
						<c:out value="${user.id}" />
					</td>
					<td>
						<c:out value="${user.name}" />
					</td>
					<td>
						<c:out value="${user.email}" />
					</td>
					<td>
						<c:out value="${user.country}" />
					</td>
					<td>
						<c:out value="${user.password}" />
					</td>
					<td>
						<a href="EditServlet?userId=<c:out value="${user.id}"/>">Update</a>
					</td>
					<td>
						<a href="DeleteServlet?userId=<c:out value="${user.id}"/>">delete</a>
					</td>
				</tr>
			</c:forEach>
		</tbody>
	</table>
	<p>
		<a href="register.jsp">Add User</a>
	</p>
</body>
</html>