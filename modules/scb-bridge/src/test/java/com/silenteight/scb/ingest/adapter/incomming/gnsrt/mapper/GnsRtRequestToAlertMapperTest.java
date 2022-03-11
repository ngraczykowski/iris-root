package com.silenteight.scb.ingest.adapter.incomming.gnsrt.mapper;

import lombok.extern.slf4j.Slf4j;

import com.silenteight.scb.ingest.adapter.incomming.common.gender.GenderDetector;
import com.silenteight.scb.ingest.adapter.incomming.common.hitdetails.HitDetailsParser;
import com.silenteight.scb.ingest.adapter.incomming.common.model.alert.Alert;
import com.silenteight.scb.ingest.adapter.incomming.gnsrt.model.GnsRtAlertStatus;
import com.silenteight.scb.ingest.adapter.incomming.gnsrt.model.request.GnsRtAlert;
import com.silenteight.scb.ingest.adapter.incomming.gnsrt.model.request.GnsRtRecommendationRequest;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;

import static org.assertj.core.api.Assertions.*;

@Slf4j
class GnsRtRequestToAlertMapperTest {

  private static Map<String, Integer> requestsWithAmountOfAlerts;

  static {
    requestsWithAmountOfAlerts = new HashMap<>();
    requestsWithAmountOfAlerts.put("Request1.json", 1);
    requestsWithAmountOfAlerts.put("Request4.json", 1);
    requestsWithAmountOfAlerts.put("Request5.json", 2);
    requestsWithAmountOfAlerts.put("Request6.json", 1);
    requestsWithAmountOfAlerts.put("Request7.json", 2);
    requestsWithAmountOfAlerts.put("Request8.json", 3);
    requestsWithAmountOfAlerts.put("Request9.json", 2);
    requestsWithAmountOfAlerts.put("Request10.json", 2);
    requestsWithAmountOfAlerts.put("Request11.json", 2);
    requestsWithAmountOfAlerts.put("Request12.json", 1);
    requestsWithAmountOfAlerts.put("Request13.json", 1);
    requestsWithAmountOfAlerts.put("Request14.json", 1);
    requestsWithAmountOfAlerts.put("Request16.json", 1);
    requestsWithAmountOfAlerts.put("Request17.json", 1);
    requestsWithAmountOfAlerts.put("Request18.json", 1);
    requestsWithAmountOfAlerts.put("Request19.json", 1);
    requestsWithAmountOfAlerts.put("Request20.json", 1);
    requestsWithAmountOfAlerts.put("Request21.json", 1);
    requestsWithAmountOfAlerts.put("Request22.json", 1);
  }

  private ObjectMapper objectMapper;
  private GnsRtRequestToAlertMapper gnsRtRequestToAlertMapper =
      new GnsRtRequestToAlertMapper(new HitDetailsParser(), new GenderDetector());

  @BeforeEach
  void setUp() {
    objectMapper = new ObjectMapper();
    objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
    objectMapper.registerModule(new JavaTimeModule());
  }

  @Test
  void shouldValidateAmountOfAlertsAfterMapping() {
    requestsWithAmountOfAlerts.forEach((name, amountOfAlerts) -> {
      //given
      GnsRtRecommendationRequest gnsRtRecommendationRequest = createRequest(name);
      List<GnsRtAlert> gnsRtAlerts = getAlerts(gnsRtRecommendationRequest);

      //when
      List<Alert> alerts = gnsRtRequestToAlertMapper.map(gnsRtRecommendationRequest);

      //then
      assertThat(alerts.size()).isEqualTo(amountOfAlerts);
      verifyAlerts(gnsRtAlerts, alerts);
    });
  }

  @Valid
  @NotNull
  private static List<GnsRtAlert> getAlerts(
      GnsRtRecommendationRequest gnsRtRecommendationRequest) {
    return gnsRtRecommendationRequest
        .getScreenCustomerNameRes()
        .getScreenCustomerNameResPayload()
        .getScreenCustomerNameResInfo()
        .getImmediateResponseData()
        .getAlerts();
  }

  private static void verifyAlerts(List<GnsRtAlert> gnsRtAlerts, List<Alert> alerts) {
    assertThat(alerts.stream().findFirst().isPresent()).isTrue();
    assertThat(alerts.stream().findFirst().get().id().sourceId()).isEqualTo(
        gnsRtAlerts
            .stream()
            .filter(x -> x.getAlertStatus() == GnsRtAlertStatus.POTENTIAL_MATCH)
            .findFirst()
            .map(GnsRtAlert::getAlertId)
            .get());
  }

  private GnsRtRecommendationRequest createRequest(String fileName) {
    try {
      return objectMapper.readValue(
          Path.of("src/test/resources/gnsRtRequests/", fileName).toFile(),
          GnsRtRecommendationRequest.class);
    } catch (IOException e) {
      throw new UncheckedIOException(e);
    }
  }

  @Test
  void shouldValidateAlertIdAfterMapping() {
    //given
    var gnsRtRecommendationRequest = createRequest("Request1.json");

    //when
    List<Alert> alerts = gnsRtRequestToAlertMapper.map(gnsRtRecommendationRequest);

    //then
    assertThat(alerts).hasSize(1);
    var alertId = alerts.get(0).id();
    assertThat(alertId.sourceId()).isEqualTo("IN_BTCH_DUDL!737FC9C1-BDE337AD-75A2086D-78C6F14C");
    assertThat(alertId.discriminator()).isEqualTo("2020-02-20T13:28:02Z");
  }

  @Test
  void shouldValidateCountryAndDesignationAfterMapping() {
    //given
    GnsRtRecommendationRequest gnsRtRecommendationRequest = createRequest("Request1.json");

    //when
    List<Alert> alerts = gnsRtRequestToAlertMapper.map(gnsRtRecommendationRequest);

    //then
    var matches = alerts.get(0).matches();
    var match = matches.stream()
        .filter(m -> m.id().sourceId().equals("AS05305170"))
        .findFirst();

    assertThat(match.isPresent());

    var details = match.get().matchedParty();

    assertThat(details.wlCountry()).isEqualTo("INDIA");
    assertThat(details.wlDesignation()).isEqualTo("GWL");
  }

  @Test
  void shouldValidateMatchedPartyNationality() {
    //given
    var gnsRtRecommendationRequest = createRequest("Request21.json");

    //when
    var alerts = gnsRtRequestToAlertMapper.map(gnsRtRecommendationRequest);

    //then
    var matches = alerts.get(0).matches();
    var match = matches.stream()
        .filter(m -> m.id().sourceId().equals("AS05305170"))
        .findFirst();

    assertThat(match.isPresent());

    var details = match.get().matchedParty();

    assertThat(details.wlNationality()).isNull();
  }

  @Test
  void shouldValidateMatchedPartyNationalityAndGenderTakenFromAdditionalInfo() {
    //given
    var gnsRtRecommendationRequest = createRequest("Request22.json");

    //when
    var alerts = gnsRtRequestToAlertMapper.map(gnsRtRecommendationRequest);

    //then
    var matches = alerts.get(0).matches();
    var match = matches.stream()
        .filter(m -> m.id().sourceId().equals("AS05305170"))
        .findFirst();

    assertThat(match.isPresent());

    var details = match.get().matchedParty();

    assertThat(details.wlNationality()).isEqualTo("PAKISTAN");
    assertThat(details.wlGender()).isEqualTo("MALE");
  }
}
