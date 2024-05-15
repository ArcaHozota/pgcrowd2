package jp.co.toshiba.ppocph.service.impl;

import static jp.co.toshiba.ppocph.jooq.Tables.CITIES;
import static jp.co.toshiba.ppocph.jooq.Tables.DISTRICTS;

import java.util.List;

import org.jooq.DSLContext;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import jp.co.toshiba.ppocph.dto.CitiesRecordDto;
import jp.co.toshiba.ppocph.dto.CityDto;
import jp.co.toshiba.ppocph.jooq.tables.records.CitiesRecord;
import jp.co.toshiba.ppocph.service.ICityService;
import jp.co.toshiba.ppocph.utils.CommonProjectUtils;
import jp.co.toshiba.ppocph.utils.Pagination;
import jp.co.toshiba.ppocph.utils.ResultDto;
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
			return ResultDto.failed(OgumaProjectConstants.MESSAGE_CITY_NAME_DUPLICATED);
		}
		return ResultDto.successWithoutData();
	}

	@Override
	public Pagination<CityDto> getCitiesByKeyword(final Integer pageNum, final String keyword) {
		final int offset = (pageNum - 1) * OgumaProjectConstants.DEFAULT_PAGE_SIZE;
		if (CommonProjectUtils.isEmpty(keyword)) {
			final Integer totalRecords = this.dslContext.selectCount().from(CITIES).innerJoin(DISTRICTS)
					.on(DISTRICTS.ID.eq(CITIES.DISTRICT_ID))
					.where(CITIES.DELETE_FLG.eq(OgumaProjectConstants.LOGIC_DELETE_INITIAL)).fetchSingle()
					.into(Integer.class);
			final List<CitiesRecordDto> citiesRecords = this.dslContext
					.select(CITIES, DISTRICTS.NAME.as("districtName")).from(CITIES).innerJoin(DISTRICTS)
					.on(DISTRICTS.ID.eq(CITIES.DISTRICT_ID))
					.where(CITIES.DELETE_FLG.eq(OgumaProjectConstants.LOGIC_DELETE_INITIAL))
					.limit(OgumaProjectConstants.DEFAULT_PAGE_SIZE).offset(offset).fetchInto(CitiesRecordDto.class);
			final List<CityDto> cityDtos = citiesRecords.stream()
					.map(item -> new CityDto(item.getId(), item.getName(), item.getDistrictId(),
							item.getPronunciation(), item.getDistrictName(), item.getPopulation(), item.getCityFlag()))
					.toList();
			return Pagination.of(cityDtos, totalRecords, pageNum, OgumaProjectConstants.DEFAULT_PAGE_SIZE);
		}
		final String searchStr = CommonProjectUtils.getDetailKeyword(keyword);
		final Integer totalRecords = this.dslContext.selectCount().from(CITIES).innerJoin(DISTRICTS)
				.on(DISTRICTS.ID.eq(CITIES.DISTRICT_ID))
				.where(CITIES.DELETE_FLG.eq(OgumaProjectConstants.LOGIC_DELETE_INITIAL)).and(CITIES.NAME.like(searchStr)
						.or(CITIES.PRONUNCIATION.like(searchStr)).or(DISTRICTS.NAME.like(searchStr)))
				.fetchSingle().into(Integer.class);
		final List<CitiesRecordDto> citiesRecords = this.dslContext.select(CITIES, DISTRICTS.NAME.as("districtName"))
				.from(CITIES).innerJoin(DISTRICTS).on(DISTRICTS.ID.eq(CITIES.DISTRICT_ID))
				.where(CITIES.DELETE_FLG.eq(OgumaProjectConstants.LOGIC_DELETE_INITIAL))
				.and(CITIES.NAME.like(searchStr).or(CITIES.PRONUNCIATION.like(searchStr))
						.or(DISTRICTS.NAME.like(searchStr)))
				.limit(OgumaProjectConstants.DEFAULT_PAGE_SIZE).offset(offset).fetchInto(CitiesRecordDto.class);
		final List<CityDto> cityDtos = citiesRecords
				.stream().map(item -> new CityDto(item.getId(), item.getName(), item.getDistrictId(),
						item.getPronunciation(), item.getDistrictName(), item.getPopulation(), item.getCityFlag()))
				.toList();
		return Pagination.of(cityDtos, totalRecords, pageNum, OgumaProjectConstants.DEFAULT_PAGE_SIZE);
	}

	@Override
	public ResultDto<String> remove(final Long id) {
		this.dslContext.update(CITIES).set(CITIES.DELETE_FLG, OgumaProjectConstants.LOGIC_DELETE_FLG)
				.where(CITIES.ID.eq(id)).execute();
		return ResultDto.successWithoutData();
	}

	@Override
	public void save(final CityDto cityDto) {
		final CitiesRecord citiesRecord = this.dslContext.newRecord(CITIES);
		citiesRecord.setId(SnowflakeUtils.snowflakeId());
		citiesRecord.setName(cityDto.name());
		citiesRecord.setDistrictId(cityDto.districtId());
		citiesRecord.setPronunciation(cityDto.pronunciation());
		citiesRecord.setPopulation(cityDto.population());
		citiesRecord.setCityFlag(cityDto.cityFlag());
		citiesRecord.setDeleteFlg(OgumaProjectConstants.LOGIC_DELETE_INITIAL);
		citiesRecord.insert();
	}

	@Override
	public ResultDto<String> update(final CityDto cityDto) {
		final CitiesRecord citiesRecord = this.dslContext.selectFrom(CITIES)
				.where(CITIES.DELETE_FLG.eq(OgumaProjectConstants.LOGIC_DELETE_INITIAL)).and(CITIES.ID.eq(cityDto.id()))
				.fetchSingle();
		final CityDto aCityDto = new CityDto(citiesRecord.getId(), citiesRecord.getName(), citiesRecord.getDistrictId(),
				citiesRecord.getPronunciation(), null, citiesRecord.getPopulation(), null);
		if (CommonProjectUtils.isEqual(aCityDto, cityDto)) {
			return ResultDto.failed(OgumaProjectConstants.MESSAGE_STRING_NOCHANGE);
		}
		citiesRecord.setName(cityDto.name());
		citiesRecord.setDistrictId(cityDto.districtId());
		citiesRecord.setPopulation(cityDto.population());
		citiesRecord.setPronunciation(cityDto.pronunciation());
		try {
			this.dslContext.update(CITIES).set(citiesRecord)
					.where(CITIES.DELETE_FLG.eq(OgumaProjectConstants.LOGIC_DELETE_INITIAL))
					.and(CITIES.ID.eq(citiesRecord.getId())).execute();
		} catch (final DataIntegrityViolationException e) {
			return ResultDto.failed(OgumaProjectConstants.MESSAGE_CITY_NAME_DUPLICATED);
		}
		return ResultDto.successWithoutData();
	}
}
