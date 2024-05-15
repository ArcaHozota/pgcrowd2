package jp.co.toshiba.ppocph.service.impl;

import static jp.co.toshiba.ppocph.jooq.Tables.EMPLOYEES;
import static jp.co.toshiba.ppocph.jooq.Tables.EMPLOYEE_ROLE;
import static jp.co.toshiba.ppocph.jooq.Tables.ROLES;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.jooq.DSLContext;
import org.jooq.exception.NoDataFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import jp.co.toshiba.ppocph.common.OgumaProjectConstants;
import jp.co.toshiba.ppocph.config.OgumaPasswordEncoder;
import jp.co.toshiba.ppocph.dto.EmployeeDto;
import jp.co.toshiba.ppocph.exception.OgumaProjectException;
import jp.co.toshiba.ppocph.jooq.tables.records.EmployeeRoleRecord;
import jp.co.toshiba.ppocph.jooq.tables.records.EmployeesRecord;
import jp.co.toshiba.ppocph.jooq.tables.records.RolesRecord;
import jp.co.toshiba.ppocph.service.IEmployeeService;
import jp.co.toshiba.ppocph.utils.CommonProjectUtils;
import jp.co.toshiba.ppocph.utils.Pagination;
import jp.co.toshiba.ppocph.utils.ResultDto;
import jp.co.toshiba.ppocph.utils.SnowflakeUtils;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;

/**
 * 社員サービス実装クラス
 *
 * @author ArkamaHozota
 * @since 1.00beta
 */
