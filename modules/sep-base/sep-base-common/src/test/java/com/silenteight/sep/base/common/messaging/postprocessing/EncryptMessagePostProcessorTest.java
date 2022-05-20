package com.silenteight.sep.base.common.messaging.postprocessing;

import com.silenteight.sep.base.common.messaging.encryption.AmqpMessageEncypter;
import com.silenteight.sep.base.common.messaging.postprocessing.EncryptMessagePostProcessor;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageProperties;

import static java.nio.charset.StandardCharsets.UTF_8;
import static org.assertj.core.api.Assertions.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;

@ExtendWith(MockitoExtension.class)
class EncryptMessagePostProcessorTest {

  @Mock
  private AmqpMessageEncypter ampqMessageEncypter;

  @InjectMocks
  private EncryptMessagePostProcessor underTest;

  @Test
  void givenMessage_usesEncryptorAndReturns() {
    var inputMessage = new Message("text".getBytes(UTF_8), new MessageProperties());
    var encryptedMessage = new Message("encyptedText".getBytes(UTF_8), new MessageProperties());
    given(ampqMessageEncypter.encrypt(inputMessage)).willReturn(encryptedMessage);

    var actual = underTest.postProcessMessage(inputMessage);

    assertThat(actual).isEqualTo(encryptedMessage);
    then(ampqMessageEncypter).should().encrypt(inputMessage);
  }
}
