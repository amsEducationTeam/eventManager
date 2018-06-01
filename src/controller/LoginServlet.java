package controller;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import dao.DaoFactory;
import dao.UsersDao;
import domain.Users;

/**
 * Servlet implementation class Login
 */
@WebServlet("/login")
public class LoginServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

		request.getRequestDispatcher("view/login.jsp").forward(request,  response);
	}


	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// requestとresponseからパラメーターの取得
		String loginId = request.getParameter("loginId");
		String loginPass = request.getParameter("loginPass");

		// loginIdとloginPassの正規化チェック 半角英数字、ハイフン、アンダースコアのみ許可
		// 両方ともtrueのときにログイン処理を続行
		if(loginId.matches("[0-9a-zA-Z\\-\\_]+") && loginPass.matches("[0-9a-zA-Z\\-\\_]+")) {
			// データベースから該当ユーザーの情報を取得
			// 該当ユーザーがいない場合又はパスワード間違いの場合、取得失敗、ログインページへ遷移(エラー付き)
			// 取得成功ならセッションにユーザーデータの一部を記録して"eventlist"サーブレットへ遷移
			try {
				UsersDao UsersDao = DaoFactory.createUsersDao();
				Users user = UsersDao.findByLoginIdAndLoginPass(loginId,  loginPass);
				String pageName = "eventToday";
				if(user != null) {
					// セッションにユーザー情報"loginId"を登録
					request.getSession().setAttribute("id", user.getId());
					// セッションにユーザー情報"loginId"を登録
					request.getSession().setAttribute("loginId", user.getLoginId());
					//セッションにユーザ名をセット(ヘッダー表示用)
					request.getSession().setAttribute("name", user.getName());
					// ユーザー種別"type_id"をセッションに登録, 2なら管理者, 1なら一般
					request.getSession().setAttribute("type_id", user.getTypeId());
					request.setAttribute("servName", pageName);
					request.getRequestDispatcher("EventServlet").forward(request,  response);
				} else {
					request.setAttribute("error", true);
					request.getRequestDispatcher("view/login.jsp").forward(request,  response);
				}
			} catch (Exception e) {
				throw new ServletException(e);
			}
		} else {
		// if文、文字列が半角英数字、ハイフン、アンダースコア以外の場合は以下の処理
		request.setAttribute("errorchar", true);
		request.getRequestDispatcher("view/login.jsp").forward(request,  response);
		}
	}
}
