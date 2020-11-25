package com.silenteight.serp.governance.branchquery;

import lombok.RequiredArgsConstructor;

import com.silenteight.proto.serp.v1.governance.VectorSolution;
import com.silenteight.proto.serp.v1.recommendation.BranchSolution;

import com.google.protobuf.ByteString;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import javax.annotation.Nonnull;

import static java.util.Collections.emptyList;
import static java.util.function.Predicate.not;
import static java.util.stream.Collectors.toCollection;
import static java.util.stream.Collectors.toList;

@RequiredArgsConstructor
public class VectorSolutionFinder {

  private final VectorSolutionQueryRepository repository;

  @Transactional(readOnly = true)
  public Collection<VectorSolution> findSolutions(
      String decisionGroup,
      Collection<ByteString> vectorSignatures) {

    return new SolutionFinder(decisionGroup, vectorSignatures).findAll();
  }

  @RequiredArgsConstructor
  private class SolutionFinder {

    private final String decisionGroup;
    private final Collection<ByteString> vectorSignatures;

    private List<ByteString> solvedVectors;

    List<VectorSolution> findAll() {
      if (vectorSignatures.isEmpty())
        return emptyList();

      int expectedCapacity = vectorSignatures.size();
      solvedVectors = new ArrayList<>(expectedCapacity);

      List<VectorSolution> solutions = solveVectors(expectedCapacity);
      solutions.addAll(collectUnsolvedVectors(solvedVectors));

      solvedVectors = null;

      return solutions;
    }

    private List<VectorSolution> solveVectors(int expectedCapacity) {
      // XXX(ahaczewski): Partition vectorSignatures to prevent SQL errors. Shouldn't be a problem
      //  as there are no alerts with over 32k matches, but then -- because there are no such alerts
      //  now, it doesn't mean there won't be. You have been warned.
      return repository
          .findAllByDecisionGroupAndVectorSignatureIn(decisionGroup, vectorSignatures)
          .map(VectorSolutionQuery::asVectorSolution)
          .peek(vs -> solvedVectors.add(vs.getVectorSignature()))
          .collect(toCollection(() -> new ArrayList<>(expectedCapacity)));
    }

    private List<VectorSolution> collectUnsolvedVectors(List<ByteString> solvedVectors) {
      return vectorSignatures
          .stream()
          .filter(not(solvedVectors::contains))
          .map(this::createUnsolvedVector)
          .collect(toList());
    }

    @Nonnull
    private VectorSolution createUnsolvedVector(ByteString featureVector) {
      return VectorSolution.newBuilder()
          .setSolution(BranchSolution.BRANCH_NO_DECISION)
          .setVectorSignature(featureVector)
          .build();
    }
  }
}
