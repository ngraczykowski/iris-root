package com.silenteight.adjudication.engine.analysis.categoryrequest.jdbc;

import lombok.extern.slf4j.Slf4j;

import com.silenteight.adjudication.engine.analysis.categoryrequest.domain.CategoryMap;
import com.silenteight.adjudication.engine.analysis.categoryrequest.domain.MatchAlert;
import com.silenteight.adjudication.engine.analysis.categoryrequest.domain.MissingCategoryResult;
import com.silenteight.adjudication.engine.analysis.categoryrequest.domain.MissingMatchCategory;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.type.CollectionType;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.ResultSetExtractor;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static org.apache.commons.lang3.exception.ExceptionUtils.rethrow;

@Slf4j
class SelectMissingMatchCategoryValuesQuery {

  private static final String QUERY = "WITH missing_match_category_values AS (\n"
      + "    SELECT apr.analysis_id                 as analysis_id,\n"
      + "           ac.category                     as category,\n"
      + "           ac.category_id                  as category_id,\n"
      + "           jsonb_build_object(\n"
      + "                   'matchId', am.match_id,\n"
      + "                   'alertId', am.alert_id) as match_alert\n"
      + "    FROM ae_pending_recommendation apr\n"
      + "             JOIN ae_match am ON am.alert_id = apr.alert_id\n"
      + "             JOIN ae_analysis_category aac ON apr.analysis_id = aac.analysis_id\n"
      + "             JOIN ae_category ac ON ac.category_id = aac.category_id\n"
      + "             LEFT JOIN ae_match_category_value amcv\n"
      + "                       ON amcv.match_id = am.match_id AND\n"
      + "                          amcv.category_id = ac.category_id\n"
      + "    WHERE amcv.category_id IS NULL\n"
      + "    LIMIT ?\n"
      + ")\n"
      + "SELECT mmcv.analysis_id,\n"
      + "       mmcv.category,\n"
      + "       mmcv.category_id,\n"
      + "       jsonb_agg(mmcv.match_alert) as match_alerts\n"
      + "FROM missing_match_category_values mmcv\n"
      + "WHERE mmcv.analysis_id = ?\n"
      + "GROUP BY 1, 2, 3";

  private final JdbcTemplate jdbcTemplate;
  private final ObjectMapper objectMapper;
  private final int batchSize;
  private final CollectionType matchAlertListType;

  SelectMissingMatchCategoryValuesQuery(
      ObjectMapper objectMapper, JdbcTemplate jdbcTemplate, int batchSize) {

    this.objectMapper = objectMapper;
    this.jdbcTemplate = jdbcTemplate;
    this.batchSize = batchSize;

    matchAlertListType = this.objectMapper
        .getTypeFactory()
        .constructCollectionType(ArrayList.class, MatchAlert.class);
  }

  MissingCategoryResult execute(long analysisId) {
    return jdbcTemplate.query(
        QUERY,
        new SqlMissingMatchCategoryExtractor(),
        batchSize, analysisId);
  }

  private final class SqlMissingMatchCategoryExtractor implements
      ResultSetExtractor<MissingCategoryResult> {

    @SuppressWarnings("InnerClassTooDeeplyNested")
    @Override
    public MissingCategoryResult extractData(ResultSet rs) throws SQLException {
      var missingMatchCategories = new ArrayList<MissingMatchCategory>();
      var categories = new HashMap<String, Long>();

      while (rs.next()) {
        var category = rs.getString("category");
        var categoryId = rs.getLong("category_id");

        var matchAlerts = deserializeMatchAlerts(rs);
        missingMatchCategories.add(
            MissingMatchCategory.builder().categoryName(category).matches(matchAlerts).build());
        categories.putIfAbsent(category, categoryId);
      }

      return new MissingCategoryResult(missingMatchCategories, new CategoryMap(categories));
    }

    private List<MatchAlert> deserializeMatchAlerts(ResultSet rs) throws SQLException {
      try {
        return objectMapper.readValue(rs.getString("match_alerts"), matchAlertListType);
      } catch (JsonProcessingException e) {
        return rethrow(e);
      }
    }
  }
}
