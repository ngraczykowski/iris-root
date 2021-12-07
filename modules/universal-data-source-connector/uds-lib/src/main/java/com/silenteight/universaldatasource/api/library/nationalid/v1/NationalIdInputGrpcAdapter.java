package com.silenteight.universaldatasource.api.library.nationalid.v1;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import com.silenteight.datasource.api.nationalid.v1.NationalIdInputServiceGrpc.NationalIdInputServiceBlockingStub;
import com.silenteight.universaldatasource.api.library.UniversalDataSourceLibraryRuntimeException;

import com.google.common.collect.Lists;
import io.vavr.control.Try;

import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

@Slf4j
@RequiredArgsConstructor
public class NationalIdInputGrpcAdapter implements NationalIdInputServiceClient {

  public static final String COULD_NOT_GET_NATIONAL_ID_INPUTS_ERROR_MSG =
      "Couldn't fetch national id inputs";

  private final NationalIdInputServiceBlockingStub blockingStub;
  private final long deadlineInSeconds;

  @Override
  public List<BatchGetMatchNationalIdInputsOut> batchGetMatchTransactionInputs(
      BatchGetMatchNationalIdInputsIn request) {
    return Try
        .of(() -> getStub().batchGetMatchNationalIdInputs(
            request.toBatchGetMatchNationalIdInputsRequest()))
        .map(Lists::newArrayList)
        .map(list -> list.stream()
            .map(BatchGetMatchNationalIdInputsOut::createFrom)
            .collect(Collectors.toList()))
        .onSuccess(response -> log.info("Received {} objects", response.size()))
        .onFailure(e -> log.error(COULD_NOT_GET_NATIONAL_ID_INPUTS_ERROR_MSG, e))
        .getOrElseThrow(e -> new UniversalDataSourceLibraryRuntimeException(
            COULD_NOT_GET_NATIONAL_ID_INPUTS_ERROR_MSG, e));
  }

  private NationalIdInputServiceBlockingStub getStub() {
    return blockingStub.withDeadlineAfter(deadlineInSeconds, TimeUnit.SECONDS);
  }
}
