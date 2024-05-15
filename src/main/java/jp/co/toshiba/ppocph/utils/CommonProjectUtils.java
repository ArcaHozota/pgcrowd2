package jp.co.toshiba.ppocph.utils;

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.collections4.BidiMap;
import org.apache.commons.collections4.bidimap.DualHashBidiMap;
import org.springframework.http.MediaType;
import org.springframework.lang.Nullable;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import com.alibaba.fastjson2.JSON;

import jp.co.toshiba.ppocph.config.ResponseLoginDto;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

/**
 * プロジェクト共通ツール
 *
 * @author ArkamaHozota
 * @since 1.00beta
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class CommonProjectUtils {

	/**
	 * UTF-8キャラセット
	 */
	public static final Charset CHARSET_UTF8 = StandardCharsets.UTF_8;

	/**
	 * 空のストリング
	 */
	public static final String EMPTY_STRING = "";

	/**
	 * 全角半角変換マップ
	 */
	private static final BidiMap<String, String> HALF_FULL_CONVERTOR = new DualHashBidiMap<>();

	static {
		HALF_FULL_CONVERTOR.put("！", "!");
		HALF_FULL_CONVERTOR.put("”", "\"");
		HALF_FULL_CONVERTOR.put("＃", "#");
		HALF_FULL_CONVERTOR.put("＄", "$");
		HALF_FULL_CONVERTOR.put("％", "%");
		HALF_FULL_CONVERTOR.put("＆", "&");
		HALF_FULL_CONVERTOR.put("’", "'");
		HALF_FULL_CONVERTOR.put("（", "(");
		HALF_FULL_CONVERTOR.put("）", ")");
		HALF_FULL_CONVERTOR.put("＊", "*");
		HALF_FULL_CONVERTOR.put("＋", "+");
		HALF_FULL_CONVERTOR.put("，", ",");
		HALF_FULL_CONVERTOR.put("－", "-");
		HALF_FULL_CONVERTOR.put("．", ".");
		HALF_FULL_CONVERTOR.put("／", "/");
		HALF_FULL_CONVERTOR.put("０", "0");
		HALF_FULL_CONVERTOR.put("１", "1");
		HALF_FULL_CONVERTOR.put("２", "2");
		HALF_FULL_CONVERTOR.put("３", "3");
		HALF_FULL_CONVERTOR.put("４", "4");
		HALF_FULL_CONVERTOR.put("５", "5");
		HALF_FULL_CONVERTOR.put("６", "6");
		HALF_FULL_CONVERTOR.put("７", "7");
		HALF_FULL_CONVERTOR.put("８", "8");
		HALF_FULL_CONVERTOR.put("９", "9");
		HALF_FULL_CONVERTOR.put("：", ":");
		HALF_FULL_CONVERTOR.put("；", ";");
		HALF_FULL_CONVERTOR.put("＜", "<");
		HALF_FULL_CONVERTOR.put("＝", "=");
		HALF_FULL_CONVERTOR.put("＞", ">");
		HALF_FULL_CONVERTOR.put("？", "?");
		HALF_FULL_CONVERTOR.put("＠", "@");
		HALF_FULL_CONVERTOR.put("Ａ", "A");
		HALF_FULL_CONVERTOR.put("Ｂ", "B");
		HALF_FULL_CONVERTOR.put("Ｃ", "C");
		HALF_FULL_CONVERTOR.put("Ｄ", "D");
		HALF_FULL_CONVERTOR.put("Ｅ", "E");
		HALF_FULL_CONVERTOR.put("Ｆ", "F");
		HALF_FULL_CONVERTOR.put("Ｇ", "G");
		HALF_FULL_CONVERTOR.put("Ｈ", "H");
		HALF_FULL_CONVERTOR.put("Ｉ", "I");
		HALF_FULL_CONVERTOR.put("Ｊ", "J");
		HALF_FULL_CONVERTOR.put("Ｋ", "K");
		HALF_FULL_CONVERTOR.put("Ｌ", "L");
		HALF_FULL_CONVERTOR.put("Ｍ", "M");
		HALF_FULL_CONVERTOR.put("Ｎ", "N");
		HALF_FULL_CONVERTOR.put("Ｏ", "O");
		HALF_FULL_CONVERTOR.put("Ｐ", "P");
		HALF_FULL_CONVERTOR.put("Ｑ", "Q");
		HALF_FULL_CONVERTOR.put("Ｒ", "R");
		HALF_FULL_CONVERTOR.put("Ｓ", "S");
		HALF_FULL_CONVERTOR.put("Ｔ", "T");
		HALF_FULL_CONVERTOR.put("Ｕ", "U");
		HALF_FULL_CONVERTOR.put("Ｖ", "V");
		HALF_FULL_CONVERTOR.put("Ｗ", "W");
		HALF_FULL_CONVERTOR.put("Ｘ", "X");
		HALF_FULL_CONVERTOR.put("Ｙ", "Y");
		HALF_FULL_CONVERTOR.put("Ｚ", "Z");
		HALF_FULL_CONVERTOR.put("［", "[");
		HALF_FULL_CONVERTOR.put("￥", "\\");
		HALF_FULL_CONVERTOR.put("］", "]");
		HALF_FULL_CONVERTOR.put("＾", "^");
		HALF_FULL_CONVERTOR.put("＿", "_");
		HALF_FULL_CONVERTOR.put("｀", "`");
		HALF_FULL_CONVERTOR.put("ａ", "a");
		HALF_FULL_CONVERTOR.put("ｂ", "b");
		HALF_FULL_CONVERTOR.put("ｃ", "c");
		HALF_FULL_CONVERTOR.put("ｄ", "d");
		HALF_FULL_CONVERTOR.put("ｅ", "e");
		HALF_FULL_CONVERTOR.put("ｆ", "f");
		HALF_FULL_CONVERTOR.put("ｇ", "g");
		HALF_FULL_CONVERTOR.put("ｈ", "h");
		HALF_FULL_CONVERTOR.put("ｉ", "i");
		HALF_FULL_CONVERTOR.put("ｊ", "j");
		HALF_FULL_CONVERTOR.put("ｋ", "k");
		HALF_FULL_CONVERTOR.put("ｌ", "l");
		HALF_FULL_CONVERTOR.put("ｍ", "m");
		HALF_FULL_CONVERTOR.put("ｎ", "n");
		HALF_FULL_CONVERTOR.put("ｏ", "o");
		HALF_FULL_CONVERTOR.put("ｐ", "p");
		HALF_FULL_CONVERTOR.put("ｑ", "q");
		HALF_FULL_CONVERTOR.put("ｒ", "r");
		HALF_FULL_CONVERTOR.put("ｓ", "s");
		HALF_FULL_CONVERTOR.put("ｔ", "t");
		HALF_FULL_CONVERTOR.put("ｕ", "u");
		HALF_FULL_CONVERTOR.put("ｖ", "v");
		HALF_FULL_CONVERTOR.put("ｗ", "w");
		HALF_FULL_CONVERTOR.put("ｘ", "x");
		HALF_FULL_CONVERTOR.put("ｙ", "y");
		HALF_FULL_CONVERTOR.put("ｚ", "z");
		HALF_FULL_CONVERTOR.put("｛", "{");
		HALF_FULL_CONVERTOR.put("｜", "|");
		HALF_FULL_CONVERTOR.put("｝", "}");
		HALF_FULL_CONVERTOR.put("～", "~");
		HALF_FULL_CONVERTOR.put("。", "｡");
		HALF_FULL_CONVERTOR.put("「", "｢");
		HALF_FULL_CONVERTOR.put("」", "｣");
		HALF_FULL_CONVERTOR.put("、", "､");
		HALF_FULL_CONVERTOR.put("・", "･");
		HALF_FULL_CONVERTOR.put("ァ", "ｧ");
		HALF_FULL_CONVERTOR.put("ィ", "ｨ");
		HALF_FULL_CONVERTOR.put("ゥ", "ｩ");
		HALF_FULL_CONVERTOR.put("ェ", "ｪ");
		HALF_FULL_CONVERTOR.put("ォ", "ｫ");
		HALF_FULL_CONVERTOR.put("ャ", "ｬ");
		HALF_FULL_CONVERTOR.put("ュ", "ｭ");
		HALF_FULL_CONVERTOR.put("ョ", "ｮ");
		HALF_FULL_CONVERTOR.put("ッ", "ｯ");
		HALF_FULL_CONVERTOR.put("ー", "ｰ");
		HALF_FULL_CONVERTOR.put("ア", "ｱ");
		HALF_FULL_CONVERTOR.put("イ", "ｲ");
		HALF_FULL_CONVERTOR.put("ウ", "ｳ");
		HALF_FULL_CONVERTOR.put("エ", "ｴ");
		HALF_FULL_CONVERTOR.put("オ", "ｵ");
		HALF_FULL_CONVERTOR.put("カ", "ｶ");
		HALF_FULL_CONVERTOR.put("キ", "ｷ");
		HALF_FULL_CONVERTOR.put("ク", "ｸ");
		HALF_FULL_CONVERTOR.put("ケ", "ｹ");
		HALF_FULL_CONVERTOR.put("コ", "ｺ");
		HALF_FULL_CONVERTOR.put("サ", "ｻ");
		HALF_FULL_CONVERTOR.put("シ", "ｼ");
		HALF_FULL_CONVERTOR.put("ス", "ｽ");
		HALF_FULL_CONVERTOR.put("セ", "ｾ");
		HALF_FULL_CONVERTOR.put("ソ", "ｿ");
		HALF_FULL_CONVERTOR.put("タ", "ﾀ");
		HALF_FULL_CONVERTOR.put("チ", "ﾁ");
		HALF_FULL_CONVERTOR.put("ツ", "ﾂ");
		HALF_FULL_CONVERTOR.put("テ", "ﾃ");
		HALF_FULL_CONVERTOR.put("ト", "ﾄ");
		HALF_FULL_CONVERTOR.put("ナ", "ﾅ");
		HALF_FULL_CONVERTOR.put("ニ", "ﾆ");
		HALF_FULL_CONVERTOR.put("ヌ", "ﾇ");
		HALF_FULL_CONVERTOR.put("ネ", "ﾈ");
		HALF_FULL_CONVERTOR.put("ノ", "ﾉ");
		HALF_FULL_CONVERTOR.put("ハ", "ﾊ");
		HALF_FULL_CONVERTOR.put("ヒ", "ﾋ");
		HALF_FULL_CONVERTOR.put("フ", "ﾌ");
		HALF_FULL_CONVERTOR.put("ヘ", "ﾍ");
		HALF_FULL_CONVERTOR.put("ホ", "ﾎ");
		HALF_FULL_CONVERTOR.put("マ", "ﾏ");
		HALF_FULL_CONVERTOR.put("ミ", "ﾐ");
		HALF_FULL_CONVERTOR.put("ム", "ﾑ");
		HALF_FULL_CONVERTOR.put("メ", "ﾒ");
		HALF_FULL_CONVERTOR.put("モ", "ﾓ");
		HALF_FULL_CONVERTOR.put("ヤ", "ﾔ");
		HALF_FULL_CONVERTOR.put("ユ", "ﾕ");
		HALF_FULL_CONVERTOR.put("ヨ", "ﾖ");
		HALF_FULL_CONVERTOR.put("ラ", "ﾗ");
		HALF_FULL_CONVERTOR.put("リ", "ﾘ");
		HALF_FULL_CONVERTOR.put("ル", "ﾙ");
		HALF_FULL_CONVERTOR.put("レ", "ﾚ");
		HALF_FULL_CONVERTOR.put("ロ", "ﾛ");
		HALF_FULL_CONVERTOR.put("ワ", "ﾜ");
		HALF_FULL_CONVERTOR.put("ヲ", "ｦ");
		HALF_FULL_CONVERTOR.put("ン", "ﾝ");
		HALF_FULL_CONVERTOR.put("ガ", "ｶﾞ");
		HALF_FULL_CONVERTOR.put("ギ", "ｷﾞ");
		HALF_FULL_CONVERTOR.put("グ", "ｸﾞ");
		HALF_FULL_CONVERTOR.put("ゲ", "ｹﾞ");
		HALF_FULL_CONVERTOR.put("ゴ", "ｺﾞ");
		HALF_FULL_CONVERTOR.put("ザ", "ｻﾞ");
		HALF_FULL_CONVERTOR.put("ジ", "ｼﾞ");
		HALF_FULL_CONVERTOR.put("ズ", "ｽﾞ");
		HALF_FULL_CONVERTOR.put("ゼ", "ｾﾞ");
		HALF_FULL_CONVERTOR.put("ゾ", "ｿﾞ");
		HALF_FULL_CONVERTOR.put("ダ", "ﾀﾞ");
		HALF_FULL_CONVERTOR.put("ヂ", "ﾁﾞ");
		HALF_FULL_CONVERTOR.put("ヅ", "ﾂﾞ");
		HALF_FULL_CONVERTOR.put("デ", "ﾃﾞ");
		HALF_FULL_CONVERTOR.put("ド", "ﾄﾞ");
		HALF_FULL_CONVERTOR.put("バ", "ﾊﾞ");
		HALF_FULL_CONVERTOR.put("ビ", "ﾋﾞ");
		HALF_FULL_CONVERTOR.put("ブ", "ﾌﾞ");
		HALF_FULL_CONVERTOR.put("ベ", "ﾍﾞ");
		HALF_FULL_CONVERTOR.put("ボ", "ﾎﾞ");
		HALF_FULL_CONVERTOR.put("パ", "ﾊﾟ");
		HALF_FULL_CONVERTOR.put("ピ", "ﾋﾟ");
		HALF_FULL_CONVERTOR.put("プ", "ﾌﾟ");
		HALF_FULL_CONVERTOR.put("ペ", "ﾍﾟ");
		HALF_FULL_CONVERTOR.put("ポ", "ﾎﾟ");
		HALF_FULL_CONVERTOR.put("ヴ", "ｳﾞ");
		HALF_FULL_CONVERTOR.put("ヷ", "ﾜﾞ");
		HALF_FULL_CONVERTOR.put("ヺ", "ｦﾞ");
		HALF_FULL_CONVERTOR.put("゛", "ﾞ");
		HALF_FULL_CONVERTOR.put("゜", "ﾟ");
		HALF_FULL_CONVERTOR.put("\u3000", " ");
	}

	/**
	 * 現在のリクエストがAJAXリクエストであるかどうかを判断する
	 *
	 * @param request リクエスト
	 * @return true: ajax-request, false: no-ajax
	 */
	public static boolean discernRequestType(final HttpServletRequest request) {
		// リクエストヘッダー情報の取得する
		final String acceptInformation = request.getHeader("Accept");
		final String xRequestInformation = request.getHeader("X-Requested-With");
		// 判断して返却する
		return acceptInformation != null && acceptInformation.length() > 0
				&& acceptInformation.contains("application/json")
				|| xRequestInformation != null && xRequestInformation.length() > 0
						&& "XMLHttpRequest".equals(xRequestInformation);
	}

	/**
	 * 共通権限管理ストリーム
	 *
	 * @param stream 権限ストリーム
	 * @return List<String>
	 */
	public static List<String> getAuthNames(final Stream<SimpleGrantedAuthority> stream) {
		return stream.map(SimpleGrantedAuthority::getAuthority).collect(Collectors.toList());
	}

	/**
	 * ファジークエリ用の検索文を取得する
	 *
	 * @param keyword 検索文
	 * @return ファジークエリ
	 */
	public static String getDetailKeyword(final String keyword) {
		final StringBuilder builder = new StringBuilder();
		builder.append("%");
		for (final char aChar : keyword.toCharArray()) {
			final String charAt = String.valueOf(aChar);
			builder.append(charAt).append("%");
		}
		return builder.toString();
	}

	/**
	 * 該当文字列はすべて半角かどうかを判断する
	 *
	 * @param hankaku 文字列
	 * @return true: すべて半角文字列, false: 全角文字も含める
	 */
	public static boolean isAllHankaku(final String hankaku) {
		final List<String> zenkakuList = new ArrayList<>(HALF_FULL_CONVERTOR.keySet());
		for (final char aChar : hankaku.toCharArray()) {
			if (zenkakuList.contains(String.valueOf(aChar))) {
				return false;
			}
		}
		return true;
	}

	/**
	 * 該当文字列はすべて全角かどうかを判断する
	 *
	 * @param zenkaku 文字列
	 * @return true: すべて全角文字列, false: 半角文字も含める
	 */
	public static boolean isAllZenkaku(final String zenkaku) {
		final List<String> hankakuList = new ArrayList<>(HALF_FULL_CONVERTOR.values());
		for (final char aChar : zenkaku.toCharArray()) {
			if (hankakuList.contains(String.valueOf(aChar))) {
				return false;
			}
		}
		return true;
	}

	/**
	 * ある文字列はすべて数字であるかどうかを判断する
	 *
	 * @param string ストリング
	 * @return true: すべて数字, false: 文字も含める
	 */
	public static boolean isDigital(@Nullable final String string) {
		if (CommonProjectUtils.isEmpty(string)) {
			return false;
		}
		return Pattern.compile("\\d*").matcher(string).matches();
	}

	/**
	 * 当ストリングは空かどうかを判断する
	 *
	 * @param str ストリング
	 * @return true: 空, false: 空ではない
	 */
	public static boolean isEmpty(@Nullable final String str) {
		return str == null || str.isEmpty() || str.isBlank();
	}

	/**
	 * 二つのロング数はイコールすることを判断する
	 *
	 * @param long1 ロング1
	 * @param long2 ロング2
	 * @return true: イコール, false: イコールしない
	 */
	public static boolean isEqual(@Nullable final Long long1, @Nullable final Long long2) {
		if (long1 == null && long2 == null || long1.longValue() == long2.longValue()) {
			return true;
		}
		return false;
	}

	/**
	 * 二つのオブジェクトはイコールすることを判断する
	 *
	 * @param obj1 オブジェクト1
	 * @param obj2 オブジェクト2
	 * @return true: イコール, false: イコールしない
	 */
	public static boolean isEqual(@Nullable final Object obj1, @Nullable final Object obj2) {
		if (obj1 == null && obj2 == null || obj1 != null && obj1.equals(obj2)) {
			return true;
		}
		return false;
	}

	/**
	 * 二つのストリングはイコールすることを判断する
	 *
	 * @param str1 ストリング1
	 * @param str2 ストリング2
	 * @return true: イコール, false: イコールしない
	 */
	public static boolean isEqual(@Nullable final String str1, @Nullable final String str2) {
		if (str1 == null && str2 == null) {
			return true;
		}
		if (str1 == null || str2 == null || str1.length() != str2.length()) {
			return false;
		}
		return str1.trim().equals(str2.trim());
	}

	/**
	 * 当ストリングは空ではないかどうかを判断する
	 *
	 * @param str ストリング
	 * @return true: 空ではない, false: 空
	 */
	public static boolean isNotEmpty(@Nullable final String str) {
		return !CommonProjectUtils.isEmpty(str);
	}

	/**
	 * 二つのロング数はイコールしないことを判断する
	 *
	 * @param long1 ロング1
	 * @param long2 ロング2
	 * @return true: イコールしない, false: イコール
	 */
	public static boolean isNotEqual(@Nullable final Long long1, @Nullable final Long long2) {
		return !CommonProjectUtils.isEqual(long1, long2);
	}

	/**
	 * 二つのオブジェクトはイコールしないことを判断する
	 *
	 * @param obj1 オブジェクト1
	 * @param obj2 オブジェクト2
	 * @return true: イコールしない, false: イコール
	 */
	public static boolean isNotEqual(@Nullable final Object obj1, @Nullable final Object obj2) {
		return !CommonProjectUtils.isEqual(obj1, obj2);
	}

	/**
	 * 二つのストリングはイコールしないことを判断する
	 *
	 * @param str1 ストリング1
	 * @param str2 ストリング2
	 * @return true: イコールしない, false: イコール
	 */
	public static boolean isNotEqual(@Nullable final String str1, @Nullable final String str2) {
		return !CommonProjectUtils.isEqual(str1, str2);
	}

	/**
	 * 文字列をクライアントにレンダリングする
	 *
	 * @param response リスポンス
	 * @param string   ストリング
	 */
	@SuppressWarnings("deprecation")
	public static void renderString(final HttpServletResponse response, final ResponseLoginDto aResult) {
		try {
			response.setStatus(aResult.getCode());
			response.setContentType(MediaType.APPLICATION_JSON_UTF8.toString());
			response.getWriter().print(JSON.toJSONString(aResult));
			response.getWriter().close();
		} catch (final IOException e) {
			// do nothing
		}
	}

	/**
	 * 全角から半角へ変換
	 *
	 * @param zenkaku 全角文字
	 * @return 半角文字
	 */
	public static String toHankaku(@Nullable final String zenkaku) {
		if (CommonProjectUtils.isEmpty(zenkaku)) {
			return EMPTY_STRING;
		}
		final StringBuilder builder = new StringBuilder();
		final List<String> zenkakuList = new ArrayList<>(HALF_FULL_CONVERTOR.keySet());
		for (final char charAt : zenkaku.toCharArray()) {
			final String charAtString = String.valueOf(charAt);
			if (zenkakuList.contains(charAtString)) {
				builder.append(HALF_FULL_CONVERTOR.get(charAtString));
			} else {
				builder.append(charAtString);
			}
		}
		return builder.toString();
	}

	/**
	 * 半角から全角へ変換
	 *
	 * @param hankaku 半角文字
	 * @return 全角文字
	 */
	public static String toZenkaku(@Nullable final String hankaku) {
		if (CommonProjectUtils.isEmpty(hankaku)) {
			return EMPTY_STRING;
		}
		final StringBuilder builder = new StringBuilder();
		final List<String> hankakuList = new ArrayList<>(HALF_FULL_CONVERTOR.values());
		for (final char charAt : hankaku.toCharArray()) {
			final String charAtString = String.valueOf(charAt);
			if (hankakuList.contains(charAtString)) {
				builder.append(HALF_FULL_CONVERTOR.inverseBidiMap().get(charAtString));
			} else {
				builder.append(charAtString);
			}
		}
		return builder.toString();
	}
}