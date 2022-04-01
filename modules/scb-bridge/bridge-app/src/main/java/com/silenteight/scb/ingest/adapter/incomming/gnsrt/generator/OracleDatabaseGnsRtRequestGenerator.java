package com.silenteight.scb.ingest.adapter.incomming.gnsrt.generator;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import com.silenteight.scb.ingest.adapter.incomming.common.alertrecord.AlertRecord;
import com.silenteight.scb.ingest.adapter.incomming.common.alertrecord.AlertRecordMapper;
import com.silenteight.scb.ingest.adapter.incomming.gnsrt.model.request.GnsRtRecommendationRequest;

import org.intellij.lang.annotations.Language;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.lang.Nullable;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import static org.springframework.util.CollectionUtils.isEmpty;

@RequiredArgsConstructor
class OracleDatabaseGnsRtRequestGenerator implements GnsRtRequestGenerator {

  private static final GnsRtRequestRecordDataMapper ROW_MAPPER = new GnsRtRequestRecordDataMapper();
  private static final GnsRtRequestWithRandomSystemIdMapper ROW_MAPPER_WITH_RANDOM_SYSTEM_ID =
      new GnsRtRequestWithRandomSystemIdMapper();
  @Language("Oracle")
  private static final String QUERY_TEMPLATE = "SELECT R.SYSTEM_ID,\n"
      + "       R.BATCH_ID,\n"
      + "       R.CHAR_SEP,\n"
      + "       R.DB_ACCOUNT,\n"
      + "       R.DB_CITY,\n"
      + "       R.DB_COUNTRY,\n"
      + "       R.DB_DOB,\n"
      + "       R.DB_NAME,\n"
      + "       R.DB_POB,\n"
      + "       R.FILTERED,\n"
      + "       R.RECORD,\n"
      + "       R.RECORD_ID,\n"
      + "       R.TYPE_OF_REC,\n"
      + "       R.LAST_DEC_BATCH_ID,\n"
      + "       R.UNIT,\n"
      + "       R.FMT_NAME,\n"
      + "       H.DETAILS\n"
      + "FROM FFF_RECORDS R\n"
      + "         JOIN FFF_HITS_DETAILS H ON H.SYSTEM_ID = R.SYSTEM_ID\n"
      + "WHERE R.<filter_column> = ?";

  @Language("Oracle")
  private static final String QUERY_ONE_ALERT_TEMPLATE = "SELECT * FROM (SELECT R.SYSTEM_ID,\n"
      + "       R.BATCH_ID,\n"
      + "       R.CHAR_SEP,\n"
      + "       R.DB_ACCOUNT,\n"
      + "       R.DB_CITY,\n"
      + "       R.DB_COUNTRY,\n"
      + "       R.DB_DOB,\n"
      + "       R.DB_NAME,\n"
      + "       R.DB_POB,\n"
      + "       R.FILTERED,\n"
      + "       R.RECORD,\n"
      + "       R.RECORD_ID,\n"
      + "       R.TYPE_OF_REC,\n"
      + "       R.LAST_DEC_BATCH_ID,\n"
      + "       R.UNIT,\n"
      + "       R.FMT_NAME,\n"
      + "       H.DETAILS\n"
      + "FROM FFF_RECORDS SAMPLE(1) R\n"
      + "         JOIN FFF_HITS_DETAILS H ON H.SYSTEM_ID = R.SYSTEM_ID)\n"
      + "WHERE ROWNUM = 1";

  private final JdbcTemplate jdbcTemplate;
  private final GnsRtRequestMapper mapper;

  @Override
  public GnsRtRecommendationRequest generateBySystemId(@NonNull String systemId) {
    return mapper.map(fetchData("SYSTEM_ID", systemId));
  }

  @Override
  public GnsRtRecommendationRequest generateByRecordId(@NonNull String recordId) {
    return mapper.map(fetchData("RECORD_ID", recordId));
  }

  @Override
  public GnsRtRecommendationRequest generateWithRandomSystemId() {
    return mapper.map(fetchData());
  }

  private List<AlertRecord> fetchData(String filterColumn, String value) {
    var query = prepareQuery(filterColumn);
    var alerts = jdbcTemplate.query(query, new Object[] { value }, ROW_MAPPER);
    if (isEmpty(alerts))
      throw new EmptyResultDataAccessException(1);

    return alerts;
  }

  private List<AlertRecord> fetchData() {
    var alerts =
        jdbcTemplate.query(QUERY_ONE_ALERT_TEMPLATE, ROW_MAPPER_WITH_RANDOM_SYSTEM_ID);
    if (isEmpty(alerts))
      throw new EmptyResultDataAccessException(1);

    return alerts;
  }

  private static String prepareQuery(String filterColumn) {
    return QUERY_TEMPLATE.replace("<filter_column>", filterColumn);
  }

  private static class GnsRtRequestRecordDataMapper implements RowMapper<AlertRecord> {

    @Nullable
    @Override
    public AlertRecord mapRow(ResultSet resultSet, int rowNum) throws SQLException {
      return AlertRecordMapper.mapResultSet(resultSet);
    }
  }

  private static class GnsRtRequestWithRandomSystemIdMapper implements RowMapper<AlertRecord> {

    @Nullable
    @Override
    public AlertRecord mapRow(ResultSet resultSet, int rowNum) throws SQLException {
      return AlertRecordWithRandomSystemIdMapper.mapResultSet(resultSet);
    }
  }
}
