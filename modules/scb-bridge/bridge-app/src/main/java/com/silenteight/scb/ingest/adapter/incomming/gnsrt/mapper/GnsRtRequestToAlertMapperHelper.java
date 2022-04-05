package com.silenteight.scb.ingest.adapter.incomming.gnsrt.mapper;

import lombok.RequiredArgsConstructor;

import com.silenteight.scb.ingest.adapter.incomming.common.model.ObjectId;
import com.silenteight.scb.ingest.adapter.incomming.common.model.alert.Alert;
import com.silenteight.scb.ingest.adapter.incomming.common.model.alert.Alert.Flag;
import com.silenteight.scb.ingest.adapter.incomming.common.model.alert.AlertDetails;
import com.silenteight.scb.ingest.adapter.incomming.common.model.match.Match;
import com.silenteight.scb.ingest.adapter.incomming.gnsrt.model.request.*;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.UUID;

import static com.google.common.base.Strings.nullToEmpty;

@RequiredArgsConstructor
class GnsRtRequestToAlertMapperHelper {

  private static final String UNIT_SEPARATOR = "!";

  Alert createAlert(
      GnsRtRecommendationRequest request, GnsRtAlert alert,
      String internalBatchId, List<Match> matches) {
    GnsRtScreenCustomerNameRes customerNameRes = request.getScreenCustomerNameRes();

    GnsRtScreenCustomerNameResInfo screenCustomerNameResInfo = customerNameRes
        .getScreenCustomerNameResPayload()
        .getScreenCustomerNameResInfo();

    GnsRtScreenCustomerNameResInfoHeader header = screenCustomerNameResInfo
        .getHeader();

    Instant immediateResponseTimestamp = screenCustomerNameResInfo
        .getImmediateResponseData()
        .getImmediateResponseTimestamp();

    ScreenableData screenableData = screenCustomerNameResInfo.getScreenableData();

    var recordId = nullToEmpty(extractRecordId(alert.getAlertId()));
    var userBankId = header.getUserBankID();
    var trackingId = customerNameRes.getHeader().getOriginationDetails().getTrackingId();

    return Alert.builder()
        .id(makeId(alert, immediateResponseTimestamp))
        .decisionGroup(nullToEmpty(extractUnit(alert.getAlertId())))
        .flags(Flag.PROCESS.getValue() | Flag.RECOMMEND.getValue())
        .state(Alert.State.STATE_CORRECT)
        .generatedAt(immediateResponseTimestamp)
        .receivedAt(Instant.now())
        .alertedParty(GnsRtAlertedPartyCreator.createAlertedParty(screenableData, recordId))
        .matches(matches)
        .details(createDetails(alert.getAlertId(), internalBatchId, userBankId, trackingId))
        .build();
  }

  private static ObjectId makeId(GnsRtAlert alert, Instant immediateResponseTimestamp) {
    var discriminator = getDiscriminatorValue(immediateResponseTimestamp);

    return ObjectId
        .builder()
        .id(UUID.randomUUID())
        .discriminator(discriminator)
        .sourceId(alert.getAlertId())
        .build();
  }

  private static String getDiscriminatorValue(Instant immediateResponseTimestamp) {
    return String.valueOf(immediateResponseTimestamp.truncatedTo(ChronoUnit.SECONDS));
  }

  private static AlertDetails createDetails(
      String systemId,
      String internalBatchId,
      String userBankId,
      String trackingId) {
    return AlertDetails.builder()
        .batchId(nullToEmpty(trackingId))
        .internalBatchId(internalBatchId)
        .unit(nullToEmpty(extractUnit(systemId)))
        .account(nullToEmpty(userBankId))
        .systemId(systemId)
        .build();
  }

  private static String extractRecordId(String alertId) {
    return alertId.substring(alertId.indexOf(UNIT_SEPARATOR) + 1);
  }

  private static String extractUnit(String alertId) {
    return alertId.substring(0, alertId.indexOf(UNIT_SEPARATOR));
  }
}
