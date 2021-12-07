package com.silenteight.universaldatasource.api.library.bankidentificationcodes.v1;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Value;

import com.silenteight.datasource.api.bankidentificationcodes.v1.BatchGetMatchBankIdentificationCodesInputsResponse;

import java.util.List;
import java.util.stream.Collectors;

@Value
@Builder(access = AccessLevel.PACKAGE)
public class BatchGetMatchBankIdentificationCodesInputsOut {

  @Builder.Default
  List<BankIdentificationCodesInputOut> bankIdentificationCodesInputs = List.of();

  static BatchGetMatchBankIdentificationCodesInputsOut createFrom(
      BatchGetMatchBankIdentificationCodesInputsResponse request) {
    return BatchGetMatchBankIdentificationCodesInputsOut.builder()
        .bankIdentificationCodesInputs(request.getBankIdentificationCodesInputsList()
            .stream()
            .map(BankIdentificationCodesInputOut::createFrom)
            .collect(Collectors.toList()))
        .build();
  }
}
