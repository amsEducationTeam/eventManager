<%@ page pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html>
<html lang="ja">
<head>
<title>ユーザ詳細</title>
</head>
<body>
	<%@ include file="navbar.jsp"%>
	<div class="col-md-10 col-md-offset-1">
		<h1>ユーザ詳細</h1>

		<table class="table">

		<!--<c:out value="${user.name}" />-->

			<tr>
				<th class="col-xs-2">ID</th>
				<td class="col-xs-10"><c:out value="${user.id}" /></td>
			</tr>
			<tr>
				<th>氏名</th>
				<td><c:out value="${user.name}" /></td>
			</tr>
			<tr>
				<th>所属グループ</th>
				<td><c:out value="${user.groupName}" /></td>
			</tr>
		</table>
		<div class="btn-toolbar">
  <div class="btn-group">
			<a href="User?servletName=userList"><button type="button" class="btn btn-primary" >一覧に戻る</button></a>

		<form action="User" method="get">
			<input type="hidden" name="userId" value="${user.id}">
			<input type="hidden" name="servletName" value="userEdit">
			<input type="submit" class="btn btn-default" value="編集" />
		</form>
		<section>
		<button type="button" class="btn btn-danger"
			data-toggle="modal" data-target="#deleteAlert">削除
		</button>
		</section>


		<!-- モーダル・ダイアログ -->
		<section>
		<div class="modal fade" id="deleteAlert" tabindex="-1">
			<div class="modal-dialog">
				<div class="modal-content">
					<div class="modal-header">
						<button type="button" class="close" data-dismiss="modal">
							<span>×</span>
						</button>
						<h5 class="modal-title">本当に削除してよろしいですか？</h5></div>
					<div class="modal-footer" align="right">

						<form action="User" method="post" >
							<input type="hidden" name="userId" value="${user.id}">
							<input type="hidden" name="servletName" value="userDelete">
							<input type="submit" class="btn btn-primary pull-right"  value="OK" />
						</form>
						<button type="button" class="btn btn-default pull-right" data-dismiss="modal">Cancel</button>
					</div>
				</div>
			</div>
		</div>
		</section>
		</div>
		</div>
	</div>
</body>
</html>