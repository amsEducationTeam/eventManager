package fileio;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.*;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

public class MemberFileReaderTest {

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
	}

	@AfterClass
	public static void tearDownAfterClass() throws Exception {
	}

	@Test
	public void 正常系testMain() {
		int valid_data_quantity = 9;
		try {
			MemberFileReader MembersFileReader = new MemberFileReader("c:\\work\\member_20180402.csv",valid_data_quantity);
			String result = MembersFileReader.main();
			assertThat(result, is("100"));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Test
	public void testEnableLine() {
		fail("まだ実装されていません");
	}

	@Test
	public void testMainStringArray() {
		fail("まだ実装されていません");
	}

	@Test
	public void testMemberFileReader() {
		fail("まだ実装されていません");
	}

	@Test
	public void 異常系testMain() {
		fail("まだ実装されていません");
	}

	@Test
	public void 異常系testEnableLine() {
		fail("まだ実装されていません");
	}

	@Test
	public void 異常系testMainStringArray() {
		fail("まだ実装されていません");
	}

	@Test
	public void 異常系testMemberFileReader() {
		fail("まだ実装されていません");
	}

}
