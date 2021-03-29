package com.silenteight.hsbc.bridge.domain;

import lombok.Builder;
import lombok.Value;

@Builder
@Value
public class RelatedPair {

  Integer caseId;
  EntityComposite relatedCustomerEntitiesPair;
  IndividualComposite relatedCustomerIndividualsPair;
}
