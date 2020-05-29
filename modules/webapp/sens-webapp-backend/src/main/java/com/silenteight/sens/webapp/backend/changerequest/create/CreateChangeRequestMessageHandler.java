package com.silenteight.sens.webapp.backend.changerequest.create;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import com.silenteight.proto.serp.v1.changerequest.CreateChangeRequestCommand;
import com.silenteight.sens.webapp.audit.correlation.RequestCorrelation;
import com.silenteight.sens.webapp.backend.changerequest.domain.ChangeRequestService;

import static com.silenteight.protocol.utils.MoreTimestamps.toOffsetDateTime;
import static com.silenteight.protocol.utils.Uuids.toJavaUuid;

@RequiredArgsConstructor
public class CreateChangeRequestMessageHandler {

  @NonNull
  private final ChangeRequestService changeRequestService;

  public void handle(CreateChangeRequestCommand command) {
    RequestCorrelation.set(toJavaUuid(command.getCorrelationId()));

    changeRequestService.create(
        toJavaUuid(command.getBulkChangeId()),
        command.getMakerUsername(),
        command.getMakerComment(),
        toOffsetDateTime(command.getCreatedAt()));
  }
}
