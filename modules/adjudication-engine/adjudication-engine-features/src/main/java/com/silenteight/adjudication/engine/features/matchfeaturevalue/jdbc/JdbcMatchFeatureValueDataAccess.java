package com.silenteight.adjudication.engine.features.matchfeaturevalue.jdbc;

import com.silenteight.adjudication.engine.features.matchfeaturevalue.MatchFeatureValueDataAccess;
import com.silenteight.adjudication.engine.features.matchfeaturevalue.dto.MatchFeatureValue;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
class JdbcMatchFeatureValueDataAccess implements MatchFeatureValueDataAccess {

  private final InsertMatchFeatureValueBatchQuery query;
  private final DeleteMatchFeatureValueBatchQuery deleteQuery;

  JdbcMatchFeatureValueDataAccess(JdbcTemplate template, NamedParameterJdbcTemplate namedTemplate) {
    query = new InsertMatchFeatureValueBatchQuery(template);
    deleteQuery = new DeleteMatchFeatureValueBatchQuery(namedTemplate);
  }

  @Transactional
  @Override
  public int saveAll(Iterable<MatchFeatureValue> featureValues) {
    return query.execute(featureValues);
  }

  @Transactional
  @Override
  public int delete(Iterable<String> features) {
    return deleteQuery.execute(features);
  }
}
