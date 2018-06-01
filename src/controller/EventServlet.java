package controller;

import java.io.IOException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import dao.AttendDao;
import dao.DaoFactory;
import dao.EventsDao;
import domain.Events;

/**
 * Servlet implementation class EventServlet
 */
@WebServlet("/EventServlet")
public class EventServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	protected final String EVENT_TODAY = "eventToday";
	protected final String EVENT_LIST = "eventList";
	protected final String EVENT_INFO = "eventInfo";
	protected final String EVENT_EDIT = "eventEdit";
	protected final String EVENT_INSERT = "eventInsert";
	protected final String EVENT_DELETE = "eventDelete";


	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// どのページ遷移かをパラメータから取得し定数と比較
		//EventServName eventServName = EventServName.valueOf(request.getParameter("servName"));
		String pageName = (String)request.getSession().getAttribute("servName");

		switch(pageName){
			case EVENT_TODAY:
				eventToday(request, response);
				break;
			case EVENT_LIST:
				int page = 0;
				try {
					try {
						page = Integer.parseInt(request.getParameter("page")); //eventlist.jspから送られてきたのを受け取る

						//pageに開始ページを格納する
						if (page == 1) {
							page = 0;
						} else {
							page = 5 * (page - 1);
						}
						request.getSession().setAttribute("event_page", page); //セッションに格納する
					} catch (Exception e) {
						int prevnext = Integer.parseInt(request.getParameter("prevnext")); //eventslistから送られてきたのを受け取る
						//セッションに保存したevent_pageを取得し、変数に格納する
						int event_page = (Integer) request.getSession().getAttribute("event_page");

						//←→をするための仕組みを準備する
						if (prevnext == 1) {
							//もどる
							event_page = event_page - 5;
						} else {
							//すすむ
							event_page = event_page + 5;
						}
						request.getSession().setAttribute("event_page", event_page); //セッションに格納する
					}
				} catch (Exception e) {
					page = 0; //navbarなどからこのページにきたときの処理
					request.getSession().setAttribute("event_page", page); //セッションに格納する
				}
				int id = (Integer) request.getSession().getAttribute("id");
				try {
					//セッションに保存したevent_pageを取得し、変数に格納する
					int event_page = (Integer) request.getSession().getAttribute("event_page");
					EventsDao eventsDao = DaoFactory.createEventsDao();
					List<Events> eventsList = eventsDao.findfive(eventsDao.findAll(event_page), id);
					//lastpageを設定する
					double a = (eventsDao.countAll());
					int lastpage = (int) Math.ceil(a / 5);
					request.setAttribute("eventsList", eventsList);
					request.setAttribute("lastpage", lastpage);
					request.getRequestDispatcher("view/eventlist.jsp").forward(request, response);
				} catch (Exception e) {
					throw new ServletException(e);
				}
				break;
			case EVENT_INFO:
				eventInfo();
				break;
			case EVENT_EDIT:
				eventEdit();
				break;
			case EVENT_INSERT:
				eventInsert();
				break;
			case EVENT_DELETE:
				// doGetメソッドでは無効な処理 404ページへ遷移
				break;
		}

	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		//EventServName eventServName = EventServName.valueOf(request.getParameter("servName"));
		String pageName = (String)request.getAttribute("servName");

		switch(pageName) {
		case EVENT_TODAY:
			eventToday(request, response);
			break;
		case EVENT_LIST:
			// doGetメソッドでは無効な処理 404ページへ遷移
			break;
		case EVENT_INFO:
			// doGetメソッドでは無効な処理 404ページへ遷移
			break;
		case EVENT_EDIT:
			eventEditPost();
			break;
		case EVENT_INSERT:
			eventInsertPost();
			break;
		case EVENT_DELETE:
			//int eventId = Integer.parseInt(request.getParameter("info"));
			eventDeletePost(request, response);
			break;
		}
	}


	protected void eventToday(HttpServletRequest request, HttpServletResponse response)throws ServletException, IOException {
		// 処理内容
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
	protected void eventList(HttpServletRequest request, HttpServletResponse response)throws ServletException, IOException  {
		//pagenation用
				int page = 0;
				try {
					try {
						page = Integer.parseInt(request.getParameter("page")); //eventlist.jspから送られてきたのを受け取る

						//pageに開始ページを格納する
						if (page == 1) {
							page = 0;
						} else {
							page = 5 * (page - 1);
						}
						request.getSession().setAttribute("event_page", page); //セッションに格納する
					} catch (Exception e) {
						int prevnext = Integer.parseInt(request.getParameter("prevnext")); //eventslistから送られてきたのを受け取る
						//セッションに保存したevent_pageを取得し、変数に格納する
						int event_page = (Integer) request.getSession().getAttribute("event_page");

						//←→をするための仕組みを準備する
						if (prevnext == 1) {
							//もどる
							event_page = event_page - 5;
						} else {
							//すすむ
							event_page = event_page + 5;
						}
						request.getSession().setAttribute("event_page", event_page); //セッションに格納する
					}
				} catch (Exception e) {
					page = 0; //navbarなどからこのページにきたときの処理
					request.getSession().setAttribute("event_page", page); //セッションに格納する
				}
				int id = (Integer) request.getSession().getAttribute("id");
				try {
					//セッションに保存したevent_pageを取得し、変数に格納する
					int event_page = (Integer) request.getSession().getAttribute("event_page");
					EventsDao eventsDao = DaoFactory.createEventsDao();
					List<Events> eventsList = eventsDao.findfive(eventsDao.findAll(event_page), id);
					//lastpageを設定する
					double a = (eventsDao.countAll());
					int lastpage = (int) Math.ceil(a / 5);
					request.setAttribute("eventsList", eventsList);
					request.setAttribute("lastpage", lastpage);
					request.getRequestDispatcher("view/eventlist.jsp").forward(request, response);
				} catch (Exception e) {
					throw new ServletException(e);
				}
	}
	protected void eventInsert()  {
		//

	}
	protected void eventInfo() {
		//
	}
	protected void eventEdit() {
		//

	}
	protected void eventEditPost() {
		//
	}
	protected void eventInsertPost() {
		//
	}
	protected void eventDeletePost(HttpServletRequest request, HttpServletResponse response)throws ServletException, IOException {
		//
		int eventId = Integer.parseInt(request.getParameter("info"));
	try {
		EventsDao eventsDao = DaoFactory.createEventsDao();
		AttendDao attendDao = DaoFactory.createAttendDao();
		Events event = new Events();
		event.setId(eventId);
		attendDao.deleteByEventId(event);
		eventsDao.delete(event);
		request.getRequestDispatcher("view/eventdelDone.jsp").forward(request, response);
	} catch (Exception e) {
		throw new ServletException(e);
	}
	}





}
