package com.silenteight.connector.ftcc.ingest.domain;

import com.silenteight.sep.base.testing.BaseDataJpaTest;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;

import static com.silenteight.connector.ftcc.ingest.domain.MessageFixtures.BATCH_ID;
import static com.silenteight.connector.ftcc.ingest.domain.MessageFixtures.PAYLOAD;
import static org.assertj.core.api.Assertions.*;

@Transactional
@TestPropertySource("classpath:/data-test.properties")
@ContextConfiguration(classes = { IngestTestConfiguration.class })
class MessageServiceTest extends BaseDataJpaTest {

  @Autowired
  private MessageService underTest;

  @Autowired
  private MessageRepository messageRepository;

  @Test
  void shouldCreateMessage() {
    // when
    underTest.create(BATCH_ID, PAYLOAD);

    // then
    Collection<MessageEntity> messages = messageRepository.findAllByBatchId(BATCH_ID);
    assertThat(messages).isNotEmpty();
    MessageEntity message = messages.iterator().next();
    assertThat(message.getBatchId()).isEqualTo(BATCH_ID);
    assertThat(message.getPayload()).isEqualTo(PAYLOAD);
  }
}
