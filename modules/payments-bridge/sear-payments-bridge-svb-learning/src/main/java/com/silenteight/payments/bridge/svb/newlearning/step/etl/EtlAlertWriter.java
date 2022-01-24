package com.silenteight.payments.bridge.svb.newlearning.step.etl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.intellij.lang.annotations.Language;
import org.springframework.batch.item.ItemWriter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.SqlParameter;
import org.springframework.jdbc.object.BatchSqlUpdate;
import org.springframework.stereotype.Service;

import java.sql.Types;
import java.util.List;

import static java.util.stream.Collectors.toList;

@RequiredArgsConstructor
@Slf4j
@Service
class EtlAlertWriter implements ItemWriter<LearningProcessedAlert> {


  @Language("PostgreSQL")
  private static final String SQL =
      "INSERT INTO pb_learning_processed_alert (job_id, "
          + "result, error_message, fkco_v_system_id, file_name, created_at)\n"
          + "VALUES (?, ?, ?, ?, ?, now())\n";
  private final JdbcTemplate jdbcTemplate;

  @Override
  public void write(List<? extends LearningProcessedAlert> items) {
    log.info(
        "Writing processed alerts = {} with result = {}",
        items.stream().map(LearningProcessedAlert::getFkcoVSystemId).collect(
            toList()),
        items.stream().map(LearningProcessedAlert::getResult).collect(
            toList()));
    update(items);
  }

  void update(List<? extends LearningProcessedAlert> alerts) {
    var batchSqlUpdate = createQuery();

    alerts.forEach(alert -> batchSqlUpdate.update(
        alert.getJobId(),
        alert.getResult(),
        alert.getErrorMessage(),
        alert.getFkcoVSystemId(),
        alert.getFkcoVSystemId()));
    batchSqlUpdate.flush();
  }

  private BatchSqlUpdate createQuery() {
    var batchSqlUpdate = new BatchSqlUpdate();
    batchSqlUpdate.setJdbcTemplate(jdbcTemplate);
    batchSqlUpdate.setSql(SQL);
    batchSqlUpdate.declareParameter(new SqlParameter("job_id", Types.BIGINT));
    batchSqlUpdate.declareParameter(new SqlParameter("result", Types.VARCHAR));
    batchSqlUpdate.declareParameter(new SqlParameter("error_message", Types.VARCHAR));
    batchSqlUpdate.declareParameter(new SqlParameter("fkco_v_system_id", Types.VARCHAR));
    batchSqlUpdate.declareParameter(new SqlParameter("file_name", Types.VARCHAR));
    batchSqlUpdate.compile();
    return batchSqlUpdate;
  }
}
