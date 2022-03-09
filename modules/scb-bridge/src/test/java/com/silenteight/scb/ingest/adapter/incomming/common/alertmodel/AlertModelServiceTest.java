package com.silenteight.scb.ingest.adapter.incomming.common.alertmodel;

import com.silenteight.proto.serp.v1.alert.AlertModel;
import com.silenteight.sep.base.testing.messaging.MessageSenderSpy;
import com.silenteight.sep.base.testing.messaging.MessageSenderSpyFactory;

import com.google.protobuf.Message;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AlertModelServiceTest {

  @Mock
  private AlertModelFactory alertModelFactory;

  private AlertModelService underTest;

  private MessageSenderSpy messageSender;

  @BeforeEach
  void setUp() {
    MessageSenderSpyFactory factory = new MessageSenderSpyFactory();
    AlertModelConfiguration configuration = new AlertModelConfiguration(factory);

    underTest = configuration.alertModelService(alertModelFactory);
    messageSender = factory.getLastMessageSender();
  }

  @Test
  void sendsAlertModelOnApplicationStart() {
    AlertModel dummyModel = AlertModel.newBuilder().build();
    when(alertModelFactory.get()).thenReturn(dummyModel);

    underTest.applicationStarted();

    verify(alertModelFactory).get();
    Message sentMessage = messageSender.getSentMessage();
    assertThat(sentMessage).isNotNull();
    assertThat(sentMessage.toByteArray()).isEqualTo(dummyModel.toByteArray());
  }
}