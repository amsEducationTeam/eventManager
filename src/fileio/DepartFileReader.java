package fileio;

import java.io.IOException;
import java.nio.file.NoSuchFileException;
import java.util.ArrayList;
import java.util.List;

import javax.naming.NamingException;

import com.javaranch.unittest.helper.sql.pool.JNDIUnitTestHelper;

import dao.DaoFactory;
import dao.DepartDao;
import domain.DataValid;
import domain.Depart;

public class DepartFileReader extends EventMgFileIO {
	static final int VALID_DATA_QUANTITY = 4;
	private static int data_amount = 0;

	public static void main(String args[]) {

		try {
			DepartFileReader DepartFileReader = new DepartFileReader("c:\\work\\department_20180601.csv",
					VALID_DATA_QUANTITY);

			/*
			 *Junitを使うまではこれで接続します
			 */
			try {
				JNDIUnitTestHelper.init("WebContent/WEB-INF/classes/jndi_unit_test_helper.properties");
			} catch (NamingException | IOException e) {
				e.printStackTrace();
			}

			String result = DepartFileReader.main();
			System.out.print(result);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * ファイル名と列数をセットします
	 * @param	fileName	パスを含めたファイル名
	 * @param	cols	データタイプコード列を含めた列数
	 *
	 * **/
	DepartFileReader(String filename, int columns) {
		super(filename, columns);
	}

	/**
	 * このクラスのメイン処理です
	 * @return String 結果コードを返却します
	 */
	public String main() {

		String result = null; //結果

		List<String[]> fileRead = new ArrayList<String[]>();
		try {
			fileRead = enableFile();//ファイル有効性チェック
		} catch (NoSuchFileException e) {
			result = "指定のファイルが存在しません";
			return result;
		}
		result = getResult(); //結果セット
		if (!result.equals(SUCCESS)) {//異常であれば終了
			return result;
		}

		// ドメインリスト
		List<Depart> DepartList = new ArrayList<>();

		for (String[] columns : fileRead) {

			// データ有効性チェック
			if (enableLine(columns)) {
								// ドメインにセット
								Depart acoData = new Depart();
								acoData.setDepartment(columns[1]);
								acoData.setFloor(Integer.parseInt(columns[2]));
								acoData.setPosition_type(Integer.parseInt(columns[3]));
								// リストに追加
								DepartList.add(acoData);
								data_amount++;
			} else {
				result = "データ有効性エラー";
				return result;
			}
		}

			// Departリストデータをinsert
			try {
				DepartDao DepartDao = DaoFactory.createDepartDao();
				result = DepartDao.insert(DepartList,data_amount);

			} catch (Exception e) {
				e.printStackTrace();
				result = "DB接続エラー";
				return result;
			}
		return result;
	}

	/**
	 * データ有効性チェック
	 * String型配列に格納したファイルのD行の
	 * データが有効かであるか検査します
	 *
	 * @return	処理結果を返却します
	 * @param	columns	ファイルのD行
	 *			index0には"D"が格納されています
	 *			検査対象はindex1からになります
	 * **/
	protected boolean enableLine(String[] columns) {

		// データ行の列で空のデータがないか
		for (int i = 1; i < columns.length; i++) {
			//空のデータがあれば終了
			if (!DataValid.isNotNull(columns[i])) {
				return false;
			}
		}

		//データ項目の個別チェック
		if (!DataValid.limitChar(columns[1], 50) ||
				!DataValid.limitChar(columns[3], 8) ||
				!DataValid.chkLiteAndNum(columns[3])) {
			return false;

		}
		return true;

	}

}
