package com.silenteight.customerbridge.gnsrt.model.request;

import com.silenteight.customerbridge.gnsrt.model.GnsRtAlertStatus;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.time.Instant;
import java.util.List;
import java.util.Set;
import javax.annotation.Nonnull;
import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;

import static com.silenteight.customerbridge.gnsrt.model.GnsRtAlertStatus.FALSE_MATCH;
import static com.silenteight.customerbridge.gnsrt.model.GnsRtAlertStatus.MATCH;
import static com.silenteight.customerbridge.gnsrt.model.GnsRtAlertStatus.POTENTIAL_MATCH;
import static java.util.Collections.emptyList;
import static java.util.List.of;
import static org.assertj.core.api.Assertions.*;

class GnsRtRecommendationRequestValidationTest {

  private static final String SOME_WATCHLIST_TYPE = "AM";
  private Validator validator;

  @BeforeEach
  void setUp() {
    ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
    validator = factory.getValidator();
  }

  private void assertViolationMessagesAsExpected(
      GnsRtRecommendationRequest request, String... messages) {

    assertThat(validate(request))
        .extracting(ConstraintViolation::getMessage)
        .containsExactlyInAnyOrder(messages);
  }

  private Set<ConstraintViolation<GnsRtRecommendationRequest>> validate(
      GnsRtRecommendationRequest request) {

    return validator.validate(request);
  }

  @Nonnull
  private GnsRtRecommendationRequest request(List<GnsRtAlert> alerts) {
    var info = new GnsRtScreenCustomerNameResInfo();
    info.setHeader(infoHeader());
    info.setScreenableData(screenableData());
    info.setImmediateResponseData(responseData(alerts));

    var screenCustomerNameRes = new GnsRtScreenCustomerNameRes();
    screenCustomerNameRes.setScreenCustomerNameResPayload(payload(info));
    screenCustomerNameRes.setHeader(header());

    GnsRtRecommendationRequest request = new GnsRtRecommendationRequest();
    request.setScreenCustomerNameRes(screenCustomerNameRes);
    return request;
  }

  @Nonnull
  private static ImmediateResponseData responseData(List<GnsRtAlert> alerts) {
    var immediateResponseData = new ImmediateResponseData();
    immediateResponseData.setAlerts(alerts);
    immediateResponseData.setImmediateResponseTimestamp(Instant.now());
    return immediateResponseData;
  }

  @Nonnull
  private static GnsRtScreenCustomerNameResInfoHeader infoHeader() {
    var infoHeader = new GnsRtScreenCustomerNameResInfoHeader();
    infoHeader.setUserBankID("userBankID");
    return infoHeader;
  }

  @Nonnull
  private static ScreenableData screenableData() {
    var screenableData = new ScreenableData();
    screenableData.setAmlCountry("amlCountry");
    screenableData.setSourceSystemIdentifier("sourceSystemIdentifier");
    return screenableData;
  }

  @Nonnull
  private static GnsRtScreenCustomerNameResPayload payload(
      GnsRtScreenCustomerNameResInfo info) {

    var payload = new GnsRtScreenCustomerNameResPayload();
    payload.setScreenCustomerNameResInfo(info);
    return payload;
  }

  @Nonnull
  private static GnsRtScreenCustomerNameResHeader header() {
    var header = new GnsRtScreenCustomerNameResHeader();
    var details = new GnsRtOriginationDetails();
    details.setTrackingId("trackingId");
    header.setOriginationDetails(details);
    return header;
  }

  @Nested
  class AllPotentialMatchesHasValidAlertIdTest {

    @Test
    void invalidAlertIdTest() {
      var request = request(of(alert("INVALID_ALERT_ID")));

      assertViolationMessagesAsExpected(request, "Alert has invalid id.");
    }

    @Nonnull
    private GnsRtAlert alert(String alertId) {
      var alert = new GnsRtAlert();
      alert.setAlertStatus(POTENTIAL_MATCH);
      alert.setWatchlistType(SOME_WATCHLIST_TYPE);
      alert.setAlertId(alertId);
      return alert;
    }

    @Test
    void validAlertIdTest() {
      var request = request(of(alert("US_PERD_DUDL!8A1DFAFE-EA1041B0-9F1426E6-DBD75121|")));

      assertViolationMessagesAsExpected(request);
    }
  }

  @Nested
  class AtLeastOnePotentialMatchTest {

    @Test
    void nullAlertListTest() {
      var request = request(null);

      assertViolationMessagesAsExpected(request,
          "must not be null", "No Alerts with status POTENTIAL_MATCH.");
    }

    @Test
    void missingAlertsTest() {
      var request = request(emptyList());

      assertViolationMessagesAsExpected(request, "No Alerts with status POTENTIAL_MATCH.");
    }

    @Test
    void alertsWithoutPotentialMatch() {
      var request = request(of(alert(FALSE_MATCH), alert(MATCH)));

      assertViolationMessagesAsExpected(request, "No Alerts with status POTENTIAL_MATCH.");
    }

    @Nonnull
    private GnsRtAlert alert(GnsRtAlertStatus falseMatch) {
      var alert = new GnsRtAlert();
      alert.setAlertStatus(falseMatch);
      alert.setWatchlistType(SOME_WATCHLIST_TYPE);
      alert.setAlertId("US_PERD_DUDL!8A1DFAFE-EA1041B0-9F1426E6-DBD75121|");
      return alert;
    }

    @Test
    void alertsWithPotentialMatch() {
      var request = request(of(alert(MATCH), alert(POTENTIAL_MATCH)));

      assertViolationMessagesAsExpected(request);
    }
  }
}
