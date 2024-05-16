package jp.co.toshiba.ppocph.controller;

import static com.opensymphony.xwork2.Action.ERROR;
import static com.opensymphony.xwork2.Action.LOGIN;
import static com.opensymphony.xwork2.Action.NONE;
import static com.opensymphony.xwork2.Action.SUCCESS;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.InterceptorRef;
import org.apache.struts2.convention.annotation.Namespace;
import org.apache.struts2.convention.annotation.Result;
import org.apache.struts2.convention.annotation.Results;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;

import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.ActionSupport;

import jp.co.toshiba.ppocph.common.PgCrowdConstants;
import jp.co.toshiba.ppocph.common.PgCrowdURLConstants;
import jp.co.toshiba.ppocph.dto.EmployeeDto;
import jp.co.toshiba.ppocph.dto.RoleDto;
import jp.co.toshiba.ppocph.service.IEmployeeService;
import jp.co.toshiba.ppocph.service.IRoleService;
import jp.co.toshiba.ppocph.utils.CommonProjectUtils;
import jp.co.toshiba.ppocph.utils.Pagination;
import jp.co.toshiba.ppocph.utils.ResultDto;
import lombok.Getter;
import lombok.Setter;

/**
 * 社員情報処理ハンドラ
 *
 * @author ArkamaHozota
 * @since 1.00beta
 */
@Getter
@Setter
@Namespace(PgCrowdURLConstants.URL_EMPLOYEE_NAMESPACE)
@Results({ @Result(name = SUCCESS, location = "/templates/admin-pages.ftl"),
		@Result(name = ERROR, location = "/templates/system-error.ftl"),
		@Result(name = NONE, type = "json", params = { "root", "responsedJsondata" }),
		@Result(name = LOGIN, location = "/templates/logintoroku.ftl") })
@Controller
public class EmployeeHandler extends ActionSupport {

	private static final long serialVersionUID = -6017782752547971104L;

	/**
	 * 社員サービスインターフェス
	 */
	@Resource
	private IEmployeeService iEmployeeService;

	/**
	 * 社員サービスインターフェス
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
	private final EmployeeDto employeeDto = new EmployeeDto();

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

	/**
	 * 削除権限チェック
	 *
	 * @return String
	 */
	@PreAuthorize("hasAuthority('employee%delete')")
	@Action(PgCrowdURLConstants.URL_CHECK_DELETE)
	public String checkDelete() {
		this.setResponsedJsondata(ResultDto.successWithoutData());
		return NONE;
	}

	/**
	 * getter for employeeDto
	 *
	 * @return EmployeeDto
	 */
	private EmployeeDto getEmployeeDto() {
		this.employeeDto.setId(this.getId());
		this.employeeDto.setLoginAccount(this.getLoginAccount());
		this.employeeDto.setUsername(this.getUsername());
		this.employeeDto.setPassword(this.getPassword());
		this.employeeDto.setEmail(this.getEmail());
		this.employeeDto.setDateOfBirth(this.getDateOfBirth());
		this.employeeDto.setRoleId(this.getRoleId());
		return this.employeeDto;
	}

	/**
	 * 社員情報を削除する
	 *
	 * @return String
	 */
	@PreAuthorize("hasAuthority('employee%delete')")
	@Action(PgCrowdURLConstants.URL_INFO_DELETE)
	public String infoDelete() {
		final String userId = ActionContext.getContext().getServletRequest().getParameter("userId");
		this.iEmployeeService.remove(Long.parseLong(userId));
		this.setResponsedJsondata(ResultDto.successWithoutData());
		return NONE;
	}

	/**
	 * 社員情報を保存する
	 *
	 * @return String
	 */
	@PreAuthorize("hasAuthority('employee%edition')")
	@Action(value = PgCrowdURLConstants.URL_INFO_INSERT, interceptorRefs = { @InterceptorRef("json") })
	public String infoSave() {
		final EmployeeDto employeeDto2 = this.getEmployeeDto();
		this.iEmployeeService.save(employeeDto2);
		this.setResponsedJsondata(ResultDto.successWithoutData());
		return NONE;
	}

	/**
	 * 社員情報を更新する
	 *
	 * @return String
	 */
	@PreAuthorize("hasAuthority('employee%retrieve')")
	@Action(value = PgCrowdURLConstants.URL_INFO_UPDATE, interceptorRefs = { @InterceptorRef("json") })
	public String infoUpdate() {
		final EmployeeDto employeeDto2 = this.getEmployeeDto();
		final ResultDto<String> updateInfo = this.iEmployeeService.update(employeeDto2);
		this.setResponsedJsondata(updateInfo);
		return NONE;
	}

	/**
	 * ログイン画面初期表示する
	 *
	 * @return String
	 */
	@Action(PgCrowdURLConstants.URL_TO_LOGIN)
	public String initial() {
		return LOGIN;
	}

