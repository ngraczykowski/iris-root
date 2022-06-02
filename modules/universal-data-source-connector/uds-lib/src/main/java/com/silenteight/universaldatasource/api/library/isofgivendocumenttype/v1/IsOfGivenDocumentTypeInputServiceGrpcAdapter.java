package com.silenteight.universaldatasource.api.library.isofgivendocumenttype.v1;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import com.silenteight.datasource.api.isofgivendocumenttype.v1.IsOfGivenDocumentTypeInputServiceGrpc.IsOfGivenDocumentTypeInputServiceBlockingStub;
import com.silenteight.universaldatasource.api.library.UniversalDataSourceLibraryRuntimeException;

import com.google.common.collect.Lists;
import io.vavr.control.Try;

import java.util.List;
import java.util.concurrent.TimeUnit;

import static java.util.stream.Collectors.toList;

@Slf4j
@RequiredArgsConstructor
public class IsOfGivenDocumentTypeInputServiceGrpcAdapter
    implements IsOfGivenDocumentTypeInputServiceClient {

  public static final String COULD_NOT_FETCH_IS_OF_GIVEN_DOCUMENT_TYPE_INPUTS_ERR_MSG =
      "Couldn't fetch is of given document type inputs";

  private final IsOfGivenDocumentTypeInputServiceBlockingStub blockingStub;
  private final long deadlineInSeconds;

  @Override
  public List<BatchGetIsOfGivenDocumentTypeInputsOut> getBatchGetIsOfGivenDocumentTypeInputs(
      BatchGetIsOfGivenDocumentTypeInputsIn request) {
    return Try
        .of(() -> getStub().batchGetIsOfGivenDocumentTypeInputs(
            request.toBatchGetIsOfGivenDocumentTypeInputsRequest()))
        .map(Lists::newArrayList)
        .map(list -> list
            .stream()
            .map(BatchGetIsOfGivenDocumentTypeInputsOut::createFrom)
            .collect(toList()))
        .onSuccess(response -> log.info("Received {} objects", response.size()))
        .onFailure(e -> log.error(COULD_NOT_FETCH_IS_OF_GIVEN_DOCUMENT_TYPE_INPUTS_ERR_MSG, e))
        .getOrElseThrow(e -> new UniversalDataSourceLibraryRuntimeException(
            COULD_NOT_FETCH_IS_OF_GIVEN_DOCUMENT_TYPE_INPUTS_ERR_MSG, e));
  }

  private IsOfGivenDocumentTypeInputServiceBlockingStub getStub() {
    return blockingStub.withDeadlineAfter(deadlineInSeconds, TimeUnit.SECONDS);
  }
}
