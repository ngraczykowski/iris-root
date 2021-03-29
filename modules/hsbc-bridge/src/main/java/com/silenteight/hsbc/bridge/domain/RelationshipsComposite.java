package com.silenteight.hsbc.bridge.domain;

import lombok.Builder;
import lombok.Value;

import java.util.List;

@Builder
@Value
public class RelationshipsComposite {

  List<RelatedPair> relatedPairs;
}
