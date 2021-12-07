package com.silenteight.universaldatasource.api.library.location.v1;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import com.silenteight.datasource.api.location.v1.LocationInputServiceGrpc.LocationInputServiceBlockingStub;
import com.silenteight.universaldatasource.api.library.UniversalDataSourceLibraryRuntimeException;

import com.google.common.collect.Lists;
import io.vavr.control.Try;

import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

@Slf4j
@RequiredArgsConstructor
public class LocationInputGrpcAdapter implements LocationInputServiceClient {

  public static final String COULD_NOT_GET_LOCATION_INPUTS_ERROR_MSG =
      "Couldn't fetch location inputs";

  private final LocationInputServiceBlockingStub blockingStub;
  private final long deadlineInSeconds;

  @Override
  public List<BatchGetMatchLocationInputsOut> batchGetMatchLocationInputs(
      BatchGetMatchLocationInputsIn request) {
    return Try
        .of(() -> getStub().batchGetMatchLocationInputs(
            request.toBatchGetMatchLocationInputsRequest()))
        .map(Lists::newArrayList)
        .map(list -> list.stream()
            .map(BatchGetMatchLocationInputsOut::createFrom)
            .collect(Collectors.toList()))
        .onSuccess(response -> log.info("Received {} objects", response.size()))
        .onFailure(e -> log.error(COULD_NOT_GET_LOCATION_INPUTS_ERROR_MSG, e))
        .getOrElseThrow(e -> new UniversalDataSourceLibraryRuntimeException(
            COULD_NOT_GET_LOCATION_INPUTS_ERROR_MSG, e));
  }

  private LocationInputServiceBlockingStub getStub() {
    return blockingStub.withDeadlineAfter(deadlineInSeconds, TimeUnit.SECONDS);
  }
}
