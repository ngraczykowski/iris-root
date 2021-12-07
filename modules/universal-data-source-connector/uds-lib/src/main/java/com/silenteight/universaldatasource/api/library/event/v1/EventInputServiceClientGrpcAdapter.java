package com.silenteight.universaldatasource.api.library.event.v1;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import com.silenteight.datasource.api.event.v1.EventInputServiceGrpc.EventInputServiceBlockingStub;
import com.silenteight.universaldatasource.api.library.UniversalDataSourceLibraryRuntimeException;

import com.google.common.collect.Lists;
import io.vavr.control.Try;

import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

@Slf4j
@RequiredArgsConstructor
public class EventInputServiceClientGrpcAdapter implements EventInputServiceClient {

  public static final String COULD_NOT_FETCH_EVENT_INPUTS_ERR_MSG = "Couldn't fetch event inputs";

  private final EventInputServiceBlockingStub blockingStub;
  private final long deadlineInSeconds;

  @Override
  public List<BatchGetMatchEventInputsOut> getBatchGetMatchEventInputs(
      BatchGetMatchEventInputsIn request) {
    return Try
        .of(() -> getStub().batchGetMatchEventInputs(request.toBatchGetMatchEventInputsRequest()))
        .map(Lists::newArrayList)
        .map(list -> list
            .stream()
            .map(BatchGetMatchEventInputsOut::createFrom)
            .collect(Collectors.toList()))
        .onSuccess(response -> log.info("Received {} objects", response.size()))
        .onFailure(e -> log.error(COULD_NOT_FETCH_EVENT_INPUTS_ERR_MSG, e))
        .getOrElseThrow(e -> new UniversalDataSourceLibraryRuntimeException(
            COULD_NOT_FETCH_EVENT_INPUTS_ERR_MSG, e));
  }

  private EventInputServiceBlockingStub getStub() {
    return blockingStub.withDeadlineAfter(deadlineInSeconds, TimeUnit.SECONDS);
  }
}
