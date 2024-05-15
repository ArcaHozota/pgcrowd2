package jp.co.toshiba.ppocph.dto;

import java.io.Serializable;

import lombok.Data;

/**
 * 社員情報転送クラス
 *
 * @author ArkamaHozota
 * @since 1.00beta
 */
@Data
public final class EmployeeDto implements Serializable {

	private static final long serialVersionUID = -3234114533598999540L;

	/**
	 * ID
	 */
	private String id;

	/**
	 * アカウント
	 */
	private String loginAccount;

	/**
	 * ユーザ名称
	 */
	private String username;

	/**
	 * パスワード
	 */
	private String password;

	/**
	 * メール
	 */
	private String email;

	/**
	 * 生年月日
	 */
	private String dateOfBirth;

	/**
	 * 役割ID
	 */
	private String roleId;
}
