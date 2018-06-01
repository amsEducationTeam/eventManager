//package controller;
//
//import java.io.IOException;
//
//import javax.servlet.ServletException;
//import javax.servlet.annotation.WebServlet;
//import javax.servlet.http.HttpServlet;
//import javax.servlet.http.HttpServletRequest;
//import javax.servlet.http.HttpServletResponse;
//
//import org.mindrot.jbcrypt.BCrypt;
//
//import dao.DaoFactory;
//import dao.UsersDao;
//import domain.Users;
//

///**
// * Servlet implementation class AddUserServlet
// */
//@WebServlet("/UserInsertServlet")
//public class UserInsertServlet extends HttpServlet {
//	private static final long serialVersionUID = 1L;
//
//	/**
//	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
//	 */
//	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
//		request.getRequestDispatcher("view/userinsert.jsp").forward(request, response);
//	}
//
//	/**
//	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
//	 */
//	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
//		// パラメータの取得
//		String name = request.getParameter("name");
//		String loginId = request.getParameter("login_id");
//		String loginPass = request.getParameter("login_pass");
//		int group = Integer.parseInt(request.getParameter("group_id"));
//
//		// loginIdとloginPassの正規化チェック 半角英数字、ハイフン、アンダースコアのみ許可
//		if(loginId.matches("[0-9a-zA-Z\\-\\_]+") && loginPass.matches("[0-9a-zA-Z\\-\\_]+")) {
//			// パスワードのハッシュ化
//			String hashedPass = BCrypt.hashpw(loginPass, BCrypt.gensalt());
//
//			// データの追加
//			Users user = new Users();
//			user.setName(name);
//			user.setLoginId(loginId);
//			user.setLoginPass(hashedPass);
//			user.setGroup(group);
//
//			try {
//				UsersDao UsersDao = DaoFactory.createUsersDao();
//				// login_idが使われているかチェックする
//				if(UsersDao.CheckLoginId(loginId)) {
//					UsersDao.insert(user);
//					request.getRequestDispatcher("view/userinsertDone.jsp").forward(request, response);
//				} else {
//					// login_idが他のユーザーのlogin_idとかぶった場合の処理内容
//					request.setAttribute("error", true);
//					request.getRequestDispatcher("view/useredit.jsp").forward(request,  response);
//				}
//			} catch (Exception e) {
//				throw new ServletException(e);
//			}
//		}else {
//			// if文、文字列が半角英数字、ハイフン、アンダースコア以外の場合は以下の処理
//			request.setAttribute("errorchar", true);
//			request.getRequestDispatcher("view/userinsert.jsp").forward(request,  response);
//		}
//	}
//}
