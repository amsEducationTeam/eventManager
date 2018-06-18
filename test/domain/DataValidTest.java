package domain;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.*;

import java.util.Date;

import org.junit.Test;

public class DataValidTest {

	@Test
	public void testIsNotNullInteger() {
		int intNum = 5;
		boolean check = DataValid.isNotNull(intNum);
		assertThat(check, is(true));

		Integer falseNum= null;
		boolean check2 = DataValid.isNotNull(falseNum);
		assertThat(check2, is(false));
	}

	@Test
	public void testIsNotNullString() {
		String StringChar ="あいうえお";
		boolean check = DataValid.isNotNull(StringChar);
		assertThat(check, is(true));

		String falseChar =null;
		boolean check2 = DataValid.isNotNull(falseChar);
		assertThat(check2, is(false));
	}

	@Test
	public void testIsNotNullDate() {
		Date date = new Date();
        boolean check = DataValid.isNotNull(date);
        assertThat(check, is(true));

        Date date2 = null;
        boolean check2 = DataValid.isNotNull(date2);
        assertThat(check2, is(false));
	}

	@Test
	public void testLimitCharStringInt() {
		String str ="あいうえお";
		boolean check =DataValid.limitChar(str, 5);
		assertThat(check, is(true));

		String falsestr ="あいうえおかきくけこ";
		boolean check2 =DataValid.limitChar(falsestr, 5);
		assertThat(check2, is(false));
	}

	@Test
	public void testLimitCharIntInt() {
		int testNum = 12345;
		boolean check =DataValid.limitChar(testNum, 5);
		assertThat(check, is(true));

		int falseNum = 1234567890;
		boolean check2 =DataValid.limitChar(falseNum, 5);
		assertThat(check2, is(false));
	}

	@Test
	public void testChkLiteAndNum() {
		String hankaku="aiueo1234";
		boolean check =DataValid.chkLiteAndNum(hankaku);
		assertThat(check, is(true));

		String falsehankaku="aiueo-1234";
		boolean check2 =DataValid.chkLiteAndNum(falsehankaku);
		assertThat(check2, is(false));
	}

	@Test
	public void testIsAlphanum() {
		String hankaku="aiueo-1234";
		boolean check =DataValid.isAlphanum(hankaku);
		assertThat(check, is(true));

		String hankaku2="aiueo_1234";
		boolean check2 =DataValid.isAlphanum(hankaku2);
		assertThat(check2, is(false));
	}

	@Test
	public void testInNotNum() {
		String hankaku="aiueo";
		boolean check =DataValid.inNotNum(hankaku);
		assertThat(check, is(true));

		String falsehankaku="aiueo1234";
		boolean check2 =DataValid.inNotNum(falsehankaku);
		assertThat(check2, is(false));
	}

	@Test
	public void testIsNum() {
		String hankaku="12345678";
		boolean check =DataValid.isNum(hankaku);
		assertThat(check, is(true));

		String falsehankaku="aiueo1234";
		boolean check2 =DataValid.isNum(falsehankaku);
		assertThat(check2, is(false));
	}

	@Test
	public void testIsKana() {
		String kana="アイウエオ";
		boolean check =DataValid.isKana(kana);
		assertThat(check, is(true));

		String falsekana="ABCDE";
		boolean check2 =DataValid.isKana(falsekana);
		assertThat(check2, is(false));
	}

	@Test
	public void testIsRange() {
		int value=5;
		int begin=0;
		int end=10;
		boolean check =DataValid.isRange(value, begin, end);
		assertThat(check, is(true));

		int falsevalue=5;
		int falsebegin=2;
		int falseend=4;
		boolean check2 =DataValid.isRange(falsevalue, falsebegin, falseend);
		assertThat(check2, is(false));
	}

	@Test
	public void testIsDateFormat() {
		String date="2018/06/12";
		String type="yyyy/MM/dd";
		boolean check =DataValid.isDateFormat(date,type);
		assertThat(check, is(true));
		String date1="2月27日";
		String type1="M月d日";
		boolean check1 =DataValid.isDateFormat(date1,type1);
		assertThat(check1, is(true));

		String falsedate="2018/06/12";
		String falsetype="M月d日";
		boolean check2 =DataValid.isDateFormat(falsedate,falsetype);
		assertThat(check2, is(false));
	}

	@Test
	public void testIsTimeFormat() {
		String time="23:50";
		boolean check =DataValid.isTimeFormat(time);
		assertThat(check, is(true));

		String falsetime="12時50分";
		boolean checkfalse =DataValid.isTimeFormat(falsetime);
		assertThat(checkfalse, is(false));
	}

	@Test
	public void testIsTelFormat() {
		String tel="00-1234-5678";
		boolean check =DataValid.isTelFormat(tel);
		assertThat(check, is(true));

		String tel1="(00)1234-5678";
		boolean check1 =DataValid.isTelFormat(tel1);
		assertThat(check1, is(true));

		String tel2="080-1234-5678";
		boolean check2 =DataValid.isTelFormat(tel2);
		assertThat(check2, is(true));

		String tel3="050-1234-5678";
		boolean check3 =DataValid.isTelFormat(tel3);
		assertThat(check3, is(true));

		String tel4="0120-123-456";
		boolean check4 =DataValid.isTelFormat(tel4);
		assertThat(check4, is(true));

		String falsetel="0000-1111-2222";
		boolean checkfalse =DataValid.isTelFormat(falsetel);
		assertThat(checkfalse, is(false));
	}

}
