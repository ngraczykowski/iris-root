package com.silenteight.scb.ingest.adapter.incomming.cbs.reactive;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import com.silenteight.scb.ingest.adapter.incomming.cbs.gateway.CbsAckAlert;
import com.silenteight.scb.ingest.adapter.incomming.cbs.gateway.CbsAckGateway;
import com.silenteight.scb.ingest.adapter.incomming.cbs.gateway.CbsOutput;
import com.silenteight.scb.ingest.adapter.incomming.cbs.gateway.CbsOutput.State;

import org.springframework.dao.TransientDataAccessException;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

import java.sql.SQLTransientException;
import java.util.Collection;

@RequiredArgsConstructor
@Slf4j
public class ReactiveCbsAckService {

  private final CbsAckGateway ackGateway;

  public Flux<AckAlertResult> ackReadAlerts(@NonNull Collection<AckAlertRequest> alerts) {
    return Flux.defer(() -> Flux
        .fromIterable(alerts)
        .index()
        .flatMap(indexAlertTuple ->
            ackSingleAlert(indexAlertTuple.getT1(), indexAlertTuple.getT2())));
  }

  private Mono<AckAlertResult> ackSingleAlert(long index, AckAlertRequest ackAlertRequest) {
    return Mono
        .just(ackAlertRequest)
        .publishOn(Schedulers.immediate())
        .map(this::doAckSingleAlert)
        .map(state -> new AckAlertResult(state, index));
  }

  private CbsOutput.State doAckSingleAlert(AckAlertRequest ackAlertRequest) {
    CbsAckAlert ackAlert = ackAlertRequest.asCbsAckAlert();
    String systemId = ackAlertRequest.getSystemId();

    try {
      return ackGateway.ackReadAlert(ackAlert).getState();
    } catch (TransientDataAccessException e) {
      return logAndReturnTemporaryFailure(systemId, e);
    } catch (Exception e) {
      if (e.getCause() instanceof SQLTransientException) {
        return logAndReturnTemporaryFailure(systemId, e);
      }
      log.error("Error when trying to ACK an alert: systemId={}", systemId, e);
      return State.ERROR;
    }
  }

  private State logAndReturnTemporaryFailure(String systemId, Exception e) {
    log.error("Temporarily failed to ACK an alert: systemId={}", systemId, e);
    return State.TEMPORARY_FAILURE;
  }
}
