<%@ page pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html>
<html lang="ja">
<head>
<title>ユーザ一覧</title>
</head>
<body>
	<%@ include file="navbar.jsp"%>
	<div class="col-md-10 col-md-offset-1">
		<h1>ユーザ一覧</h1>


		<%
		//最終ページ番号をセッションから取得する
		Object lastpage = session.getAttribute("lastpage");

			//現在のページ番号を計算する
			Object user_page = session.getAttribute("user_page");
			int a = (int) user_page;
			int now = (a / 5) + 1;
		%>


		<nav>
			<ul class="pagination">
				<% if(now!=1){ %>
				<li><a href="UserListServlet?prevnext=1" aria-label="前のページへ">
						<span aria-hidden="true">«</span>
				</a></li>
				<%}

				int i = 1;%>

				<c:forEach var="i" begin="1" end="${lastpage }">

					<li <%

				if (now == i) {%> class="active" <%}%>>
						<a href="UserListServlet?page=<%= i %>"><%= i %></a>
					</li>
					<%i++; %>
				</c:forEach>

					<%
					if(now!=i-1){ %>
					<li><a href="UserListServlet?prevnext=2" aria-label="次のページへ">
							<span aria-hidden="true">»</span>
					</a></li>
					<%}
				%>

			</ul>
		</nav>







		<table class="table table-bordered table-hover ">
			<tr>
				<th>ID</th>
				<th>氏名</th>
				<th>所属グループ</th>
				<th>詳細</th>

			</tr>
			<c:forEach items="${usersList}" var="user">
				<tr>
					<td><c:out value="${user.id}" /></td>
					<td><c:out value="${user.name}" /></td>
					<td><c:out value="${user.groupName}" /></td>
					<td>
						<form action="UserListServlet" method="post">
							<input type="hidden" name="info" value="${user.id}">
							<input type="hidden" name="flag" value="1">
							<input type="submit" class="btn btn-info" value="詳細" />
						</form>
					</td>

				</tr>
			</c:forEach>
		</table>
		<form action="User" method="post">
			<!--  view/addMember.jsp-->
			<input type="hidden" name="flag" value="2">
			<input type="submit" class="btn btn-primary" value="ユーザの登録" />
		</form>
	</div>
</body>
</html>