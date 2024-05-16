package jp.co.toshiba.ppocph.service.impl;

import static jp.co.toshiba.ppocph.jooq.Tables.CITIES;
import static jp.co.toshiba.ppocph.jooq.Tables.DISTRICTS;

import java.util.List;

import org.jooq.DSLContext;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import jp.co.toshiba.ppocph.common.PgCrowdConstants;
import jp.co.toshiba.ppocph.dto.CityDto;
import jp.co.toshiba.ppocph.jooq.Keys;
import jp.co.toshiba.ppocph.jooq.tables.records.CitiesRecord;
import jp.co.toshiba.ppocph.service.ICityService;
import jp.co.toshiba.ppocph.utils.CommonProjectUtils;
import jp.co.toshiba.ppocph.utils.Pagination;
import jp.co.toshiba.ppocph.utils.ResultDto;
import jp.co.toshiba.ppocph.utils.SecondBeanUtils;
import jp.co.toshiba.ppocph.utils.SnowflakeUtils;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;

/**
 * 都市サービス実装クラス
 *
 * @author ArkamaHozota
 * @since 7.89
 */
@Service
@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public final class CityServiceImpl implements ICityService {

	/**
	 * 共通リポジトリ
	 */
	private final DSLContext dslContext;

	@Override
	public ResultDto<String> checkDuplicated(final String name, final Long districtId) {
		final Integer cityNameCount = this.dslContext.selectCount().from(DISTRICTS).innerJoin(CITIES)
				.on(CITIES.DISTRICT_ID.eq(DISTRICTS.ID)).where(DISTRICTS.ID.eq(districtId)).and(CITIES.NAME.eq(name))
				.fetchSingle().into(Integer.class);
		if (cityNameCount > 0) {
			return ResultDto.failed(PgCrowdConstants.MESSAGE_CITY_NAME_DUPLICATED);
		}
		return ResultDto.successWithoutData();
	}

	@Override
	public Pagination<CityDto> getCitiesByKeyword(final Integer pageNum, final String keyword) {
		final int offset = (pageNum - 1) * PgCrowdConstants.DEFAULT_PAGE_SIZE;
		if (CommonProjectUtils.isEmpty(keyword)) {
			final Integer totalRecords = this.dslContext.selectCount().from(CITIES).innerJoin(DISTRICTS)
					.on(DISTRICTS.ID.eq(CITIES.DISTRICT_ID))
					.where(CITIES.DELETE_FLG.eq(PgCrowdConstants.LOGIC_DELETE_INITIAL)).fetchSingle()
					.into(Integer.class);
			final List<CityDto> cityDtos = this.dslContext
					.select(CITIES.ID, CITIES.NAME, CITIES.DISTRICT_ID, CITIES.PRONUNCIATION, CITIES.CITY_FLAG,
							CITIES.POPULATION, DISTRICTS.NAME.as("districtName"))
					.from(CITIES).innerJoin(DISTRICTS).onKey(Keys.CITIES__FK_CITIES_DISTRICTS)
					.where(CITIES.DELETE_FLG.eq(PgCrowdConstants.LOGIC_DELETE_INITIAL))
					.limit(PgCrowdConstants.DEFAULT_PAGE_SIZE).offset(offset).fetchInto(CityDto.class);
			return Pagination.of(cityDtos, totalRecords, pageNum, PgCrowdConstants.DEFAULT_PAGE_SIZE);
		}
		final String searchStr = CommonProjectUtils.getDetailKeyword(keyword);
		final Integer totalRecords = this.dslContext.selectCount().from(CITIES).innerJoin(DISTRICTS)
				.on(DISTRICTS.ID.eq(CITIES.DISTRICT_ID))
				.where(CITIES.DELETE_FLG.eq(PgCrowdConstants.LOGIC_DELETE_INITIAL)).and(CITIES.NAME.like(searchStr)
						.or(CITIES.PRONUNCIATION.like(searchStr)).or(DISTRICTS.NAME.like(searchStr)))
				.fetchSingle().into(Integer.class);
		final List<CityDto> cityDtos = this.dslContext
				.select(CITIES.ID, CITIES.NAME, CITIES.DISTRICT_ID, CITIES.PRONUNCIATION, CITIES.CITY_FLAG,
						CITIES.POPULATION, DISTRICTS.NAME.as("districtName"))
				.from(CITIES).innerJoin(DISTRICTS).onKey(Keys.CITIES__FK_CITIES_DISTRICTS)
				.where(CITIES.DELETE_FLG.eq(PgCrowdConstants.LOGIC_DELETE_INITIAL))
				.and(CITIES.NAME.like(searchStr).or(CITIES.PRONUNCIATION.like(searchStr))
						.or(DISTRICTS.NAME.like(searchStr)))
				.limit(PgCrowdConstants.DEFAULT_PAGE_SIZE).offset(offset).fetchInto(CityDto.class);
		return Pagination.of(cityDtos, totalRecords, pageNum, PgCrowdConstants.DEFAULT_PAGE_SIZE);
	}

	@Override
	public ResultDto<String> remove(final Long id) {
		this.dslContext.update(CITIES).set(CITIES.DELETE_FLG, PgCrowdConstants.LOGIC_DELETE_FLG).where(CITIES.ID.eq(id))
				.execute();
		return ResultDto.successWithoutData();
	}

	@Override
	public void save(final CityDto cityDto) {
		final CitiesRecord citiesRecord = this.dslContext.newRecord(CITIES);
		SecondBeanUtils.copyNullableProperties(cityDto, citiesRecord);
		citiesRecord.setId(SnowflakeUtils.snowflakeId());
		citiesRecord.setDeleteFlg(PgCrowdConstants.LOGIC_DELETE_INITIAL);
		citiesRecord.insert();
	}

	@Override
	public ResultDto<String> update(final CityDto cityDto) {
		final CitiesRecord citiesRecord = this.dslContext.selectFrom(CITIES)
				.where(CITIES.DELETE_FLG.eq(PgCrowdConstants.LOGIC_DELETE_INITIAL))
				.and(CITIES.ID.eq(Long.parseLong(cityDto.getId()))).fetchSingle();
		final CityDto aCityDto = new CityDto();
		SecondBeanUtils.copyNullableProperties(citiesRecord, aCityDto);
		aCityDto.setId(citiesRecord.getId().toString());
		if (CommonProjectUtils.isEqual(aCityDto, cityDto)) {
			return ResultDto.failed(PgCrowdConstants.MESSAGE_STRING_NOCHANGE);
		}
		SecondBeanUtils.copyNullableProperties(cityDto, citiesRecord);
		try {
			this.dslContext.update(CITIES).set(citiesRecord)
					.where(CITIES.DELETE_FLG.eq(PgCrowdConstants.LOGIC_DELETE_INITIAL))
					.and(CITIES.ID.eq(citiesRecord.getId())).execute();
		} catch (final DataIntegrityViolationException e) {
			return ResultDto.failed(PgCrowdConstants.MESSAGE_CITY_NAME_DUPLICATED);
		}
		return ResultDto.successWithoutData();
	}
}
