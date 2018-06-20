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
	private final String FILE_NAME[] = {"C:\\work\\department_20180601.csv",
			"C:\\xxxx.csv", "C:\\work\\lossDataFile.csv"};
	private final int COLUMNS = 4;
	private final DepartFileReader DEP_FIL_RED = new DepartFileReader(FILE_NAME[0], COLUMNS);

	static DataSource ds;

	private final static String DEP_NAME[] = {"人事部", "経理部", "総務部", "営業部", "開発部"};
	private final static int DEP_FLOOR[] = {4, 2, 2, 3, 4};
	protected final String FAULT_DATA[] =
		{"123456789012345678901234567890123456789012345678901234567890", "1145141919", "部長"};

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
				stmt.executeUpdate(sqlTrn);

				String sqlDep = "INSERT INTO department(department, floor) VALUES"
						+ " ('"+DEP_NAME[0]+"', "+DEP_FLOOR[0]+"),"
						+ " ('"+DEP_NAME[1]+"', "+DEP_FLOOR[1]+"),"
						+ " ('"+DEP_NAME[2]+"', "+DEP_FLOOR[2]+"),"
						+ " ('"+DEP_NAME[3]+"', "+DEP_FLOOR[3]+"),"
						+ " ('"+DEP_NAME[4]+"', "+DEP_FLOOR[4]+");";
				Statement stmtDep = (Statement) conn.createStatement();
				stmtDep.executeUpdate(sqlDep);

				//エラーがなければコミットする
				conn.commit();

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


	// 正常パターン
	@Test
	public void testMain() throws NamingException, IOException {
		JNDIUnitTestHelper.init("WebContent/WEB-INF/classes/jndi_unit_test_helper.properties");

		String result = DEP_FIL_RED.main();
		System.out.print(result);

		assertThat(result, is("100"));
	}


	@Test
	public void faultTestMainFile() throws NamingException, IOException {
		// 存在しないファイル
		DepartFileReader DepFaultFilReader = new DepartFileReader(FILE_NAME[1], COLUMNS);
		JNDIUnitTestHelper.init("WebContent/WEB-INF/classes/jndi_unit_test_helper.properties");

		String result = DepFaultFilReader.main();
		assertThat(result, is("ファイル読み込みエラー"));

		DepartFileReader DepFaultFilRed = new DepartFileReader(FILE_NAME[2], COLUMNS);
		String result2 = DepFaultFilRed.main();
		assertThat(result2, is("指定のファイルが存在しません"));

		// fileRead失敗

//		// enableLine(columns) false
//		String[] faultEnableString = {"12", "", "25"};
//		DepFilReader.main(faultEnableString);

		// departDao.insert失敗

	}

	@Test
	public void faultTestEnableLineFile() throws NamingException, IOException {
		JNDIUnitTestHelper.init("WebContent/WEB-INF/classes/jndi_unit_test_helper.properties");

		// データ行にnull
		String[] LostString = {"12", "", "DEP_NAME[2]"};
		boolean dataNull = DEP_FIL_RED.enableLine(LostString);
		assertThat(dataNull, is(false));

		// 部署名の文字数超過
		String[] overString1 = {"D", "FAULT_DATA[0]", "DEP_FLOOR[1]", "DEP_FLOOR[1]"};
		boolean dataOver1 = DEP_FIL_RED.enableLine(overString1);
		assertThat(dataOver1, is(false));

		// 部長IDの文字超過
		String[] overString3 = {"D", "DEP_FLOOR[2]", "DEP_FLOOR[1]", "FAULT_DATA[1]"};
		boolean dataOver3 = DEP_FIL_RED.enableLine(overString3);
		assertThat(dataOver3, is(false));

		// 部長IDの文字規定違反
		String[] faultString3 = {"D", "DEP_NAME[1]", "DEP_FLOOR[1]", "FAULT_DATA[2]"};
		boolean dataFault3 = DEP_FIL_RED.enableLine(faultString3);
		assertThat(dataFault3, is(false));
	}


	@Test
	public void testEnableLine() {
//		DepartFileReader DepartFileReader = new DepartFileReader(FILE_NAME, COLUMNS);
//		// データチェック
//		boolean result = DepartFileReader.enableLine();// 引数String[]

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
