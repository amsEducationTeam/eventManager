package fileio;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.*;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.charset.MalformedInputException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.NoSuchFileException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.naming.InitialContext;
import javax.sql.DataSource;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import com.TestDBAccess;
import com.mysql.jdbc.Statement;

public class PlaceFileReaderTest extends TestDBAccess {
	private final static String FILE_NAME = "C:\\work_1\\place_20180601.csv";
	private final int COLUMNS = 8;
	private static  DataSource testds;
	private static List<String[]> dataList;
	/** 正常結果値100 */
	protected static final String SUCCESS = "100";
	/** ファイルの開始行 */
	//private static final String START_ROW = "S";
	/** ファイルのヘッダ行 */
	//private static final String HEAD_ROW = "H";
	/** ファイルのデータ行 */
	private static final String DATA_ROW = "D";
	/** ファイルの終了行 */
	//private static final String END_ROW = "E";
	/** 区切り文字 カンマ */
	private static final String SEPARATE = ",";

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		InitialContext ctx = null;

		try {
			ctx = new InitialContext();
			testds = (DataSource)ctx.lookup("java:comp/env/jdbc/eventdb2");
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

		//Placeテーブルの削除
		try (Connection conn = testds.getConnection() ) {

			try {
				//SQL処理
				String sqlDel = "DELETE FROM place;";
				Statement stmt = (Statement) conn.createStatement();
				stmt.executeUpdate(sqlDel);

			}catch (Exception e) {
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

		Path path=Paths.get(FILE_NAME);

		dataList = new ArrayList<>();;

		try (BufferedReader reader = Files.newBufferedReader(path, StandardCharsets.UTF_8)) {
			String line;
			while ((line = reader.readLine()) != null) {

				String[] columns = line.split(SEPARATE); // 区切り文字","で分割する

				//D行をコレクションに格納
				if (DATA_ROW.equals(columns[0])) {
					dataList.add(columns);
				}
			} //whileの閉じ --終端

			reader.close();
		}catch(MalformedInputException e) {
			e.printStackTrace();
			dataList.clear();
		} catch (IOException e) {
			e.printStackTrace();
			System.out.println("ファイル読み込みエラー");
			dataList.clear();

		}

	}

	@AfterClass
	public static void tearDownAfterClass() throws Exception {
		//Placeテーブルの削除
		try (Connection conn = testds.getConnection() ) {
			try {
				//SQL処理
				String sqlDel = "DELETE FROM place;";
				Statement stmt = (Statement) conn.createStatement();
				stmt.executeUpdate(sqlDel);

			}
			//挿入時にエラーが発生したらロールバックしてエラー文を表示
			catch (Exception e) {
				System.out.println("error3");
				e.printStackTrace();
				conn.rollback();

			} finally {
				try {
					if (conn != null) {
						conn.close();
						//System.out.println("切断しました");
					}
				} catch (SQLException e) {
					System.out.println("error4");
				}
			}
		}
	}

	@Before
	public void setUp() throws Exception {
	}

	@After
	public void tearDown() throws Exception {
	}

	// mainメソッド、正常テスト、インスタンスしてコンストラクタにファイルパスを渡す
	@Test
	public void testMain() {
		try {
			PlaceFileReader PlaceFileReader = new PlaceFileReader(FILE_NAME, COLUMNS);

			String result = PlaceFileReader.main();
			assertThat(result, is(SUCCESS));

		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	// 各ファイルごとに調整したデータチェックのテスト
	@Test
	public void testEnableLine() {
		try {
			PlaceFileReader PlaceFileReader = new PlaceFileReader(FILE_NAME, COLUMNS);
			//int i = 1;
			for(String[] columns:dataList) {
				assertThat(PlaceFileReader.enableLine(columns), is(true));
				//i++;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}


	}


	// ファイル有効性チェックを行い
	// 有効性チェックが正常であれば
	// D行を格納したList<String[]>を返却します
	@Test
	public void testEnableFile() {
		try {
			PlaceFileReader PlaceFileReader = new PlaceFileReader(FILE_NAME, COLUMNS);

			List<String[]> fileRead = new ArrayList<String[]>();
			try {
				fileRead = PlaceFileReader.enableFile();//ファイル有効性チェック
			} catch (NoSuchFileException e) {
				e.printStackTrace();
			}

			int i=0;
			for(String[] cols:dataList) {
				int j = 0;
				String[] fileCols = fileRead.get(i);
				for(String value:cols) {
					//System.out.print(fileCols[j] + ":" + value + " " );
					assertThat(fileCols[j], is(value));
					j++;
				}
				System.out.println();
				i++;
			}

		} catch (NoSuchFileException e) {
			e.printStackTrace();
		}
	}

	// String result 初期値nullを返却 期待値null
	@Test
	public void testGetResult() {
		try {
			PlaceFileReader PlaceFileReader = new PlaceFileReader(FILE_NAME, COLUMNS);
			String result =  PlaceFileReader.getResult();
			assertThat(result, is(not(notNullValue())));
		} catch (NoSuchFileException e) {
			e.printStackTrace();
		}

	}

	// データタイプ(S,H,D,E)のみが入っているか検査します
	// それ以外であればfalseを返却します
	// MatchTypes(String d)
	@Test
	public void testMatchType() {
		try {
			PlaceFileReader PlaceFileReader = new PlaceFileReader(FILE_NAME, COLUMNS);
			for(String[] cols:dataList) {
				assertThat(PlaceFileReader.matchType(cols[0]), is(true));
			}
		} catch (NoSuchFileException e) {
			e.printStackTrace();
		}
	}

	// ----------------------- 以下　異常値テスト -------------------------------------------


}
