package jp.co.toshiba.ppocph.config;

import java.io.Serializable;

import lombok.Getter;
import lombok.Setter;

/**
 * リスポンスエンティティ
 *
 * @author ArkamaHozota
 * @since 2.04
 */
@Getter
@Setter
public class ResponseLoginDto implements Serializable {

	private static final long serialVersionUID = -5033762283136118856L;

	/**
	 * HTTPステータスコード
	 */
	private Integer code;

	/**
	 * エラーメッセージ
	 */
	private String message;

	/**
	 * コンストラクタ
	 */
	ResponseLoginDto() {
		this.code = 200;
	}

	/**
	 * パラメータつきコンストラクタ
	 *
	 * @param code    コード
	 * @param message メッセージ
	 */
	public ResponseLoginDto(final Integer code, final String message) {
		this.code = code;
		this.message = message;
	}
}
