package com.silenteight.customerbridge.common.alertrecord;

import lombok.Builder;
import lombok.NonNull;
import lombok.Value;

import com.silenteight.proto.serp.scb.v1.ScbDecisionDetails;
import com.silenteight.proto.serp.v1.alert.AnalystSolution;
import com.silenteight.proto.serp.v1.alert.Decision;
import com.silenteight.proto.serp.v1.common.ObjectId;
import com.silenteight.protocol.utils.ObjectIds;
import com.silenteight.sep.base.common.protocol.AnyUtils;

import java.time.Instant;
import java.util.UUID;
import java.util.stream.Stream;
import javax.annotation.Nullable;

import static com.google.common.base.Strings.nullToEmpty;
import static com.google.protobuf.Timestamp.getDefaultInstance;
import static com.silenteight.proto.serp.v1.alert.AnalystSolution.ANALYST_NO_SOLUTION;
import static com.silenteight.protocol.utils.MoreTimestamps.toTimestampOrDefault;
import static org.apache.commons.lang3.StringUtils.isEmpty;
import static org.apache.commons.lang3.StringUtils.isNotEmpty;

@Value
@Builder
public class DecisionRecord {

  private static final String[] SYSTEM_OPERATORS = new String[] { "FFFFEED", "FSK" };

  @NonNull
  String systemId;

  @NonNull
  String operator;

  @Nullable
  Instant decisionDate;

  int type;

  @Nullable
  String comments;

  AnalystSolution solution;

  @Nullable
  String stateName;

  public Decision toDecision() {
    var builder = Decision.newBuilder()
        .setAuthorId(operator)
        .setComment(nullToEmpty(comments))
        .setCreatedAt(toTimestampOrDefault(decisionDate, getDefaultInstance()))
        .setId(makeDecisionId())
        .setSolution(solution);

    if (isNotEmpty(stateName))
      builder.setDetails(AnyUtils.pack(ScbDecisionDetails.newBuilder()
          .setStateName(stateName)
          .build()));

    return builder.build();
  }

  private ObjectId makeDecisionId() {
    return ObjectIds.fromUuidAndSource(
        UUID.randomUUID(), operator + "@" + type, String.valueOf(decisionDate));
  }

  public boolean isAnalystDecision() {
    if (isEmpty(operator)) {
      return false;
    }

    return Stream.of(SYSTEM_OPERATORS).noneMatch(s -> s.equalsIgnoreCase(operator));
  }

  public boolean isResetDecision() {
    return !isAnalystDecision() && isAnalystNoSolution();
  }

  private boolean isAnalystNoSolution() {
    return solution == ANALYST_NO_SOLUTION;
  }
}
