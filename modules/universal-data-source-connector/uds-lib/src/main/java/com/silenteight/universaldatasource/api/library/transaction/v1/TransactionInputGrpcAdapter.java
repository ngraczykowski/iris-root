package com.silenteight.universaldatasource.api.library.transaction.v1;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import com.silenteight.datasource.api.transaction.v1.TransactionInputServiceGrpc.TransactionInputServiceBlockingStub;
import com.silenteight.universaldatasource.api.library.UniversalDataSourceLibraryRuntimeException;

import com.google.common.collect.Lists;
import io.vavr.control.Try;

import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

@Slf4j
@RequiredArgsConstructor
public class TransactionInputGrpcAdapter implements TransactionInputServiceClient {

  public static final String COULD_NOT_GET_TRANSACTION_INPUTS_ERROR_MSG =
      "Couldn't fetch transaction inputs";

  private final TransactionInputServiceBlockingStub blockingStub;
  private final long deadlineInSeconds;

  @Override
  public List<BatchGetMatchTransactionInputsOut> batchGetMatchTransactionInputs(
      BatchGetMatchTransactionInputsIn request) {
    return Try
        .of(() -> getStub().batchGetMatchTransactionInputs(
            request.toBatchGetMatchTransactionInputsRequest()))
        .map(Lists::newArrayList)
        .map(list -> list.stream()
            .map(BatchGetMatchTransactionInputsOut::createFrom)
            .collect(Collectors.toList()))
        .onSuccess(response -> log.info("Received {} objects", response.size()))
        .onFailure(e -> log.error(COULD_NOT_GET_TRANSACTION_INPUTS_ERROR_MSG, e))
        .getOrElseThrow(e -> new UniversalDataSourceLibraryRuntimeException(
            COULD_NOT_GET_TRANSACTION_INPUTS_ERROR_MSG, e));
  }

  private TransactionInputServiceBlockingStub getStub() {
    return blockingStub.withDeadlineAfter(deadlineInSeconds, TimeUnit.SECONDS);
  }
}
