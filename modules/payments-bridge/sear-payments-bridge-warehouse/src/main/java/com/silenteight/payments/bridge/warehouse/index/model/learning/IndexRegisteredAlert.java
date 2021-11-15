package com.silenteight.payments.bridge.warehouse.index.model.learning;

import lombok.Value;

import java.util.List;

@Value
public class IndexRegisteredAlert {

  IndexAlertIdSet alertIdSet;
  List<String> matchNames;
  IndexAnalystDecision decision;

}
