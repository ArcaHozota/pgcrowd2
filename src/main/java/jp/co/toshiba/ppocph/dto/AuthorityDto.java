package jp.co.toshiba.ppocph.dto;

import java.io.Serializable;

import lombok.Data;

/**
 * 権限情報転送クラス
 *
 * @author ArkamaHozota
 * @since 1.00beta
 */
@Data
public final class AuthorityDto implements Serializable {

	private static final long serialVersionUID = 3942444394837632903L;

	/**
	 * ID
	 */
	private String id;

	/**
	 * 名称
	 */
	private String name;

	/**
	 * タイトル
	 */
	private String title;

	/**
	 * 親ディレクトリID
	 */
	private String categoryId;
}
