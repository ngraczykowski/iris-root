package com.silenteight.payments.bridge.svb.oldetl.response;

import lombok.Builder;
import lombok.Value;

import java.util.List;

@Value
@Builder
public class AlertedPartyData {

  List<String> names;
  List<String> addresses;
  List<String> ctryTowns;
  List<String> nameAddresses;
  boolean noAcctNumFlag;
  int numOfLines;
  int messageLength;

  // DO NOT USE THIS:
  SourceSystem sourceSystem;
  MessageFieldStructure messageFieldStructure;
}
