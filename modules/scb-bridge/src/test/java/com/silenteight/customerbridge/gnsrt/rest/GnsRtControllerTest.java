package com.silenteight.customerbridge.gnsrt.rest;

import com.silenteight.customerbridge.gnsrt.model.GnsRtAlertStatus;
import com.silenteight.customerbridge.gnsrt.model.request.*;
import com.silenteight.customerbridge.gnsrt.model.response.GnsRtRecommendationResponse;
import com.silenteight.customerbridge.gnsrt.recommendation.GnsRtRecommendationUseCase;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import io.grpc.Status;
import io.grpc.StatusRuntimeException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import reactor.core.publisher.Mono;

import java.time.Instant;

import static java.util.Collections.singletonList;
import static org.mockito.Mockito.*;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.request;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class GnsRtControllerTest {

  private MockMvc myMockController;
  private ObjectMapper objectMapper = new ObjectMapper();
  private GnsRtRecommendationUseCase useCase = mock(GnsRtRecommendationUseCase.class);

  @BeforeEach
  void setUp() {
    myMockController = MockMvcBuilders
        .standaloneSetup(new GnsRtController(useCase))
        .build();
    objectMapper.registerModule(new JavaTimeModule());
  }

  @Test
  void shouldValidateRequestAndReturnStatusOk() throws Exception {
    //given
    GnsRtRecommendationRequest request = createRequest();

    //when
    when(useCase.recommend(request))
        .thenReturn(Mono.just(new GnsRtRecommendationResponse()));

    //then
    myMockController
        .perform(post("/v1/gnsrt/recommendation")
            .content(objectMapper.writeValueAsString(request))
            .contentType(APPLICATION_JSON_VALUE))
        .andExpect(request().asyncStarted())
        .andExpect(status().isOk());
  }

  private static GnsRtRecommendationRequest createRequest() {
    GnsRtAlert alert = new GnsRtAlert();
    alert.setAlertId("a!b");
    alert.setWatchlistType("AM");
    alert.setAlertStatus(GnsRtAlertStatus.POTENTIAL_MATCH);

    ImmediateResponseData immediateResponseData = new ImmediateResponseData();
    immediateResponseData.setAlerts(singletonList(alert));
    immediateResponseData.setImmediateResponseTimestamp(Instant.ofEpochSecond(1000));

    GnsRtScreenCustomerNameResInfoHeader gnsRtScreenCustomerNameResInfoHeader =
        new GnsRtScreenCustomerNameResInfoHeader();
    gnsRtScreenCustomerNameResInfoHeader.setUserBankID("BANK_ID");

    GnsRtOriginationDetails gnsRtOriginationDetails = new GnsRtOriginationDetails();
    gnsRtOriginationDetails.setTrackingId("NUMBER");

    ScreenableData screenableData = new ScreenableData();
    screenableData.setAmlCountry("AML");
    screenableData.setSourceSystemIdentifier("SOURCE_ID");

    GnsRtScreenCustomerNameResInfo screenCustomerNameResInfo = new GnsRtScreenCustomerNameResInfo();
    screenCustomerNameResInfo.setScreenableData(screenableData);
    screenCustomerNameResInfo.setImmediateResponseData(immediateResponseData);
    screenCustomerNameResInfo.setHeader(gnsRtScreenCustomerNameResInfoHeader);

    GnsRtScreenCustomerNameResPayload screenCustomerNameResPayload =
        new GnsRtScreenCustomerNameResPayload();
    screenCustomerNameResPayload.setScreenCustomerNameResInfo(screenCustomerNameResInfo);

    GnsRtScreenCustomerNameResHeader gnsRtScreenCustomerNameResHeader =
        new GnsRtScreenCustomerNameResHeader();
    gnsRtScreenCustomerNameResHeader.setOriginationDetails(gnsRtOriginationDetails);

    GnsRtScreenCustomerNameRes screenCustomerNameRes = new GnsRtScreenCustomerNameRes();
    screenCustomerNameRes.setScreenCustomerNameResPayload(screenCustomerNameResPayload);
    screenCustomerNameRes.setHeader(gnsRtScreenCustomerNameResHeader);

    GnsRtRecommendationRequest gnsRtRecommendationRequest = new GnsRtRecommendationRequest();
    gnsRtRecommendationRequest.setScreenCustomerNameRes(screenCustomerNameRes);

    return gnsRtRecommendationRequest;
  }

  @Test
  void shouldReturnServiceUnavailableWhenStatusRuntimeException() throws Exception {
    //given
    GnsRtRecommendationRequest request = createRequest();

    //when
    when(useCase.recommend(request))
        .thenThrow(new StatusRuntimeException(Status.DEADLINE_EXCEEDED));

    //then
    myMockController
        .perform(post("/v1/gnsrt/recommendation")
            .content(objectMapper.writeValueAsString(request))
            .contentType(APPLICATION_JSON_VALUE))
        .andExpect(request().asyncNotStarted())
        .andExpect(status().isServiceUnavailable());
  }
}
