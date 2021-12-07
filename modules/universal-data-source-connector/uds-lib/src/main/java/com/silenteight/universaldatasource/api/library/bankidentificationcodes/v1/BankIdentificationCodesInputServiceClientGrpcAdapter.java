package com.silenteight.universaldatasource.api.library.bankidentificationcodes.v1;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import com.silenteight.datasource.api.bankidentificationcodes.v1.BankIdentificationCodesInputServiceGrpc.BankIdentificationCodesInputServiceBlockingStub;
import com.silenteight.universaldatasource.api.library.UniversalDataSourceLibraryRuntimeException;

import com.google.common.collect.Lists;
import io.vavr.control.Try;

import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

@Slf4j
@RequiredArgsConstructor
public class BankIdentificationCodesInputServiceClientGrpcAdapter
    implements BankIdentificationCodesInputServiceClient {

  public static final String COULD_NOT_FETCH_BANK_IDENTIFICATION_CODES_ERR_MSG =
      "Couldn't bank identification codes";

  private final BankIdentificationCodesInputServiceBlockingStub blockingStub;
  private final long deadlineInSeconds;

  @Override
  public List<BatchGetMatchBankIdentificationCodesInputsOut>
      getBatchGetMatchBankIdentificationCodesInputs(
        BatchGetMatchBankIdentificationCodesInputsIn request) {
    return Try
        .of(() -> getStub().batchGetMatchBankIdentificationCodesInputs(
            request.toBatchGetMatchBankIdentificationCodesInputsRequest()))
        .map(Lists::newArrayList)
        .map(list -> list
            .stream()
            .map(BatchGetMatchBankIdentificationCodesInputsOut::createFrom)
            .collect(Collectors.toList()))
        .onSuccess(response -> log.info("Received {} objects", response.size()))
        .onFailure(e -> log.error(COULD_NOT_FETCH_BANK_IDENTIFICATION_CODES_ERR_MSG, e))
        .getOrElseThrow(e -> new UniversalDataSourceLibraryRuntimeException(
            COULD_NOT_FETCH_BANK_IDENTIFICATION_CODES_ERR_MSG, e));
  }


  private BankIdentificationCodesInputServiceBlockingStub getStub() {
    return blockingStub.withDeadlineAfter(deadlineInSeconds, TimeUnit.SECONDS);
  }
}
