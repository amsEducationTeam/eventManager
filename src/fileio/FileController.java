package fileio;

import java.nio.file.NoSuchFileException;
import java.util.ResourceBundle;

import org.apache.log4j.Logger;

import logTest.LogTest;
public class FileController {

	private static final Logger logger = Logger.getLogger(LogTest.class);
	private static String code=null;

	public static String member(String fileName) {
		ResourceBundle rb = ResourceBundle.getBundle("fileIO");
		MemberFileReader  member=new MemberFileReader((String)rb.getString(fileName),Integer.parseInt(rb.getString(fileName+"column")));

		try {
			code=member.main();

		} catch (NoSuchFileException e) {

			System.out.println(e);
		}

		output();
		return code;
	}



	public static String account(String fileName) {

		ResourceBundle rb = ResourceBundle.getBundle("fileIO");
		AccountFileReader  account=new AccountFileReader((String)rb.getString(fileName),Integer.parseInt(rb.getString(fileName+"column")));


		try {
			code=account.main();
		} catch (Exception e) {


			System.out.println(e);
		}
		output();
		return code;
	}

	public static String place(String fileName) throws NoSuchFileException, NumberFormatException {

		ResourceBundle rb = ResourceBundle.getBundle("fileIO");
		PlaceFileReader  place=new PlaceFileReader((String)rb.getString(fileName),Integer.parseInt(rb.getString(fileName+"column")));


		try {
			code=place.main();
		} catch (Exception e) {


			System.out.println(e);
		}
		output();
		return code;
	}

	public static String depart(String fileName) throws NoSuchFileException, NumberFormatException {

		ResourceBundle rb = ResourceBundle.getBundle("fileIO");
		DepartFileReader  depart=new DepartFileReader((String)rb.getString(fileName),Integer.parseInt(rb.getString(fileName+"column")));


		try {
			code=depart.main();
		} catch (Exception e) {


			System.out.println(e);
		}
		output();
		return code;
	}

	public static void  output() {

		if(code.equals("100")) {
			logger.info(code);
		}else {
			logger.error(code);
		}


	}

}