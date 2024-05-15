package jp.co.toshiba.ppocph.controller;

import static com.opensymphony.xwork2.Action.ERROR;
import static com.opensymphony.xwork2.Action.LOGIN;
import static com.opensymphony.xwork2.Action.NONE;
import static com.opensymphony.xwork2.Action.SUCCESS;

import java.util.List;

import javax.annotation.Resource;

import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.InterceptorRef;
import org.apache.struts2.convention.annotation.Namespace;
import org.apache.struts2.convention.annotation.Result;
import org.apache.struts2.convention.annotation.Results;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;

import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.ActionSupport;

import jp.co.toshiba.ppocph.common.PgCrowdURLConstants;
import jp.co.toshiba.ppocph.dto.CityDto;
import jp.co.toshiba.ppocph.dto.DistrictDto;
import jp.co.toshiba.ppocph.service.ICityService;
import jp.co.toshiba.ppocph.service.IDistrictService;
import jp.co.toshiba.ppocph.utils.Pagination;
import jp.co.toshiba.ppocph.utils.ResultDto;
import lombok.Getter;
import lombok.Setter;

/**
 * 都市管理ハンドラ
 *
 * @author ArkamaHozota
 * @since 1.00beta
 */
@Getter
@Setter
@Namespace(PgCrowdURLConstants.URL_CITY_NAMESPACE)
@Results({ @Result(name = SUCCESS, location = "/templates/city-pages.ftl"),
		@Result(name = ERROR, location = "/templates/system-error.ftl"),
		@Result(name = NONE, type = "json", params = { "root", "responsedJsondata" }),
		@Result(name = LOGIN, location = "/templates/admin-login.ftl") })
@Controller
public class CityHandler extends ActionSupport {

	private static final long serialVersionUID = -6017782752547971104L;

	/**
	 * 都市サービスインターフェス
	 */
	@Resource
	private ICityService iCityService;

	/**
	 * 地域サービスインターフェス
	 */
	@Resource
	private IDistrictService iDistrictService;

	/**
	 * JSONリスポンス
	 */
	private transient ResultDto<? extends Object> responsedJsondata;

	/**
	 * 情報転送クラス
	 */
	private final CityDto cityDto = new CityDto();

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

	/**
	 * 名称重複チェック
	 *
	 * @return String
	 */
	@Action(PgCrowdURLConstants.URL_CHECK_NAME)
	public String checkDuplicated() {
		final String nameVal = ActionContext.getContext().getServletRequest().getParameter("nameVal");
		final String districtId2 = ActionContext.getContext().getServletRequest().getParameter("districtId");
		final ResultDto<String> checkDuplicated = this.iCityService.checkDuplicated(nameVal,
				Long.parseLong(districtId2));
		this.setResponsedJsondata(checkDuplicated);
		return NONE;
	}

	/**
	 * 編集権限チェック
	 *
	 * @return String
	 */
	@PreAuthorize("hasAuthority('city%edition')")
	@Action(PgCrowdURLConstants.URL_CHECK_EDITION)
	public String checkEdition() {
		this.setResponsedJsondata(ResultDto.successWithoutData());
		return NONE;
	}

	/**
	 * getter for cityDto
	 *
	 * @return CityDto
	 */
	private CityDto getCityDto() {
		this.cityDto.setId(this.getId());
		this.cityDto.setName(this.getName());
		this.cityDto.setPopulation(this.getPopulation());
		this.cityDto.setPronunciation(this.getPronunciation());
		this.cityDto.setDistrictId(this.getDistrictId());
		this.cityDto.setDistrictName(this.getDistrictName());
		this.cityDto.setCityFlag(this.getCityFlag());
		return this.cityDto;
	}

	/**
	 * 地域一覧を取得する
	 *
	 * @return String
	 */
	@Action(PgCrowdURLConstants.URL_DISTRICT_LIST)
	public String getDistricts() {
		final String cityId = ActionContext.getContext().getServletRequest().getParameter("cityId");
		final List<DistrictDto> districtsByCityId = this.iDistrictService.getDistrictsByCityId(cityId);
		this.setResponsedJsondata(ResultDto.successWithData(districtsByCityId));
		return NONE;
	}

	/**
	 * 都市情報を削除する
	 *
	 * @return String
	 */
	@PreAuthorize("hasAuthority('city%edition')")
	@Action(PgCrowdURLConstants.URL_INFO_DELETE)
	public String infoDelete() {
		final String cityId = ActionContext.getContext().getServletRequest().getParameter("cityId");
		final ResultDto<String> remove = this.iCityService.remove(Long.parseLong(cityId));
		this.setResponsedJsondata(remove);
		return NONE;
	}

	/**
	 * 都市情報を保存する
	 *
	 * @return String
	 */
	@PreAuthorize("hasAuthority('city%edition')")
	@Action(value = PgCrowdURLConstants.URL_INFO_INSERT, interceptorRefs = { @InterceptorRef("json") })
	public String infoSave() {
		final CityDto cityDto2 = this.getCityDto();
		this.iCityService.save(cityDto2);
		this.setResponsedJsondata(ResultDto.successWithoutData());
		return NONE;
	}

	/**
	 * 都市情報を更新する
	 *
	 * @return String
	 */
	@PreAuthorize("hasAuthority('city%edition')")
	@Action(value = PgCrowdURLConstants.URL_INFO_UPDATE, interceptorRefs = { @InterceptorRef("json") })
	public String infoUpdate() {
		final CityDto cityDto2 = this.getCityDto();
		final ResultDto<String> update = this.iCityService.update(cityDto2);
		this.setResponsedJsondata(update);
		return NONE;
	}

	/**
	 * 情報一覧画面初期表示する
	 *
	 * @return String
	 */
	@PreAuthorize("hasAuthority('city%retrieve')")
	@Action(PgCrowdURLConstants.URL_PAGINATION)
	public String pagination() {
		final String pageNum = ActionContext.getContext().getServletRequest().getParameter("pageNum");
		final String keyword = ActionContext.getContext().getServletRequest().getParameter("keyword");
		final Pagination<CityDto> cities = this.iCityService.getCitiesByKeyword(Integer.parseInt(pageNum), keyword);
		this.setResponsedJsondata(ResultDto.successWithData(cities));
		return NONE;
	}
}
