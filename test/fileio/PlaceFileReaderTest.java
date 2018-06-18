package fileio;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.*;

import java.io.IOException;
import java.nio.file.NoSuchFileException;

import javax.naming.NamingException;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import com.javaranch.unittest.helper.sql.pool.JNDIUnitTestHelper;

public class PlaceFileReaderTest {
	private final String FILE_NAME = "C:\\work_1\\place_20180601.csv";
	private final int COLUMNS = 8;

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
	}

	@AfterClass
	public static void tearDownAfterClass() throws Exception {
	}

	@Before
	public void setUp() throws Exception {
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void testMain() {
		try {
			PlaceFileReader PlaceFileReader = new PlaceFileReader(FILE_NAME, COLUMNS);
			/*
			 *Junitを使うまではこれで接続します
			 */
			try {
				JNDIUnitTestHelper.init("WebContent/WEB-INF/classes/jndi_unit_test_helper.properties");
			} catch (NamingException | IOException e) {
				e.printStackTrace();
			}

			String result = PlaceFileReader.main();
			assertThat(result, is("100"));

			System.out.print(result);
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	@Test
	public void testEnableLine() {
		fail("まだ実装されていません");
	}

	//クラスのメインメソッド
	//ここでインスタンス宣言してファイル名のセットを行っている
	@Test
	public void testMainStringArray() {

		fail("まだ実装されていません");
	}

	//コンストラクタ
	@Test
	public void testPlaceFileReader() {
		try {
			PlaceFileReader placeFileReader = new PlaceFileReader(FILE_NAME, COLUMNS);
			fail("test");
		} catch (NoSuchFileException e) {
			e.printStackTrace();
		}
	}

	@Test
	public void testEventMgFileIO() {
		fail("まだ実装されていません");
	}

	@Test
	public void testEnableFile() {
		fail("まだ実装されていません");
	}

	@Test
	public void testGetResult() {
		fail("まだ実装されていません");
	}

	@Test
	public void testMatchType() {
		fail("まだ実装されていません");
	}

}
