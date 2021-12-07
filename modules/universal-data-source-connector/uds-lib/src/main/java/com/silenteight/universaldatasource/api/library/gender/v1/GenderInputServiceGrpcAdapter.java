package com.silenteight.universaldatasource.api.library.gender.v1;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import com.silenteight.datasource.api.gender.v1.GenderInputServiceGrpc.GenderInputServiceBlockingStub;
import com.silenteight.universaldatasource.api.library.UniversalDataSourceLibraryRuntimeException;

import com.google.common.collect.Lists;
import io.vavr.control.Try;

import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

@Slf4j
@RequiredArgsConstructor
public class GenderInputServiceGrpcAdapter implements GenderInputServiceClient {

  public static final String COULD_NOT_FETCH_GENDER_INPUTS_ERR_MSG =
      "Couldn't fetch gender inputs";

  private final GenderInputServiceBlockingStub blockingStub;
  private final long deadlineInSeconds;

  @Override
  public List<BatchGetMatchGenderInputsOut> getBatchGetMatchGenderInputs(
      BatchGetMatchGenderInputsIn request) {
    return Try
        .of(() -> getStub().batchGetMatchGenderInputs(
            request.toBatchGetMatchGenderInputsRequest()))
        .map(Lists::newArrayList)
        .map(list -> list
            .stream()
            .map(BatchGetMatchGenderInputsOut::createFrom)
            .collect(Collectors.toList()))
        .onSuccess(response -> log.info("Received {} objects", response.size()))
        .onFailure(e -> log.error(COULD_NOT_FETCH_GENDER_INPUTS_ERR_MSG, e))
        .getOrElseThrow(e -> new UniversalDataSourceLibraryRuntimeException(
            COULD_NOT_FETCH_GENDER_INPUTS_ERR_MSG, e));
  }

  private GenderInputServiceBlockingStub getStub() {
    return blockingStub.withDeadlineAfter(deadlineInSeconds, TimeUnit.SECONDS);
  }
}
