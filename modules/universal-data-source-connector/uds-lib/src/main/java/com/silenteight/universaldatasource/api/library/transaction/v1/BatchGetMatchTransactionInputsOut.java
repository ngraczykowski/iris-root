package com.silenteight.universaldatasource.api.library.transaction.v1;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Value;

import com.silenteight.datasource.api.transaction.v1.BatchGetMatchTransactionInputsResponse;

import java.util.List;
import java.util.stream.Collectors;

@Value
@Builder(access = AccessLevel.PACKAGE)
public class BatchGetMatchTransactionInputsOut {

  @Builder.Default
  List<TransactionInputOut> transactionInputs = List.of();

  static BatchGetMatchTransactionInputsOut createFrom(
      BatchGetMatchTransactionInputsResponse response) {
    return BatchGetMatchTransactionInputsOut.builder()
        .transactionInputs(response.getTransactionInputsList().stream()
            .map(TransactionInputOut::createFrom)
            .collect(Collectors.toList()))
        .build();
  }
}
