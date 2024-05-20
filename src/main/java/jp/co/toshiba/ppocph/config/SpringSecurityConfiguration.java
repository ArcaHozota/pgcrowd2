package jp.co.toshiba.ppocph.config;

import java.util.UUID;

import javax.annotation.Resource;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder.BCryptVersion;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.csrf.CookieCsrfTokenRepository;
import org.springframework.security.web.header.HeaderWriterFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.web.bind.annotation.RequestMethod;

import jp.co.toshiba.ppocph.common.PgCrowdConstants;
import jp.co.toshiba.ppocph.common.PgCrowdURLConstants;
import jp.co.toshiba.ppocph.listener.PgCrowdUserDetailsService;
import jp.co.toshiba.ppocph.utils.CommonProjectUtils;
import lombok.extern.log4j.Log4j2;

/**
 * SpringSecurity配置クラス
 *
 * @author ArkamaHozota
 * @since 1.00beta
 */
@Log4j2
@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class SpringSecurityConfiguration {

	/**
	 * ログインサービス
	 */
	@Resource
	private PgCrowdUserDetailsService pgCrowd2UserDetailsService;

	@Bean
	@Order(1)
	protected AuthenticationManager authenticationManager(final AuthenticationManagerBuilder auth) {
		return auth.authenticationProvider(this.daoAuthenticationProvider()).getObject();
	}

	@Bean
	@Order(0)
	protected DaoAuthenticationProvider daoAuthenticationProvider() {
		final PgCrowdDaoAuthenticationProvider provider = new PgCrowdDaoAuthenticationProvider();
		provider.setUserDetailsService(this.pgCrowd2UserDetailsService);
		provider.setPasswordEncoder(new BCryptPasswordEncoder(BCryptVersion.$2Y, 7));
		return provider;
	}

	@Bean
	@Order(2)
	protected SecurityFilterChain filterChain(final HttpSecurity http) throws Exception {
		http.addFilterBefore(new CSPNonceFilter(), HeaderWriterFilter.class)
				.authorizeRequests(requests -> requests
						.antMatchers(PgCrowdURLConstants.URL_STATIC_RESOURCE,
								PgCrowdURLConstants.URL_EMPLOYEE_NAMESPACE.concat("/")
										.concat(PgCrowdURLConstants.URL_REGISTER))
						.permitAll().anyRequest().authenticated())
				.csrf(csrf -> csrf
						.ignoringRequestMatchers(
								new AntPathRequestMatcher(PgCrowdURLConstants.URL_STATIC_RESOURCE,
										RequestMethod.GET.toString()),
								new AntPathRequestMatcher(PgCrowdURLConstants.URL_EMPLOYEE_NAMESPACE.concat("/")
										.concat(PgCrowdURLConstants.URL_LOGIN), RequestMethod.POST.toString()),
								new AntPathRequestMatcher(PgCrowdURLConstants.URL_EMPLOYEE_NAMESPACE.concat("/")
										.concat(PgCrowdURLConstants.URL_LOG_OUT), RequestMethod.POST.toString()))
						.csrfTokenRepository(new CookieCsrfTokenRepository()))
				.exceptionHandling(
						handling -> handling.authenticationEntryPoint((request, response, authenticationException) -> {
							final ResponseLoginDto responseResult = new ResponseLoginDto(
									HttpStatus.UNAUTHORIZED.value(), authenticationException.getMessage());
							CommonProjectUtils.renderString(response, responseResult);
						}))
				.formLogin(login -> login
						.loginPage(PgCrowdURLConstants.URL_EMPLOYEE_NAMESPACE.concat("/")
								.concat(PgCrowdURLConstants.URL_TO_LOGIN))
						.loginProcessingUrl(PgCrowdURLConstants.URL_EMPLOYEE_NAMESPACE.concat("/")
								.concat(PgCrowdURLConstants.URL_LOGIN))
						.defaultSuccessUrl(PgCrowdURLConstants.URL_CATEGORY_NAMESPACE.concat("/")
								.concat(PgCrowdURLConstants.URL_TO_MAINMENU))
						.permitAll().usernameParameter("loginAcct").passwordParameter("userPswd"))
				.logout(logout -> logout
						.logoutUrl(PgCrowdURLConstants.URL_EMPLOYEE_NAMESPACE.concat("/")
								.concat(PgCrowdURLConstants.URL_LOG_OUT))
						.logoutSuccessUrl(PgCrowdURLConstants.URL_EMPLOYEE_NAMESPACE.concat("/")
								.concat(PgCrowdURLConstants.URL_TO_LOGIN)))
				.headers(headers -> headers.contentSecurityPolicy("script-src 'nonce-{nonce}' 'strict-dynamic'"))
				.rememberMe(remember -> remember.key(UUID.randomUUID().toString())
						.tokenValiditySeconds(PgCrowdConstants.DEFAULT_TOKEN_EXPIRED));
		log.info(PgCrowdConstants.MESSAGE_SPRING_SECURITY);
		return http.build();
	}
}
