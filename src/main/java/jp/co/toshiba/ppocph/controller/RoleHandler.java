package jp.co.toshiba.ppocph.controller;

import static com.opensymphony.xwork2.Action.ERROR;
import static com.opensymphony.xwork2.Action.LOGIN;
import static com.opensymphony.xwork2.Action.NONE;
import static com.opensymphony.xwork2.Action.SUCCESS;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
import jp.co.toshiba.ppocph.dto.AuthorityDto;
import jp.co.toshiba.ppocph.dto.RoleDto;
import jp.co.toshiba.ppocph.service.IRoleService;
import jp.co.toshiba.ppocph.utils.Pagination;
import jp.co.toshiba.ppocph.utils.ResultDto;
import lombok.Getter;
import lombok.Setter;

/**
 * 役割情報処理ハンドラ
 *
 * @author ArkamaHozota
 * @since 1.00beta
 */
@Getter
@Setter
@Namespace(PgCrowdURLConstants.URL_ROLE_NAMESPACE)
@Results({ @Result(name = SUCCESS, location = "/templates/role-pages.ftl"),
		@Result(name = ERROR, location = "/templates/system-error.ftl"),
		@Result(name = NONE, type = "json", params = { "root", "responsedJsondata" }),
		@Result(name = LOGIN, location = "/templates/admin-login.ftl") })
@Controller
public class RoleHandler extends ActionSupport {

	private static final long serialVersionUID = 7483637181412284924L;

	/**
	 * 役割サービスインターフェス
	 */
	@Resource
	private IRoleService iRoleService;

	/**
	 * JSONリスポンス
	 */
	private transient ResultDto<? extends Object> responsedJsondata;

	/**
	 * 情報転送クラス
	 */
	private final RoleDto roleDto = new RoleDto();

	/**
	 * ID
	 */
	private String id;

	/**
	 * 名称
	 */
	private String name;

	/**
	 * 権限IDリスト
	 */
	private List<String> authIds;

	/**
	 * 役割IDリスト
	 */
	private List<String> roleIds;

	/**
	 * 削除権限チェック
	 *
	 * @return String
	 */
	@PreAuthorize("hasAuthority('role%delete')")
	@Action(PgCrowdURLConstants.URL_CHECK_DELETE)
	public String checkDelete() {
		this.setResponsedJsondata(ResultDto.successWithoutData());
		return NONE;
	}

	/**
	 * 名称重複チェック
	 *
	 * @return String
	 */
	@Action(PgCrowdURLConstants.URL_CHECK_NAME)
	public String checkDuplicated() {
		final String roleName = ActionContext.getContext().getServletRequest().getParameter("roleName");
		final ResultDto<String> checkDuplicated = this.iRoleService.checkDuplicated(roleName);
		this.setResponsedJsondata(checkDuplicated);
		return NONE;
	}

	/**
	 * 編集権限チェック
	 *
	 * @return String
	 */
	@PreAuthorize("hasAuthority('role%edition')")
	@Action(PgCrowdURLConstants.URL_CHECK_EDITION)
	public String checkEdition() {
		this.setResponsedJsondata(ResultDto.successWithoutData());
		return NONE;
	}

	/**
	 * 権限情報を更新する
	 *
	 * @return String
	 */
	@PreAuthorize("hasAuthority('role%edition')")
	@Action(value = PgCrowdURLConstants.URL_DO_ASSIGNMENT, interceptorRefs = { @InterceptorRef("json") })
	public String doAssignment() {
		final List<String> authIds2 = this.getAuthIds();
		final List<String> roleIds2 = this.getRoleIds();
		final Map<String, List<String>> paramMaps = new HashMap<>();
		paramMaps.put("authIds", authIds2);
		paramMaps.put("roleIds", roleIds2);
		final ResultDto<String> doAssignment = this.iRoleService.doAssignment(paramMaps);
		this.setResponsedJsondata(doAssignment);
		return NONE;
	}

	/**
	 * 付与された権限情報を取得する
	 *
	 * @return String
	 */
	@PreAuthorize("hasAuthority('role%edition')")
	@Action(PgCrowdURLConstants.URL_AUTH_ASSIGNED)
	public String getAssignedAuths() {
		final String fuyoId = ActionContext.getContext().getServletRequest().getParameter("fuyoId");
		final List<String> authIdsById = this.iRoleService.getAuthIdsById(Long.parseLong(fuyoId));
		this.setResponsedJsondata(ResultDto.successWithData(authIdsById));
		return NONE;
	}

	/**
	 * 権限情報を取得する
	 *
	 * @return String
	 */
	@PreAuthorize("hasAuthority('role%edition')")
	@Action(PgCrowdURLConstants.URL_AUTH_LIST)
	public String getAuths() {
		final List<AuthorityDto> authList = this.iRoleService.getAuthList();
		this.setResponsedJsondata(ResultDto.successWithData(authList));
		return NONE;
	}

	/**
	 * getter for roleDto
	 *
	 * @return RoleDto
	 */
	private RoleDto getRoleDto() {
		this.roleDto.setId(this.getId());
		this.roleDto.setName(this.getName());
		return this.roleDto;
	}

	/**
	 * 役割情報を削除する
	 *
	 * @return String
	 */
	@PreAuthorize("hasAuthority('role%delete')")
	@Action(PgCrowdURLConstants.URL_INFO_DELETE)
	public String infoDelete() {
		final String roleId = ActionContext.getContext().getServletRequest().getParameter("id");
		final ResultDto<String> remove = this.iRoleService.remove(Long.parseLong(roleId));
		this.setResponsedJsondata(remove);
		return NONE;
	}

	/**
	 * 役割情報を保存する
	 *
	 * @return String
	 */
	@PreAuthorize("hasAuthority('role%edition')")
	@Action(value = PgCrowdURLConstants.URL_INFO_INSERT, interceptorRefs = { @InterceptorRef("json") })
	public String infoSave() {
		final RoleDto roleDto2 = this.getRoleDto();
		this.iRoleService.save(roleDto2);
		this.setResponsedJsondata(ResultDto.successWithoutData());
		return NONE;
	}

	/**
	 * 役割情報を更新する
	 *
	 * @return String
	 */
	@PreAuthorize("hasAuthority('role%edition')")
	@Action(value = PgCrowdURLConstants.URL_INFO_UPDATE, interceptorRefs = { @InterceptorRef("json") })
	public String infoUpdate() {
		final RoleDto roleDto2 = this.getRoleDto();
		final ResultDto<String> update = this.iRoleService.update(roleDto2);
		this.setResponsedJsondata(update);
		return NONE;
	}

	/**
	 * 情報一覧画面初期表示する
	 *
	 * @return String
	 */
	@PreAuthorize("hasAuthority('role%retrieve')")
	@Action(PgCrowdURLConstants.URL_PAGINATION)
	public String pagination() {
		final String pageNum = ActionContext.getContext().getServletRequest().getParameter("pageNum");
		final String keyword = ActionContext.getContext().getServletRequest().getParameter("keyword");
		final Pagination<RoleDto> roleDtos = this.iRoleService.getRolesByKeyword(Integer.parseInt(pageNum), keyword);
		this.setResponsedJsondata(ResultDto.successWithData(roleDtos));
		return NONE;
	}

	/**
	 * 情報一覧画面へ移動する
	 *
	 * @return String
	 */
	@PreAuthorize("hasAuthority('role%retrieve')")
	@Action(PgCrowdURLConstants.URL_TO_PAGES)
	public String toPages() {
		return SUCCESS;
	}
}
