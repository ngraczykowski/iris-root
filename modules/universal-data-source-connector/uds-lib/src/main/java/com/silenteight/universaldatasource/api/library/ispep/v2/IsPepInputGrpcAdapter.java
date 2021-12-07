package com.silenteight.universaldatasource.api.library.ispep.v2;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import com.silenteight.datasource.api.ispep.v2.IsPepInputServiceGrpc.IsPepInputServiceBlockingStub;
import com.silenteight.universaldatasource.api.library.UniversalDataSourceLibraryRuntimeException;

import com.google.common.collect.Lists;
import io.vavr.control.Try;

import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

@Slf4j
@RequiredArgsConstructor
public class IsPepInputGrpcAdapter implements IsPepInputServiceClient {

  public static final String COULD_NOT_GET_IS_PEP_INPUTS_ERROR_MSG = "Couldn't fetch isPep inputs";

  private final IsPepInputServiceBlockingStub blockingStub;
  private final long deadlineInSeconds;

  @Override
  public List<BatchGetMatchIsPepInputsOut> batchGetMatchIsPepInputs(
      BatchGetMatchIsPepInputsIn request) {
    return Try
        .of(() -> getStub().batchGetMatchIsPepInputs(request.toBatchGetMatchIsPepInputsRequest()))
        .map(Lists::newArrayList)
        .map(list -> list.stream()
            .map(BatchGetMatchIsPepInputsOut::createFrom)
            .collect(Collectors.toList()))
        .onSuccess(response -> log.info("Received {} objects", response.size()))
        .onFailure(e -> log.error(COULD_NOT_GET_IS_PEP_INPUTS_ERROR_MSG, e))
        .getOrElseThrow(e -> new UniversalDataSourceLibraryRuntimeException(
            COULD_NOT_GET_IS_PEP_INPUTS_ERROR_MSG, e));
  }

  private IsPepInputServiceBlockingStub getStub() {
    return blockingStub.withDeadlineAfter(deadlineInSeconds, TimeUnit.SECONDS);
  }
}
