package jp.co.toshiba.ppocph.listener;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.security.authentication.AuthenticationCredentialsNotFoundException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.InsufficientAuthenticationException;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import jp.co.toshiba.ppocph.common.PgCrowdConstants;
import jp.co.toshiba.ppocph.dto.EmployeeDto;
import jp.co.toshiba.ppocph.utils.SecondBeanUtils;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;

/**
 * ログインコントローラ(SpringSecurity関連)
 *
 * @author ArkamaHozota
 * @since 2.00
 */
@Component
@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public class PgCrowdUserDetailsService implements UserDetailsService {

	/**
	 * 社員管理マッパー
	 */
	private final EmployeeRepository employeeRepository;

	/**
	 * 社員役割連携マッパー
	 */
	private final EmployeeRoleRepository employeeRoleRepository;

	/**
	 * 役割マッパー
	 */
	private final RoleRepository roleRepository;

	/**
	 * 役割権限マッパー
	 */
	private final RoleAuthRepository roleAuthRepository;

	/**
	 * 権限マッパー
	 */
	private final AuthorityRepository authorityRepository;

	@Override
	public UserDetails loadUserByUsername(final String username) throws UsernameNotFoundException {
		final Specification<Employee> where1 = (root, query, criteriaBuilder) -> criteriaBuilder
				.equal(root.get("deleteFlg"), PgCrowdConstants.LOGIC_DELETE_INITIAL);
		final Specification<Employee> where2 = (root, query, criteriaBuilder) -> criteriaBuilder
				.equal(root.get("loginAccount"), username);
		final Specification<Employee> where3 = (root, query, criteriaBuilder) -> criteriaBuilder
				.equal(root.get("email"), username);
		final Specification<Employee> specification1 = Specification.where(where1)
				.and(Specification.where(where2).or(where3));
		final Optional<Employee> optionalEmployee = this.employeeRepository.findOne(specification1);
		if (optionalEmployee.isEmpty()) {
			throw new DisabledException(PgCrowdConstants.MESSAGE_SPRINGSECURITY_LOGINERROR1);
		}
		final Employee employee = optionalEmployee.get();
		final EmployeeDto employeeDto = new EmployeeDto();
		SecondBeanUtils.copyNullableProperties(employee, employeeDto);
		employeeDto.setId(employee.getId().toString());
		final Optional<EmployeeRole> optionalEmployeeRole = this.employeeRoleRepository.findById(employee.getId());
		if (optionalEmployeeRole.isEmpty()) {
			throw new InsufficientAuthenticationException(PgCrowdConstants.MESSAGE_SPRINGSECURITY_LOGINERROR2);
		}
		final Specification<Role> where4 = (root, query, criteriaBuilder) -> criteriaBuilder
				.equal(root.get("deleteFlg"), PgCrowdConstants.LOGIC_DELETE_INITIAL);
		final Specification<Role> where5 = (root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get("id"),
				optionalEmployeeRole.get().getRoleId());
		final Specification<Role> specification2 = Specification.where(where4).and(where5);
		final Optional<Role> optionalRole = this.roleRepository.findOne(specification2);
		if (optionalRole.isEmpty()) {
			throw new InsufficientAuthenticationException(PgCrowdConstants.MESSAGE_SPRINGSECURITY_LOGINERROR2);
		}
		final Specification<RoleAuth> where6 = (root, query, criteriaBuilder) -> criteriaBuilder
				.equal(root.get("roleId"), optionalRole.get().getId());
		final List<RoleAuth> roleAuths = this.roleAuthRepository.findAll(where6);
		if (roleAuths.isEmpty()) {
			throw new AuthenticationCredentialsNotFoundException(PgCrowdConstants.MESSAGE_SPRINGSECURITY_LOGINERROR3);
		}
		final List<Long> authIds = roleAuths.stream().map(RoleAuth::getAuthId).collect(Collectors.toList());
		final List<SimpleGrantedAuthority> authorities = this.authorityRepository.findAllById(authIds).stream()
				.map(item -> new SimpleGrantedAuthority(item.getName())).collect(Collectors.toList());
		return new SecurityAdmin(employeeDto, authorities);
	}

}
