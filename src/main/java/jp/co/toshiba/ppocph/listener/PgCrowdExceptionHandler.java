package jp.co.toshiba.ppocph.listener;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts2.dispatcher.DefaultDispatcherErrorHandler;
import org.springframework.security.access.AccessDeniedException;

import jp.co.toshiba.ppocph.common.PgCrowdConstants;
import jp.co.toshiba.ppocph.config.ResponseLoginDto;
import jp.co.toshiba.ppocph.utils.CommonProjectUtils;
import lombok.extern.log4j.Log4j2;

/**
 * Struts2例外処理インターセプト
 *
 * @author ArkamaHozota
 * @since 2.98
 */
@Log4j2
public final class PgCrowdExceptionHandler extends DefaultDispatcherErrorHandler {

	@Override
	protected void sendErrorResponse(final HttpServletRequest request, final HttpServletResponse response,
			final int code, final Exception exception) {
		ResponseLoginDto responseResult = null;
		try {
			// WW-1977: Only put errors in the request when code is a 500 error
			if (exception instanceof AccessDeniedException) {
				responseResult = new ResponseLoginDto(code, PgCrowdConstants.MESSAGE_SPRINGSECURITY_REQUIRED_AUTH);
			} else {
				// WW-4103: Only logs error when application error occurred, not Struts error
				responseResult = new ResponseLoginDto(code, exception.getMessage());
			}
			log.error("Exception occurred during processing request: {}", responseResult.getMessage());
			CommonProjectUtils.renderString(response, responseResult);
		} catch (final IllegalStateException ise) {
			// Log illegalstate instead of passing unrecoverable exception to calling thread
			log.warn("Unable to send error response, code: {}; isCommited: {};", code, response.isCommitted(), ise);
		}
	}
}
