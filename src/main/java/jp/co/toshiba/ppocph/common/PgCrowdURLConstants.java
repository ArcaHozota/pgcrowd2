package jp.co.toshiba.ppocph.common;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

/**
 * プロジェクトURLコンスタント
 *
 * @author ArkamaHozota
 * @since 1.00beta
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class PgCrowdURLConstants {

	public static final String URL_CATEGORY_NAMESPACE = "/pgcrowd/category";

	public static final String URL_CITY_NAMESPACE = "/pgcrowd/city";

	public static final String URL_DISTRICT_NAMESPACE = "/pgcrowd/district";

	public static final String URL_EMPLOYEE_NAMESPACE = "/pgcrowd/employee";

	public static final String URL_ROLE_NAMESPACE = "/pgcrowd/role";

	public static final String URL_SVG_NAMESPACE = "/pgcrowd/svgImages";

	public static final String URL_MAINMENU_ICONS = "getIcons";

	public static final String URL_INIT_TEMPLATE = "initial";

	public static final String URL_CHECK_NAME = "check";

	public static final String URL_CHECK_EDITION = "checkEdition";

	public static final String URL_CHECK_DELETE = "checkDelete";

	public static final String URL_TO_LOGIN = "login";

	public static final String URL_LOG_OUT = "logout";

	public static final String URL_LOGIN = "doLogin";

	public static final String URL_REGISTER = "toroku";

	public static final String URL_TO_MAINMENU = "toMainmenu";

	public static final String URL_TO_ADDITION = "toAddition";

	public static final String URL_TO_EDITION = "toEdition";

	public static final String URL_TO_PAGES = "toPages";

	public static final String URL_PAGINATION = "pagination";

	public static final String URL_INFO_DELETE = "infoDelete";

	public static final String URL_INFO_INSERT = "infoSave";

	public static final String URL_INFO_UPDATE = "infoUpdate";

	public static final String URL_AUTH_LIST = "getAuthlist";

	public static final String URL_AUTH_ASSIGNED = "getAssignedAuth";

	public static final String URL_DO_ASSIGNMENT = "authAssignment";

	public static final String URL_MENU_INITIAL = "menuInitial";

	public static final String URL_TO_DISTRICTS = "toDistricts";

	public static final String URL_TO_CITIES = "toCities";

	public static final String URL_DISTRICT_LIST = "getDistrictlist";

	public static final String URL_CITY_FLAGS = "getCityFlags";

	public static final String URL_DISTRICT_FLAGS = "getFlags";

	public static final String URL_STATIC_RESOURCE = "/static/**";
}
