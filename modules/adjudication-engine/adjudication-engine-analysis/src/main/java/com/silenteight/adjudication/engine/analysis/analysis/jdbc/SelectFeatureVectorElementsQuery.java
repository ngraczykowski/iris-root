package com.silenteight.adjudication.engine.analysis.analysis.jdbc;

import lombok.RequiredArgsConstructor;

import com.silenteight.adjudication.engine.analysis.analysis.domain.PolicyAndFeatureVectorElements;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

import java.sql.ResultSet;
import java.sql.SQLException;

@RequiredArgsConstructor
@Component
public class SelectFeatureVectorElementsQuery {

  private static final PolicyAndFeatureVectorElementsMapper ROW_MAPPER =
      new PolicyAndFeatureVectorElementsMapper();

  private final JdbcTemplate jdbcTemplate;

  PolicyAndFeatureVectorElements execute(long analysisId) {
    return jdbcTemplate.queryForObject(
        "SELECT aa.policy, aafveq.category_names, aafveq.feature_names\n"
            + "FROM ae_analysis_feature_vector_elements_query aafveq\n"
            + "         JOIN ae_analysis aa ON aafveq.analysis_id = aa.analysis_id\n"
            + "WHERE aafveq.analysis_id = ?",
        ROW_MAPPER, analysisId);
  }

  private static final class PolicyAndFeatureVectorElementsMapper
      implements RowMapper<PolicyAndFeatureVectorElements> {

    @Override
    public PolicyAndFeatureVectorElements mapRow(ResultSet rs, int rowNum)
        throws SQLException {

      return new PolicyAndFeatureVectorElements(
          rs.getString(1), getFeatureElements(rs, 2), getFeatureElements(rs, 3));
    }

    private static String[] getFeatureElements(ResultSet rs, int columnIndex)
        throws SQLException {

      return (String[]) rs.getArray(columnIndex).getArray();
    }
  }
}
