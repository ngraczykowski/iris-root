package com.silenteight.scb.ingest.adapter.incomming.cbs.reactive;

import lombok.RequiredArgsConstructor;

import com.silenteight.scb.ingest.adapter.incomming.cbs.alertid.AlertId;
import com.silenteight.scb.ingest.adapter.incomming.cbs.gateway.CbsAckAlert;
import com.silenteight.scb.ingest.adapter.incomming.cbs.gateway.CbsAckGateway;
import com.silenteight.scb.ingest.adapter.incomming.cbs.gateway.CbsOutput;
import com.silenteight.scb.ingest.adapter.incomming.cbs.gateway.CbsOutput.State;

import org.junit.jupiter.api.RepeatedTest;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatcher;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Flux;
import reactor.test.StepVerifier;
import reactor.test.StepVerifier.Step;

import java.util.Map;

import static java.util.Arrays.asList;
import static java.util.Collections.singleton;
import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ReactiveCbsAckServiceTest {

  @Mock
  private CbsAckGateway cbsAckGateway;

  @InjectMocks
  private ReactiveCbsAckService underTest;

  @Test
  void givenSingleAlert_emitsOk() {
    // Given
    AckAlertRequest request = mockRequest("system-id", "batch-id");
    doReturn(createCbsOutput(State.OK)).when(cbsAckGateway)
        .ackReadAlert(alertWithSystemId(request.getSystemId()));

    // Then
    StepVerifier
        .create(underTest.ackReadAlerts(singleton(request)).log())
        .assertNext(result -> assertThat(result.getState()).isEqualTo(State.OK))
        .expectComplete()
        .verify();
  }

  private AckAlertRequest mockRequest(String systemId, String batchId) {
    return AckAlertRequest
        .builder()
        .alertId(new AlertId(systemId, batchId))
        .build();
  }

  private static CbsAckAlert alertWithSystemId(String systemId) {
    return argThat(new SingleCbsAckAlertArgumentMatcher(systemId));
  }

  private static CbsOutput createCbsOutput(State state) {
    CbsOutput cbsOutput = new CbsOutput();
    cbsOutput.setState(state);
    return cbsOutput;
  }

  @RepeatedTest(10)
  void givenMultipleAlerts_keepsOrdering() {
    // Given
    AckAlertRequest okRequest = mockRequest("ok-id", "batch-id");
    AckAlertRequest errorRequest = mockRequest("error-id", "batch-id");
    AckAlertRequest nonFatalRequest = mockRequest("transient-id", "batch-id");
    Map<Long, State> expectedStates = Map.of(
        0L, State.OK,
        1L, State.ERROR,
        2L, State.TEMPORARY_FAILURE);

    // When
    doReturn(createCbsOutput(State.OK)).when(cbsAckGateway)
        .ackReadAlert(alertWithSystemId(okRequest.getSystemId()));
    doReturn(createCbsOutput(State.ERROR)).when(cbsAckGateway)
        .ackReadAlert(alertWithSystemId(errorRequest.getSystemId()));
    doReturn(createCbsOutput(State.TEMPORARY_FAILURE)).when(cbsAckGateway)
        .ackReadAlert(alertWithSystemId(nonFatalRequest.getSystemId()));

    Flux<AckAlertResult> alertResultFlux =
        underTest.ackReadAlerts(asList(okRequest, errorRequest, nonFatalRequest)).log();

    // Then
    Step<AckAlertResult> verifier = StepVerifier.create(alertResultFlux);

    for (int expectationNo = 0; expectationNo < expectedStates.size(); expectationNo++) {
      verifier = verifier.assertNext(result ->
          assertThat(result.getState()).isEqualTo(expectedStates.get(result.getIndex())));
    }

    verifier.expectComplete().verify();
  }

  @Test
  void givenFatalError_emitsErrorResult() {
    // Given
    AckAlertRequest request = mockRequest("system-id", "batch-id");
    doReturn(createCbsOutput(State.ERROR)).when(cbsAckGateway)
        .ackReadAlert(alertWithSystemId(request.getSystemId()));

    // Then
    StepVerifier
        .create(underTest.ackReadAlerts(singleton(request)).log())
        .assertNext(result -> assertThat(result.getState()).isEqualTo(State.ERROR))
        .expectComplete()
        .verify();
  }

  @Test
  void givenNonFatalError_emitsErrorResult() {
    // Given
    AckAlertRequest request = mockRequest("system-id", "batch-id");
    doReturn(createCbsOutput(State.TEMPORARY_FAILURE)).when(cbsAckGateway)
        .ackReadAlert(alertWithSystemId(request.getSystemId()));

    // Then
    StepVerifier
        .create(underTest.ackReadAlerts(singleton(request)).log())
        .assertNext(result -> assertThat(result.getState()).isEqualTo(State.TEMPORARY_FAILURE))
        .expectComplete()
        .verify();
  }

  @RequiredArgsConstructor
  private static class SingleCbsAckAlertArgumentMatcher implements ArgumentMatcher<CbsAckAlert> {

    private final String expectedSystemId;

    @Override
    public boolean matches(CbsAckAlert argument) {
      return argument.getAlertExternalId().equals(expectedSystemId);
    }
  }
}