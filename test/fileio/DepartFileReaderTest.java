package fileio;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.*;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;

import org.junit.BeforeClass;
import org.junit.Test;

import com.TestDBAccess;
import com.javaranch.unittest.helper.sql.pool.JNDIUnitTestHelper;

public class DepartFileReaderTest extends TestDBAccess {
	final String FILE_NAME = "C:\\work\\department_20180601.csv";
	final int COLUMNS = 4;
	static DataSource ds;

//	private final static int DEP_ID[] = {1, 2, 3, 4, 5};
	private final static String DEP_NAME[] = {"人事部", "経理部", "総務部", "営業部", "開発部"};
	private final static int DEP_FLOOR[] = {4, 2, 2, 3, 4};

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		InitialContext ctx = null;

		try {
			ctx = new InitialContext();
			ds = (DataSource)ctx.lookup("java:comp/env/jdbc/eventdb2");
		}catch(Exception e) {
			if(ctx != null) {
				try {
					ctx.close();
				}catch(Exception el) {
					throw new RuntimeException(el);
				}
			}
			throw new RuntimeException(e);
		}

		//
		try (Connection conn = ds.getConnection() ) {

			try {
				//オートコミットを切る
				conn.setAutoCommit(false);
				String sqlTrn = "TRUNCATE TABLE eventdb2.department;";
				Statement stmt = (Statement) conn.createStatement();
System.out.println("truncateしました");
				stmt.executeUpdate(sqlTrn);

				String sqlDep = "INSERT INTO department(department, floor) VALUES"
						+ " ('"+DEP_NAME[0]+"', "+DEP_FLOOR[0]+"),"
						+ " ('"+DEP_NAME[1]+"', "+DEP_FLOOR[1]+"),"
						+ " ('"+DEP_NAME[2]+"', "+DEP_FLOOR[2]+"),"
						+ " ('"+DEP_NAME[3]+"', "+DEP_FLOOR[3]+"),"
						+ " ('"+DEP_NAME[4]+"', "+DEP_FLOOR[4]+");";
				Statement stmtDep = (Statement) conn.createStatement();
System.out.println("insertしました");
				stmtDep.executeUpdate(sqlDep);
System.out.println("update後");

				//エラーがなければコミットする
				conn.commit();
System.out.println("commit後");

			}
			//挿入時にエラーが発生したらロールバックしてエラー文を表示
			catch (Exception e) {
				System.out.println("error1");
				e.printStackTrace();
				conn.rollback();

			} finally {
				try {
					if (conn != null) {
						conn.close();
						//System.out.println("切断しました");
					}
				} catch (SQLException e) {
					System.out.println("error2");
				}
			}
		}
	}

	@Test
	public void testMain() throws NamingException, IOException {
		DepartFileReader DepartFileReader = new DepartFileReader(FILE_NAME, COLUMNS);
//		try {
			JNDIUnitTestHelper.init("WebContent/WEB-INF/classes/jndi_unit_test_helper.properties");
//		} catch (NamingException | IOException e) {
//			e.printStackTrace();
//		}
		String result = DepartFileReader.main();
		System.out.print(result);

		assertThat(result, is("100"));
	}

	@Test
	public void testEnableLine() {
		// データチェック

		// columnsの1～4の全てが空ではあるか
		// columnsの1が50文字以下ではないか
		// 同上の3が8文字以下ではないか
		// 同上の3が半角英数字(ハイフン含む)ではないか
		// 以上4点が全て当てはまらない時、trueを返す
	}

	// void mainなので、テスト方法不明
	@Test
	public void testMainStringArray() throws NamingException, IOException {
		// main(String)
		// DepartFileReader(引数：csvファイル、定数[4])を呼び出し
			// JNDIHelperで接続
		// DepartFileReader.mainを呼び出し、戻り値をresultに格納
	}

	// テスト方法不明
	@Test
	public void testDepartFileReader() {
		// DepartFileReader(filename, columns)
		// filenameとcolumnsをEventsFileIOにセット

	}

}
