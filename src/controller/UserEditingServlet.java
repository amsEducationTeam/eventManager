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
// * Servlet implementation class UserEditingServlet
// */
//@WebServlet("/UserEditingServlet")
//public class UserEditingServlet extends HttpServlet {
//	private static final long serialVersionUID = 1L;
//	//private int userId;
//	private int editingId; // userId
//	private String loginId;
//
//
//
//	/**
//	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
//	 */
//	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
//		// セッションからユーザーIdを取得しメンバーに格納
//		//this.userId=Integer.parseInt(request.getParameter("userId"));
//		this.editingId = (int)request.getSession().getAttribute("editingId");
//
//		try {
//			UsersDao UsersDao=DaoFactory.createUsersDao();
//			Users user=UsersDao.findById(editingId);
//			this.loginId = user.getLoginId();
//			request.setAttribute("user", user);
//			request.getRequestDispatcher("view/useredit.jsp").forward(request, response);
//		} catch (Exception e) {
//			throw new ServletException(e);
//		  }
//	}
//
//	/**
//	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
//	 */
//	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
//		// リクエストから値を取得
//		String name=request.getParameter("name");
//		String login_id=request.getParameter("login_id");
//		String login_pass=request.getParameter("login_pass");
//		int group=Integer.parseInt(request.getParameter("group_id"));
//
//		// loginIdとloginPassの正規化チェック 半角英数字、ハイフン、アンダースコアのみ許可
//		if(login_id.matches("[0-9a-zA-Z\\-\\_]+") && login_pass.matches("[0-9a-zA-Z\\-\\_]+")) {
//			String hashedPass = BCrypt.hashpw(login_pass, BCrypt.gensalt());
//			// メンバーからユーザーIdを取得しインスタンスにセット
//			Users user=new Users();
//			user.setId(editingId);
//			user.setName(name);
//			user.setLoginId(login_id);
//			user.setLoginPass(hashedPass);
//			user.setGroup(group);
//
//			try {
//				UsersDao UsersDao=DaoFactory.createUsersDao();
//				// login_idが使われているかチェックする
//				if(UsersDao.CheckLoginId(login_id) || loginId.equals(login_id)) {
//					UsersDao.update(user);
//					request.setAttribute("userId", editingId);
//					request.getRequestDispatcher("view/usereditDone.jsp").forward(request, response);
//				} else {
//					// login_idが他のユーザーのlogin_idとかぶった場合の処理内容
//					request.setAttribute("error", true);
//					request.getRequestDispatcher("view/useredit.jsp").forward(request,  response);
//				}
//			} catch (Exception e) {
//				throw new ServletException(e);
//			}
//		}else if(login_id.matches("[0-9a-zA-Z\\-\\_]+") && login_pass == ""){// パスワード無しの場合の更新処理
//			Users user=new Users();
//			user.setId(editingId);
//			user.setName(name);
//			user.setLoginId(login_id);
//			user.setGroup(group);
//			try {
//				UsersDao UsersDao=DaoFactory.createUsersDao();
//				// login_idが使われているかチェックする
//				if(UsersDao.CheckLoginId(login_id) || loginId.equals(login_id)) {
//					UsersDao.updateWhithoutPass(user);
//					request.setAttribute("userId", editingId);
//					request.getRequestDispatcher("view/usereditDone.jsp").forward(request, response);
//					} else {
//					// login_idが他のユーザーのlogin_idとかぶった場合の処理内容
//					request.setAttribute("error", true);
//					request.getRequestDispatcher("view/useredit.jsp").forward(request,  response);
//				}
//			} catch (Exception e) {
//				throw new ServletException(e);
//			}
//		}else {
//			request.setAttribute("errorchar", true);
//			request.getRequestDispatcher("view/useredit.jsp").forward(request,  response);
//		}
//	}
//}
