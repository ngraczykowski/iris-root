package com.silenteight.customerbridge.cbs.gateway;

import com.silenteight.customerbridge.cbs.domain.CbsHitDetails;
import com.silenteight.customerbridge.cbs.domain.NeoFlag;

import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Optional;

class CbsHitDetailsRowMapper implements RowMapper<CbsHitDetails> {

  private static final String SYSTEM_ID_COLUMN_NAME = "system_id";
  private static final String BATCH_ID_COLUMN_NAME = "batch_id";
  private static final String SEQ_NO_COLUMN_NAME = "seq_no";
  private static final String HIT_NEO_FLAG_COLUMN_NAME = "hit_neo_flag";

  @Override
  public CbsHitDetails mapRow(ResultSet resultSet, int i) throws SQLException {
    var builder = CbsHitDetails.builder()
        .systemId(resultSet.getString(SYSTEM_ID_COLUMN_NAME))
        .batchId(resultSet.getString(BATCH_ID_COLUMN_NAME))
        .seqNo(resultSet.getInt(SEQ_NO_COLUMN_NAME));

    if (isNeoFlagPresent(resultSet)) {
      Optional<NeoFlag> neoFlag = NeoFlag.parse(resultSet.getString(HIT_NEO_FLAG_COLUMN_NAME));
      neoFlag.ifPresent(builder::hitNeoFlag);
    }

    return builder.build();
  }

  private static boolean isNeoFlagPresent(ResultSet resultSet) throws SQLException {
    return resultSet.getString(HIT_NEO_FLAG_COLUMN_NAME) != null;
  }
}
