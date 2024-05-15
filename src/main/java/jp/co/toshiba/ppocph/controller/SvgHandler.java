package jp.co.toshiba.ppocph.controller;

import static com.opensymphony.xwork2.Action.ERROR;
import static com.opensymphony.xwork2.Action.LOGIN;
import static com.opensymphony.xwork2.Action.NONE;
import static com.opensymphony.xwork2.Action.SUCCESS;

import java.io.IOException;
import java.io.InputStream;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts2.action.ServletResponseAware;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Namespace;
import org.apache.struts2.convention.annotation.Result;
import org.apache.struts2.convention.annotation.Results;
import org.springframework.core.io.DefaultResourceLoader;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Controller;

import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.ActionSupport;

import jp.co.toshiba.ppocph.common.PgCrowdURLConstants;
import jp.co.toshiba.ppocph.utils.CommonProjectUtils;
import jp.co.toshiba.ppocph.utils.ResultDto;
import lombok.Getter;
import lombok.Setter;

/**
 * 共通SVGイメージハンドラ
 *
 * @author ArkamaHozota
 * @since 1.00beta
 */
@Getter
@Setter
@Namespace(PgCrowdURLConstants.URL_SVG_NAMESPACE)
@Results({ @Result(name = SUCCESS, location = "/templates/mainmenu.ftl"),
		@Result(name = ERROR, location = "/templates/system-error.ftl"),
		@Result(name = NONE, type = "json", params = { "root", "responsedJsondata" }),
		@Result(name = LOGIN, location = "/templates/admin-login.ftl") })
@Controller
public class SvgHandler extends ActionSupport implements ServletResponseAware {

	private static final long serialVersionUID = -171237033831060185L;

	/**
	 * リスポンス
	 */
	private transient HttpServletResponse response;

	/**
	 * JSONリスポンス
	 */
	private transient ResultDto<? extends Object> responsedJsondata;

	/**
	 * ResourceLoader
	 */
	private final ResourceLoader resourceLoader = new DefaultResourceLoader();

	/**
	 * メインメニューアイコン取得する
	 *
	 * @param svgSource ソースパス
	 * @param response  リスポンス
	 * @throws IOException
	 */
	@Action(value = PgCrowdURLConstants.URL_MAINMENU_ICONS, results = { @Result(type = "stream") })
	public String getSvgImage() throws IOException {
		final String svgSource = ActionContext.getContext().getServletRequest().getParameter("icons");
		final Resource resource = this.getResourceLoader().getResource("classpath:/static/image/icons/" + svgSource);
		final InputStream inputStream = resource.getInputStream();
		final byte[] buffer = new byte[(int) resource.getFile().length()];
		inputStream.read(buffer);
		inputStream.close();
		this.getResponse().setContentType("image/svg+xml");
		this.getResponse().setCharacterEncoding(CommonProjectUtils.CHARSET_UTF8.name());
		final ServletOutputStream outputStream = this.getResponse().getOutputStream();
		outputStream.write(buffer);
		outputStream.flush();
		outputStream.close();
		return null;
	}

	/**
	 * 都市アイコン取得する
	 *
	 * @param svgSource ソースパス
	 * @param response  リスポンス
	 * @throws IOException
	 */
	@Action(value = PgCrowdURLConstants.URL_CITY_FLAGS, results = { @Result(type = "stream") })
	public String getSvgImageCity() throws IOException {
		final String svgSource = ActionContext.getContext().getServletRequest().getParameter("flags");
		final Resource resource = this.getResourceLoader().getResource("classpath:/static/image/flags/" + svgSource);
		final InputStream inputStream = resource.getInputStream();
		final byte[] buffer = new byte[(int) resource.getFile().length()];
		inputStream.read(buffer);
		inputStream.close();
		this.getResponse().setContentType("image/svg+xml");
		this.getResponse().setCharacterEncoding(CommonProjectUtils.CHARSET_UTF8.name());
		final ServletOutputStream outputStream = this.getResponse().getOutputStream();
		outputStream.write(buffer);
		outputStream.flush();
		outputStream.close();
		return null;
	}

	/**
	 * 地域アイコン取得する
	 *
	 * @param svgSource ソースパス
	 * @param response  リスポンス
	 * @throws IOException
	 */
	@Action(value = PgCrowdURLConstants.URL_DISTRICT_FLAGS, results = { @Result(type = "stream") })
	public String getSvgImageDistrict() throws IOException {
		final String svgSource = ActionContext.getContext().getServletRequest().getParameter("flags");
		final Resource resource = this.getResourceLoader()
				.getResource("classpath:/static/image/flags/prefectures/" + svgSource);
		final InputStream inputStream = resource.getInputStream();
		final byte[] buffer = new byte[(int) resource.getFile().length()];
		inputStream.read(buffer);
		inputStream.close();
		this.getResponse().setContentType("image/svg+xml");
		this.getResponse().setCharacterEncoding(CommonProjectUtils.CHARSET_UTF8.name());
		final ServletOutputStream outputStream = this.getResponse().getOutputStream();
		outputStream.write(buffer);
		outputStream.flush();
		outputStream.close();
		return null;
	}

	@Override
	public void withServletResponse(final HttpServletResponse response) {
		this.response = response;
	}
}
