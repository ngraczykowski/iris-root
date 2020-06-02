package com.silenteight.sens.webapp.backend.bulkchange;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import com.silenteight.proto.serp.v1.governance.CreateBulkBranchChangeCommand;
import com.silenteight.proto.serp.v1.governance.CreateBulkBranchChangeCommand.BranchSolutionChange;
import com.silenteight.proto.serp.v1.governance.CreateBulkBranchChangeCommand.Builder;
import com.silenteight.proto.serp.v1.governance.CreateBulkBranchChangeCommand.EnablementChange;
import com.silenteight.proto.serp.v1.governance.ReasoningBranchId;
import com.silenteight.sens.webapp.audit.correlation.RequestCorrelation;
import com.silenteight.sens.webapp.audit.trace.AuditTracer;
import com.silenteight.sens.webapp.grpc.BranchSolutionMapper;

import org.jetbrains.annotations.NotNull;

import static com.silenteight.protocol.utils.Uuids.fromJavaUuid;
import static com.silenteight.sens.webapp.logging.SensWebappLogMarkers.CHANGE_REQUEST;
import static java.util.stream.Collectors.toList;

@Slf4j
@RequiredArgsConstructor
class CreateBulkChangeUseCase {

  private final CreateBulkChangeMessageGateway gateway;
  private final AuditTracer auditTracer;

  void apply(@NonNull CreateBulkChangeCommand command) {
    log.debug(CHANGE_REQUEST, "Creating Bulk Change requested, command={}", command);

    gateway.send(getCreateBulkBranchChangeCommand(command));
    auditTracer.save(new BulkChangeCreationRequestedEvent(command));
  }

  @NotNull
  private static CreateBulkBranchChangeCommand getCreateBulkBranchChangeCommand(
      @NonNull CreateBulkChangeCommand command) {

    Builder builder = CreateBulkBranchChangeCommand
        .newBuilder()
        .setId(fromJavaUuid(command.getBulkChangeId()))
        .setCorrelationId(fromJavaUuid(RequestCorrelation.id()))
        .addAllReasoningBranchIds(command
            .getReasoningBranchIds()
            .stream()
            .map(reasoningBranchIdDto -> ReasoningBranchId
                .newBuilder()
                .setDecisionTreeId(reasoningBranchIdDto.getDecisionTreeId())
                .setFeatureVectorId(reasoningBranchIdDto.getFeatureVectorId())
                .build()
            ).collect(toList()));

    command.getEnablement().ifPresent(activation -> setEnablement(builder, activation));
    command.getSolution().ifPresent(solution -> setSolution(builder, solution));

    return builder.build();
  }

  private static void setEnablement(Builder builder, Boolean activation) {
    builder.setEnablementChange(EnablementChange.newBuilder().setEnabled(activation).build());
  }

  private static void setSolution(Builder builder, String solution) {
    builder.setSolutionChange(
        BranchSolutionChange
            .newBuilder()
            .setSolution(BranchSolutionMapper.map(solution))
            .build());
  }
}
