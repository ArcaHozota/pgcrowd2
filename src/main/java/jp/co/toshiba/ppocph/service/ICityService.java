package jp.co.toshiba.ppocph.service;

import jp.co.toshiba.ppocph.dto.CityDto;
import jp.co.toshiba.ppocph.utils.Pagination;
import jp.co.toshiba.ppocph.utils.ResultDto;

/**
 * 都市サービスインターフェス
 *
 * @author ArkamaHozota
 * @since 7.89
 */
public interface ICityService {

	/**
	 * 都市名称重複するかどうかをチェックする
	 *
	 * @param name       都市名称
	 * @param districtId 地域ID
	 * @return ResultDto<String>
	 */
	ResultDto<String> checkDuplicated(String name, Long districtId);

	/**
	 * キーワードによって都市情報を検索する
	 *
	 * @param pageNum ページングナンバー
	 * @param keyword キーワード
	 * @return Pagination<CityDto>
	 */
	Pagination<CityDto> getCitiesByKeyword(Integer pageNum, String keyword);

	/**
	 * IDによって情報を削除する
	 *
	 * @param id 都市ID
	 * @return ResultDto<String>
	 */
	ResultDto<String> remove(Long id);

	/**
	 * 都市情報追加
	 *
	 * @param cityDto 都市情報転送クラス
	 */
	void save(CityDto cityDto);

	/**
	 * 都市情報更新
	 *
	 * @param cityDto 都市情報転送クラス
	 */
	ResultDto<String> update(CityDto cityDto);
}
