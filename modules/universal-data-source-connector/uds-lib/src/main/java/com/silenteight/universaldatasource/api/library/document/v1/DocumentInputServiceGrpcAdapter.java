package com.silenteight.universaldatasource.api.library.document.v1;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import com.silenteight.datasource.api.document.v1.DocumentInputServiceGrpc.DocumentInputServiceBlockingStub;
import com.silenteight.universaldatasource.api.library.UniversalDataSourceLibraryRuntimeException;

import com.google.common.collect.Lists;
import io.vavr.control.Try;

import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

@Slf4j
@RequiredArgsConstructor
public class DocumentInputServiceGrpcAdapter implements DocumentInputServiceClient {

  public static final String COULD_NOT_FETCH_DOCUMENT_INPUTS_ERR_MSG =
      "Couldn't fetch document inputs";

  private final DocumentInputServiceBlockingStub blockingStub;
  private final long deadlineInSeconds;

  @Override
  public List<BatchGetMatchDocumentInputsOut> getBatchGetMatchDocumentInputs(
      BatchGetMatchDocumentInputsIn request) {
    return Try
        .of(() -> getStub().batchGetMatchDocumentInputs(
            request.toBatchGetMatchDocumentInputsRequest()))
        .map(Lists::newArrayList)
        .map(list -> list
            .stream()
            .map(BatchGetMatchDocumentInputsOut::createFrom)
            .collect(Collectors.toList()))
        .onSuccess(response -> log.info("Received {} objects", response.size()))
        .onFailure(e -> log.error(COULD_NOT_FETCH_DOCUMENT_INPUTS_ERR_MSG, e))
        .getOrElseThrow(e -> new UniversalDataSourceLibraryRuntimeException(
            COULD_NOT_FETCH_DOCUMENT_INPUTS_ERR_MSG, e));
  }


  private DocumentInputServiceBlockingStub getStub() {
    return blockingStub.withDeadlineAfter(deadlineInSeconds, TimeUnit.SECONDS);
  }
}
