package com.silenteight.adjudication.engine.alerts.alert;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import com.silenteight.adjudication.engine.alerts.alert.domain.InsertLabelRequest;

import java.time.OffsetDateTime;
import java.util.Locale;
import java.util.UUID;

import static com.silenteight.adjudication.engine.alerts.match.MatchFixtures.inMemoryMatchFacade;
import static java.util.concurrent.ThreadLocalRandom.current;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class AlertFixtures {

  private static final String[] COUNTRIES = Locale.getISOCountries();
  private static final String[] BATCHES = { "BTCH", "PERD", "EMPL" };
  private static final String[] WATCHLISTS = { "PEPL", "AM", "DENY" };

  static AlertFacade inMemoryAlertFacade() {
    var alertRepository = new InMemoryAlertRepository();
    var alertLabelDataAccess = new InMemoryAlertLabelDataAccess();

    var createAlertsUseCase = new CreateAlertsUseCase(alertRepository, inMemoryMatchFacade());
    var addAlertLabels = new AddLabelsUseCase(alertLabelDataAccess);
    var removeAlertLabels = new RemoveLabelUseCase(alertLabelDataAccess);
    var deleteAlerts = new DeleteAlertsUseCase(alertRepository);

    return new AlertFacade(createAlertsUseCase, addAlertLabels, removeAlertLabels, deleteAlerts);
  }

  static AlertEntity randomAlertEntity() {
    var batchType = getRandomBatchType();
    var alertId = String.format("%s!%s", batchType, getRandomUuid());

    return AlertEntity.builder()
        .clientAlertIdentifier(alertId)
        .alertedAt(OffsetDateTime.now().minusDays(1))
        .label("batch_type", batchType)
        .build();
  }

  static String getRandomAlertId() {
    return String.format("%s!%s", getRandomBatchType(), getRandomUuid());
  }

  static String getRandomBatchType() {
    var country = getRandomCountry();
    var batch = BATCHES[current().nextInt(0, BATCHES.length)];
    var watchlist = WATCHLISTS[current().nextInt(0, WATCHLISTS.length)];
    return String.format("%s_%s_%s", country, batch, watchlist);
  }

  static String getRandomUuid() {
    return UUID.randomUUID().toString().toUpperCase();
  }

  static String getRandomCountry() {
    return COUNTRIES[current().nextInt(0, COUNTRIES.length)];
  }

  public static InsertLabelRequest createLabelInsertRequest() {
    return InsertLabelRequest
        .builder()
        .alertId(1L)
        .labelName("labeleczka")
        .labelValue("valueczka")
        .build();
  }
}
