package com.silenteight.universaldatasource.api.library.country.v1;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import com.silenteight.datasource.api.country.v1.CountryInputServiceGrpc.CountryInputServiceBlockingStub;
import com.silenteight.universaldatasource.api.library.UniversalDataSourceLibraryRuntimeException;

import com.google.common.collect.Lists;
import io.vavr.control.Try;

import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

@Slf4j
@RequiredArgsConstructor
public class CountryInputClientGrpcAdapter implements CountryInputClientService {

  public static final String COULD_NOT_FETCH_ERR_MSG = "Couldn't fetch country inputs";

  private final CountryInputServiceBlockingStub blockingStub;
  private final long deadlineInSeconds;

  @Override
  public List<BatchGetMatchCountryInputsOut> getBatchGetMatchCountryInputs(
      BatchGetMatchCountryInputsIn request) {
    return Try
        .of(() -> getStub().batchGetMatchCountryInputs(
            request.toBatchGetMatchCountryInputsRequest()))
        .map(Lists::newArrayList)
        .map(list -> list
            .stream()
            .map(BatchGetMatchCountryInputsOut::createFrom)
            .collect(Collectors.toList()))
        .onSuccess(response -> log.info("Received {} objects", response.size()))
        .onFailure(e -> log.error(COULD_NOT_FETCH_ERR_MSG, e))
        .getOrElseThrow(
            e -> new UniversalDataSourceLibraryRuntimeException(COULD_NOT_FETCH_ERR_MSG, e));
  }

  private CountryInputServiceBlockingStub getStub() {
    return blockingStub.withDeadlineAfter(deadlineInSeconds, TimeUnit.SECONDS);
  }
}
