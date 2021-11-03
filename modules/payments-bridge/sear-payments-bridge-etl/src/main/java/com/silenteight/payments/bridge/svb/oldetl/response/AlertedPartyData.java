package com.silenteight.payments.bridge.svb.oldetl.response;

import lombok.Builder;
import lombok.Singular;
import lombok.Value;

import org.apache.commons.lang3.StringUtils;

import java.util.List;
import java.util.Optional;

@Value
@Builder
public class AlertedPartyData {

  @Singular
  List<String> names;
  @Singular
  List<String> addresses;
  @Singular
  List<String> ctryTowns;
  @Singular
  List<String> nameAddresses;

  String accountNumber;

  // DO NOT USE THIS:
  boolean noAcctNumFlag;
  int numOfLines;
  int messageLength;

  MessageFieldStructure messageFieldStructure;

  public Optional<String> getAccountNumberOrFirstName() {
    if (StringUtils.isNotBlank(accountNumber))
      return Optional.of(accountNumber);
    else
      return names.stream().map(String::trim).findFirst();
  }

  public Optional<String> getFirstAlertedPartyName() {
    return names.stream().findFirst();
  }

  public Optional<String> getFirstAlertedPartyAddress() {
    return addresses.stream().findFirst();
  }

  public Optional<String> getFirstAlertedPartyCtryTown() {
    return ctryTowns.stream().findFirst();
  }

  public Optional<String> getFirstAlertedPartyNameAddress() {
    return nameAddresses.stream().findFirst();
  }
}
