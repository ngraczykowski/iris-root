package com.silenteight.universaldatasource.api.library.date.v1;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import com.silenteight.datasource.api.date.v1.DateInputServiceGrpc.DateInputServiceBlockingStub;
import com.silenteight.universaldatasource.api.library.UniversalDataSourceLibraryRuntimeException;

import com.google.common.collect.Lists;
import io.vavr.control.Try;

import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

@Slf4j
@RequiredArgsConstructor
public class DateInputServiceGrpcAdapter implements DateInputServiceClient {

  public static final String COULD_NOT_FETCH_ERR_MSG = "Couldn't fetch date inputs";

  private final DateInputServiceBlockingStub blockingStub;
  private final long deadlineInSeconds;

  @Override
  public List<BatchGetMatchDateInputsOut> getBatchGetMatchDateInputs(
      BatchGetMatchDateInputsIn request) {
    return Try
        .of(() -> getStub().batchGetMatchDateInputs(request.toBatchGetMatchDateInputsRequest()))
        .map(Lists::newArrayList)
        .map(list -> list
            .stream()
            .map(BatchGetMatchDateInputsOut::createFrom)
            .collect(Collectors.toList()))
        .onSuccess(response -> log.info("Received {} objects", response.size()))
        .onFailure(e -> log.error(COULD_NOT_FETCH_ERR_MSG, e))
        .getOrElseThrow(
            e -> new UniversalDataSourceLibraryRuntimeException(COULD_NOT_FETCH_ERR_MSG, e));
  }

  private DateInputServiceBlockingStub getStub() {
    return blockingStub.withDeadlineAfter(deadlineInSeconds, TimeUnit.SECONDS);
  }
}
