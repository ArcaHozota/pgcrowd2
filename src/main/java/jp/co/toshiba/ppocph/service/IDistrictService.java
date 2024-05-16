package jp.co.toshiba.ppocph.service;

import java.util.List;

import jp.co.toshiba.ppocph.dto.DistrictDto;
import jp.co.toshiba.ppocph.utils.Pagination;
import jp.co.toshiba.ppocph.utils.ResultDto;

/**
 * 地域サービスインターフェス
 *
 * @author ArkamaHozota
 * @since 1.00beta
 */
public interface IDistrictService {

	/**
	 * 地方リストを取得する
	 *
	 * @return List<String>
	 */
	List<String> getDistrictChihos();

	/**
	 * 都市IDによって地域一覧を取得する
	 *
	 * @param cityId 都市ID
	 * @return List<DistrictDto>
	 */
	List<DistrictDto> getDistrictsByCityId(String cityId);

	/**
	 * キーワードによって地域情報を取得する
	 *
	 * @param pageNum ページ数
	 * @param keyword キーワード
	 * @return Pagination<DistrictDto>
	 */
	Pagination<DistrictDto> getDistrictsByKeyword(Integer pageNum, String keyword);

	/**
	 * 地域情報更新
	 *
	 * @param districtDto 地域情報転送クラス
	 * @return ResultDto<String>
	 */
	ResultDto<String> update(DistrictDto districtDto);
}
