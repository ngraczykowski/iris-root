package com.silenteight.adjudication.engine.analysis.matchrecommendation.jdbc;

import com.silenteight.adjudication.engine.analysis.matchrecommendation.domain.PendingMatch;
import com.silenteight.sep.base.common.support.jackson.JsonConversionHelper;
import com.silenteight.solving.api.v1.FeatureVectorSolution;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.intellij.lang.annotations.Language;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import javax.sql.DataSource;

class SelectPendingMatchesQuery {

  private final JdbcTemplate jdbcTemplate;
  private static final PendingMatchMapper ROW_MAPPER = new PendingMatchMapper();
  @Language("PostgreSQL")
  private static final String SQL = "SELECT ams.match_id, ams.solution, ams.match_context\n"
      + "FROM ae_match_solution ams\n"
      + "         LEFT JOIN ae_match_recommendation amr\n"
      + "                   ON ams.match_id = amr.match_id AND ams.analysis_id = amr.analysis_id\n"
      + "WHERE amr.match_recommendation_id IS NULL\n"
      + "AND ams.analysis_id = ?\n"
      + "GROUP BY 1, 2, 3;";

  SelectPendingMatchesQuery(int fetchSize, DataSource dataSource) {
    if (fetchSize == 0) {
      throw new IllegalArgumentException(
          String.format("Fetch size %s must be greater than 0", fetchSize));
    }

    jdbcTemplate = new JdbcTemplate(dataSource, true);
    jdbcTemplate.setFetchSize(fetchSize);
    jdbcTemplate.setMaxRows(fetchSize);
  }

  List<PendingMatch> execute(long analysisId) {
    return jdbcTemplate.query(SQL, ROW_MAPPER, analysisId);
  }

  private static final class PendingMatchMapper implements RowMapper<PendingMatch> {

    private static final ObjectMapper MAPPER = JsonConversionHelper.INSTANCE.objectMapper();

    @Override
    public PendingMatch mapRow(ResultSet rs, int rowNum) throws SQLException {
      var matchId = rs.getObject(1, Long.class);
      var matchSolution = rs.getString(2);
      var matchContext = rs.getString(3);

      return PendingMatch.builder()
          .matchId(matchId)
          .matchSolution(FeatureVectorSolution.valueOf(matchSolution))
          .matchContexts(readMatchContext(matchContext))
          .build();
    }

    private static ObjectNode readMatchContext(String matchContext) {
      try {
        return MAPPER.readValue(matchContext, ObjectNode.class);
      } catch (JsonProcessingException e) {
        throw new MatchContextsJsonNodeReadException(e);
      }
    }
  }

  static class MatchContextsJsonNodeReadException extends RuntimeException {

    private static final long serialVersionUID = 3876426976327990398L;

    MatchContextsJsonNodeReadException(Throwable cause) {
      super(cause);
    }
  }
}
