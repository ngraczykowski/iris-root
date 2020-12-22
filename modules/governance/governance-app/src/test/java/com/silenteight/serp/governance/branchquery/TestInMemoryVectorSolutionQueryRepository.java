package com.silenteight.serp.governance.branchquery;

import com.google.protobuf.ByteString;

import java.util.*;
import java.util.stream.Stream;

class TestInMemoryVectorSolutionQueryRepository implements VectorSolutionQueryRepository {

  private Map<String, Collection<VectorSolutionQuery>> store = new HashMap<>();

  void store(VectorSolutionQuery query) {
    store.computeIfAbsent(query.getDecisionGroup(), k -> new ArrayList<>());
    store.get(query.getDecisionGroup()).add(query);
  }

  @Override
  public Stream<VectorSolutionQuery> findAllByDecisionGroupAndVectorSignatureIn(
      String decisionGroup, Collection<ByteString> vectorSignatures) {

    return Optional.ofNullable(store.get(decisionGroup))
        .map(solutions -> solutions.stream()
            .filter(s -> vectorSignatures.contains(s.getVectorSignature())))
        .orElse(Stream.empty());
  }
}
