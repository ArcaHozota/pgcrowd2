package jp.co.toshiba.ppocph.controller;

import static com.opensymphony.xwork2.Action.ERROR;
import static com.opensymphony.xwork2.Action.LOGIN;
import static com.opensymphony.xwork2.Action.NONE;
import static com.opensymphony.xwork2.Action.SUCCESS;

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
import jp.co.toshiba.ppocph.dto.DistrictDto;
import jp.co.toshiba.ppocph.service.IDistrictService;
import jp.co.toshiba.ppocph.utils.Pagination;
import jp.co.toshiba.ppocph.utils.ResultDto;
import lombok.Getter;
import lombok.Setter;

/**
 * 地域管理ハンドラ
 *
 * @author ArkamaHozota
 * @since 1.00beta
 */
@Getter
@Setter
@Namespace(PgCrowdURLConstants.URL_DISTRICT_NAMESPACE)
@Results({ @Result(name = SUCCESS, location = "/templates/district-pages.ftl"),
		@Result(name = ERROR, location = "/templates/system-error.ftl"),
		@Result(name = NONE, type = "json", params = { "root", "responsedJsondata" }),
		@Result(name = LOGIN, location = "/templates/admin-login.ftl") })
@Controller
public class DistrictHandler extends ActionSupport {

	private static final long serialVersionUID = 646905610745129665L;

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
	private final DistrictDto districtDto = new DistrictDto();

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

	/**
	 * 編集権限チェック
	 *
	 * @return String
	 */
	@PreAuthorize("hasAuthority('district%edition')")
	@Action(PgCrowdURLConstants.URL_CHECK_EDITION)
	public String checkEdition() {
		this.setResponsedJsondata(ResultDto.successWithoutData());
		return NONE;
	}

	/**
	 * getter for districtDto
	 *
	 * @return DistrictDto
	 */
	private DistrictDto getDistrictDto() {
		this.districtDto.setId(this.getId());
		this.districtDto.setName(this.getName());
		this.districtDto.setChiho(this.getChiho());
		this.districtDto.setShutoId(this.getShutoId());
		this.districtDto.setShutoName(this.getShutoName());
		this.districtDto.setPopulation(this.getPopulation());
		this.districtDto.setDistrictFlag(this.getDistrictFlag());
		return this.districtDto;
	}

	/**
	 * 地域情報を更新する
	 *
	 * @return String
	 */
	@PreAuthorize("hasAuthority('district%edition')")
	@Action(value = PgCrowdURLConstants.URL_INFO_UPDATE, interceptorRefs = { @InterceptorRef("json") })
	public String infoUpdate() {
		final DistrictDto districtDto2 = this.getDistrictDto();
		final ResultDto<String> update = this.iDistrictService.update(districtDto2);
		this.setResponsedJsondata(update);
		return NONE;
	}

	/**
	 * 情報一覧画面初期表示する
	 *
	 * @return String
	 */
	@PreAuthorize("hasAuthority('district%retrieve')")
	@Action(PgCrowdURLConstants.URL_PAGINATION)
	public String pagination() {
		final String pageNum = ActionContext.getContext().getServletRequest().getParameter("pageNum");
		final String keyword = ActionContext.getContext().getServletRequest().getParameter("keyword");
		final Pagination<DistrictDto> districtsByKeyword = this.iDistrictService
				.getDistrictsByKeyword(Integer.parseInt(pageNum), keyword);
		this.setResponsedJsondata(ResultDto.successWithData(districtsByKeyword));
		return NONE;
	}
}
