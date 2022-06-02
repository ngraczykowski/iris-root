package com.silenteight.universaldatasource.api.library.comparedates.v1;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import com.silenteight.datasource.api.compareDates.v1.CompareDatesInputServiceGrpc.CompareDatesInputServiceBlockingStub;
import com.silenteight.universaldatasource.api.library.UniversalDataSourceLibraryRuntimeException;

import com.google.common.collect.Lists;
import io.vavr.control.Try;

import java.util.List;
import java.util.concurrent.TimeUnit;

import static java.util.stream.Collectors.toList;

@Slf4j
@RequiredArgsConstructor
public class CompareDatesInputServiceGrpcAdapter implements CompareDatesInputServiceClient {

  public static final String COULD_NOT_FETCH_COMPARE_DATES_INPUTS_ERR_MSG =
      "Couldn't fetch compare dates inputs";

  private final CompareDatesInputServiceBlockingStub blockingStub;
  private final long deadlineInSeconds;

  @Override
  public List<BatchGetCompareDatesInputsOut> getBatchGetCompareDatesInputs(
      BatchGetCompareDatesInputsIn request) {
    return Try
        .of(() -> getStub().batchGetCompareDatesInputs(
            request.toBatchGetCompareDatesInputsRequest()))
        .map(Lists::newArrayList)
        .map(list -> list
            .stream()
            .map(BatchGetCompareDatesInputsOut::createFrom)
            .collect(toList()))
        .onSuccess(response -> log.info("Received {} objects", response.size()))
        .onFailure(e -> log.error(COULD_NOT_FETCH_COMPARE_DATES_INPUTS_ERR_MSG, e))
        .getOrElseThrow(e -> new UniversalDataSourceLibraryRuntimeException(
            COULD_NOT_FETCH_COMPARE_DATES_INPUTS_ERR_MSG, e));
  }

  private CompareDatesInputServiceBlockingStub getStub() {
    return blockingStub.withDeadlineAfter(deadlineInSeconds, TimeUnit.SECONDS);
  }
}
