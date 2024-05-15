package jp.co.toshiba.ppocph.dto;

import java.io.Serializable;

import lombok.Data;

/**
 * 都市情報転送クラス
 *
 * @author ArkamaHozota
 * @since 1.00beta
 */
@Data
public final class CityDto implements Serializable {

	private static final long serialVersionUID = 4051616975308402460L;

	/**
	 * ID
	 */
	private String id;

	/**
	 * 名称
	 */
	private String name;

	/**
	 * 都道府県ID
	 */
	private Long districtId;

	/**
	 * 読み方
	 */
	private String pronunciation;

	/**
	 * 都道府県名称
	 */
	private String districtName;

	/**
	 * 人口数量
	 */
	private Long population;

	/**
	 * 市町村旗
	 */
	private String cityFlag;
}
