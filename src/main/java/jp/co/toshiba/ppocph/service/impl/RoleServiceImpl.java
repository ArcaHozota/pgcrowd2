package jp.co.toshiba.ppocph.service.impl;

import static jp.co.toshiba.ppocph.jooq.Tables.AUTHORITIES;
import static jp.co.toshiba.ppocph.jooq.Tables.ROLES;
import static jp.co.toshiba.ppocph.jooq.Tables.ROLE_AUTH;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

import org.jooq.DSLContext;
import org.springframework.stereotype.Service;

import jp.co.toshiba.ppocph.common.OgumaProjectConstants;
import jp.co.toshiba.ppocph.dto.AuthorityDto;
import jp.co.toshiba.ppocph.dto.RoleDto;
import jp.co.toshiba.ppocph.jooq.tables.records.AuthoritiesRecord;
import jp.co.toshiba.ppocph.jooq.tables.records.RoleAuthRecord;
import jp.co.toshiba.ppocph.service.IRoleService;
import jp.co.toshiba.ppocph.utils.CommonProjectUtils;
import jp.co.toshiba.ppocph.utils.Pagination;
import jp.co.toshiba.ppocph.utils.ResultDto;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;

/**
 * 役割サービス実装クラス
 *
 * @author ArkamaHozota
 * @since 4.46
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
		return roleNameCount > 0 ? ResultDto.failed(OgumaProjectConstants.MESSAGE_ROLE_NAME_DUPLICATED)
				: ResultDto.successWithoutData();
	}

	@Override
	public ResultDto<String> doAssignment(final Map<String, List<Long>> paramMap) {
		final Long[] idArray = { 1L, 5L, 9L, 12L };
		final Long roleId = paramMap.get("roleId").get(0);
		final List<RoleAuthRecord> roleAuthRecords = this.dslContext.selectFrom(ROLE_AUTH)
				.where(ROLE_AUTH.ROLE_ID.eq(roleId)).fetchInto(RoleAuthRecord.class);
		final List<Long> remnantAuthIds = roleAuthRecords.stream().map(RoleAuthRecord::getAuthId).sorted().toList();
		final List<Long> authIds = paramMap.get("authIdArray").stream().filter(a -> !Arrays.asList(idArray).contains(a))
				.sorted().toList();
		if (CommonProjectUtils.isEqual(remnantAuthIds, authIds)) {
			return ResultDto.failed(OgumaProjectConstants.MESSAGE_STRING_NOCHANGE);
		}
		this.dslContext.deleteFrom(ROLE_AUTH).where(ROLE_AUTH.ROLE_ID.eq(roleId)).execute();
		final List<RoleAuthRecord> newRoleAuthRecords = authIds.stream().map(item -> {
			final RoleAuthRecord roleAuthRecord = this.dslContext.newRecord(ROLE_AUTH);
			roleAuthRecord.setAuthId(item);
			roleAuthRecord.setRoleId(roleId);
			return roleAuthRecord;
		}).toList();
		try {
			this.dslContext.insertInto(ROLE_AUTH).values(newRoleAuthRecords).execute();
		} catch (final Exception e) {
			return ResultDto.failed(OgumaProjectConstants.MESSAGE_STRING_FORBIDDEN2);
		}
		return ResultDto.successWithoutData();
	}

	@Override
	public List<Long> getAuthIdsById(final Long id) {
		final List<RoleAuthRecord> roleAuthRecords = this.dslContext.selectFrom(ROLE_AUTH)
				.where(ROLE_AUTH.ROLE_ID.eq(id)).fetchInto(RoleAuthRecord.class);
		return roleAuthRecords.stream().map(RoleAuthRecord::getAuthId).toList();
	}

	@Override
	public List<AuthorityDto> getAuthList() {
		final List<AuthoritiesRecord> authoritiesRecords = this.dslContext.selectFrom(AUTHORITIES)
				.fetchInto(AuthoritiesRecord.class);
		return authoritiesRecords.stream()
				.map(item -> new AuthorityDto(item.getId(), item.getName(), item.getTitle(), item.getCategoryId()))
				.toList();
	}

	@Override
	public RoleDto getRoleById(final Long id) {
		return null;
//		final Role role = this.roleRepository.findById(id).orElseThrow(() -> {
//			throw new OgumaProjectException(OgumaProjectConstants.MESSAGE_STRING_NOT_EXISTS);
//		});
//		return new RoleDto(role.getId(), role.getName());
	}

	@Override
	public List<RoleDto> getRolesByEmployeeId(final Long employeeId) {
		return null;
//		final List<RoleDto> secondRoles = new ArrayList<>();
//		final RoleDto secondRole = new RoleDto(0L, OgumaProjectConstants.DEFAULT_ROLE_NAME);
//		final Specification<Role> where1 = (root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get(DELETE_FLG),
//				OgumaProjectConstants.LOGIC_DELETE_INITIAL);
//		final Specification<Role> specification1 = Specification.where(where1);
//		final List<RoleDto> roleDtos = this.roleRepository.findAll(specification1).stream()
//				.map(item -> new RoleDto(item.getId(), item.getName())).toList();
//		secondRoles.add(secondRole);
//		secondRoles.addAll(roleDtos);
//		if (employeeId == null) {
//			return secondRoles;
//		}
//		final Optional<EmployeeRole> roledOptional = this.employeeExRepository.findById(employeeId);
//		if (roledOptional.isEmpty()) {
//			return secondRoles;
//		}
//		secondRoles.clear();
//		final Long roleId = roledOptional.get().getRoleId();
//		final List<RoleDto> selectedRole = roleDtos.stream().filter(a -> Objects.equals(a.id(), roleId)).toList();
//		secondRoles.addAll(selectedRole);
//		secondRoles.addAll(roleDtos);
//		return secondRoles.stream().distinct().toList();
	}

	@Override
	public Pagination<RoleDto> getRolesByKeyword(final Integer pageNum, final String keyword) {
		return null;
//		final PageRequest pageRequest = PageRequest.of(pageNum - 1, OgumaProjectConstants.DEFAULT_PAGE_SIZE,
//				Sort.by(Direction.ASC, "id"));
//		final Specification<Role> where1 = (root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get(DELETE_FLG),
//				OgumaProjectConstants.LOGIC_DELETE_INITIAL);
//		final Specification<Role> specification = Specification.where(where1);
//		if (OgumaProjectUtils.isEmpty(keyword)) {
//			final Page<Role> pages = this.roleRepository.findAll(specification, pageRequest);
//			final List<RoleDto> roleDtos = pages.stream().map(item -> new RoleDto(item.getId(), item.getName()))
//					.toList();
//			return Pagination.of(roleDtos, pages.getTotalElements(), pageNum, OgumaProjectConstants.DEFAULT_PAGE_SIZE);
//		}
//		if (OgumaProjectUtils.isDigital(keyword)) {
//			final Page<Role> byIdLike = this.roleRepository.findByIdLike(keyword,
//					OgumaProjectConstants.LOGIC_DELETE_INITIAL, pageRequest);
//			final List<RoleDto> roleDtos = byIdLike.stream().map(item -> new RoleDto(item.getId(), item.getName()))
//					.toList();
//			return Pagination.of(roleDtos, byIdLike.getTotalElements(), pageNum,
//					OgumaProjectConstants.DEFAULT_PAGE_SIZE);
//		}
//		final String searchStr = OgumaProjectUtils.getDetailKeyword(keyword);
//		final Specification<Role> where2 = (root, query, criteriaBuilder) -> criteriaBuilder.like(root.get(ROLE_NAME),
//				searchStr);
//		final Page<Role> pages = this.roleRepository.findAll(specification.and(where2), pageRequest);
//		final List<RoleDto> roleDtos = pages.stream().map(item -> new RoleDto(item.getId(), item.getName())).toList();
//		return Pagination.of(roleDtos, pages.getTotalElements(), pageNum, OgumaProjectConstants.DEFAULT_PAGE_SIZE);
	}

	@Override
	public ResultDto<String> remove(final Long id) {
//		final Specification<EmployeeRole> where = (root, query, criteriaBuilder) -> criteriaBuilder
//				.equal(root.get(ROLE_ID), id);
//		final Specification<EmployeeRole> specification = Specification.where(where);
//		final List<EmployeeRole> list = this.employeeExRepository.findAll(specification);
//		if (!list.isEmpty()) {
//			return ResultDto.failed(OgumaProjectConstants.MESSAGE_STRING_FORBIDDEN);
//		}
//		final Role role = this.roleRepository.findById(id).orElseThrow(() -> {
//			throw new OgumaProjectException(OgumaProjectConstants.MESSAGE_STRING_FATAL_ERROR);
//		});
//		role.setDeleteFlg(OgumaProjectConstants.LOGIC_DELETE_FLG);
//		this.roleRepository.saveAndFlush(role);
		return ResultDto.successWithoutData();
	}

	@Override
	public void save(final RoleDto roleDto) {
//		final Role role = new Role();
//		SecondBeanUtils.copyNullableProperties(roleDto, role);
//		role.setId(SnowflakeUtils.snowflakeId());
//		role.setDeleteFlg(OgumaProjectConstants.LOGIC_DELETE_INITIAL);
//		this.roleRepository.saveAndFlush(role);
	}

	@Override
	public ResultDto<String> update(final RoleDto roleDto) {
//		final Role role = this.roleRepository.findById(roleDto.id()).orElseThrow(() -> {
//			throw new OgumaProjectException(OgumaProjectConstants.MESSAGE_STRING_FATAL_ERROR);
//		});
//		final Role originalEntity = new Role();
//		SecondBeanUtils.copyNullableProperties(role, originalEntity);
//		SecondBeanUtils.copyNullableProperties(roleDto, role);
//		if (originalEntity.equals(role)) {
//			return ResultDto.failed(OgumaProjectConstants.MESSAGE_STRING_NOCHANGE);
//		}
//		try {
//			this.roleRepository.saveAndFlush(role);
//		} catch (final DataIntegrityViolationException e) {
//			return ResultDto.failed(OgumaProjectConstants.MESSAGE_ROLE_NAME_DUPLICATED);
//		}
		return ResultDto.successWithoutData();
	}
}
