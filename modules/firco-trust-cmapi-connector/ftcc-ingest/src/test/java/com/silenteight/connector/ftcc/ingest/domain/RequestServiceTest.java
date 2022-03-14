package com.silenteight.connector.ftcc.ingest.domain;

import com.silenteight.sep.base.testing.BaseDataJpaTest;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

import static com.silenteight.connector.ftcc.ingest.domain.RequestFixtures.BATCH_ID;
import static org.assertj.core.api.Assertions.*;

@Transactional
@TestPropertySource("classpath:/data-test.properties")
@ContextConfiguration(classes = { IngestTestConfiguration.class })
class RequestServiceTest extends BaseDataJpaTest {

  @Autowired
  private RequestService underTest;

  @Autowired
  private RequestRepository requestRepository;

  @Test
  void shouldCreateMessage() {
    // when
    underTest.create(BATCH_ID);

    // then
    Optional<RequestEntity> requestOpt = requestRepository.findByBatchId(BATCH_ID);
    assertThat(requestOpt).isPresent();
    RequestEntity request = requestOpt.get();
    assertThat(request.getBatchId()).isEqualTo(BATCH_ID);
  }
}
