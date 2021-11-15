package com.silenteight.payments.bridge.warehouse.index.model.learning;

import lombok.Value;

import java.util.List;

@Value
public class IndexAlert {

  IndexAlertIdSet alertIdSet;
  List<IndexMatch> matches;
  IndexAnalystDecision decision;

}
