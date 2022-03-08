package com.silenteight.scb.ingest.adapter.incomming.gnsrt.mapper;

import lombok.RequiredArgsConstructor;

import com.silenteight.proto.serp.scb.v1.ScbAlertDetails;
import com.silenteight.proto.serp.v1.alert.Alert;
import com.silenteight.proto.serp.v1.alert.Alert.Flags;
import com.silenteight.proto.serp.v1.alert.Alert.State;
import com.silenteight.proto.serp.v1.alert.Match;
import com.silenteight.proto.serp.v1.common.ObjectId;
import com.silenteight.protocol.utils.Uuids;
import com.silenteight.scb.ingest.adapter.incomming.gnsrt.model.request.*;
import com.silenteight.sep.base.common.protocol.AnyUtils;

import com.google.protobuf.Any;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.List;

import static com.google.common.base.Strings.nullToEmpty;
import static com.google.protobuf.Timestamp.getDefaultInstance;
import static com.silenteight.protocol.utils.MoreTimestamps.toTimestamp;
import static com.silenteight.protocol.utils.MoreTimestamps.toTimestampOrDefault;

@RequiredArgsConstructor
class GnsRtRequestToAlertMapperHelper {

  private static final String UNIT_SEPARATOR = "!";

  Alert createAlert(GnsRtRecommendationRequest request, GnsRtAlert alert, List<Match> matches) {
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

    return Alert
        .newBuilder()
        .setId(makeId(alert, immediateResponseTimestamp))
        .setDecisionGroup(nullToEmpty(extractUnit(alert.getAlertId())))
        .setSecurityGroup(header.getCountryCode())
        .setState(State.STATE_CORRECT)
        .setFlags(Flags.FLAG_PROCESS_VALUE | Flags.FLAG_RECOMMEND_VALUE)
        .setGeneratedAt(toTimestampOrDefault(immediateResponseTimestamp, getDefaultInstance()))
        .setReceivedAt(toTimestamp(Instant.now()))
        .setAlertedParty(GnsRtAlertedPartyCreator.createAlertedParty(screenableData, recordId))
        .addAllMatches(matches)
        .setDetails(createDetails(alert.getAlertId(), userBankId, trackingId))
        .build();
  }

  private static ObjectId makeId(GnsRtAlert alert, Instant immediateResponseTimestamp) {
    var discriminator = getDiscriminatorValue(immediateResponseTimestamp);

    return ObjectId
        .newBuilder()
        .setId(Uuids.random())
        .setDiscriminator(discriminator)
        .setSourceId(alert.getAlertId())
        .build();
  }

  private static String getDiscriminatorValue(Instant immediateResponseTimestamp) {
    return String.valueOf(immediateResponseTimestamp.truncatedTo(ChronoUnit.SECONDS));
  }

  private static Any createDetails(String systemId, String userBankId, String trackingId) {
    ScbAlertDetails.Builder detailsBuilder = ScbAlertDetails
        .newBuilder()
        .setBatchId(nullToEmpty(trackingId))
        .setUnit(nullToEmpty(extractUnit(systemId)))
        .setAccount(nullToEmpty(userBankId))
        .setSystemId(systemId);

    return AnyUtils.pack(detailsBuilder.build());
  }

  private static String extractRecordId(String alertId) {
    return alertId.substring(alertId.indexOf(UNIT_SEPARATOR) + 1);
  }

  private static String extractUnit(String alertId) {
    return alertId.substring(0, alertId.indexOf(UNIT_SEPARATOR));
  }
}
