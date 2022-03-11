package com.silenteight.scb.ingest.adapter.incomming.common.alertrecord;

import lombok.Builder;
import lombok.NonNull;
import lombok.Value;

import com.silenteight.scb.ingest.adapter.incomming.common.model.ObjectId;
import com.silenteight.scb.ingest.adapter.incomming.common.model.decision.Decision;

import java.time.Instant;
import java.util.UUID;
import java.util.stream.Stream;
import javax.annotation.Nullable;

import static com.google.common.base.Strings.nullToEmpty;
import static com.silenteight.scb.ingest.adapter.incomming.common.model.decision.Decision.AnalystSolution.ANALYST_NO_SOLUTION;
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

  Decision.AnalystSolution solution;

  @Nullable
  String stateName;

  public Decision toDecision() {
    var builder = Decision.builder()
        .authorId(operator)
        .comment(nullToEmpty(comments))
        .createdAt(decisionDate)
        .id(makeDecisionId())
        .solution(solution);

    if (isNotEmpty(stateName))
      builder.stateName(stateName)
          .build();

    return builder.build();
  }

  private ObjectId makeDecisionId() {
    return ObjectId.builder()
        .id(UUID.randomUUID())
        .sourceId(operator + "@" + type)
        .discriminator(String.valueOf(decisionDate))
        .build();
  }

  public boolean isResetDecision() {
    return !isAnalystDecision() && isAnalystNoSolution();
  }

  public boolean isAnalystDecision() {
    if (isEmpty(operator)) {
      return false;
    }

    return Stream.of(SYSTEM_OPERATORS).noneMatch(s -> s.equalsIgnoreCase(operator));
  }

  private boolean isAnalystNoSolution() {
    return solution == ANALYST_NO_SOLUTION;
  }
}
