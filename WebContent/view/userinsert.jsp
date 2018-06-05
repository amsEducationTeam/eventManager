<%@ page pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html>
<html lang="ja">
<head>
<title>ユーザ登録</title>
</head>
<body>
<%@ include file="navbar.jsp"%>
	<!-- Main -->
	<section>
		<div class="row">
			<div class="container col-md-10 col-md-offset-1">
				<h1>ユーザ登録</h1>
				<!-- 入力画面-->
				 	<c:if test="${!empty errorchar}">
						<div class="alert alert-warning" role="alert">ログインID、パスワードは半角英数字のみ入力できます</div>
					</c:if>
					<c:if test="${!empty error}">
						<div class="alert alert-warning" role="alert">ログインIDが既に使用されています</div>
					</c:if>
					<div class="form-group">
				<form action="User" method="post">
					<p class="bold">氏名（必須）</p>
					<p>
						<input type="text" name="name" placeholder="氏名" class="form-control" maxlength="50" required>
					</p>

					<p class="bold">フリガナ（必須）</p>
					<p>
						<input type="text" name="kana" placeholder="フリガナ" class="form-control" maxlength="100" required>
					</p>

					<p class="bold">住所（必須）</p>
					<p>
						<input type="text" name="address" placeholder="(例)東京都新宿区○○△△-□□" class="form-control" maxlength="255" required>
					</p>

					<p class="bold">電話番号（必須）</p>
					<p>
						<input type="tel" name="address" placeholder="(例)090-1234-5678" class="form-control" maxlength="13" required>
					</p>


					<p class="bold">誕生日（必須）</p>
					<p>
						<input type="date" name="birthday" placeholder="yyyy-mm-dd" class="form-control" required>
					</p>

					<p class="bold">ログインID（必須）</p>
					<p>
						<input type="text" name="login_id" placeholder="ログインID" class="form-control" maxlength="20" required>
					</p>

					<p class="bold">パスワード（必須)</p>
					<input type="text" name="login_pass" placeholder="パスワード" class="form-control" maxlength="60" required>


					<p class="bold">所属グループ（必須）</p>
					<p><select name="dep_id" class="form-control">
							<option value="1">人事部</option>
							<option value="2">経理部</option>
							<option value="3">総務部</option>
							<option value="4">営業部</option>
							<option value="5">開発部</option>


						</select>
					</p>
					<p>
						<a href="User?servletName=userList" class="btn btn-default">キャンセル</a>
						<input type="hidden" name="servletName" value="userInsert">
						<input type="submit" class="btn btn-primary" value="登録" />
					</p>
				</form>
				</div>
			</div>
		</div>
	</section>
</body>
</html>