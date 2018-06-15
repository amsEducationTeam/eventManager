package dao;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.*;

import java.io.IOException;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.naming.NamingException;

import org.junit.Before;
import org.junit.Test;

import com.javaranch.unittest.helper.sql.pool.JNDIUnitTestHelper;

import domain.Place;

public class PlaceDaoImplTest {

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
	public void 正常系testInsert() throws Exception {

		//インサート用のテストデータをセットしリストに格納
		Date date = new Date();
		Place place = new Place();
		place.setAdmin_id(9);
		place.setCapa(90);
		place.setEqu_mic(0);
		place.setEqu_projector(1);
		place.setEqu_whitebord(0);
		place.setLocking_time(date);
		place.setPlace("会議室100");

		List<Place> testList = new ArrayList();
		testList.add(place);

		//リストにセットしたテストデータをデータベースに登録
		int count = 1;
		PlaceDao placeDao = DaoFactory.createPlaceDao();
		placeDao.insert(testList, count);

		//確認のためにDBに接続
		Class.forName("com.mysql.jdbc.Driver");
		try (java.sql.Connection conn = DriverManager
				.getConnection("jdbc:mysql://127.0.0.1:3306/eventdb2?useUnicode=true"
						+ "&characterEncoding=utf8&useSSL=false", "root", "rootpass")) {

			//登録したテストデータの存在を確認
			String sql = "select * from place where place = ?;";
			PreparedStatement stmt = conn.prepareStatement(sql);
			stmt.setObject(1, place.getPlace());
			ResultSet rs = stmt.executeQuery();
			while (rs.next()) {
				assertThat(rs.getObject("place").toString(), is("会議室100"));
			}

			//テストデータを削除
			String sql2 = "DELETE FROM eventdb2.place WHERE place='会議室100';";
			PreparedStatement stmt2 = conn.prepareStatement(sql2);
			stmt2.executeUpdate();

		}
	}

	@Test
	public void 異常系testInsert() throws Exception {

		//インサート用のサンプルデータをセットしリストに格納
		//not nullに設定されているデータ「capa」に何もセットしない
		Date date = new Date();
		Place place = new Place();
		place.setAdmin_id(9);
		//place.setCapa(90);
		place.setEqu_mic(0);
		place.setEqu_projector(1);
		place.setEqu_whitebord(0);
		place.setLocking_time(date);
		place.setPlace("会議室100");

		List<Place> testList = new ArrayList();
		testList.add(place);

		//上で作成した不完全なリストをインサートする
		int count = 1;
		PlaceDao placeDao = DaoFactory.createPlaceDao();
		try {
			placeDao.insert(testList, count);
		}
		//null不可なので例外のメッセージが返ってくる
		catch (Exception e) {
			assertThat(e.getMessage(), is("Column 'capa' cannot be null"));
		}
	}

	@Test
	public void 異常系2testInsert() throws Exception {

		//インサート用のサンプルデータをセットしリストに格納
		//not nullに設定されているデータ「capa」に何もセットしない
		Date date = new Date();
		Place place = new Place();
		place.setAdmin_id(9);
		place.setCapa(90);
		place.setEqu_mic(0);
		place.setEqu_projector(100000000);
		place.setEqu_whitebord(0);
		place.setLocking_time(date);
		place.setPlace("会議室100");

		List<Place> testList = new ArrayList();
		testList.add(place);

		//上で作成した不完全なリストをインサートする
		int count = 1;
		PlaceDao placeDao = DaoFactory.createPlaceDao();
		try {
			placeDao.insert(testList, count);
		}
		//null不可なので例外のメッセージが返ってくる
		catch (Exception e) {
			assertThat(e.getMessage(), is("Column 'capa' cannot be null"));
		}
	}
}
