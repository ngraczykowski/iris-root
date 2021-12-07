package com.silenteight.universaldatasource.api.library.historicaldecisions.v1;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import com.silenteight.datasource.api.historicaldecisions.v1.HistoricalDecisionsInputServiceGrpc.HistoricalDecisionsInputServiceBlockingStub;
import com.silenteight.universaldatasource.api.library.UniversalDataSourceLibraryRuntimeException;

import com.google.common.collect.Lists;
import io.vavr.control.Try;

import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

@Slf4j
@RequiredArgsConstructor
class HistoricalDecisionsInputGrpcAdapter implements HistoricalDecisionsInputServiceClient {

  public static final String COULD_NOT_GET_HISTORICAL_DECISIONS_INPUTS_ERROR_MSG =
      "Couldn't fetch national id inputs";

  private final HistoricalDecisionsInputServiceBlockingStub blockingStub;
  private final long deadlineInSeconds;

  @Override
  public List<BatchGetMatchHistoricalDecisionsInputsOut> getBatchGetMatchHistoricalDecisionsInputs(
      BatchGetMatchHistoricalDecisionsInputsIn request) {
    return Try
        .of(() -> getStub().batchGetMatchHistoricalDecisionsInputs(
            request.toBatchGetMatchHistoricalDecisionsInputsRequest()))
        .map(Lists::newArrayList)
        .map(list -> list.stream()
            .map(BatchGetMatchHistoricalDecisionsInputsOut::createFrom)
            .collect(Collectors.toList()))
        .onSuccess(response -> log.info("Received {} objects", response.size()))
        .onFailure(e -> log.error(COULD_NOT_GET_HISTORICAL_DECISIONS_INPUTS_ERROR_MSG, e))
        .getOrElseThrow(e -> new UniversalDataSourceLibraryRuntimeException(
            COULD_NOT_GET_HISTORICAL_DECISIONS_INPUTS_ERROR_MSG, e));
  }

  private HistoricalDecisionsInputServiceBlockingStub getStub() {
    return blockingStub.withDeadlineAfter(deadlineInSeconds, TimeUnit.SECONDS);
  }
}