@Service
@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public final class EmployeeServiceImpl implements IEmployeeService {

	/**
	 * Randomナンバー
	 */
	private static final Random RANDOM = new Random();

	/**
	 * 共通リポジトリ
	 */
	private final DSLContext dslContext;

	/**
	 * エンコーダ
	 */
	private final PasswordEncoder encoder = new OgumaPasswordEncoder();

	/**
	 * 日時フォマーター
	 */
	private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

	@Override
	public ResultDto<String> checkDuplicated(final String loginAccount) {
		final Integer employeNameCount = this.dslContext.selectCount().from(EMPLOYEES)
				.where(EMPLOYEES.LOGIN_ACCOUNT.eq(loginAccount)).fetchSingle().into(Integer.class);
		return employeNameCount > 0 ? ResultDto.failed(OgumaProjectConstants.MESSAGE_STRING_DUPLICATED)
				: ResultDto.successWithoutData();
	}

	@Override
	public EmployeeDto getEmployeeById(final Long id) {
		final EmployeesRecord employeesRecord = this.dslContext.selectFrom(EMPLOYEES)
				.where(EMPLOYEES.DELETE_FLG.eq(OgumaProjectConstants.LOGIC_DELETE_INITIAL)).and(EMPLOYEES.ID.eq(id))
				.fetchSingle();
		final EmployeeRoleRecord employeeRoleRecord = this.dslContext.selectFrom(EMPLOYEE_ROLE)
				.where(EMPLOYEE_ROLE.EMPLOYEE_ID.eq(id)).fetchSingle();
		return new EmployeeDto(employeesRecord.getId(), employeesRecord.getLoginAccount(),
				employeesRecord.getUsername(), OgumaProjectConstants.DEFAULT_ROLE_NAME, employeesRecord.getEmail(),
				this.formatter.format(employeesRecord.getDateOfBirth()), employeeRoleRecord.getRoleId());
	}

	@Override
	public Pagination<EmployeeDto> getEmployeesByKeyword(final Integer pageNum, final String keyword, final Long userId,
			final String authChkFlag) {
		if (Boolean.FALSE.equals(Boolean.valueOf(authChkFlag))) {
			final List<EmployeeDto> employeeDtos = new ArrayList<>();
			final EmployeesRecord employeesRecord = this.dslContext.selectFrom(EMPLOYEES)
					.where(EMPLOYEES.DELETE_FLG.eq(OgumaProjectConstants.LOGIC_DELETE_INITIAL))
					.and(EMPLOYEES.ID.eq(userId)).fetchSingle();
			final EmployeeDto employeeDto = new EmployeeDto(employeesRecord.getId(), employeesRecord.getLoginAccount(),
					employeesRecord.getUsername(), employeesRecord.getPassword(), employeesRecord.getEmail(),
					this.formatter.format(employeesRecord.getDateOfBirth()), null);
			employeeDtos.add(employeeDto);
			return Pagination.of(employeeDtos, employeeDtos.size(), pageNum, OgumaProjectConstants.DEFAULT_PAGE_SIZE);
		}
		final int offset = (pageNum - 1) * OgumaProjectConstants.DEFAULT_PAGE_SIZE;
		if (CommonProjectUtils.isEmpty(keyword)) {
			final Integer totalRecords = this.dslContext.selectCount().from(EMPLOYEES)
					.where(EMPLOYEES.DELETE_FLG.eq(OgumaProjectConstants.LOGIC_DELETE_INITIAL)).fetchSingle()
					.into(Integer.class);
			final List<EmployeesRecord> employeesRecords = this.dslContext.selectFrom(EMPLOYEES)
					.where(EMPLOYEES.DELETE_FLG.eq(OgumaProjectConstants.LOGIC_DELETE_INITIAL))
					.limit(OgumaProjectConstants.DEFAULT_PAGE_SIZE).offset(offset).fetchInto(EmployeesRecord.class);
			final List<EmployeeDto> employeeDtos = employeesRecords.stream()
					.map(item -> new EmployeeDto(item.getId(), item.getLoginAccount(), item.getUsername(),
							item.getPassword(), item.getEmail(), this.formatter.format(item.getDateOfBirth()), null))
					.toList();
			return Pagination.of(employeeDtos, totalRecords, pageNum, OgumaProjectConstants.DEFAULT_PAGE_SIZE);
		}
		final String searchStr = CommonProjectUtils.getDetailKeyword(keyword);
		final Integer totalRecords = this.dslContext.selectCount().from(EMPLOYEES)
				.where(EMPLOYEES.DELETE_FLG.eq(OgumaProjectConstants.LOGIC_DELETE_INITIAL))
				.and(EMPLOYEES.USERNAME.like(searchStr).or(EMPLOYEES.LOGIN_ACCOUNT.like(searchStr))
						.or(EMPLOYEES.EMAIL.like(searchStr)))
				.fetchSingle().into(Integer.class);
		final List<EmployeesRecord> employeesRecords = this.dslContext.selectFrom(EMPLOYEES)
				.where(EMPLOYEES.DELETE_FLG.eq(OgumaProjectConstants.LOGIC_DELETE_INITIAL))
				.and(EMPLOYEES.USERNAME.like(searchStr).or(EMPLOYEES.LOGIN_ACCOUNT.like(searchStr))
						.or(EMPLOYEES.EMAIL.like(searchStr)))
				.limit(OgumaProjectConstants.DEFAULT_PAGE_SIZE).offset(offset).fetchInto(EmployeesRecord.class);
		final List<EmployeeDto> employeeDtos = employeesRecords.stream()
				.map(item -> new EmployeeDto(item.getId(), item.getLoginAccount(), item.getUsername(),
						item.getPassword(), item.getEmail(), this.formatter.format(item.getDateOfBirth()), null))
				.toList();
		return Pagination.of(employeeDtos, totalRecords, pageNum, OgumaProjectConstants.DEFAULT_PAGE_SIZE);
	}

	/**
	 * デフォルトのアカウントを取得する
	 *
	 * @return String
	 */
	private String getRandomStr() {
		final String stry = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
		final char[] cr1 = stry.toCharArray();
		final char[] cr2 = stry.toLowerCase().toCharArray();
		final StringBuilder builder = new StringBuilder();
		builder.append(cr1[EmployeeServiceImpl.RANDOM.nextInt(cr1.length)]);
		for (int i = 0; i < 7; i++) {
			builder.append(cr2[EmployeeServiceImpl.RANDOM.nextInt(cr2.length)]);
		}
		return builder.toString();
	}

	@Override
	public Boolean register(final EmployeeDto employeeDto) {
		final Integer emailCount = this.dslContext.selectCount().from(EMPLOYEES)
				.where(EMPLOYEES.DELETE_FLG.eq(OgumaProjectConstants.LOGIC_DELETE_INITIAL))
				.and(EMPLOYEES.EMAIL.eq(employeeDto.email())).fetchSingle().into(Integer.class);
		if (emailCount > 0) {
			return Boolean.FALSE;
		}
		final EmployeesRecord employeesRecord = this.dslContext.newRecord(EMPLOYEES);
		employeesRecord.setId(SnowflakeUtils.snowflakeId());
		employeesRecord.setLoginAccount(this.getRandomStr());
		employeesRecord.setUsername(employeeDto.username());
		employeesRecord.setPassword(this.encoder.encode(employeeDto.password()));
		employeesRecord.setEmail(employeeDto.email());
		employeesRecord.setDateOfBirth(LocalDate.parse(employeeDto.dateOfBirth(), this.formatter));
		employeesRecord.setCreatedTime(LocalDateTime.now());
		employeesRecord.setDeleteFlg(OgumaProjectConstants.LOGIC_DELETE_INITIAL);
		final RolesRecord rolesRecord = this.dslContext.selectFrom(ROLES)
				.where(ROLES.DELETE_FLG.eq(OgumaProjectConstants.LOGIC_DELETE_INITIAL)).and(ROLES.NAME.eq("正社員"))
				.fetchSingle();
		final EmployeeRoleRecord employeeRoleRecord = this.dslContext.newRecord(EMPLOYEE_ROLE);
		employeeRoleRecord.setEmployeeId(employeesRecord.getId());
		employeeRoleRecord.setRoleId(rolesRecord.getId());
		employeeRoleRecord.insert();
		employeesRecord.insert();
		return Boolean.TRUE;
	}

	@Override
	public void remove(final Long userId) {
		this.dslContext.update(EMPLOYEES).set(EMPLOYEES.DELETE_FLG, OgumaProjectConstants.LOGIC_DELETE_FLG)
				.where(EMPLOYEES.ID.eq(userId)).execute();
		this.dslContext.deleteFrom(EMPLOYEE_ROLE).where(EMPLOYEE_ROLE.EMPLOYEE_ID.eq(userId)).execute();
	}

	@Override
	public Boolean resetPassword(final EmployeeDto employeeDto) {
		try {
			final EmployeesRecord employeesRecord = this.dslContext.selectFrom(EMPLOYEES)
					.where(EMPLOYEES.DELETE_FLG.eq(OgumaProjectConstants.LOGIC_DELETE_INITIAL))
					.and(EMPLOYEES.LOGIN_ACCOUNT.eq(employeeDto.loginAccount()))
					.and(EMPLOYEES.EMAIL.eq(employeeDto.email()))
					.and(EMPLOYEES.DATE_OF_BIRTH.eq(LocalDate.parse(employeeDto.dateOfBirth(), this.formatter)))
					.fetchSingle();
			employeesRecord.setPassword(this.encoder.encode(OgumaProjectConstants.DEFAULT_PASSWORD));
			employeesRecord.insert();
		} catch (final NoDataFoundException e) {
			return Boolean.FALSE;
		} catch (final Exception e) {
			throw new OgumaProjectException(OgumaProjectConstants.MESSAGE_STRING_FATAL_ERROR);
		}
		return Boolean.TRUE;
	}

	@Override
	public void save(final EmployeeDto employeeDto) {
		final EmployeesRecord employeesRecord = this.dslContext.newRecord(EMPLOYEES);
		employeesRecord.setId(SnowflakeUtils.snowflakeId());
		employeesRecord.setLoginAccount(employeeDto.loginAccount());
		employeesRecord.setUsername(employeeDto.username());
		employeesRecord.setPassword(this.encoder.encode(employeeDto.password()));
		employeesRecord.setEmail(employeeDto.email());
		employeesRecord.setDateOfBirth(LocalDate.parse(employeeDto.dateOfBirth(), this.formatter));
		employeesRecord.setCreatedTime(LocalDateTime.now());
		employeesRecord.setDeleteFlg(OgumaProjectConstants.LOGIC_DELETE_INITIAL);
		if (employeeDto.roleId() != null && !CommonProjectUtils.isEqual(Long.valueOf(0L), employeeDto.roleId())) {
			final EmployeeRoleRecord employeeRoleRecord = this.dslContext.newRecord(EMPLOYEE_ROLE);
			employeeRoleRecord.setEmployeeId(employeesRecord.getId());
			employeeRoleRecord.setRoleId(employeeDto.roleId());
			employeeRoleRecord.insert();
		}
		employeesRecord.insert();
	}

	@Override
	public ResultDto<String> update(final EmployeeDto employeeDto) {
		final EmployeesRecord employeesRecord = this.dslContext.selectFrom(EMPLOYEES)
				.where(EMPLOYEES.DELETE_FLG.eq(OgumaProjectConstants.LOGIC_DELETE_INITIAL))
				.and(EMPLOYEES.ID.eq(employeeDto.id())).fetchSingle();
		final EmployeeRoleRecord employeeRoleRecord = this.dslContext.selectFrom(EMPLOYEE_ROLE)
				.where(EMPLOYEE_ROLE.EMPLOYEE_ID.eq(employeeDto.id())).fetchSingle();
		if (CommonProjectUtils.isEmpty(employeeDto.password())) {
			final EmployeeDto aEmployeeDto = new EmployeeDto(employeesRecord.getId(), employeesRecord.getLoginAccount(),
					employeesRecord.getUsername(), null, employeesRecord.getEmail(),
					this.formatter.format(employeesRecord.getDateOfBirth()), employeeRoleRecord.getRoleId());
			if (CommonProjectUtils.isEqual(aEmployeeDto, employeeDto)) {
				return ResultDto.failed(OgumaProjectConstants.MESSAGE_STRING_NOCHANGE);
			}
			employeesRecord.setLoginAccount(employeeDto.loginAccount());
			employeesRecord.setUsername(employeeDto.username());
			employeesRecord.setEmail(employeeDto.email());
			employeesRecord.setDateOfBirth(LocalDate.parse(employeeDto.dateOfBirth(), this.formatter));
			employeeRoleRecord.setRoleId(employeeDto.roleId());
			employeeRoleRecord.insert();
			employeesRecord.insert();
			return ResultDto.successWithoutData();
		}
		final EmployeeDto nEmployeeDto = new EmployeeDto(employeeDto.id(), employeeDto.loginAccount(),
				employeeDto.username(), this.encoder.encode(employeeDto.password()), employeeDto.email(),
				employeeDto.dateOfBirth(), employeeDto.roleId());
		final EmployeeDto aEmployeeDto = new EmployeeDto(employeesRecord.getId(), employeesRecord.getLoginAccount(),
				employeesRecord.getUsername(), employeesRecord.getPassword(), employeesRecord.getEmail(),
				this.formatter.format(employeesRecord.getDateOfBirth()), employeeRoleRecord.getRoleId());
		if (CommonProjectUtils.isEqual(nEmployeeDto, aEmployeeDto)) {
			return ResultDto.failed(OgumaProjectConstants.MESSAGE_STRING_NOCHANGE);
		}
		employeesRecord.setLoginAccount(employeeDto.loginAccount());
		employeesRecord.setUsername(employeeDto.username());
		employeesRecord.setEmail(employeeDto.email());
		employeesRecord.setDateOfBirth(LocalDate.parse(employeeDto.dateOfBirth(), this.formatter));
		employeeRoleRecord.setRoleId(employeeDto.roleId());
		employeeRoleRecord.insert();
		employeesRecord.insert();
		return ResultDto.successWithoutData();
	}
}
