<%@ page pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html lang="ja">
<head>
<title>イベント登録</title>
</head>
<body>
<%@ include file="navbar.jsp"%>
	<!-- Main -->
	<section>
		<div class="row">
			<div class="container col-md-10 col-md-offset-1">
				<h1>イベント登録</h1>
				<!-- 入力画面-->
				<div class="form-group">
				<form action="eventinsert" method="post">
					<p class="bold">タイトル（必須）</p>
					<p>
						<input type="text" name="title" class="form-control" maxlength="50" required>
					</p>

					<p class="bold">開始日時（必須）</p>
					<p>
						<input type="text" name="start" placeholder="0000-00-00 00:00:00" class="form-control" required>
					</p>

					<p class="bold">終了日時</p>
					<input type="text" name="end" placeholder="0000-00-00 00:00:00" class="form-control">


					<p class="bold">場所（必須)</p>
					<p>
						<input type="text" name="place" class="form-control" maxlength="255" required>
					</p>

					<p class="bold">対象グループ</p>
					<p><select name="group_id" class="form-control">
							<option value="8">全員</option>
							<option value="1">営業部</option>
							<option value="2">人事部</option>
							<option value="3">技術部</option>
							<option value="4">総務部</option>
							<option value="5">広報部</option>
							<option value="6">経理部</option>
							<option value="7">企画部</option>
						</select>
					</p>
					<p class="bold">詳細</p>
					<p>
						<textarea name="detail" rows="4" cols="40" class="form-control"></textarea>
					</p>
					<p>
						<a href="eventlist" class="btn btn-default">キャンセル</a>
						<input type="submit" class="btn btn-primary" value="登録" />
					</p>
				</form>
				</div>
			</div>
		</div>
	</section>
</body>
</html>