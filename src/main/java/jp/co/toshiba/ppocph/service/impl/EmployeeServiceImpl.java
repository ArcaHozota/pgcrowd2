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
import java.util.stream.Collectors;

import org.jooq.DSLContext;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder.BCryptVersion;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import jp.co.toshiba.ppocph.common.PgCrowdConstants;
import jp.co.toshiba.ppocph.dto.EmployeeDto;
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
	private final PasswordEncoder encoder = new BCryptPasswordEncoder(BCryptVersion.$2Y, 7);

	/**
	 * 日時フォマーター
	 */
	private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

	@Override
	public ResultDto<String> checkDuplicated(final String loginAccount) {
		final Integer employeNameCount = this.dslContext.selectCount().from(EMPLOYEES)
				.where(EMPLOYEES.LOGIN_ACCOUNT.eq(loginAccount)).fetchSingle().into(Integer.class);
		return employeNameCount > 0 ? ResultDto.failed(PgCrowdConstants.MESSAGE_STRING_DUPLICATED)
				: ResultDto.successWithoutData();
	}

	@Override
	public EmployeeDto getEmployeeById(final String id) {
		final EmployeeDto employeeDto = new EmployeeDto();
		final EmployeesRecord employeesRecord = this.dslContext.selectFrom(EMPLOYEES)
				.where(EMPLOYEES.DELETE_FLG.eq(PgCrowdConstants.LOGIC_DELETE_INITIAL))
				.and(EMPLOYEES.ID.eq(Long.parseLong(id))).fetchSingle();
		final EmployeeRoleRecord employeeRoleRecord = this.dslContext.selectFrom(EMPLOYEE_ROLE)
				.where(EMPLOYEE_ROLE.EMPLOYEE_ID.eq(Long.parseLong(id))).fetchSingle();
		employeeDto.setId(employeesRecord.getId().toString());
		employeeDto.setLoginAccount(employeesRecord.getLoginAccount());
		employeeDto.setUsername(employeesRecord.getUsername());
		employeeDto.setPassword(PgCrowdConstants.DEFAULT_ROLE_NAME);
		employeeDto.setEmail(employeesRecord.getEmail());
		employeeDto.setDateOfBirth(this.formatter.format(employeesRecord.getDateOfBirth()));
		employeeDto.setRoleId(employeeRoleRecord.getRoleId().toString());
		return employeeDto;
	}

	@Override
	public Pagination<EmployeeDto> getEmployeesByKeyword(final Integer pageNum, final String keyword, final Long userId,
			final String authChkFlag) {
		if (Boolean.FALSE.equals(Boolean.valueOf(authChkFlag))) {
			final List<EmployeeDto> employeeDtos = new ArrayList<>();
			final EmployeesRecord employeesRecord = this.dslContext.selectFrom(EMPLOYEES)
					.where(EMPLOYEES.DELETE_FLG.eq(PgCrowdConstants.LOGIC_DELETE_INITIAL)).and(EMPLOYEES.ID.eq(userId))
					.fetchSingle();
			final EmployeeDto employeeDto = new EmployeeDto();
			employeeDto.setId(employeesRecord.getId().toString());
			employeeDto.setLoginAccount(employeesRecord.getLoginAccount());
			employeeDto.setUsername(employeesRecord.getUsername());
			employeeDto.setPassword(employeesRecord.getPassword());
			employeeDto.setEmail(employeesRecord.getEmail());
			employeeDto.setDateOfBirth(this.formatter.format(employeesRecord.getDateOfBirth()));
			employeeDtos.add(employeeDto);
			return Pagination.of(employeeDtos, employeeDtos.size(), pageNum, PgCrowdConstants.DEFAULT_PAGE_SIZE);
		}
		final int offset = (pageNum - 1) * PgCrowdConstants.DEFAULT_PAGE_SIZE;
		if (CommonProjectUtils.isEmpty(keyword)) {
			final Integer totalRecords = this.dslContext.selectCount().from(EMPLOYEES)
					.where(EMPLOYEES.DELETE_FLG.eq(PgCrowdConstants.LOGIC_DELETE_INITIAL)).fetchSingle()
					.into(Integer.class);
			final List<EmployeesRecord> employeesRecords = this.dslContext.selectFrom(EMPLOYEES)
					.where(EMPLOYEES.DELETE_FLG.eq(PgCrowdConstants.LOGIC_DELETE_INITIAL)).orderBy(EMPLOYEES.ID.asc())
					.limit(PgCrowdConstants.DEFAULT_PAGE_SIZE).offset(offset).fetchInto(EmployeesRecord.class);
			final List<EmployeeDto> employeeDtos = employeesRecords.stream().map(item -> {
				final EmployeeDto employeeDto = new EmployeeDto();
				employeeDto.setId(item.getId().toString());
				employeeDto.setLoginAccount(item.getLoginAccount());
				employeeDto.setUsername(item.getUsername());
				employeeDto.setPassword(item.getPassword());
				employeeDto.setEmail(item.getEmail());
				employeeDto.setDateOfBirth(this.formatter.format(item.getDateOfBirth()));
				return employeeDto;
			}).collect(Collectors.toList());
			return Pagination.of(employeeDtos, totalRecords, pageNum, PgCrowdConstants.DEFAULT_PAGE_SIZE);
		}
		final String searchStr = CommonProjectUtils.getDetailKeyword(keyword);
		final Integer totalRecords = this.dslContext.selectCount().from(EMPLOYEES)
				.where(EMPLOYEES.DELETE_FLG.eq(PgCrowdConstants.LOGIC_DELETE_INITIAL))
				.and(EMPLOYEES.USERNAME.like(searchStr).or(EMPLOYEES.LOGIN_ACCOUNT.like(searchStr))
						.or(EMPLOYEES.EMAIL.like(searchStr)))
				.fetchSingle().into(Integer.class);
		final List<EmployeesRecord> employeesRecords = this.dslContext.selectFrom(EMPLOYEES)
				.where(EMPLOYEES.DELETE_FLG.eq(PgCrowdConstants.LOGIC_DELETE_INITIAL))
				.and(EMPLOYEES.USERNAME.like(searchStr).or(EMPLOYEES.LOGIN_ACCOUNT.like(searchStr))
						.or(EMPLOYEES.EMAIL.like(searchStr)))
				.orderBy(EMPLOYEES.ID.asc()).limit(PgCrowdConstants.DEFAULT_PAGE_SIZE).offset(offset)
				.fetchInto(EmployeesRecord.class);
		final List<EmployeeDto> employeeDtos = employeesRecords.stream().map(item -> {
			final EmployeeDto employeeDto = new EmployeeDto();
			employeeDto.setId(item.getId().toString());
			employeeDto.setLoginAccount(item.getLoginAccount());
			employeeDto.setUsername(item.getUsername());
			employeeDto.setPassword(item.getPassword());
			employeeDto.setEmail(item.getEmail());
			employeeDto.setDateOfBirth(this.formatter.format(item.getDateOfBirth()));
			return employeeDto;
		}).collect(Collectors.toList());
		return Pagination.of(employeeDtos, totalRecords, pageNum, PgCrowdConstants.DEFAULT_PAGE_SIZE);
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
				.where(EMPLOYEES.DELETE_FLG.eq(PgCrowdConstants.LOGIC_DELETE_INITIAL))
				.and(EMPLOYEES.EMAIL.eq(employeeDto.getEmail())).fetchSingle().into(Integer.class);
		if (emailCount > 0) {
			return Boolean.FALSE;
		}
		final EmployeesRecord employeesRecord = this.dslContext.newRecord(EMPLOYEES);
		employeesRecord.setId(SnowflakeUtils.snowflakeId());
		employeesRecord.setLoginAccount(this.getRandomStr());
		employeesRecord.setUsername(employeeDto.getUsername());
		employeesRecord.setPassword(this.encoder.encode(employeeDto.getPassword()));
		employeesRecord.setEmail(employeeDto.getEmail());
		employeesRecord.setDateOfBirth(LocalDate.parse(employeeDto.getDateOfBirth(), this.formatter));
		employeesRecord.setCreatedTime(LocalDateTime.now());
		employeesRecord.setDeleteFlg(PgCrowdConstants.LOGIC_DELETE_INITIAL);
		final RolesRecord rolesRecord = this.dslContext.selectFrom(ROLES)
				.where(ROLES.DELETE_FLG.eq(PgCrowdConstants.LOGIC_DELETE_INITIAL)).and(ROLES.NAME.eq("正社員"))
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
		this.dslContext.update(EMPLOYEES).set(EMPLOYEES.DELETE_FLG, PgCrowdConstants.LOGIC_DELETE_FLG)
				.where(EMPLOYEES.ID.eq(userId)).execute();
		this.dslContext.deleteFrom(EMPLOYEE_ROLE).where(EMPLOYEE_ROLE.EMPLOYEE_ID.eq(userId)).execute();
	}

	@Override
	public void save(final EmployeeDto employeeDto) {
		final EmployeesRecord employeesRecord = this.dslContext.newRecord(EMPLOYEES);
		employeesRecord.setId(SnowflakeUtils.snowflakeId());
		employeesRecord.setLoginAccount(employeeDto.getLoginAccount());
		employeesRecord.setUsername(employeeDto.getUsername());
		employeesRecord.setPassword(this.encoder.encode(employeeDto.getPassword()));
		employeesRecord.setEmail(employeeDto.getEmail());
		employeesRecord.setDateOfBirth(LocalDate.parse(employeeDto.getDateOfBirth(), this.formatter));
		employeesRecord.setCreatedTime(LocalDateTime.now());
		employeesRecord.setDeleteFlg(PgCrowdConstants.LOGIC_DELETE_INITIAL);
		if (CommonProjectUtils.isNotEmpty(employeeDto.getRoleId())
				&& CommonProjectUtils.isNotEqual(Long.valueOf(0L), employeeDto.getRoleId())) {
			final EmployeeRoleRecord employeeRoleRecord = this.dslContext.newRecord(EMPLOYEE_ROLE);
			employeeRoleRecord.setEmployeeId(employeesRecord.getId());
			employeeRoleRecord.setRoleId(Long.parseLong(employeeDto.getRoleId()));
			employeeRoleRecord.insert();
		}
		employeesRecord.insert();
	}

	@Override
	public ResultDto<String> update(final EmployeeDto employeeDto) {
		String nyuryokuPassword = employeeDto.getPassword();
		if (CommonProjectUtils.isEmpty(nyuryokuPassword)) {
			nyuryokuPassword = CommonProjectUtils.EMPTY_STRING;
		}
		employeeDto.setPassword(null);
		final EmployeesRecord employeesRecord = this.dslContext.selectFrom(EMPLOYEES)
				.where(EMPLOYEES.DELETE_FLG.eq(PgCrowdConstants.LOGIC_DELETE_INITIAL))
				.and(EMPLOYEES.ID.eq(Long.parseLong(employeeDto.getId()))).fetchSingle();
		final EmployeeRoleRecord employeeRoleRecord = this.dslContext.selectFrom(EMPLOYEE_ROLE)
				.where(EMPLOYEE_ROLE.EMPLOYEE_ID.eq(Long.parseLong(employeeDto.getId()))).fetchSingle();
		final EmployeeDto aEmployeeDto = new EmployeeDto();
		aEmployeeDto.setId(employeesRecord.getId().toString());
		aEmployeeDto.setLoginAccount(employeesRecord.getLoginAccount());
		aEmployeeDto.setUsername(employeesRecord.getUsername());
		aEmployeeDto.setEmail(employeesRecord.getEmail());
		aEmployeeDto.setDateOfBirth(this.formatter.format(employeesRecord.getDateOfBirth()));
		aEmployeeDto.setRoleId(employeeRoleRecord.getRoleId().toString());
		if (CommonProjectUtils.isEqual(aEmployeeDto, employeeDto)
				&& this.encoder.matches(nyuryokuPassword, employeesRecord.getPassword())) {
			return ResultDto.failed(PgCrowdConstants.MESSAGE_STRING_NOCHANGE);
		}
		employeesRecord.setLoginAccount(employeeDto.getLoginAccount());
		employeesRecord.setUsername(employeeDto.getUsername());
		if (CommonProjectUtils.isNotEmpty(nyuryokuPassword)) {
			employeesRecord.setPassword(this.encoder.encode(nyuryokuPassword));
		}
		employeesRecord.setEmail(employeeDto.getEmail());
		employeesRecord.setDateOfBirth(LocalDate.parse(employeeDto.getDateOfBirth(), this.formatter));
		employeeRoleRecord.setRoleId(Long.parseLong(employeeDto.getRoleId()));
		try {
			this.dslContext.update(EMPLOYEE_ROLE).set(employeeRoleRecord)
					.where(EMPLOYEE_ROLE.EMPLOYEE_ID.eq(employeeRoleRecord.getEmployeeId())).execute();
			this.dslContext.update(EMPLOYEES).set(employeesRecord)
					.where(EMPLOYEES.DELETE_FLG.eq(PgCrowdConstants.LOGIC_DELETE_INITIAL))
					.and(EMPLOYEES.ID.eq(employeesRecord.getId())).execute();
		} catch (final DataIntegrityViolationException e) {
			return ResultDto.failed(PgCrowdConstants.MESSAGE_STRING_DUPLICATED);
		}
		return ResultDto.successWithoutData();
	}
}
