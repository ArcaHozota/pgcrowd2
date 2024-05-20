package jp.co.toshiba.ppocph.config;

import java.io.IOException;
import java.security.SecureRandom;
import java.util.Base64;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpServletResponseWrapper;

import org.springframework.web.filter.GenericFilterBean;

/**
 * 任意CSPNonce生成フィルタ
 *
 * @author Bozhio
 * @since 1.43
 */
public class CSPNonceFilter extends GenericFilterBean {
	// Wrapper to fill the nonce value
	public static class CSPNonceResponseWrapper extends HttpServletResponseWrapper {
		private final String nonce;

		public CSPNonceResponseWrapper(final HttpServletResponse response, final String nonce) {
			super(response);
			this.nonce = nonce;
		}

		@Override
		public void setHeader(final String name, final String value) {
			if ("Content-Security-Policy".equals(name) && (value != null)) {
				super.setHeader(name, value.replace("{nonce}", this.nonce));
			} else {
				super.setHeader(name, value);
			}
		}
	}

	private static final int NONCE_SIZE = 32; // Recommended size is at least 128 bits/16 bytes
	private static final String CSP_NONCE_ATTRIBUTE = "cspNonce";

	private final SecureRandom secureRandom = new SecureRandom();

	@Override
	public void doFilter(final ServletRequest req, final ServletResponse res, final FilterChain chain)
			throws IOException, ServletException {
		final HttpServletRequest request = (HttpServletRequest) req;
		final HttpServletResponse response = (HttpServletResponse) res;

		final byte[] nonceArray = new byte[NONCE_SIZE];
		this.secureRandom.nextBytes(nonceArray);
		final String nonce = Base64.getEncoder().encodeToString(nonceArray);

		request.setAttribute(CSP_NONCE_ATTRIBUTE, nonce);
		chain.doFilter(request, new CSPNonceResponseWrapper(response, nonce));
	}
}