	/**
	 * 情報一覧画面初期表示する
	 *
	 * @return String
	 */
	@PreAuthorize("hasAuthority('employee%retrieve')")
	@Action(PgCrowdURLConstants.URL_PAGINATION)
	public String pagination() {
		final HttpServletRequest servletRequest = ActionContext.getContext().getServletRequest();
		final String pageNum = servletRequest.getParameter("pageNum");
		final String keyword = servletRequest.getParameter("keyword");
		final String userId = servletRequest.getParameter("userId");
		final String authChkFlag = servletRequest.getParameter("authChkFlag");
		final Pagination<EmployeeDto> employees = this.iEmployeeService.getEmployeesByKeyword(Integer.parseInt(pageNum),
				keyword, Long.parseLong(userId), authChkFlag);
		this.setResponsedJsondata(ResultDto.successWithData(employees));
		return NONE;
	}

	/**
	 * 情報追加画面へ移動する
	 *
	 * @return String
	 */
	@PreAuthorize("hasAuthority('employee%edition')")
	@Action(value = PgCrowdURLConstants.URL_TO_ADDITION, results = {
			@Result(name = SUCCESS, location = "/templates/admin-addinfo.ftl") })
	public String toAddition() {
		final List<RoleDto> roleDtos = this.iRoleService.getRolesByEmployeeId(null);
		ActionContext.getContext().put(PgCrowdConstants.ATTRNAME_EMPLOYEE_ROLES, roleDtos);
		return SUCCESS;
	}

	/**
	 * 情報更新画面へ移動する
	 *
	 * @return String
	 */
	@PreAuthorize("hasAuthority('employee%retrieve')")
	@Action(value = PgCrowdURLConstants.URL_TO_EDITION, results = {
			@Result(name = SUCCESS, location = "/templates/admin-editinfo.ftl") })
	public String toEdition() {
		final HttpServletRequest servletRequest = ActionContext.getContext().getServletRequest();
		final String editId = servletRequest.getParameter("editId");
		final String authChkFlag = servletRequest.getParameter("authChkFlag");
		String pageNum = servletRequest.getParameter("pageNum");
		if (!CommonProjectUtils.isDigital(pageNum)) {
			pageNum = "1";
		}
		final EmployeeDto employeeDto2 = this.iEmployeeService.getEmployeeById(editId);
		final ActionContext actionContext = ActionContext.getContext();
		if (Boolean.FALSE.equals(Boolean.valueOf(authChkFlag))) {
			final RoleDto roleDto = this.iRoleService.getRoleById(Long.parseLong(employeeDto2.getRoleId()));
			final List<RoleDto> defaultRoleDtos = new ArrayList<>();
			defaultRoleDtos.add(roleDto);
			actionContext.put(PgCrowdConstants.ATTRNAME_EMPLOYEE_ROLES, defaultRoleDtos);
		} else {
			final List<RoleDto> roleDtos = this.iRoleService.getRolesByEmployeeId(editId);
			actionContext.put(PgCrowdConstants.ATTRNAME_EMPLOYEE_ROLES, roleDtos);
		}
		actionContext.put(PgCrowdConstants.ATTRNAME_EDITED_INFO, employeeDto2);
		actionContext.put(PgCrowdConstants.ATTRNAME_PAGE_NUMBER, pageNum);
		return SUCCESS;
	}

	/**
	 * 情報一覧画面へ移動する
	 *
	 * @return String
	 */
	@PreAuthorize("hasAuthority('employee%retrieve')")
	@Action(PgCrowdURLConstants.URL_TO_PAGES)
	public String toPages() {
		String pageNum = ActionContext.getContext().getServletRequest().getParameter("pageNum");
		if (!CommonProjectUtils.isDigital(pageNum)) {
			pageNum = String.valueOf(1L);
		}
		ActionContext.getContext().put(PgCrowdConstants.ATTRNAME_PAGE_NUMBER, pageNum);
		return SUCCESS;
	}

	/**
	 * 登録する
	 *
	 * @return String
	 */
	@Action(PgCrowdURLConstants.URL_REGISTER)
	public String toroku() {
		final HttpServletRequest servletRequest = ActionContext.getContext().getServletRequest();
		final String inputEmail = servletRequest.getParameter("email");
		final String inputPassword = servletRequest.getParameter("password");
		final String inputDate = servletRequest.getParameter("dateOfBirth");
		final EmployeeDto employeeDto2 = new EmployeeDto();
		employeeDto2.setEmail(inputEmail);
		employeeDto2.setPassword(inputPassword);
		employeeDto2.setDateOfBirth(inputDate);
		final Boolean toroku = this.iEmployeeService.register(employeeDto2);
		final ActionContext actionContext = ActionContext.getContext();
		if (Boolean.FALSE.equals(toroku)) {
			actionContext.put("torokuMsg", PgCrowdConstants.MESSAGE_TOROKU_FAILURE);
		} else {
			actionContext.put("torokuMsg", PgCrowdConstants.MESSAGE_TOROKU_SUCCESS);
		}
		actionContext.put("registeredEmail", inputEmail);
		return LOGIN;
	}
}
