package com.silenteight.serp.governance.branchquery;

import com.silenteight.serp.governance.branchquery.VectorSolutionQuery.VectorSolutionQueryId;

import com.google.protobuf.ByteString;
import org.springframework.data.repository.Repository;

import java.util.Collection;
import java.util.stream.Stream;

interface VectorSolutionQueryRepository
    extends Repository<VectorSolutionQuery, VectorSolutionQueryId> {

  Stream<VectorSolutionQuery> findAllByDecisionGroupAndVectorSignatureIn(
      String decisionGroup,
      Collection<ByteString> vectorSignatures);
}
