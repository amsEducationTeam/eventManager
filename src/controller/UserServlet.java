package controller;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
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
	private String editId;
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
				String userId = request.getParameter("member_id");

				request.getSession().setAttribute("editingId", userId);
			} catch (Exception e) {


			}
			String editingId = (String) request.getSession().getAttribute("editingId");

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
			this.editId = (String) request.getSession().getAttribute("editingId");

			try {
				UsersDao UsersDao = DaoFactory.createUsersDao();
				Users user = UsersDao.findById(this.editId);
				this.loginId = user.getLogin_id();
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
				String userId = request.getParameter("member_id");

				request.getSession().setAttribute("editingId", userId);
			} catch (Exception e) {
				throw new ServletException(e);

			}
			String editingId = (String) request.getSession().getAttribute("editingId");

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

			String member_id=request.getParameter("member_id");
			String name = request.getParameter("name");
			String kana=request.getParameter("kana");
			String address=request.getParameter("address");
			String tel =request.getParameter("tel");

			//birthdayの定義
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
			Date birthday=null;
			Date hired=null;

			try {
				birthday = sdf.parse(request.getParameter("birthday"));
				hired = sdf.parse(request.getParameter("hired"));
			} catch (ParseException e1) {
				e1.printStackTrace();
			}

			String login_id = request.getParameter("login_id");
			String login_pass = request.getParameter("login_pass");
			int dep_id = Integer.parseInt(request.getParameter("dep_id"));
			//int position_type=Integer.parseInt(request.getParameter("position_type"));
			int auth_id= Integer.parseInt(request.getParameter("auth_id"));




			// loginIdとloginPassの正規化チェック 半角英数字、ハイフン、アンダースコアのみ許可
			if (login_id.matches("[0-9a-zA-Z\\-\\_]+") && login_pass.matches("[0-9a-zA-Z\\-\\_]+")) {
				// パスワードのハッシュ化
				String hashedPass = BCrypt.hashpw(login_pass, BCrypt.gensalt());

				// データの追加
				Users user = new Users();
				user.setMember_id(member_id);
				user.setName(name);
				user.setKana(kana);
				user.setAddress(address);
				user.setTel(tel);
				user.setBirthday(birthday);
				user.setHired(hired);
				user.setLogin_id(login_id);
				user.setLogin_pass(hashedPass);
				user.setDep_id(dep_id);
				user.setAuth_id(auth_id);



				try {
					UsersDao UsersDao = DaoFactory.createUsersDao();
					// login_idが使われているかチェックする
					if (UsersDao.CheckLoginId(login_id)) {
						UsersDao.insert(user);
						UsersDao.insertacount(user);
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

			String edit_member_id=request.getParameter("member_id");
			String edit_name = request.getParameter("name");
			String edit_kana=request.getParameter("kana");
			String edit_address=request.getParameter("address");
			String edit_tel=request.getParameter("tel");
			SimpleDateFormat edit_sdf = new SimpleDateFormat("yyyy-MM-dd");
			Date edit_birthday=null;
			//Date edit_hired=null;

			try {
				edit_birthday = edit_sdf.parse(request.getParameter("birthday"));
				//edit_hired = edit_sdf.parse(request.getParameter("hired"));
			} catch (ParseException e1) {
				e1.printStackTrace();
			}
			String edit_login_id = request.getParameter("login_id");
			String edit_login_pass = request.getParameter("login_pass");
			int edit_dep_id = Integer.parseInt(request.getParameter("dep_id"));
			int edit_position_type=Integer.parseInt(request.getParameter("position_type"));
			int edit_auth_id= Integer.parseInt(request.getParameter("auth_id"));
			//int group = Integer.parseInt(request.getParameter("group_id"));

			String oldlogin_id=request.getParameter("oldlogin_id");
			String oldmember_id=request.getParameter("oldmember_id");

			// loginIdとloginPassの正規化チェック 半角英数字、ハイフン、アンダースコアのみ許可
			if (edit_login_id.matches("[0-9a-zA-Z\\-\\_]+") && edit_login_pass.matches("[0-9a-zA-Z\\-\\_]+")) {
				String hashedPass = BCrypt.hashpw(edit_login_pass, BCrypt.gensalt());
				// メンバーからユーザーIdを取得しインスタンスにセット
				Users user = new Users();

				user.setMember_id(edit_member_id);
				user.setName(edit_name);
				user.setKana(edit_kana);
				user.setAddress(edit_address);
				user.setTel(edit_tel);
				user.setBirthday(edit_birthday);
				user.setLogin_id(hashedPass);
				user.setLogin_pass(edit_login_pass);
				user.setDep_id(edit_dep_id);
				user.setPosition_type(edit_position_type);
				user.setAuth_id(edit_auth_id);
				user.setOldlogin_id(oldlogin_id);
				user.setOldmember_id(oldmember_id);



				try {
					UsersDao UsersDao = DaoFactory.createUsersDao();
					// login_idが使われているかチェックする
					if (UsersDao.CheckLoginId(edit_login_id) || this.loginId.equals(edit_login_id)) {
						UsersDao.update(user);
						UsersDao.updateaccount(user);

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
			} else if (edit_login_id.matches("[0-9a-zA-Z\\-\\_]+") && edit_login_pass == "") {// パスワード無しの場合の更新処理
				Users user = new Users();

				user.setMember_id(edit_member_id);
				user.setName(edit_name);
				user.setKana(edit_kana);
				user.setAddress(edit_address);
				user.setTel(edit_tel);
				user.setBirthday(edit_birthday);

				user.setLogin_pass(edit_login_pass);
				user.setDep_id(edit_dep_id);
				user.setPosition_type(edit_position_type);
				user.setAuth_id(edit_auth_id);
				user.setOldlogin_id(oldlogin_id);
				user.setOldmember_id(oldmember_id);


				try {
					UsersDao UsersDao = DaoFactory.createUsersDao();
					// login_idが使われているかチェックする
					if (UsersDao.CheckLoginId(edit_login_id) || this.loginId.equals(edit_login_id)) {
						UsersDao.updateWhithoutPass(user);
						UsersDao.updateAccountWhithoutPass(user);
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
			String userId = request.getParameter("member_id");
			String dlete_login_id=request.getParameter("login_id");
			try {
				UsersDao UsersDao = DaoFactory.createUsersDao();
				AttendDao attendDao = DaoFactory.createAttendDao();
				Users user = new Users();
				user.setMember_id(userId);
				user.setLogin_id(dlete_login_id);
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
