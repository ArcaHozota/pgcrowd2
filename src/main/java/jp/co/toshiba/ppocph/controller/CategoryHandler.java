package jp.co.toshiba.ppocph.controller;

import static com.opensymphony.xwork2.Action.ERROR;
import static com.opensymphony.xwork2.Action.LOGIN;
import static com.opensymphony.xwork2.Action.NONE;
import static com.opensymphony.xwork2.Action.SUCCESS;

import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Namespace;
import org.apache.struts2.convention.annotation.Result;
import org.apache.struts2.convention.annotation.Results;
import org.springframework.stereotype.Controller;

import com.opensymphony.xwork2.ActionSupport;

import jp.co.toshiba.ppocph.common.PgCrowdURLConstants;
import jp.co.toshiba.ppocph.utils.ResultDto;
import lombok.Getter;
import lombok.Setter;

/**
 * 分類管理ハンドラ
 *
 * @author ArkamaHozota
 * @since 1.00beta
 */
@Getter
@Setter
@Namespace(PgCrowdURLConstants.URL_CATEGORY_NAMESPACE)
@Results({ @Result(name = SUCCESS, location = "/templates/categorykanri.ftl"),
		@Result(name = ERROR, location = "/templates/system-error.ftl"),
		@Result(name = NONE, type = "json", params = { "root", "responsedJsondata" }),
		@Result(name = LOGIN, location = "/templates/admin-login.ftl") })
@Controller
public class CategoryHandler extends ActionSupport {

	private static final long serialVersionUID = -6017782752547971104L;

	/**
	 * JSONリスポンス
	 */
	private transient ResultDto<? extends Object> responsedJsondata;

	/**
	 * 分類管理画面初期表示
	 *
	 * @return String
	 */
	@Action(PgCrowdURLConstants.URL_INIT_TEMPLATE)
	public String initial() {
		return SUCCESS;
	}

	/**
	 * メニュー管理画面初期表示
	 *
	 * @return String
	 */
	@Action(value = PgCrowdURLConstants.URL_MENU_INITIAL, results = {
			@Result(name = SUCCESS, location = "/templates/menukanri.ftl") })
	public String menuInitial() {
		return SUCCESS;
	}

	/**
	 * 都市情報画面初期表示
	 *
	 * @return String
	 */
	@Action(value = PgCrowdURLConstants.URL_TO_CITIES, results = {
			@Result(name = SUCCESS, location = "/templates/city-pages.ftl") })
	public String toCities() {
		return SUCCESS;
	}

	/**
	 * 地域情報画面初期表示
	 *
	 * @return String
	 */
	@Action(value = PgCrowdURLConstants.URL_TO_DISTRICTS, results = {
			@Result(name = SUCCESS, location = "/templates/district-pages.ftl") })
	public String toDistricts() {
		return SUCCESS;
	}

	/**
	 * メインメニューへ移動する
	 *
	 * @return String
	 */
	@Action(value = PgCrowdURLConstants.URL_TO_MAINMENU, results = {
			@Result(name = SUCCESS, location = "/templates/mainmenu.ftl") })
	public String toMainmenu() {
		return SUCCESS;
	}
}
