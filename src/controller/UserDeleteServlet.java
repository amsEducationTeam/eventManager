package controller;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import dao.AttendDao;
import dao.DaoFactory;
import dao.UsersDao;
import domain.Users;

/**
 * Servlet implementation class UserDeleteServlet
 */
@WebServlet("/UserDeleteServlet")
public class UserDeleteServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 *  doGetメソッドはいらないので削除予定
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
//	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
//		response.getWriter().append("Served at: ").append(request.getContextPath());
//	}

	/**
	 * ユーザー詳細ページ(JSP)の削除ダイアログで"OK"をクリックした際に呼び出される
	 * requestから該当ユーザーのidを取得してusersテーブルとattendsテーブルの該当レコードを削除または論理削除
	 *
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		int userId = Integer.parseInt(request.getParameter("userId"));
		try {
			UsersDao UsersDao = DaoFactory.createUsersDao();
			AttendDao attendDao = DaoFactory.createAttendDao();
			Users user = new Users();
			user.setId(userId);
			attendDao.deleteByUserId(user);
			UsersDao.delete(user);
			request.getRequestDispatcher("view/userdelDone.jsp").forward(request, response);
		} catch (Exception e) {
			throw new ServletException(e);
		}
		//request.getRequestDispatcher("view/userdelDone.jsp").forward(request, response);
	}

}
