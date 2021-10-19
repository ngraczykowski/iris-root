package com.silenteight.payments.bridge.governance.core.solvingmodel.service;

import com.silenteight.model.api.v1.SolvingModel;
import com.silenteight.payments.bridge.governance.core.solvingmodel.port.ModelPromotedToProductionReceivedGateway;
import com.silenteight.proto.payments.bridge.internal.v1.event.ModelUpdated;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ReceiveCurrentModelServiceTest {

  private ReceiveCurrentModelService service;
  @Mock
  private ModelPromotedToProductionReceivedGateway gateway;

  @BeforeEach
  void setUp() {
    service = new ReceiveCurrentModelService(gateway);
  }

  @Test
  void shouldSendMessage() {
    service.handleModelPromotedForProductionMessage(
        SolvingModel.newBuilder().build());

    verify(gateway).send(ModelUpdated.newBuilder().build());
  }
}
