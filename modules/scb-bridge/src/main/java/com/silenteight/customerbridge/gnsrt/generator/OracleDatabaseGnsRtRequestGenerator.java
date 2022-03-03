package com.silenteight.customerbridge.gnsrt.generator;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import com.silenteight.customerbridge.common.alertrecord.AlertRecord;
import com.silenteight.customerbridge.common.alertrecord.AlertRecordMapper;
import com.silenteight.customerbridge.gnsrt.model.request.GnsRtRecommendationRequest;

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

  private List<AlertRecord> fetchData(String filterColumn, String value) {
    String query = prepareQuery(filterColumn);
    List<AlertRecord> alerts = jdbcTemplate.query(query, new Object[] { value }, ROW_MAPPER);
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
}
