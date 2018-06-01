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
//import dao.DaoFactory;
//import dao.UsersDao;
//import domain.Users;

///**
// * Servlet implementation class UserInfoServlet
// */
//@WebServlet("/UserInfoServlet")
//public class UserInfoServlet extends HttpServlet {
//	private static final long serialVersionUID = 1L;
//
//	/**
//	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
//	 */
//	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
//		try {
//		int userId=Integer.parseInt(request.getParameter("userId"));
//
//		request.getSession().setAttribute("editingId", userId);
//		} catch(Exception e){
//
//		}
//		int editingId=(int)request.getSession().getAttribute("editingId");
//
//		try {
//			UsersDao UsersDao=DaoFactory.createUsersDao();
//			Users user=UsersDao.findById(editingId);
//			request.setAttribute("user", user);
//			request.getRequestDispatcher("view/userinfo.jsp").forward(request, response);
//		} catch (Exception e) {
//			throw new ServletException(e);
//		  }
//	}
//
//	/**
//	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
//	 */
//	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
//		try {
//			int userId=Integer.parseInt(request.getParameter("userId"));
//			System.out.println(userId);
//			request.getSession().setAttribute("editingId", userId);
//			} catch(Exception e){
//
//			}
//			int editingId=(int)request.getSession().getAttribute("editingId");
//			System.out.println(editingId);
//			try {
//				UsersDao UsersDao=DaoFactory.createUsersDao();
//				Users user=UsersDao.findById(editingId);
//				request.setAttribute("user", user);
//				request.getRequestDispatcher("view/userinfo.jsp").forward(request, response);
//			} catch (Exception e) {
//				throw new ServletException(e);
//			  }
//	}
//
//}
