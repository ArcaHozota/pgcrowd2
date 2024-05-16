package jp.co.toshiba.ppocph.service.impl;

import static jp.co.toshiba.ppocph.jooq.Tables.CITIES;
import static jp.co.toshiba.ppocph.jooq.Tables.DISTRICTS;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.jooq.DSLContext;
import org.jooq.Record2;
import org.jooq.Table;
import org.jooq.impl.DSL;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import jp.co.toshiba.ppocph.common.PgCrowdConstants;
import jp.co.toshiba.ppocph.dto.DistrictDto;
import jp.co.toshiba.ppocph.jooq.Keys;
import jp.co.toshiba.ppocph.jooq.tables.records.DistrictsRecord;
import jp.co.toshiba.ppocph.service.IDistrictService;
import jp.co.toshiba.ppocph.utils.CommonProjectUtils;
import jp.co.toshiba.ppocph.utils.Pagination;
import jp.co.toshiba.ppocph.utils.ResultDto;
import jp.co.toshiba.ppocph.utils.SecondBeanUtils;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;

/**
 * 地域サービス実装クラス
 *
 * @author ArkamaHozota
 * @since 1.00beta
 */
@Service
@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public final class DistrictServiceImpl implements IDistrictService {

	/**
	 * 共通リポジトリ
	 */
	private final DSLContext dslContext;

	@Override
	public List<DistrictDto> getDistrictsByCityId(final String cityId) {
		final List<DistrictDto> districtDtos = new ArrayList<>();
		final List<DistrictsRecord> districtRecords = this.dslContext.selectFrom(DISTRICTS)
				.where(DISTRICTS.DELETE_FLG.eq(PgCrowdConstants.LOGIC_DELETE_INITIAL)).orderBy(DISTRICTS.ID.asc())
				.fetchInto(DistrictsRecord.class);
		final List<DistrictDto> districtDtos1 = districtRecords.stream().map(item -> {
			final DistrictDto districtDto = new DistrictDto();
			SecondBeanUtils.copyNullableProperties(item, districtDto);
			districtDto.setId(item.getId().toString());
			return districtDto;
		}).collect(Collectors.toList());
		final DistrictDto districtDto = new DistrictDto();
		if (!CommonProjectUtils.isDigital(cityId)) {
			districtDto.setId(String.valueOf(0L));
			districtDto.setName(CommonProjectUtils.EMPTY_STRING);
			districtDto.setChiho(CommonProjectUtils.EMPTY_STRING);
		} else {
			final DistrictsRecord districtsRecord = this.dslContext.select(DISTRICTS.fields()).from(DISTRICTS)
					.innerJoin(CITIES).onKey(Keys.CITIES__FK_CITIES_DISTRICTS)
					.where(DISTRICTS.DELETE_FLG.eq(PgCrowdConstants.LOGIC_DELETE_INITIAL))
					.and(CITIES.ID.eq(Long.parseLong(cityId))).fetchSingle().into(DistrictsRecord.class);
			SecondBeanUtils.copyNullableProperties(districtsRecord, districtDto);
			districtDto.setId(districtsRecord.getId().toString());
		}
		districtDtos.add(districtDto);
		districtDtos.addAll(districtDtos1);
		return districtDtos.stream().distinct().collect(Collectors.toList());
	}

	@Override
	public Pagination<DistrictDto> getDistrictsByKeyword(final Integer pageNum, final String keyword) {
		final Table<Record2<Long, BigDecimal>> subQueryTable = this.dslContext
				.select(CITIES.DISTRICT_ID.as("districtId"), DSL.sum(CITIES.POPULATION).as("population")).from(CITIES)
				.where(CITIES.DELETE_FLG.eq(PgCrowdConstants.LOGIC_DELETE_INITIAL)).groupBy(CITIES.DISTRICT_ID)
				.asTable();
		final int offset = (pageNum - 1) * PgCrowdConstants.DEFAULT_PAGE_SIZE;
		if (CommonProjectUtils.isEmpty(keyword)) {
			final Integer totalRecords = this.dslContext.selectCount().from(DISTRICTS).innerJoin(CITIES)
					.on(CITIES.ID.eq(DISTRICTS.SHUTO_ID))
					.where(DISTRICTS.DELETE_FLG.eq(PgCrowdConstants.LOGIC_DELETE_INITIAL)).fetchSingle()
					.into(Integer.class);
			final List<DistrictDto> districtDtos = this.dslContext
					.select(DISTRICTS.ID, DISTRICTS.NAME, DISTRICTS.SHUTO_ID, CITIES.NAME.as("shutoName"),
							DISTRICTS.CHIHO, subQueryTable.field("population"), DISTRICTS.DISTRICT_FLAG)
					.from(DISTRICTS).innerJoin(CITIES).on(CITIES.ID.eq(DISTRICTS.SHUTO_ID)).innerJoin(subQueryTable)
					.on(DISTRICTS.ID.eq(subQueryTable.field("districtId", Long.class)))
					.where(DISTRICTS.DELETE_FLG.eq(PgCrowdConstants.LOGIC_DELETE_INITIAL))
					.limit(PgCrowdConstants.DEFAULT_PAGE_SIZE).offset(offset).fetchInto(DistrictDto.class);
			return Pagination.of(districtDtos, totalRecords, pageNum, PgCrowdConstants.DEFAULT_PAGE_SIZE);
		}
		final String searchStr = CommonProjectUtils.getDetailKeyword(keyword);
		final Integer totalRecords = this.dslContext.selectCount().from(DISTRICTS).innerJoin(CITIES)
				.on(CITIES.ID.eq(DISTRICTS.SHUTO_ID))
				.where(DISTRICTS.DELETE_FLG.eq(PgCrowdConstants.LOGIC_DELETE_INITIAL))
				.and(DISTRICTS.NAME.like(searchStr).or(CITIES.NAME.like(searchStr))).fetchSingle().into(Integer.class);
		final List<DistrictDto> districtDtos = this.dslContext
				.select(DISTRICTS.ID, DISTRICTS.NAME, DISTRICTS.SHUTO_ID, CITIES.NAME.as("shutoName"), DISTRICTS.CHIHO,
						subQueryTable.field("population"), DISTRICTS.DISTRICT_FLAG)
				.from(DISTRICTS).innerJoin(CITIES).on(CITIES.ID.eq(DISTRICTS.SHUTO_ID)).innerJoin(subQueryTable)
				.on(DISTRICTS.ID.eq(subQueryTable.field("districtId", Long.class)))
				.where(DISTRICTS.DELETE_FLG.eq(PgCrowdConstants.LOGIC_DELETE_INITIAL))
				.and(DISTRICTS.NAME.like(searchStr).or(CITIES.NAME.like(searchStr)))
				.limit(PgCrowdConstants.DEFAULT_PAGE_SIZE).offset(offset).fetchInto(DistrictDto.class);
		return Pagination.of(districtDtos, totalRecords, pageNum, PgCrowdConstants.DEFAULT_PAGE_SIZE);
	}

	@Override
	public ResultDto<String> update(final DistrictDto districtDto) {
		final DistrictsRecord districtsRecord = this.dslContext.selectFrom(DISTRICTS)
				.where(DISTRICTS.DELETE_FLG.eq(PgCrowdConstants.LOGIC_DELETE_INITIAL))
				.and(DISTRICTS.ID.eq(Long.parseLong(districtDto.getId()))).fetchSingle();
		final DistrictDto aDistrictDto = new DistrictDto();
		SecondBeanUtils.copyNullableProperties(districtsRecord, aDistrictDto);
		aDistrictDto.setId(districtsRecord.getId().toString());
		if (CommonProjectUtils.isEqual(aDistrictDto, districtDto)) {
			return ResultDto.failed(PgCrowdConstants.MESSAGE_STRING_NOCHANGE);
		}
		SecondBeanUtils.copyNullableProperties(districtDto, districtsRecord);
		try {
			this.dslContext.update(DISTRICTS).set(districtsRecord)
					.where(DISTRICTS.DELETE_FLG.eq(PgCrowdConstants.LOGIC_DELETE_INITIAL))
					.and(DISTRICTS.ID.eq(districtsRecord.getId())).execute();
		} catch (final DataIntegrityViolationException e) {
			return ResultDto.failed(PgCrowdConstants.MESSAGE_DISTRICT_NAME_DUPLICATED);
		}
		return ResultDto.successWithoutData();
	}
}
