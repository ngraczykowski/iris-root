package com.silenteight.sep.base.common.messaging.postprocessing;

import com.silenteight.sep.base.common.messaging.encryption.AmqpMessageDecrypter;
import com.silenteight.sep.base.common.messaging.postprocessing.DecryptMessagePostProcessor;

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
class DecryptMessagePostProcessorTest {

  @Mock
  private AmqpMessageDecrypter amqpMessageDecrypter;

  @InjectMocks
  private DecryptMessagePostProcessor underTest;

  @Test
  void givenMessage_usesDecryptorAndReturns() {
    var inputMessage = new Message("someText".getBytes(UTF_8), new MessageProperties());
    var decryptedMessage = new Message("dcryptedText".getBytes(UTF_8), new MessageProperties());
    given(amqpMessageDecrypter.decrypt(inputMessage)).willReturn(decryptedMessage);

    var actual = underTest.postProcessMessage(inputMessage);

    assertThat(actual).isEqualTo(decryptedMessage);
    then(amqpMessageDecrypter).should().decrypt(inputMessage);
  }
}
