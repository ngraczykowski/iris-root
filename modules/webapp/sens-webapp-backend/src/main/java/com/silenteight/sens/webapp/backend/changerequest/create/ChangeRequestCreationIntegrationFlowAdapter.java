package com.silenteight.sens.webapp.backend.changerequest.create;

import lombok.RequiredArgsConstructor;

import com.silenteight.proto.serp.v1.changerequest.CreateChangeRequestCommand;

import org.springframework.integration.dsl.IntegrationFlowAdapter;
import org.springframework.integration.dsl.IntegrationFlowDefinition;

import static com.silenteight.sens.webapp.backend.changerequest.create.ChangeRequestCreationIntegrationChannels.CREATED_CHANGE_REQUEST_OUTBOUND_CHANNEL;
import static com.silenteight.sens.webapp.backend.changerequest.create.ChangeRequestCreationIntegrationChannels.CREATE_CHANGE_REQUEST_INBOUND_CHANNEL;

@RequiredArgsConstructor
class ChangeRequestCreationIntegrationFlowAdapter extends IntegrationFlowAdapter {

  private final CreateChangeRequestMessageHandler createChangeRequestMessageHandler;

  @Override
  protected IntegrationFlowDefinition<?> buildFlow() {
    return from(CREATE_CHANGE_REQUEST_INBOUND_CHANNEL)
        .handle(CreateChangeRequestCommand.class, (p, h) -> {
          createChangeRequestMessageHandler.handle(p);
          return p;
        })
        .channel(CREATED_CHANGE_REQUEST_OUTBOUND_CHANNEL);
  }
}
