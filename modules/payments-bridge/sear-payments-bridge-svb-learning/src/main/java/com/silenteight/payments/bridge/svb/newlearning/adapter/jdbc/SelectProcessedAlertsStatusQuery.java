package com.silenteight.payments.bridge.svb.newlearning.adapter.jdbc;

import lombok.RequiredArgsConstructor;

import com.silenteight.payments.bridge.svb.newlearning.domain.AlertsReadingResponse;
import com.silenteight.payments.bridge.svb.newlearning.domain.ReadAlertError;

import org.intellij.lang.annotations.Language;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Component;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static com.silenteight.payments.bridge.svb.newlearning.domain.AlertProcessingResult.FAILED;
import static com.silenteight.payments.bridge.svb.newlearning.domain.AlertProcessingResult.SUCCESSFUL;

@Component
@RequiredArgsConstructor
class SelectProcessedAlertsStatusQuery {

  @Language("PostgreSQL")
  private static final String QUERY =
      "WITH failed AS (SELECT file_name,\n"
          + "                       count(*) AS count,\n"
          + "                       array_agg(error_message) AS error_messages,\n"
          + "                       array_agg(fkco_v_system_id) AS error_system_ids\n"
          + "                FROM pb_learning_processed_alert\n"
          + "                WHERE job_id = :jobId\n"
          + "                  AND file_name = :fileName\n"
          + "                  AND result = :faield\n"
          + "                GROUP BY 1),\n"
          + "     succes AS (SELECT file_name,\n"
          + "                       count(*) AS count\n"
          + "                FROM pb_learning_processed_alert\n"
          + "                WHERE job_id = :jobId\n"
          + "                  AND file_name = :fileName\n"
          + "                  AND result = :succes\n"
          + "                GROUP BY 1)\n"
          + "SELECT f.count AS failed_count,\n"
          + "       s.count AS succes_count,\n"
          + "       s.file_name AS file_name,\n"
          + "       f.error_messages AS errors,\n"
          + "       f.error_system_ids AS error_system_ids\n"
          + "FROM failed f\n"
          + "         JOIN succes s ON s.file_name = f.file_name\n"
          + "LIMIT 1";

  private final NamedParameterJdbcTemplate jdbcTemplate;

  public AlertsReadingResponse select(long jobId, String fileName) {
    var result = jdbcTemplate
        .query(
            QUERY,
            Map.of("jobId", jobId,
                "fileName", fileName,
                "succes", SUCCESSFUL.toString(),
                "faield", FAILED.toString()),
            (rs, rowNum) -> AlertsReadingResponse
                .builder()
                .failedAlerts(rs.getInt("failed_count"))
                .successfulAlerts(rs.getInt("succes_count"))
                .fileName(rs.getString("file_name"))
                .errors(readAlertErrorsMapper(rs))
                .build());

    if (result.isEmpty()) {
      return AlertsReadingResponse
          .builder()
          .errors(List.of())
          .failedAlerts(0)
          .successfulAlerts(0)
          .fileName(fileName)
          .build();
    }

    return result.get(0);
  }

  private static List<ReadAlertError> readAlertErrorsMapper(ResultSet rs) throws SQLException {
    String[] errors = (String[]) rs.getArray("errors").getArray();
    String[] systemIds = (String[]) rs.getArray("error_system_ids").getArray();

    return IntStream.range(0, errors.length)
        .mapToObj(index -> new ReadAlertError(systemIds[index], errors[index]))
        .collect(Collectors.toList());
  }
}
