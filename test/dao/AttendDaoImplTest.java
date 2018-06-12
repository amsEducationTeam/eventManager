package dao;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.*;

import java.io.IOException;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.util.List;

import javax.naming.NamingException;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import com.javaranch.unittest.helper.sql.pool.JNDIUnitTestHelper;
import com.mysql.jdbc.Connection;
import com.mysql.jdbc.Statement;

import domain.Attend;
import domain.Events;
import domain.Members;

public class AttendDaoImplTest {

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
	}

	@Before
	public void setUp() throws Exception {
		// JNDI準備
		try {
			JNDIUnitTestHelper.init("WebContent/WEB-INF/classes/jndi_unit_test_helper.properties");
		} catch (NamingException | IOException e) {
			e.printStackTrace();
		}
	}

	@Test
	public void testAttendDaoImpl() {
		//fail("まだ実装されていません");
	}

	/**
	 * attend情報をインサートしたので、findAttendメソッドで
	 * 情報が追加されているかどうか調べる
	 * @throws Exception
	 */
	@Test
	public void testInsertStringInt() throws Exception {
		AttendDao attendDao = DaoFactory.createAttendDao();
		attendDao.insert("105",6);//member_id=105, event_id=2
		List<Attend> totalAttend = attendDao.findAttends(6);//event_id=6
		String member_id = (totalAttend.get(0).getMember_id());
		String member_name = (totalAttend.get(0).getMember_name());

		assertThat(member_name, is("山本葵"));//equalTo()
		assertThat(member_id, is("105"));//equalTo()

		// test後にdeleteメソッドで情報を削除する
		attendDao.delete("105",6);
	}

	/**
	 * insertしたデータをdeleteし、データを検索して count=0 になれば良い
	 * @throws Exception
	 */
	@Test
	public void testDeleteStringInt() throws Exception {

		AttendDao attendDao = DaoFactory.createAttendDao();
		attendDao.insert("105", 8);//member_id=105, event_id=8
		attendDao.delete("105", 8);
		int member_count = 1;
		try (Connection conn = (Connection) DriverManager.getConnection(
				"jdbc:mysql://127.0.0.1:3306/eventdb2?useUnicode=true&characterEncoding=utf8", "root",
				"rootpass")) {
			String sql = "SELECT COUNT(*) from attends where event_id=8";
			Statement stmt = (Statement) conn.createStatement();
			ResultSet rs = stmt.executeQuery(sql);
			while (rs.next()) {
				member_count = Integer.parseInt(rs.getString("count(*)"));
			}
		}
		assertThat(member_count, is(0));
	}

	@Test
	public void testFindAttends() throws Exception {
		AttendDao attendDao = DaoFactory.createAttendDao();
		List<Attend> totalAttend=attendDao.findAttends(1);//event_id=1
		// member_idとmember_nameが格納されて返却されるので、それを比較する
		String member_name = (totalAttend.get(0)).getMember_name();
		String member_id = (totalAttend.get(0)).getMember_id();

		assertThat(member_name, is("山本葵"));//equalTo()
		assertThat(member_id, is("105"));
	}

	/**
	 * memberを削除した際に呼び出されるメソッド
	 * 削除したmemberのattend情報をすべて削除する
	 * insertしたデータをdeleteし、データを検索して count=0 になれば良い
	 * @throws Exception
	 */
	@Test
	public void testDeleteByMemberId() throws Exception {
		AttendDao attendDao = DaoFactory.createAttendDao();
		attendDao.insert("113", 10);//member_id=113, event_id=10
		attendDao.insert("113", 11);//member_id=113, event_id=11
		Members member = new Members();
		member.setMember_id("113");

		attendDao.deleteByMemberId(member);
		int member_count = 1;
		try (Connection conn = (Connection) DriverManager.getConnection(
				"jdbc:mysql://127.0.0.1:3306/eventdb2?useUnicode=true&characterEncoding=utf8", "root",
				"rootpass")) {
			String sql = "SELECT COUNT(*) from attends where member_id=113";
			Statement stmt = (Statement) conn.createStatement();
			ResultSet rs = stmt.executeQuery(sql);
			while (rs.next()) {
				member_count = Integer.parseInt(rs.getString("count(*)"));
			}
		}
		assertThat(member_count, is(0));
	}

	/**
	 * eventを削除した際に呼び出されるメソッド
	 * 削除したeventのattend情報をすべて削除する
	 * insertしたデータをdeleteし、データを検索して count=0 になれば良い
	 */
	@Test
	public void testDeleteByEventId() throws Exception {
		AttendDao attendDao = DaoFactory.createAttendDao();
		attendDao.insert("110", 20);//member_id=110, event_id=20
		attendDao.insert("111", 20);//member_id=111, event_id=20
		Events event = new Events();
		event.setEvent_id(20);

		attendDao.deleteByEventId(event);
		int member_count = 1;
		try (Connection conn = (Connection) DriverManager.getConnection(
				"jdbc:mysql://127.0.0.1:3306/eventdb2?useUnicode=true&characterEncoding=utf8", "root",
				"rootpass")) {
			String sql = "SELECT COUNT(*) from attends where event_id=20";
			Statement stmt = (Statement) conn.createStatement();
			ResultSet rs = stmt.executeQuery(sql);
			while (rs.next()) {
				member_count = Integer.parseInt(rs.getString("count(*)"));
			}
		}
		assertThat(member_count, is(0));
	}

}
