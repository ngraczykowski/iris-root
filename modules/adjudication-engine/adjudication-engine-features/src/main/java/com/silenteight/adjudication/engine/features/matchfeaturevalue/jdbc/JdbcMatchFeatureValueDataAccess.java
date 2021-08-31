package com.silenteight.adjudication.engine.features.matchfeaturevalue.jdbc;

import com.silenteight.adjudication.engine.features.matchfeaturevalue.MatchFeatureValueDataAccess;
import com.silenteight.adjudication.engine.features.matchfeaturevalue.dto.MatchFeatureValue;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
class JdbcMatchFeatureValueDataAccess implements MatchFeatureValueDataAccess {

  private final InsertMatchFeatureValueBatchQuery query;

  JdbcMatchFeatureValueDataAccess(JdbcTemplate template) {
    query = new InsertMatchFeatureValueBatchQuery(template);
  }

  @Transactional
  @Override
  public int saveAll(Iterable<MatchFeatureValue> featureValues) {
    return query.execute(featureValues);
  }
}
