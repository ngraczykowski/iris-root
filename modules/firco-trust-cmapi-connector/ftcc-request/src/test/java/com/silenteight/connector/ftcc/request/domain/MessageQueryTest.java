package com.silenteight.connector.ftcc.request.domain;

import lombok.NonNull;

import com.silenteight.connector.ftcc.request.domain.exception.MessageNotFoundException;
import com.silenteight.connector.ftcc.request.get.dto.MessageDto;
import com.silenteight.sep.base.testing.BaseDataJpaTest;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

import static com.silenteight.connector.ftcc.request.domain.RequestFixtures.BATCH_ID;
import static com.silenteight.connector.ftcc.request.domain.RequestFixtures.MESSAGE_ID;
import static com.silenteight.connector.ftcc.request.domain.RequestFixtures.PAYLOAD;
import static com.silenteight.sep.base.common.support.jackson.JsonConversionHelper.INSTANCE;
import static org.assertj.core.api.Assertions.*;

@Transactional
@TestPropertySource("classpath:/data-test.properties")
@ContextConfiguration(classes = { RequestDomainTestConfiguration.class })
class MessageQueryTest extends BaseDataJpaTest {

  @Autowired
  private MessageQuery underTest;

  @Autowired
  private MessageRepository messageRepository;

  @Test
  void shouldGetMessage() throws JsonProcessingException {
    // given
    persistMessage(BATCH_ID, MESSAGE_ID, INSTANCE.objectMapper().readTree(PAYLOAD));

    // when
    MessageDto message = underTest.get(BATCH_ID, MESSAGE_ID);

    // then
    assertThat(message.getBatchName()).isEqualTo("batches/" + BATCH_ID);
    assertThat(message.getMessageName()).isEqualTo("messages/" + MESSAGE_ID);
    assertThat(message.getPayload()).isEqualTo(PAYLOAD);
  }

  @Test
  void shouldThrowExceptionWhenMessageNotFound() {
    // when
    assertThatThrownBy(() -> underTest.get(BATCH_ID, MESSAGE_ID))
        .isInstanceOf(MessageNotFoundException.class)
        .hasMessage(String.format(
            "Message with batchId=%s and messageId=%s not found.", BATCH_ID, MESSAGE_ID));
  }

  private void persistMessage(@NonNull UUID batchId, @NonNull UUID messageId, JsonNode payload) {
    MessageEntity messageEntity = MessageEntity.builder()
        .batchId(batchId)
        .messageId(messageId)
        .payload(payload)
        .build();
    messageRepository.save(messageEntity);
  }
}
