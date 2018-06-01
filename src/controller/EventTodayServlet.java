package controller;

import java.io.IOException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import dao.DaoFactory;
import dao.EventsDao;
import domain.Events;
/**
 * Servlet implementation class EventServlet
 */
@WebServlet()//"/event
public class EventTodayServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

    /**
     * @see HttpServlet#HttpServlet()
     */
    public EventTodayServlet() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		//pagenation用
		int page = 0;
		try {
			try {
				page = Integer.parseInt(request.getParameter("page")); //jspから送られてきたのを受け取る

				//pageに開始ページを格納する
				if (page == 1) {
					page = 0;
				} else {
					page = 5 * (page - 1);
				}
				request.getSession().setAttribute("todayevent_page", page); //セッションに格納する
			} catch (Exception e) {
				int prevnext = Integer.parseInt(request.getParameter("prevnext")); //eventslistから送られてきたのを受け取る
				//セッションに保存したtodayevent_pageを取得し、変数に格納する
				int todayevent_page = (Integer) request.getSession().getAttribute("todayevent_page");

				//←→をするための仕組みを準備する
				if (prevnext == 1) {
					//もどる
					todayevent_page = todayevent_page - 5;
				} else {
					//すすむ
					todayevent_page = todayevent_page + 5;
				}
				request.getSession().setAttribute("todayevent_page", todayevent_page); //セッションに格納する

			}
		} catch (Exception e) {
			page = 0; //navbarなどからこのページにきたときの処理
			request.getSession().setAttribute("todayevent_page", page); //セッションに格納する
		}


		int id = (Integer)request.getSession().getAttribute("id");
		try {
			//セッションに保存したtodayevent_pageを取得し、変数に格納する
			int todayevent_page = (Integer) request.getSession().getAttribute("todayevent_page");


			EventsDao eventsDao = DaoFactory.createEventsDao();
			List<Events> eventsList = eventsDao.findfive(eventsDao.findToday(todayevent_page),id);

			//lastpageを設定する
			double a =(eventsDao.countAllToday());
			int lastpage = (int) Math.ceil(a/5);


			request.setAttribute("eventsList", eventsList);
			request.setAttribute("lastpage", lastpage);

			request.getRequestDispatcher("view/event.jsp").forward(request, response);
		} catch (Exception e) {
			throw new ServletException(e);
		}

	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}

}
