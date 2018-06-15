package fileio;

import java.io.IOException;
import java.nio.file.NoSuchFileException;
import java.util.ArrayList;
import java.util.List;

import javax.naming.NamingException;

import com.javaranch.unittest.helper.sql.pool.JNDIUnitTestHelper;

import domain.Members;

public class MemberFileReader extends EventMgFileIO {

	public static void main(String args[]) {
		int valid_data_quantity = 9;
		try {
			MemberFileReader MembersFileReader = new MemberFileReader("c:\\work_1\\Members_20180601.csv",
					valid_data_quantity);

			/*
			 *Junitを使うまではこれで接続します
			 */
			try {
				JNDIUnitTestHelper.init("WebContent/WEB-INF/classes/jndi_unit_test_helper.properties");
			} catch (NamingException | IOException e) {
				// TODO 自動生成された catch ブロック
				e.printStackTrace();
			}

			String result = MembersFileReader.main();
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
	MemberFileReader(String filename, int columns) {
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
		List<Members> MembersList = new ArrayList<>();

		for (String[] columns : fileRead) {

			// データ有効性チェック
			if (enableLine(columns)) {
				// ドメインにセット
				Members domain = new Members();
				//--ここから
				//				acoData.setMembersId(columns[1]);
				//				acoData.setLoginId(columns[2]);
				//				acoData.setLoginPass(columns[3]);
				//				acoData.setAuthId(new Integer(Integer.parseInt(columns[4])));
				//--リストに追加
				MembersList.add(domain);
			} else {
				result = "データ有効性エラー";
				return result;
			}
		}

		//リストをDB登録
		for (Members Members : MembersList) {
			// Membersリストデータをinsert
			try {
				//					MembersDao MembersDao = DaoFactory.createMembersDao();
				//					result = MembersDao.insertAcount(Members);

			} catch (Exception e) {
				e.printStackTrace();
				result = "DB接続エラー";
				return result;
			}
		}

		return SUCCESS;
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

	public boolean enableLine(String[] columns) {

		// データ行の列で空のデータがないか
		for (int i = 1; i < columns.length; i++) {
//			//空のデータがあれば終了
//			if (!DataValid.isNotNull(columns[i])) {
//				return false;
//			}
		}

		//データ行の
		//		if (!DataValid.checkCharLimit(columns[1], 8) ||
		//			!DataValid.checkNumberOnly(columns[1]) ||
		//			!DataValid.checkCharLimit(columns[2], 20) ||
		//			!DataValid.checkLiteAndNumOnly(columns[2]) ||
		//			!DataValid.checkLiteAndNumOnly(columns[3]) ||
		//			!checkCharMin(columns[3], 8) ||
		//			!DataValid.checkNumberOnly(columns[4]) ||
		//			!check1or2(columns[4])){
		//			return false;
		//
		//		}
		return true;

	}

}