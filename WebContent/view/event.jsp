<%@ page pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html>
<html lang="ja">
<head>
<title>本日のイベント</title>
</head>
<body>
<%@ include file="navbar.jsp"%>
	<!-- Header -->
	<header> </header>
<div class="col-md-10 col-md-offset-1">
	<!-- Main -->
	<section>
		<h1>本日のイベント</h1>

<!-- pagenation -->
<div align="right">
		<%
		//最終ページ番号をセッションから取得する
		Object lastpage = session.getAttribute("lastpage");

			//現在のページ番号を計算する
			Object todayevent_page = session.getAttribute("todayevent_page");
			int a = (int) todayevent_page;
			int now = (a / 5) + 1;
		%>


		<nav>
			<ul class="pagination">
				<% if(now!=1){ %>
				<li><a href="event?prevnext=1" aria-label="前のページへ">
						<span aria-hidden="true">«</span>
				</a></li>
				<%}

				int i = 1;%>

				<c:forEach var="i" begin="1" end="${lastpage }">

					<li <%

				if (now == i) {%> class="active" <%}%>>
						<a href="event?page=<%= i %>"><%= i %></a>
					</li>
					<%i++; %>
				</c:forEach>

					<%

					if(now!=i-1){ %>
					<li><a href="event?prevnext=2" aria-label="次のページへ">
							<span aria-hidden="true">»</span>
					</a></li>
					<%}
				%>

			</ul>
		</nav>
</div>
<!-- pagenation -->



		<table class="table table-bordered table-hover ">
			<tr>
				<th>タイトル</th>
				<th>開始日時</th>
				<th>場所</th>
				<th>対象グループ</th>
				<th>詳細</th>

			</tr>
			<c:forEach items="${eventsList}" var="event">
				<tr>
					<td> <c:out value="${event.title}" />
						<c:if test="${event.user_id==id}">
							<span class="label label-danger">参加</span>
						</c:if>
					</td>
					<td><fmt:formatDate value="${event.start}" pattern="y年M月d日(E) HH時mm分" /></td>
					<td><c:out value="${event.place}" /></td>
					<td><c:out value="${event.groups_name}" /></td>
					<td>
						<form action="EventServlet" method="post">
							<input type="hidden" name="info" value="${event.id}">
							<input type="hidden" name="servletName" value="eventInfo">
							<input
								type="submit" class="btn btn-info" value="詳細" />
						</form>
					</td>

				</tr>
			</c:forEach>
		</table>
	</section>


</div>
</body>
</html>