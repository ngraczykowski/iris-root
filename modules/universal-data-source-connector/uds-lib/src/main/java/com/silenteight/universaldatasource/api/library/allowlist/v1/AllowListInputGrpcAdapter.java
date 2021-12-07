package com.silenteight.universaldatasource.api.library.allowlist.v1;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import com.silenteight.datasource.api.allowlist.v1.AllowListInputServiceGrpc.AllowListInputServiceBlockingStub;
import com.silenteight.universaldatasource.api.library.UniversalDataSourceLibraryRuntimeException;

import com.google.common.collect.Lists;
import io.vavr.control.Try;

import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

@Slf4j
@RequiredArgsConstructor
public class AllowListInputGrpcAdapter implements AllowListInputServiceClient {

  public static final String COULD_NOT_FETCH_ALLOW_INPUTS_ERR_MSG = "Couldn't fetch allow inputs";

  private final AllowListInputServiceBlockingStub blockingStub;
  private final long deadlineInSeconds;

  @Override
  public List<BatchGetMatchAllowListInputsOut> getBatchGetMatchAllowListInputs(
      BatchGetMatchAllowListInputsIn request) {
    return Try
        .of(() -> getStub().batchGetMatchAllowListInputs(
            request.toBatchGetMatchAllowListInputsRequest()))
        .map(Lists::newArrayList)
        .map(list -> list
            .stream()
            .map(BatchGetMatchAllowListInputsOut::createFrom)
            .collect(Collectors.toList()))
        .onSuccess(response -> log.info("Received {} objects", response.size()))
        .onFailure(e -> log.error(COULD_NOT_FETCH_ALLOW_INPUTS_ERR_MSG, e))
        .getOrElseThrow(e -> new UniversalDataSourceLibraryRuntimeException(
            COULD_NOT_FETCH_ALLOW_INPUTS_ERR_MSG, e));
  }

  private AllowListInputServiceBlockingStub getStub() {
    return blockingStub.withDeadlineAfter(deadlineInSeconds, TimeUnit.SECONDS);
  }
}
