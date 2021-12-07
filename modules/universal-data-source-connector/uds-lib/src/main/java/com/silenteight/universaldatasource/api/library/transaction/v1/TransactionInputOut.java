package com.silenteight.universaldatasource.api.library.transaction.v1;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Value;

import com.silenteight.datasource.api.transaction.v1.TransactionInput;

import java.util.List;
import java.util.stream.Collectors;

@Value
@Builder(access = AccessLevel.PACKAGE)
public class TransactionInputOut {

  String match;

  @Builder.Default
  List<TransactionFeatureInputOut> transactionFeatureInputs = List.of();

  static TransactionInputOut createFrom(TransactionInput input) {
    return TransactionInputOut.builder()
        .match(input.getMatch())
        .transactionFeatureInputs(input.getTransactionFeatureInputsList().stream()
            .map(TransactionFeatureInputOut::createFrom)
            .collect(Collectors.toList()))
        .build();
  }
}
