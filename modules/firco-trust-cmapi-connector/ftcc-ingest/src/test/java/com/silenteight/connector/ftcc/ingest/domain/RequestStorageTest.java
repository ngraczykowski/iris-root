package com.silenteight.connector.ftcc.ingest.domain;

import com.silenteight.connector.ftcc.common.dto.input.RequestDto;
import com.silenteight.sep.base.testing.BaseDataJpaTest;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;
import java.util.Optional;

import static com.silenteight.connector.ftcc.ingest.domain.RequestFixtures.BATCH_ID;
import static com.silenteight.connector.ftcc.ingest.domain.RequestFixtures.makeRequestDto;
import static org.assertj.core.api.Assertions.*;

@Transactional
@TestPropertySource("classpath:/data-test.properties")
@ContextConfiguration(classes = { IngestTestConfiguration.class })
class RequestStorageTest extends BaseDataJpaTest {

  @Autowired
  private RequestStorage underTest;

  @Autowired
  private RequestRepository requestRepository;

  @Autowired
  private MessageRepository messageRepository;

  @Test
  void shouldRegister() {
    // given
    RequestDto requestDto = makeRequestDto();

    // when
    RequestStore requestStore = underTest.store(requestDto, BATCH_ID);

    // then
    assertThat(requestStore.getMessageIds()).isNotEmpty();
    Optional<RequestEntity> requestOpt = requestRepository.findByBatchId(BATCH_ID);
    assertThat(requestOpt).isPresent();
    RequestEntity request = requestOpt.get();
    assertThat(request.getBatchId()).isEqualTo(BATCH_ID);
    Collection<MessageEntity> messages = messageRepository.findAllByBatchId(BATCH_ID);
    assertThat(messages.size()).isEqualTo(requestDto.getMessages().size());
  }
}
