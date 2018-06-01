package controller;

import java.io.IOException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import dao.DaoFactory;
import dao.UsersDao;
import domain.Users;

/**
 * Servlet implementation class UserListServlet
 */
@WebServlet("/UserListServlet")
public class UserListServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

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
			double a =(usersDao.countAll());
			int lastpage = (int) Math.ceil(a/5);


			request.setAttribute("usersList", userList);
			request.setAttribute("lastpage", lastpage);



			request.getRequestDispatcher("view/userlist.jsp").forward(request, response);
		} catch (Exception e) {
			throw new ServletException(e);
		}
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		int userId = Integer.parseInt(request.getParameter("info"));
		request.getSession().setAttribute("editingId", userId);
		try {
			UsersDao UsersDao = DaoFactory.createUsersDao();
			Users user = UsersDao.findById(userId);
			request.setAttribute("user", user);
			request.getRequestDispatcher("view/userinfo.jsp").forward(request, response);
		} catch (Exception e) {
			throw new ServletException(e);
		}

	}

}
