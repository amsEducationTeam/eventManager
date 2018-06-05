//package controller;
//
//import java.io.IOException;
//import java.util.List;
//
//import javax.servlet.ServletException;
//import javax.servlet.annotation.WebServlet;
//import javax.servlet.http.HttpServlet;
//import javax.servlet.http.HttpServletRequest;
//import javax.servlet.http.HttpServletResponse;
//
//import dao.DaoFactory;
//import dao.EventsDao;
//import domain.Events;
///**
// * Servlet implementation class EventlistServlet
// */
//@WebServlet()// /eventlist
//public class EventlistServlet extends HttpServlet {
//	private static final long serialVersionUID = 1L;
//
//    /**
//     * @see HttpServlet#HttpServlet()
//     */
//    public EventlistServlet() {
//        super();
//        // TODO Auto-generated constructor stub
//    }
//
//	/**
//	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
//	 */
//	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
//
//		//pagenation用
//		int page = 0;
//		try {
//			try {
//				page = Integer.parseInt(request.getParameter("page")); //eventlist.jspから送られてきたのを受け取る
//
//				//pageに開始ページを格納する
//				if (page == 1) {
//					page = 0;
//				} else {
//					page = 5 * (page - 1);
//				}
//				request.getSession().setAttribute("event_page", page); //セッションに格納する
//			} catch (Exception e) {
//				int prevnext = Integer.parseInt(request.getParameter("prevnext")); //eventslistから送られてきたのを受け取る
//				//セッションに保存したevent_pageを取得し、変数に格納する
//				int event_page = (Integer) request.getSession().getAttribute("event_page");
//
//				//←→をするための仕組みを準備する
//				if (prevnext == 1) {
//					//もどる
//					event_page = event_page - 5;
//				} else {
//					//すすむ
//					event_page = event_page + 5;
//				}
//				request.getSession().setAttribute("event_page", event_page); //セッションに格納する
//
//			}
//		} catch (Exception e) {
//			page = 0; //navbarなどからこのページにきたときの処理
//			request.getSession().setAttribute("event_page", page); //セッションに格納する
//		}
//
//		int id = (Integer)request.getSession().getAttribute("id");
//
//		try {
//			//セッションに保存したevent_pageを取得し、変数に格納する
//			int event_page = (Integer) request.getSession().getAttribute("event_page");
//
//
//			EventsDao eventsDao = DaoFactory.createEventsDao();
//			List<Events> eventsList = eventsDao.findfive(eventsDao.findAll(event_page),id);
//
//			//lastpageを設定する
//			double a =(eventsDao.countAll());
//			int lastpage = (int) Math.ceil(a/5);
//
//			request.setAttribute("eventsList",eventsList);
//			request.setAttribute("lastpage", lastpage);
//
//            request.getRequestDispatcher("view/eventlist.jsp").forward(request, response);
//		} catch (Exception e) {
//			throw new ServletException(e);
//		}
//
//
//
//	}
//
//	/**
//	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
//	 */
//	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
//
//
//		// TODO Auto-generated method stub
//		doGet(request, response);}
//
//
//
//
//}
