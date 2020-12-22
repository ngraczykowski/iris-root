package com.silenteight.serp.governance.notifier;

import lombok.RequiredArgsConstructor;

import com.silenteight.proto.serp.notifier.v1.SendMailCommand;
import com.silenteight.proto.serp.v1.governance.ReasoningBranchId;
import com.silenteight.proto.serp.v1.governance.ReasoningBranchesDisabledEvent;
import com.silenteight.protocol.utils.Uuids;
import com.silenteight.sep.base.common.time.DateFormatter;

import java.util.List;
import java.util.stream.Collectors;
import javax.annotation.Nonnull;

import static com.silenteight.protocol.utils.MoreTimestamps.toInstant;

@RequiredArgsConstructor
class ReasoningBranchesDisabledHandler {

  private static final String DRY_RUN_BODY =
      "SERP Circuit Breaker [DRY RUN] has triggered with ID - %s on %s due to alert %s and "
          + "has disabled below Reasoning Branch(es):\n\n%s\n\n"
          + "This is an Auto Generated Mail. Please do not reply.";
  private static final String NORMAL_BODY =
      "SERP Circuit Breaker has triggered with ID - %s on %s due to alert %s and "
          + "has disabled below Reasoning Branch(es):\n\n%s\n\n"
          + "This is an Auto Generated Mail. Please do not reply.";

  private final boolean dryRunEnabled;
  private final DateFormatter formatter;

  SendMailCommand notifyReasoningBranchesDisabled(ReasoningBranchesDisabledEvent event) {

    return SendMailCommand
        .newBuilder()
        .setSubject(composeSubject(event.getCause().getDescription()))
        .setBody(composeBody(event))
        .build();
  }

  private String composeSubject(String description) {
    StringBuilder subject = new StringBuilder();
    if (dryRunEnabled)
      subject.append("[DRY-RUN] ");

    return subject.append("Reasoning Branches Disabled - ").append(description).toString();
  }

  @Nonnull
  private String composeBody(ReasoningBranchesDisabledEvent event) {
    String template = dryRunEnabled ? DRY_RUN_BODY : NORMAL_BODY;

    return String.format(
        template,
        Uuids.toJavaUuid(event.getCorrelationId()),
        formatter.format(toInstant(event.getCreatedAt())),
        event.getCause().getAlertId().getSourceId(),
        getListOfReasoningBranches(event.getReasoningBranchesList()));
  }

  private static String getListOfReasoningBranches(List<ReasoningBranchId> reasoningBranchesList) {
    return reasoningBranchesList
        .stream()
        .distinct()
        .map(rb -> " * " + rb.getDecisionTreeId() + "-" + rb.getFeatureVectorId())
        .collect(Collectors.joining("\n"));
  }
}
