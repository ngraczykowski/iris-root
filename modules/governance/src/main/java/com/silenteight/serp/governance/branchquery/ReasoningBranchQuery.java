package com.silenteight.serp.governance.branchquery;

import lombok.*;

import com.silenteight.proto.serp.v1.alert.VectorValue;
import com.silenteight.proto.serp.v1.governance.ReasoningBranchId;
import com.silenteight.proto.serp.v1.governance.ReasoningBranchSummary;
import com.silenteight.proto.serp.v1.recommendation.BranchSolution;
import com.silenteight.protocol.utils.MoreTimestamps;
import com.silenteight.serp.governance.branchquery.ReasoningBranchQuery.ReasoningBranchQueryId;
import com.silenteight.serp.governance.support.VectorValueListConverter;

import org.hibernate.annotations.Immutable;

import java.io.Serializable;
import java.time.OffsetDateTime;
import java.util.List;
import javax.persistence.*;

import static com.silenteight.sep.base.common.protocol.ByteStringUtils.fromBase64String;
import static java.util.stream.Collectors.toList;

@Data
@Setter(AccessLevel.PROTECTED)
@Entity
@Immutable
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@IdClass(ReasoningBranchQueryId.class)
class ReasoningBranchQuery {

  @NonNull
  @Id
  private Long decisionTreeId;

  @NonNull
  @Id
  private Long featureVectorId;

  @NonNull
  private String featureVectorSignature;

  @Enumerated(EnumType.STRING)
  private BranchSolution solution = BranchSolution.BRANCH_NO_DECISION;

  private Boolean enabled;

  @NonNull
  @Convert(converter = VectorValueListConverter.class)
  private List<VectorValue> values;

  private OffsetDateTime createdAt;

  private OffsetDateTime updatedAt;

  private OffsetDateTime lastUsedAt;

  ReasoningBranchSummary summarize() {
    List<String> featureValues = values.stream().map(VectorValue::getTextValue).collect(toList());

    BranchSolution branchSolution = solution != null ? solution : BranchSolution.BRANCH_NO_DECISION;

    boolean branchEnabled = enabled != null ? enabled : true;

    var builder = ReasoningBranchSummary
        .newBuilder()
        .setEnabled(branchEnabled)
        .setReasoningBranchId(ReasoningBranchId.newBuilder()
            .setDecisionTreeId(getDecisionTreeId())
            .setFeatureVectorId(getFeatureVectorId())
            .build())
        .setSolution(branchSolution)
        .setFeatureVectorSignature(fromBase64String(featureVectorSignature))
        .addAllFeatureValue(featureValues);

    if (getCreatedAt() != null)
      builder.setCreatedAt(MoreTimestamps.toTimestamp(getCreatedAt()));

    if (getUpdatedAt() != null)
      builder.setUpdatedAt(MoreTimestamps.toTimestamp(getUpdatedAt()));

    if (getLastUsedAt() != null)
      builder.setLastUsedAt(MoreTimestamps.toTimestamp(getLastUsedAt()));

    return builder.build();
  }

  @Data
  static class ReasoningBranchQueryId implements Serializable {

    private static final long serialVersionUID = -4224630081343013284L;

    private long decisionTreeId;
    private long featureVectorId;
  }
}
