package controller;

import java.io.IOException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.mindrot.jbcrypt.BCrypt;

import dao.AttendDao;
import dao.DaoFactory;
import dao.UsersDao;
import domain.Users;

/**
 * Servlet implementation class UserListServlet
 */
@WebServlet("/User")
public class UserServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private int editId;
	private String loginId;

	protected final String USER_LIST = "userList";
	protected final String USER_INFO = "userInfo";
	protected final String USER_INSERT = "userInsert";
	protected final String USER_EDIT = "userEdit";
	protected final String USER_DELETE = "userDelete";


	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		String servlet_id = request.getParameter("servletName");
		switch (servlet_id) {
		case USER_LIST:
			int page = 0;
			try {
				try {
					page = Integer.parseInt(request.getParameter("page")); //userlistから送られてきたのを受け取る

					//pageに開始ページを格納する
					if (page == 1) {
						page = 0;
					} else {
						page = 5 * (page - 1);
					}
					request.getSession().setAttribute("user_page", page); //セッションに格納する
				} catch (Exception e) {
					int prevnext = Integer.parseInt(request.getParameter("prevnext")); //userlistから送られてきたのを受け取る
					//セッションに保存したuser_pageを取得し、変数に格納する
					int user_page = (Integer) request.getSession().getAttribute("user_page");

					//←→をするための仕組みを準備する
					if (prevnext == 1) {
						//もどる
						user_page = user_page - 5;
					} else {
						//すすむ
						user_page = user_page + 5;
					}
					request.getSession().setAttribute("user_page", user_page); //セッションに格納する

				}
			} catch (Exception e) {
				page = 0; //navbarなどからこのページにきたときの処理
				request.getSession().setAttribute("user_page", page); //セッションに格納する
			}
			try {
				//セッションに保存したuser_pageを取得し、変数に格納する
				int user_page = (Integer) request.getSession().getAttribute("user_page");

				UsersDao usersDao = DaoFactory.createUsersDao();
				List<Users> userList = usersDao.findfive(usersDao.findAll(user_page));

				//lastpageを設定する
				double a = (usersDao.countAll());
				int lastpage = (int) Math.ceil(a / 5);

				request.setAttribute("usersList", userList);
				request.setAttribute("lastpage", lastpage);

				request.getRequestDispatcher("view/userlist.jsp").forward(request, response);
			} catch (Exception e) {
				throw new ServletException(e);
			}
			break;
		case USER_INFO:
			try {
				int userId = Integer.parseInt(request.getParameter("userId"));

				request.getSession().setAttribute("editingId", userId);
			} catch (Exception e) {

			}
			int editingId = (int) request.getSession().getAttribute("editingId");

			try {
				UsersDao UsersDao = DaoFactory.createUsersDao();
				Users user = UsersDao.findById(editingId);
				request.setAttribute("user", user);
				request.getRequestDispatcher("view/userinfo.jsp").forward(request, response);
			} catch (Exception e) {
				throw new ServletException(e);
			}
			break;
		case USER_INSERT:
			request.getRequestDispatcher("view/userinsert.jsp").forward(request, response);
			break;
		case USER_EDIT:
			this.editId = (int) request.getSession().getAttribute("editingId");

			try {
				UsersDao UsersDao = DaoFactory.createUsersDao();
				Users user = UsersDao.findById(this.editId);
				this.loginId = user.getLoginId();
				request.setAttribute("user", user);
				request.getRequestDispatcher("view/useredit.jsp").forward(request, response);
			} catch (Exception e) {
				throw new ServletException(e);
			}
			break;
		case USER_DELETE:
			break;
		}
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		String servlet_id = request.getParameter("servletName");
		switch (servlet_id) {
		case USER_LIST:

			break;
		case USER_INFO:
			try {
				int userId = Integer.parseInt(request.getParameter("userId"));

				request.getSession().setAttribute("editingId", userId);
			} catch (Exception e) {

			}
			int editingId = (int) request.getSession().getAttribute("editingId");

			try {
				UsersDao UsersDao = DaoFactory.createUsersDao();
				Users user = UsersDao.findById(editingId);
				request.setAttribute("user", user);
				request.getRequestDispatcher("view/userinfo.jsp").forward(request, response);
			} catch (Exception e) {
				throw new ServletException(e);
			}

			break;
		case USER_INSERT:
			String insertName = request.getParameter("name");
			String loginId = request.getParameter("login_id");
			String loginPass = request.getParameter("login_pass");
			int insertGroup = Integer.parseInt(request.getParameter("group_id"));

			// loginIdとloginPassの正規化チェック 半角英数字、ハイフン、アンダースコアのみ許可
			if (loginId.matches("[0-9a-zA-Z\\-\\_]+") && loginPass.matches("[0-9a-zA-Z\\-\\_]+")) {
				// パスワードのハッシュ化
				String hashedPass = BCrypt.hashpw(loginPass, BCrypt.gensalt());

				// データの追加
				Users user = new Users();
				user.setName(insertName);
				user.setLoginId(loginId);
				user.setLoginPass(hashedPass);
				user.setGroup(insertGroup);

				try {
					UsersDao UsersDao = DaoFactory.createUsersDao();
					// login_idが使われているかチェックする
					if (UsersDao.CheckLoginId(loginId)) {
						UsersDao.insert(user);
						request.getRequestDispatcher("view/userinsertDone.jsp").forward(request, response);
					} else {
						// login_idが他のユーザーのlogin_idとかぶった場合の処理内容
						request.setAttribute("error", true);
						request.getRequestDispatcher("view/useredit.jsp").forward(request, response);
					}
				} catch (Exception e) {
					throw new ServletException(e);
				}
			} else {
				// if文、文字列が半角英数字、ハイフン、アンダースコア以外の場合は以下の処理
				request.setAttribute("errorchar", true);
				request.getRequestDispatcher("view/userinsert.jsp").forward(request, response);
			}
			break;

		case USER_EDIT:
			String name = request.getParameter("name");
			String login_id = request.getParameter("login_id");
			String login_pass = request.getParameter("login_pass");
			int group = Integer.parseInt(request.getParameter("group_id"));

			// loginIdとloginPassの正規化チェック 半角英数字、ハイフン、アンダースコアのみ許可
			if (login_id.matches("[0-9a-zA-Z\\-\\_]+") && login_pass.matches("[0-9a-zA-Z\\-\\_]+")) {
				String hashedPass = BCrypt.hashpw(login_pass, BCrypt.gensalt());
				// メンバーからユーザーIdを取得しインスタンスにセット
				Users user = new Users();
				user.setId(editId);
				user.setName(name);
				user.setLoginId(login_id);
				user.setLoginPass(hashedPass);
				user.setGroup(group);

				try {
					UsersDao UsersDao = DaoFactory.createUsersDao();
					// login_idが使われているかチェックする
					if (UsersDao.CheckLoginId(login_id) || this.loginId.equals(login_id)) {
						UsersDao.update(user);
						request.setAttribute("userId", editId);
						request.getRequestDispatcher("view/usereditDone.jsp").forward(request, response);
					} else {
						// login_idが他のユーザーのlogin_idとかぶった場合の処理内容
						request.setAttribute("error", true);
						request.getRequestDispatcher("view/useredit.jsp").forward(request, response);
					}
				} catch (Exception e) {
					throw new ServletException(e);
				}
			} else if (login_id.matches("[0-9a-zA-Z\\-\\_]+") && login_pass == "") {// パスワード無しの場合の更新処理
				Users user = new Users();
				user.setId(editId);
				user.setName(name);
				user.setLoginId(login_id);
				user.setGroup(group);
				try {
					UsersDao UsersDao = DaoFactory.createUsersDao();
					// login_idが使われているかチェックする
					if (UsersDao.CheckLoginId(login_id) || this.loginId.equals(login_id)) {
						UsersDao.updateWhithoutPass(user);
						request.setAttribute("userId", editId);
						request.getRequestDispatcher("view/usereditDone.jsp").forward(request, response);
					} else {
						// login_idが他のユーザーのlogin_idとかぶった場合の処理内容
						request.setAttribute("error", true);
						request.getRequestDispatcher("view/useredit.jsp").forward(request, response);
					}
				} catch (Exception e) {
					throw new ServletException(e);
				}
			} else {
				request.setAttribute("errorchar", true);
				request.getRequestDispatcher("view/useredit.jsp").forward(request, response);
			}
			break;
		case USER_DELETE:
			int userId = Integer.parseInt(request.getParameter("userId"));
			try {
				UsersDao UsersDao = DaoFactory.createUsersDao();
				AttendDao attendDao = DaoFactory.createAttendDao();
				Users user = new Users();
				user.setId(userId);
				attendDao.deleteByUserId(user);
				UsersDao.delete(user);

			} catch (Exception e) {
				throw new ServletException(e);
			}
			request.getRequestDispatcher("view/userdelDone.jsp").forward(request, response);
			break;
		}

	}


}
