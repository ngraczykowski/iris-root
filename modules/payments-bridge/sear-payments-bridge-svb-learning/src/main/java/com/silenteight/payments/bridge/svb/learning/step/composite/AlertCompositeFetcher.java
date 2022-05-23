package com.silenteight.payments.bridge.svb.learning.step.composite;

import lombok.extern.slf4j.Slf4j;

import com.silenteight.payments.bridge.common.agents.ContextualLearningProperties;
import com.silenteight.payments.bridge.common.app.LearningProperties;
import com.silenteight.payments.bridge.svb.learning.domain.AlertComposite;
import com.silenteight.payments.bridge.svb.learning.domain.AlertDetails;
import com.silenteight.payments.bridge.svb.learning.step.composite.exception.FetchingComposeDataException;

import org.intellij.lang.annotations.Language;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.stereotype.Service;
import org.springframework.util.IdGenerator;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import javax.sql.DataSource;

import static com.silenteight.payments.bridge.svb.learning.step.composite.AlertDetailsRowMapper.mapRow;
import static java.util.stream.Collectors.toList;

@Slf4j
@Service
@EnableConfigurationProperties(ContextualLearningProperties.class)
class AlertCompositeFetcher extends BaseCompositeFetcher<List<Long>, List<AlertComposite>> {

  @Language("PostgreSQL")
  private static final String ALERTS_QUERY =
      "SELECT *\n"
          + "FROM pb_learning_alert\n"
          + "WHERE learning_alert_id IN (%s)";

  private final HitCompositeFetcher hitCompositeFetcher;
  private final ActionCompositeFetcher actionCompositeFetcher;
  private final LearningProperties properties;
  private final ContextualLearningProperties contextualLearningProperties;


  public AlertCompositeFetcher(
      DataSource dataSource, IdGenerator idGenerator, HitCompositeFetcher hitCompositeFetcher,
      ActionCompositeFetcher actionCompositeFetcher,
      LearningProperties properties,
      ContextualLearningProperties contextualLearningProperties) {
    super(dataSource);
    this.hitCompositeFetcher = hitCompositeFetcher;
    this.actionCompositeFetcher = actionCompositeFetcher;
    this.properties = properties;
    this.contextualLearningProperties = contextualLearningProperties;
  }

  @Override
  protected List<AlertComposite> fetchWithConnection(Connection connection, List<Long> alertIds) {
    var alertsDetails = fetchAlertsDetails(connection, alertIds);
    var fkcoIds = alertsDetails.stream().map(AlertDetails::getFkcoId).collect(toList());
    var hits = hitCompositeFetcher.fetchWithConnection(connection, fkcoIds);
    var actions = actionCompositeFetcher.fetchWithConnection(connection, fkcoIds);
    return alertsDetails
        .stream()
        .map(ad -> AlertComposite
            .builder()
            .discriminator(properties.getDiscriminatorPrefix())
            .alertDetails(ad)
            .hits(hits.get(ad.getFkcoId()))
            .actions(actions.get(ad.getFkcoId()))
            .contextualModelBuilder(contextualLearningProperties.getModelBuilder())
            .build())
        .collect(toList());
  }


  private List<AlertDetails> fetchAlertsDetails(Connection connection, List<Long> alertIds) {
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

  private List<AlertDetails> createAlerts(ResultSet resultSet) throws
      SQLException {

    var result = new ArrayList<AlertDetails>();
    while (resultSet.next()) {
      var alert = mapRow(resultSet, properties.getTimeZone());
      result.add(alert);
    }
    return result;
  }
}
