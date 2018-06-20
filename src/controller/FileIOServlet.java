package controller;

import java.io.IOException;
import java.util.ResourceBundle;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import fileio.FileController;
/**
 * Servlet implementation class FileIOServlet
 */
@WebServlet("/FileIOServlet")
public class FileIOServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	protected final String MEMBER_INSERT="memberInsert";
	protected final String ACCOUNT_INSERT="accountInsert";
	protected final String PLACE_INSERT ="placeInsert";
	protected final String DEPART_INSERT="departInsert";

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

		request.getRequestDispatcher("view/MasterInsert.jsp").forward(request, response);
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {




		String fileName = (String)request.getParameter("fileName");
		ResourceBundle rb = ResourceBundle.getBundle("fileIO");
		String error = fileName + "error";
		String complete = fileName + "complete";
		System.out.println((String)rb.getString(fileName));//動作確認用なのでコメントアウト
		System.out.println((String)rb.getString(fileName+"column"));//動作確認用なのでコメントアウト

		switch(fileName) {

		case MEMBER_INSERT:
			FileController.member(fileName);

		case ACCOUNT_INSERT:
			FileController.account(fileName);

		case PLACE_INSERT:
			FileController.place(fileName);

		case DEPART_INSERT:
			FileController.depart(fileName);
		}




		try {
			// マスター登録するjavaクラスのコントローラーをインスタンス
			// インスタンス.メソッド名(rb.getString(fileName),rb.getString(fileName + "column"));
			request.setAttribute(complete, "test");


		}catch(Exception e) {
			request.setAttribute(error, "testt2");
			throw new ServletException(e);

		}


		request.getRequestDispatcher("view/MasterInsert.jsp").forward(request, response);
	}

}
