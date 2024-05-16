package jp.co.toshiba.ppocph.service.impl;

import static jp.co.toshiba.ppocph.jooq.Tables.AUTHORITIES;
import static jp.co.toshiba.ppocph.jooq.Tables.EMPLOYEE_ROLE;
import static jp.co.toshiba.ppocph.jooq.Tables.ROLES;
import static jp.co.toshiba.ppocph.jooq.Tables.ROLE_AUTH;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.jooq.DSLContext;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import jp.co.toshiba.ppocph.common.PgCrowdConstants;
import jp.co.toshiba.ppocph.dto.AuthorityDto;
import jp.co.toshiba.ppocph.dto.RoleDto;
import jp.co.toshiba.ppocph.jooq.tables.records.AuthoritiesRecord;
import jp.co.toshiba.ppocph.jooq.tables.records.EmployeeRoleRecord;
import jp.co.toshiba.ppocph.jooq.tables.records.RoleAuthRecord;
import jp.co.toshiba.ppocph.jooq.tables.records.RolesRecord;
import jp.co.toshiba.ppocph.service.IRoleService;
import jp.co.toshiba.ppocph.utils.CommonProjectUtils;
import jp.co.toshiba.ppocph.utils.Pagination;
import jp.co.toshiba.ppocph.utils.ResultDto;
import jp.co.toshiba.ppocph.utils.SnowflakeUtils;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;

/**
 * 役割サービス実装クラス
 *
 * @author ArkamaHozota
 * @since 1.00beta
 */
