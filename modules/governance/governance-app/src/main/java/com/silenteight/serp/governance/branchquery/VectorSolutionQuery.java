package com.silenteight.serp.governance.branchquery;

import lombok.*;

import com.silenteight.proto.serp.v1.governance.ReasoningBranchId;
import com.silenteight.proto.serp.v1.governance.VectorSolution;
import com.silenteight.proto.serp.v1.recommendation.BranchSolution;
import com.silenteight.sep.base.common.support.hibernate.ByteStringConverter;
import com.silenteight.serp.governance.branchquery.VectorSolutionQuery.VectorSolutionQueryId;

import com.google.protobuf.ByteString;
import org.hibernate.annotations.Immutable;

import java.io.Serializable;
import javax.persistence.*;

@Data
@Setter(AccessLevel.PROTECTED)
@Entity
@Immutable
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@IdClass(VectorSolutionQueryId.class)
class VectorSolutionQuery {

  @NonNull
  @Id
  private Long decisionTreeId;

  @NonNull
  @Id
  private Long featureVectorId;

  @NonNull
  private String decisionGroup;

  @NonNull
  @Enumerated(EnumType.STRING)
  private BranchSolution solution;

  @NonNull
  @Convert(converter = ByteStringConverter.class)
  private ByteString vectorSignature;

  VectorSolution asVectorSolution() {
    return VectorSolution
        .newBuilder()
        .setSolution(solution)
        .setVectorSignature(vectorSignature)
        .setReasoningBranch(
            ReasoningBranchId
                .newBuilder()
                .setDecisionTreeId(decisionTreeId)
                .setFeatureVectorId(featureVectorId))
        .build();
  }

  @Data
  static class VectorSolutionQueryId implements Serializable {

    private static final long serialVersionUID = -4224630081343013284L;

    private long decisionTreeId;
    private long featureVectorId;
  }
}
