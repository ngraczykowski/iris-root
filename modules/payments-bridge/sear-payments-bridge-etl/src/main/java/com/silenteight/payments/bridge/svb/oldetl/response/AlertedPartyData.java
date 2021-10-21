package com.silenteight.payments.bridge.svb.oldetl.response;

import lombok.Builder;
import lombok.Singular;
import lombok.Value;

import java.util.List;

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

  // DO NOT USE THIS:
  boolean noAcctNumFlag;
  int numOfLines;
  int messageLength;

  MessageFieldStructure messageFieldStructure;
}