@Service
@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public final class RoleServiceImpl implements IRoleService {

	/**
	 * 共通リポジトリ
	 */
	private final DSLContext dslContext;

	@Override
	public ResultDto<String> checkDuplicated(final String name) {
		final Integer roleNameCount = this.dslContext.selectCount().from(ROLES).where(ROLES.NAME.eq(name)).fetchSingle()
				.into(Integer.class);
		return roleNameCount > 0 ? ResultDto.failed(PgCrowdConstants.MESSAGE_ROLE_NAME_DUPLICATED)
				: ResultDto.successWithoutData();
	}

	@Override
	public ResultDto<String> doAssignment(final Map<String, List<String>> paramMap) {
		final Long[] idArray = { 1L, 5L, 9L, 12L };
		final Long roleId = Long.parseLong(paramMap.get("roleIds").get(0));
		final List<RoleAuthRecord> roleAuthRecords = this.dslContext.selectFrom(ROLE_AUTH)
				.where(ROLE_AUTH.ROLE_ID.eq(roleId)).fetchInto(RoleAuthRecord.class);
		final List<Long> remnantAuthIds = roleAuthRecords.stream().map(RoleAuthRecord::getAuthId).sorted()
				.collect(Collectors.toList());
		final List<Long> authIds = paramMap.get("authIds").stream().map(Long::parseLong)
				.filter(a -> !Arrays.asList(idArray).contains(a)).sorted().collect(Collectors.toList());
		if (CommonProjectUtils.isEqual(remnantAuthIds, authIds)) {
			return ResultDto.failed(PgCrowdConstants.MESSAGE_STRING_NOCHANGE);
		}
		this.dslContext.deleteFrom(ROLE_AUTH).where(ROLE_AUTH.ROLE_ID.eq(roleId)).execute();
		final List<RoleAuthRecord> newRoleAuthRecords = authIds.stream().map(item -> {
			final RoleAuthRecord roleAuthRecord = this.dslContext.newRecord(ROLE_AUTH);
			roleAuthRecord.setAuthId(item);
			roleAuthRecord.setRoleId(roleId);
			return roleAuthRecord;
		}).collect(Collectors.toList());
		try {
			newRoleAuthRecords.forEach(RoleAuthRecord::insert);
		} catch (final Exception e) {
			return ResultDto.failed(PgCrowdConstants.MESSAGE_STRING_FORBIDDEN2);
		}
		return ResultDto.successWithoutData();
	}

	@Override
	public List<String> getAuthIdsById(final Long id) {
		final List<RoleAuthRecord> roleAuthRecords = this.dslContext.selectFrom(ROLE_AUTH)
				.where(ROLE_AUTH.ROLE_ID.eq(id)).fetchInto(RoleAuthRecord.class);
		return roleAuthRecords.stream().map(item -> item.getAuthId().toString()).collect(Collectors.toList());
	}

	@Override
	public List<AuthorityDto> getAuthList() {
		final List<AuthoritiesRecord> authoritiesRecords = this.dslContext.selectFrom(AUTHORITIES)
				.orderBy(AUTHORITIES.ID.asc()).fetchInto(AuthoritiesRecord.class);
		return authoritiesRecords.stream().map(item -> {
			final AuthorityDto authorityDto = new AuthorityDto();
			authorityDto.setId(item.getId().toString());
			authorityDto.setName(item.getName());
			authorityDto.setTitle(item.getTitle());
			authorityDto.setCategoryId(String.valueOf(item.getCategoryId()));
			return authorityDto;
		}).collect(Collectors.toList());
	}

	@Override
	public RoleDto getRoleById(final Long id) {
		final RolesRecord rolesRecord = this.dslContext.selectFrom(ROLES)
				.where(ROLES.DELETE_FLG.eq(PgCrowdConstants.LOGIC_DELETE_INITIAL)).and(ROLES.ID.eq(id)).fetchSingle();
		final RoleDto roleDto = new RoleDto();
		roleDto.setId(rolesRecord.getId().toString());
		roleDto.setName(rolesRecord.getName());
		return roleDto;
	}

	@Override
	public List<RoleDto> getRolesByEmployeeId(final String employeeId) {
		final List<RoleDto> roleDtos = new ArrayList<>();
		final List<RolesRecord> rolesRecords = this.dslContext.selectFrom(ROLES)
				.where(ROLES.DELETE_FLG.eq(PgCrowdConstants.LOGIC_DELETE_INITIAL)).orderBy(ROLES.ID.asc())
				.fetchInto(RolesRecord.class);
		final List<RoleDto> roleDtos1 = rolesRecords.stream().map(item -> {
			final RoleDto roleDto = new RoleDto();
			roleDto.setId(item.getId().toString());
			roleDto.setName(item.getName());
			return roleDto;
		}).distinct().collect(Collectors.toList());
		if (CommonProjectUtils.isEmpty(employeeId)) {
			final RoleDto roleDto = new RoleDto();
			roleDto.setId(String.valueOf(0L));
			roleDto.setName(PgCrowdConstants.DEFAULT_ROLE_NAME);
			roleDtos.add(roleDto);
		} else {
			final EmployeeRoleRecord employeeRoleRecord = this.dslContext.selectFrom(EMPLOYEE_ROLE)
					.where(EMPLOYEE_ROLE.EMPLOYEE_ID.eq(Long.parseLong(employeeId))).fetchSingle();
			final List<RoleDto> list = roleDtos1.stream()
					.filter(a -> CommonProjectUtils.isEqual(employeeRoleRecord.getRoleId(), Long.parseLong(a.getId())))
					.collect(Collectors.toList());
			roleDtos.addAll(list);
		}
		roleDtos.addAll(roleDtos1);
		return roleDtos.stream().distinct().collect(Collectors.toList());
	}

	@Override
	public Pagination<RoleDto> getRolesByKeyword(final Integer pageNum, final String keyword) {
		final int offset = (pageNum - 1) * PgCrowdConstants.DEFAULT_PAGE_SIZE;
		if (CommonProjectUtils.isEmpty(keyword)) {
			final Integer totalRecords = this.dslContext.selectCount().from(ROLES)
					.where(ROLES.DELETE_FLG.eq(PgCrowdConstants.LOGIC_DELETE_INITIAL)).fetchSingle()
					.into(Integer.class);
			final List<RoleDto> roleDtos = this.dslContext.select(ROLES.ID, ROLES.NAME).from(ROLES)
					.where(ROLES.DELETE_FLG.eq(PgCrowdConstants.LOGIC_DELETE_INITIAL)).orderBy(ROLES.ID.asc())
					.limit(PgCrowdConstants.DEFAULT_PAGE_SIZE).offset(offset).fetchInto(RoleDto.class);
			return Pagination.of(roleDtos, totalRecords, pageNum, PgCrowdConstants.DEFAULT_PAGE_SIZE);
		}
		final String searchStr = CommonProjectUtils.getDetailKeyword(keyword);
		final Integer totalRecords = this.dslContext.selectCount().from(ROLES)
				.where(ROLES.DELETE_FLG.eq(PgCrowdConstants.LOGIC_DELETE_INITIAL))
				.and(ROLES.NAME.like(searchStr).or(ROLES.ID.like(keyword))).fetchSingle().into(Integer.class);
		final List<RoleDto> roleDtos = this.dslContext.select(ROLES.ID, ROLES.NAME).from(ROLES)
				.where(ROLES.DELETE_FLG.eq(PgCrowdConstants.LOGIC_DELETE_INITIAL))
				.and(ROLES.NAME.like(searchStr).or(ROLES.ID.like(keyword))).orderBy(ROLES.ID.asc())
				.limit(PgCrowdConstants.DEFAULT_PAGE_SIZE).offset(offset).fetchInto(RoleDto.class);
		return Pagination.of(roleDtos, totalRecords, pageNum, PgCrowdConstants.DEFAULT_PAGE_SIZE);
	}

	@Override
	public ResultDto<String> remove(final Long id) {
		final Integer roleUsageCount = this.dslContext.selectCount().from(EMPLOYEE_ROLE)
				.where(EMPLOYEE_ROLE.ROLE_ID.eq(id)).fetchSingle().into(Integer.class);
		if (roleUsageCount > 0) {
			return ResultDto.failed(PgCrowdConstants.MESSAGE_STRING_FORBIDDEN);
		}
		this.dslContext.update(ROLES).set(ROLES.DELETE_FLG, PgCrowdConstants.LOGIC_DELETE_FLG).where(ROLES.ID.eq(id))
				.execute();
		return ResultDto.successWithoutData();
	}

	@Override
	public void save(final RoleDto roleDto) {
		final RolesRecord rolesRecord = this.dslContext.newRecord(ROLES);
		rolesRecord.setId(SnowflakeUtils.snowflakeId());
		rolesRecord.setName(roleDto.getName());
		rolesRecord.setDeleteFlg(PgCrowdConstants.LOGIC_DELETE_INITIAL);
		rolesRecord.insert();
	}

	@Override
	public ResultDto<String> update(final RoleDto roleDto) {
		final RolesRecord rolesRecord = this.dslContext.selectFrom(ROLES)
				.where(ROLES.DELETE_FLG.eq(PgCrowdConstants.LOGIC_DELETE_INITIAL))
				.and(ROLES.ID.eq(Long.parseLong(roleDto.getId()))).fetchSingle();
		final RoleDto aRoleDto = new RoleDto();
		aRoleDto.setId(rolesRecord.getId().toString());
		aRoleDto.setName(roleDto.getName());
		if (CommonProjectUtils.isEqual(aRoleDto, roleDto)) {
			return ResultDto.failed(PgCrowdConstants.MESSAGE_STRING_NOCHANGE);
		}
		rolesRecord.setName(roleDto.getName());
		try {
			this.dslContext.update(ROLES).set(rolesRecord)
					.where(ROLES.DELETE_FLG.eq(PgCrowdConstants.LOGIC_DELETE_INITIAL))
					.and(ROLES.ID.eq(rolesRecord.getId())).execute();
		} catch (final DataIntegrityViolationException e) {
			return ResultDto.failed(PgCrowdConstants.MESSAGE_ROLE_NAME_DUPLICATED);
		}
		return ResultDto.successWithoutData();
	}
}
