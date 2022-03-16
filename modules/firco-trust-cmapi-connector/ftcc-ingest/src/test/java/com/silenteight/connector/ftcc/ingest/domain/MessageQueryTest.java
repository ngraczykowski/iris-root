package com.silenteight.connector.ftcc.ingest.domain;

import lombok.NonNull;

import com.silenteight.connector.ftcc.ingest.domain.dto.MessageDetailsDto;
import com.silenteight.connector.ftcc.ingest.domain.exception.MessageNotFoundException;
import com.silenteight.sep.base.testing.BaseDataJpaTest;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

import static com.silenteight.connector.ftcc.ingest.domain.MessageFixtures.BATCH_ID;
import static com.silenteight.connector.ftcc.ingest.domain.MessageFixtures.MESSAGE_ID;
import static com.silenteight.connector.ftcc.ingest.domain.MessageFixtures.PAYLOAD;
import static com.silenteight.sep.base.common.support.jackson.JsonConversionHelper.INSTANCE;
import static java.lang.String.format;
import static org.assertj.core.api.Assertions.*;

@Transactional
@TestPropertySource("classpath:/data-test.properties")
@ContextConfiguration(classes = { IngestTestConfiguration.class })
class MessageQueryTest extends BaseDataJpaTest {

  @Autowired
  private MessageQuery underTest;

  @Autowired
  private MessageRepository messageRepository;

  @Test
  void shouldGetMessageDetails() throws JsonProcessingException {
    // given
    persistMessage(BATCH_ID, MESSAGE_ID, INSTANCE.objectMapper().readTree(PAYLOAD));

    // when
    MessageDetailsDto message = underTest.details(BATCH_ID, MESSAGE_ID);

    // then
    assertThat(message.getBatchName()).isEqualTo("batches/" + BATCH_ID);
    assertThat(message.getMessageName()).isEqualTo("messages/" + MESSAGE_ID);
    assertThat(message.getPayload()).isEqualTo(PAYLOAD);
  }

  @Test
  void shouldThrowExceptionWhenMessageNotFound() {
    // when
    assertThatThrownBy(() -> underTest.details(BATCH_ID, MESSAGE_ID))
        .isInstanceOf(MessageNotFoundException.class)
        .hasMessage(format(
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
