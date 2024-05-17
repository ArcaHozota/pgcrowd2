package jp.co.toshiba.ppocph.service.impl;

import static jp.co.toshiba.ppocph.jooq.Tables.CHIHOS;
import static jp.co.toshiba.ppocph.jooq.Tables.CITIES;
import static jp.co.toshiba.ppocph.jooq.Tables.DISTRICTS;
import static jp.co.toshiba.ppocph.jooq.Tables.SHUTOS;

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
import jp.co.toshiba.ppocph.dto.CityDto;
import jp.co.toshiba.ppocph.dto.DistrictDto;
import jp.co.toshiba.ppocph.jooq.Keys;
import jp.co.toshiba.ppocph.jooq.tables.records.ChihosRecord;
import jp.co.toshiba.ppocph.jooq.tables.records.CitiesRecord;
import jp.co.toshiba.ppocph.jooq.tables.records.DistrictsRecord;
import jp.co.toshiba.ppocph.service.IDistrictService;
import jp.co.toshiba.ppocph.utils.CommonProjectUtils;
import jp.co.toshiba.ppocph.utils.Pagination;
import jp.co.toshiba.ppocph.utils.ResultDto;
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
	public List<String> getDistrictChihos(final String chiho) {
		final List<String> chihoList = new ArrayList<>();
		final List<String> chihos = this.dslContext.selectDistinct(CHIHOS.NAME).from(CHIHOS)
				.where(CHIHOS.NAME.ne(chiho)).fetchInto(String.class);
		chihoList.add(chiho);
		chihoList.addAll(chihos);
		return chihoList;
	}

	@Override
	public List<CityDto> getDistrictCities(final DistrictDto districtDto) {
		final List<CityDto> shutos = new ArrayList<>();
		final List<CitiesRecord> districtCities = this.dslContext.selectFrom(CITIES)
				.where(CITIES.DELETE_FLG.eq(PgCrowdConstants.LOGIC_DELETE_INITIAL))
				.and(CITIES.DISTRICT_ID.eq(Long.parseLong(districtDto.getId()))).orderBy(CITIES.ID.asc())
				.fetchInto(CitiesRecord.class);
		final List<CityDto> cityDtos = districtCities.stream().map(item -> {
			final CityDto cityDto = new CityDto();
			cityDto.setId(item.getId().toString());
			cityDto.setName(item.getName());
			return cityDto;
		}).filter(a -> CommonProjectUtils.isNotEqual(a.getId(), districtDto.getShutoId())).collect(Collectors.toList());
		final CityDto cityDto = new CityDto();
		cityDto.setId(districtDto.getShutoId());
		cityDto.setName(districtDto.getShutoName());
		shutos.add(cityDto);
		shutos.addAll(cityDtos);
		return shutos;
	}

	@Override
	public List<DistrictDto> getDistrictsByCityId(final String cityId) {
		final List<DistrictDto> districtDtos = new ArrayList<>();
		final List<DistrictDto> districtDtos1 = this.dslContext
				.select(DISTRICTS.ID, DISTRICTS.NAME, CHIHOS.NAME.as("chiho")).from(DISTRICTS).innerJoin(CHIHOS)
				.onKey(Keys.DISTRICTS__FK_DISTRICTS_CHIHOS)
				.where(DISTRICTS.DELETE_FLG.eq(PgCrowdConstants.LOGIC_DELETE_INITIAL)).orderBy(DISTRICTS.ID.asc())
				.fetchInto(DistrictDto.class);
		if (!CommonProjectUtils.isDigital(cityId)) {
			final DistrictDto districtDto = new DistrictDto();
			districtDto.setId(String.valueOf(0L));
			districtDto.setName(PgCrowdConstants.DEFAULT_ROLE_NAME);
			districtDto.setChiho(CommonProjectUtils.EMPTY_STRING);
			districtDtos.add(districtDto);
		} else {
			final DistrictDto districtDto = this.dslContext
					.select(DISTRICTS.ID, DISTRICTS.NAME, CHIHOS.NAME.as("chiho")).from(DISTRICTS).innerJoin(CHIHOS)
					.onKey(Keys.DISTRICTS__FK_DISTRICTS_CHIHOS).innerJoin(CITIES)
					.onKey(Keys.CITIES__FK_CITIES_DISTRICTS)
					.where(DISTRICTS.DELETE_FLG.eq(PgCrowdConstants.LOGIC_DELETE_INITIAL))
					.and(CITIES.ID.eq(Long.parseLong(cityId))).fetchSingle().into(DistrictDto.class);
			districtDtos.add(districtDto);
		}
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
			final Integer totalRecords = this.dslContext.selectCount().from(DISTRICTS).innerJoin(CHIHOS)
					.onKey(Keys.DISTRICTS__FK_DISTRICTS_CHIHOS).innerJoin(SHUTOS).on(SHUTOS.ID.eq(DISTRICTS.SHUTO_ID))
					.where(DISTRICTS.DELETE_FLG.eq(PgCrowdConstants.LOGIC_DELETE_INITIAL)).fetchSingle()
					.into(Integer.class);
			final List<DistrictDto> districtDtos = this.dslContext
					.select(DISTRICTS.ID, DISTRICTS.NAME, DISTRICTS.SHUTO_ID, SHUTOS.SHUTO_NAME,
							CHIHOS.NAME.as("chiho"), subQueryTable.field("population"), DISTRICTS.DISTRICT_FLAG)
					.from(DISTRICTS).innerJoin(CHIHOS).onKey(Keys.DISTRICTS__FK_DISTRICTS_CHIHOS).innerJoin(SHUTOS)
					.on(SHUTOS.ID.eq(DISTRICTS.SHUTO_ID)).innerJoin(subQueryTable)
					.on(DISTRICTS.ID.eq(subQueryTable.field("districtId", Long.class)))
					.where(DISTRICTS.DELETE_FLG.eq(PgCrowdConstants.LOGIC_DELETE_INITIAL)).orderBy(DISTRICTS.ID.asc())
					.limit(PgCrowdConstants.DEFAULT_PAGE_SIZE).offset(offset).fetchInto(DistrictDto.class);
			return Pagination.of(districtDtos, totalRecords, pageNum, PgCrowdConstants.DEFAULT_PAGE_SIZE);
		}
		final String searchStr = CommonProjectUtils.getDetailKeyword(keyword);
		final Integer totalRecords = this.dslContext.selectCount().from(DISTRICTS).innerJoin(CHIHOS)
				.onKey(Keys.DISTRICTS__FK_DISTRICTS_CHIHOS).innerJoin(SHUTOS).on(SHUTOS.ID.eq(DISTRICTS.SHUTO_ID))
				.where(DISTRICTS.DELETE_FLG.eq(PgCrowdConstants.LOGIC_DELETE_INITIAL)).and(DISTRICTS.NAME
						.like(searchStr).or(CHIHOS.NAME.like(searchStr)).or(SHUTOS.SHUTO_NAME.like(searchStr)))
				.fetchSingle().into(Integer.class);
		final List<DistrictDto> districtDtos = this.dslContext
				.select(DISTRICTS.ID, DISTRICTS.NAME, DISTRICTS.SHUTO_ID, SHUTOS.SHUTO_NAME, CHIHOS.NAME.as("chiho"),
						subQueryTable.field("population"), DISTRICTS.DISTRICT_FLAG)
				.from(DISTRICTS).innerJoin(CHIHOS).onKey(Keys.DISTRICTS__FK_DISTRICTS_CHIHOS).innerJoin(SHUTOS)
				.on(SHUTOS.ID.eq(DISTRICTS.SHUTO_ID)).innerJoin(subQueryTable)
				.on(DISTRICTS.ID.eq(subQueryTable.field("districtId", Long.class)))
				.where(DISTRICTS.DELETE_FLG.eq(PgCrowdConstants.LOGIC_DELETE_INITIAL))
				.and(DISTRICTS.NAME.like(searchStr).or(CHIHOS.NAME.like(searchStr))
						.or(SHUTOS.SHUTO_NAME.like(searchStr)))
				.orderBy(DISTRICTS.ID.asc()).limit(PgCrowdConstants.DEFAULT_PAGE_SIZE).offset(offset)
				.fetchInto(DistrictDto.class);
		return Pagination.of(districtDtos, totalRecords, pageNum, PgCrowdConstants.DEFAULT_PAGE_SIZE);
	}

	@Override
	public ResultDto<String> update(final DistrictDto districtDto) {
		final DistrictDto aDistrictDto = this.dslContext
				.select(DISTRICTS.ID, DISTRICTS.NAME, DISTRICTS.SHUTO_ID, CHIHOS.NAME.as("chiho")).from(DISTRICTS)
				.innerJoin(CHIHOS).onKey(Keys.DISTRICTS__FK_DISTRICTS_CHIHOS)
				.where(DISTRICTS.DELETE_FLG.eq(PgCrowdConstants.LOGIC_DELETE_INITIAL))
				.and(DISTRICTS.ID.eq(Long.parseLong(districtDto.getId()))).fetchSingle().into(DistrictDto.class);
		if (CommonProjectUtils.isEqual(aDistrictDto, districtDto)) {
			return ResultDto.failed(PgCrowdConstants.MESSAGE_STRING_NOCHANGE);
		}
		final DistrictsRecord districtsRecord = this.dslContext.selectFrom(DISTRICTS)
				.where(DISTRICTS.DELETE_FLG.eq(PgCrowdConstants.LOGIC_DELETE_INITIAL))
				.and(DISTRICTS.ID.eq(Long.parseLong(districtDto.getId()))).fetchSingle();
		final ChihosRecord chihosRecord = this.dslContext.selectFrom(CHIHOS)
				.where(CHIHOS.NAME.eq(districtDto.getChiho())).fetchSingle();
		districtsRecord.setName(districtDto.getName());
		districtsRecord.setChihoId(chihosRecord.getId());
		districtsRecord.setShutoId(Long.parseLong(districtDto.getShutoId()));
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
