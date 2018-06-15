package fileio;

import java.io.IOException;
import java.nio.file.NoSuchFileException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.naming.NamingException;

import com.javaranch.unittest.helper.sql.pool.JNDIUnitTestHelper;

import dao.DaoFactory;
import dao.MembersDao;
import domain.DataValid;
import domain.Members;

public class MemberFileReader extends EventMgFileIO {
	private String fileName;

	public static void main(String args[]) {
		int valid_data_quantity = 9;
		try {
			MemberFileReader MembersFileReader = new MemberFileReader("c:\\work_1\\Member_20180601.csv",
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
		this.fileName=filename;
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
				Date birthday =null;
				Date hired = null;
				//--ここから
				if(!DataValid.isNotNull(columns[1]))
					return "REQUIRED_SPECIFICATION";
				if(!DataValid.limitChar(columns[1],8))
					return "INCORRECT_FORMAT_ERROR";
				if(!DataValid.isNotNull(columns[2]))
					return "REQUIRED_SPECIFICATION";
				if(!DataValid.limitChar(columns[2], 50))
					return "INCORRECT_FORMAT_ERROR";
				if(!DataValid.isKana(columns[3]))
					return "INCORRECT_FORMAT_ERROR";
				if(!DataValid.limitChar(columns[3],50))
					return "INCORRECT_FORMAT_ERROR";
				if(!DataValid.isNotNull(columns[4]))
					return "REQUIRED_SPECIFICATION";
				if(!DataValid.isDateFormat(columns[4],"yyyy/M/d"))
					return "DATE_FORMAT_ERROR";
				if(!DataValid.isNotNull(columns[5]))
					return "REQUIRED_SPECIFICATION";
				if(!DataValid.isNotNull(columns[6]))
					return "REQUIRED_SPECIFICATION";
				if(!DataValid.isTelFormat(columns[6]))
					return "INCORRECT_FORMAT_ERROR";
				if(!DataValid.isNotNull(columns[7]))
					return "REQUIRED_SPECIFICATION";
				if(!DataValid.isDateFormat(columns[7], "M月d日"))
					return "DATE_FORMAT_ERROR";
				if(!DataValid.isNotNull(columns[8]))
					return "REQUIRED_SPECIFICATION";
				if(!DataValid.isRange(Integer.parseInt(columns[8]),1,5))
					return "OUT_OF_INDEX_ERROR";

				// 誕生日の型をDate型に変換
				try {
					DateFormat df = new SimpleDateFormat("yyyy/MM/dd");
					birthday = df.parse(columns[4]);
				} catch (ParseException p) {
					p.printStackTrace();
				}

				// 入社日をDate型に変換
				try {

					String fileNames[] = fileName.split("_",0);
					String year = fileNames[2].substring(0, 4) + "年";
					DateFormat df = new SimpleDateFormat("yyyy年M月d日");
					hired = df.parse(year + columns[7]);
				} catch (ParseException p) {
					p.printStackTrace();
				}

				// memberのインスタンスに値を格納
				domain.setMember_id(columns[1]);
				domain.setName(columns[2]);
				domain.setKana(columns[3]);
				domain.setBirthday(birthday);
				domain.setAddress(columns[5]);
				domain.setTel(columns[6]);
				domain.setHired(hired);
				domain.setDep_id(Integer.parseInt(columns[8]));

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
				MembersDao MembersDao = DaoFactory.createMembersDao();
				MembersDao.insert(Members);

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
