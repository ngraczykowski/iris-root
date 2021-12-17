package com.silenteight.payments.bridge.svb.newlearning.step.composite;

import lombok.extern.slf4j.Slf4j;

import com.silenteight.payments.bridge.svb.newlearning.domain.AlertComposite;
import com.silenteight.payments.bridge.svb.newlearning.domain.AlertDetails;
import com.silenteight.payments.bridge.svb.newlearning.step.composite.exception.FetchingComposeDataException;

import org.intellij.lang.annotations.Language;
import org.springframework.stereotype.Service;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import javax.sql.DataSource;

import static com.silenteight.payments.bridge.svb.newlearning.step.composite.AlertDetailsRowMapper.mapRow;
import static java.util.stream.Collectors.toList;

@Slf4j
@Service
class AlertCompositeFetcher extends BaseCompositeFetcher<List<Long>, List<AlertComposite>> {

  @Language("PostgreSQL")
  private static final String ALERTS_QUERY =
      "SELECT learning_alert_id, fkco_id "
          + "FROM pb_learning_alert WHERE fkco_id IN (%s)";

  private final HitCompositeFetcher hitCompositeFetcher;
  private final ActionCompositeFetcher actionCompositeFetcher;

  public AlertCompositeFetcher(
      DataSource dataSource, HitCompositeFetcher hitCompositeFetcher,
      ActionCompositeFetcher actionCompositeFetcher) {
    super(dataSource);
    this.hitCompositeFetcher = hitCompositeFetcher;
    this.actionCompositeFetcher = actionCompositeFetcher;
  }

  @Override
  protected List<AlertComposite> fetchWithConnection(Connection connection, List<Long> alertIds) {
    var alertsDetails = fetchAlertsDetails(connection, alertIds);
    var fkcoIds = alertsDetails.stream().map(AlertDetails::getFkcoId).collect(toList());
    var hits = hitCompositeFetcher.fetchWithConnection(connection, fkcoIds);
    var actions = actionCompositeFetcher.fetchWithConnection(connection, fkcoIds);
    return alertsDetails.stream().map(ad -> ad.toAlertComposite(hits, actions)).collect(toList());
  }

  private static List<AlertDetails> fetchAlertsDetails(Connection connection, List<Long> alertIds) {
    var preparedQuery = prepareQuery(ALERTS_QUERY, alertIds);

    try (PreparedStatement statement = connection.prepareStatement(preparedQuery)) {
      setQueryParameters(statement, alertIds);
      try (ResultSet resultSet = statement.executeQuery()) {
        return createAlerts(resultSet);
      }
    } catch (SQLException e) {
      log.error("Failed do fetch alert details: {}", e.getMessage());
      throw new FetchingComposeDataException(e);
    }
  }

  private static List<AlertDetails> createAlerts(ResultSet resultSet) throws
      SQLException {

    var result = new ArrayList<AlertDetails>();
    while (resultSet.next()) {
      var alert = mapRow(resultSet);
      result.add(alert);
    }
    return result;
  }
}
