package com.silenteight.universaldatasource.api.library.name.v1;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import com.silenteight.datasource.api.name.v1.NameInputServiceGrpc.NameInputServiceBlockingStub;
import com.silenteight.universaldatasource.api.library.UniversalDataSourceLibraryRuntimeException;

import com.google.common.collect.Lists;
import io.vavr.control.Try;

import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

@Slf4j
@RequiredArgsConstructor
public class NameInputGrpcAdapter implements NameInputServiceClient {

  public static final String COULD_NOT_GET_NAME_INPUTS_ERROR_MSG = "Couldn't fetch name inputs";

  private final NameInputServiceBlockingStub blockingStub;
  private final long deadlineInSeconds;

  @Override
  public List<BatchGetMatchNameInputsOut> batchGetMatchNameInputs(
      BatchGetMatchNameInputsIn request) {
    return Try
        .of(() -> getStub().batchGetMatchNameInputs(request.toBatchGetMatchNameInputsRequest()))
        .map(Lists::newArrayList)
        .map(list -> list.stream()
            .map(BatchGetMatchNameInputsOut::createFrom)
            .collect(Collectors.toList()))
        .onSuccess(response -> log.info("Received {} objects", response.size()))
        .onFailure(e -> log.error(COULD_NOT_GET_NAME_INPUTS_ERROR_MSG, e))
        .getOrElseThrow(e -> new UniversalDataSourceLibraryRuntimeException(
            COULD_NOT_GET_NAME_INPUTS_ERROR_MSG, e));
  }

  private NameInputServiceBlockingStub getStub() {
    return blockingStub.withDeadlineAfter(deadlineInSeconds, TimeUnit.SECONDS);
  }


}
