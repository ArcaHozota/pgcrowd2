package jp.co.toshiba.ppocph.dto;

import java.io.Serializable;

import lombok.Data;

/**
 * 地域情報転送クラス
 *
 * @author ArkamaHozota
 * @since 1.00beta
 */
@Data
public final class DistrictDto implements Serializable {

	private static final long serialVersionUID = 3826168628785387126L;

	/**
	 * ID
	 */
	private String id;

	/**
	 * 名称
	 */
	private String name;

	/**
	 * 州都ID
	 */
	private String shutoId;

	/**
	 * 州都名称
	 */
	private String shutoName;

	/**
	 * 地方名称
	 */
	private String chiho;

	/**
	 * 人口数量
	 */
	private Long population;

	/**
	 * 都道府県旗
	 */
	private String districtFlag;
}
