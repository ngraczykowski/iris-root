package com.silenteight.searpayments.scb.etl.response;

import com.silenteight.searpayments.scb.etl.utils.AbstractMessageStructure.SourceSystem;
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
  SourceSystem sourceSystem;
  MessageFieldStructure messageFieldStructure;
}
