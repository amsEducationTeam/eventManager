package fileio;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.*;

import javax.naming.InitialContext;
import javax.sql.DataSource;

import org.junit.AfterClass;
import org.junit.Test;

import com.TestDBAccess;

import dao.DaoFactory;
import dao.MembersDao;
import domain.Members;

public class MemberFileReaderTest extends TestDBAccess {
	static final int valid_data_quantity = 9;
	static MemberFileReader MembersFileReader = new MemberFileReader("c:\\work\\member_20180402.csv",
			valid_data_quantity);
	private static DataSource testds;
	static final String MEMBER_ID="105";
	static final String NAME="山本葵";
	static final String KANA="ヤマモトアオイ";
	static final String BIRTHDAY="1995/12/10";
	static final String ADDRESS="東京都新宿区飯田橋54-10-1";
	static final String TEL="090-6433-1233";
	static final String ENTER="4月1日";
	static final String DEP_ID="2";
	static final String EMPTY="";

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
	}

	@AfterClass
	public static void tearDownAfterClass() throws Exception {
		MembersDao membersDao=DaoFactory.createMembersDao();
		Members members = new Members();
		members.setMember_id(MEMBER_ID);
		membersDao.delete(members);
	}

	@Test
	public void 正常系testMain() {
		try {
			String result = MembersFileReader.main();
			assertThat(result, is("100"));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Test
	public void 正常系testEnableLine() {
		String[] test= {EMPTY,MEMBER_ID,NAME,KANA,BIRTHDAY,ADDRESS,TEL,ENTER,DEP_ID};
		assertThat(MembersFileReader.enableLine(test), is(true));

	}

	@Test
	public void testMainStringArray() {
		fail("まだ実装されていません");
	}

	@Test
	public void testMemberFileReader() {
	}

	@Test
	public void 異常系testMain() {
		try {
			MemberFileReader MembersFileReader = new MemberFileReader("c:\\work\\member.csv",
					valid_data_quantity);
			String result = MembersFileReader.main();
			assertThat(result, is("ファイル読み込みエラー"));
		} catch (Exception e) {
			e.printStackTrace();
		}
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
