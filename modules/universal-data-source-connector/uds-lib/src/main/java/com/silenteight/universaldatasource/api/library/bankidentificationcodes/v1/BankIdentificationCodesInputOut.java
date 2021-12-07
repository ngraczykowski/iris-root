package com.silenteight.universaldatasource.api.library.bankidentificationcodes.v1;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Value;

import com.silenteight.datasource.api.bankidentificationcodes.v1.BankIdentificationCodesInput;

import java.util.List;
import java.util.stream.Collectors;

@Value
@Builder(access = AccessLevel.PACKAGE)
public class BankIdentificationCodesInputOut {

  String match;

  @Builder.Default
  List<BankIdentificationCodesFeatureInputOut> identificationCodesFeatureInputs = List.of();

  static BankIdentificationCodesInputOut createFrom(BankIdentificationCodesInput request) {
    return BankIdentificationCodesInputOut.builder()
        .match(request.getMatch())
        .identificationCodesFeatureInputs(request.getBankIdentificationCodesFeatureInputsList()
            .stream()
            .map(BankIdentificationCodesFeatureInputOut::createFrom)
            .collect(Collectors.toList())
        )
        .build();
  }
}
