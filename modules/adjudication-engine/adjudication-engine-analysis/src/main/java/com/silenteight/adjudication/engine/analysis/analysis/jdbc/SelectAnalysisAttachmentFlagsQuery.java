package com.silenteight.adjudication.engine.analysis.analysis.jdbc;

import lombok.RequiredArgsConstructor;

import com.silenteight.adjudication.engine.analysis.analysis.domain.AnalysisAttachmentFlags;
import com.silenteight.sep.base.aspects.metrics.Timed;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

import java.sql.ResultSet;
import java.sql.SQLException;

@RequiredArgsConstructor
@Component
class SelectAnalysisAttachmentFlagsQuery {

  private final JdbcTemplate jdbcTemplate;
  private static final AnalysisAttachmentFlagsMapper MAPPER = new AnalysisAttachmentFlagsMapper();

  @Timed(percentiles = { 0.5, 0.95, 0.99 }, histogram = true)
  AnalysisAttachmentFlags execute(long analysisId) {
    var result = jdbcTemplate.queryForObject(
        "SELECT  a.attach_recommendation, a.attach_metadata \n"
            + "FROM ae_analysis a \n"
            + "WHERE a.analysis_id=?", MAPPER, analysisId);
    return result;
  }

  private static final class AnalysisAttachmentFlagsMapper
      implements RowMapper<AnalysisAttachmentFlags> {

    @Override
    public AnalysisAttachmentFlags mapRow(ResultSet rs, int rowNum)
        throws SQLException {
      return new AnalysisAttachmentFlags(rs.getBoolean(1), rs.getBoolean(2));
    }
  }
}
