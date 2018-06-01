<%@ page pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html>
<html lang="ja">
<head>
<meta charset="UTF-8">
<title>ユーザ編集</title>
</head>
<body>
<%@ include file="navbar.jsp"%>
	<!-- Main -->
	<section>
		<div class="row">
			<div class="container col-md-10 col-md-offset-1">
				<h1>ユーザ編集</h1>
				<!-- 入力画面-->
				<c:if test="${!empty errorchar}">
						<div class="alert alert-warning" role="alert">ログインID、パスワードは半角英数字のみ入力できます</div>
					</c:if>
					<div class="form-group">
				<form action="User" method="post">
					<p class="bold">氏名（必須）</p>
					<p>
						<input type="text" name="name" placeholder="氏名" class="form-control" value="${user.name}" maxlength="50" required >
					</p>

					<p class="bold">ログインID（必須）</p>

						<c:if test="${!empty error}">
						<div class="alert alert-warning" role="alert">ログインIDが既に使用されています</div>
						</c:if>
					<p>
						<input type="text" name="login_id" placeholder="ログインID" class="form-control" value="${user.loginId}" maxlength="20" required>
					</p>

					<p class="bold">パスワード（変更の場合のみ)</p>
					<input type="text" name="login_pass" placeholder="パスワード" class="form-control" maxlength="60"  placeholder="パスワード">

					<p class="bold">所属グループ</p>
					<p><select name="group_id" class="form-control" >
							<option value="1">営業部</option>
							<option value="2">人事部</option>
							<option value="3">技術部</option>
							<option value="4">総務部</option>
							<option value="5">広報部</option>
							<option value="6">経理部</option>
							<option value="7">企画部</option>
						</select>
					</p>
					<p>
						<a href="User?userId=${user.id}&servletName=userInfo" type="button" class="btn btn-default">キャンセル</a>
						<input type="hidden" name="servletName" value="userEdit"/>
						<input type="submit" class="btn btn-primary" value="登録" />
					</p>
				</form>
				</div>
			</div>
		</div>
	</section>
</body>
</html>