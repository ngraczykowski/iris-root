package com.silenteight.universaldatasource.api.library.freetext.v1;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import com.silenteight.datasource.api.freetext.v1.FreeTextInputServiceGrpc.FreeTextInputServiceBlockingStub;
import com.silenteight.universaldatasource.api.library.UniversalDataSourceLibraryRuntimeException;

import com.google.common.collect.Lists;
import io.vavr.control.Try;

import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

@Slf4j
@RequiredArgsConstructor
public class FreeTextInputGrpcServiceAdapter implements FreeTextInputServiceClient {

  public static final String COULD_NOT_FETCH_FREE_TEXT_INPUTS_ERR_MSG =
      "Couldn't fetch free text inputs";

  private final FreeTextInputServiceBlockingStub blockingStub;
  private final long deadlineInSeconds;

  @Override
  public List<BatchGetMatchFreeTextInputsOut> getBatchGetMatchFreeTextInputs(
      BatchGetMatchFreeTextInputsIn request) {
    return Try
        .of(() -> getStub().batchGetMatchFreeTextInputs(
            request.toBatchGetMatchFreeTextInputsRequest()))
        .map(Lists::newArrayList)
        .map(list -> list
            .stream()
            .map(BatchGetMatchFreeTextInputsOut::createFrom)
            .collect(Collectors.toList()))
        .onSuccess(response -> log.info("Received {} objects", response.size()))
        .onFailure(e -> log.error(COULD_NOT_FETCH_FREE_TEXT_INPUTS_ERR_MSG, e))
        .getOrElseThrow(e -> new UniversalDataSourceLibraryRuntimeException(
            COULD_NOT_FETCH_FREE_TEXT_INPUTS_ERR_MSG, e));
  }

  private FreeTextInputServiceBlockingStub getStub() {
    return blockingStub.withDeadlineAfter(deadlineInSeconds, TimeUnit.SECONDS);
  }
}
