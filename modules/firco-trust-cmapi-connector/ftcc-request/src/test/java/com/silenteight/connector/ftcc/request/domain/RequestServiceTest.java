package com.silenteight.connector.ftcc.request.domain;

import com.silenteight.sep.base.testing.BaseDataJpaTest;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

import static org.assertj.core.api.Assertions.*;

@Transactional
@TestPropertySource("classpath:/data-test.properties")
@ContextConfiguration(classes = { RequestDomainTestConfiguration.class })
class RequestServiceTest extends BaseDataJpaTest {

  @Autowired
  private RequestService underTest;

  @Autowired
  private RequestRepository requestRepository;

  @Test
  void shouldCreateMessage() {
    // when
    underTest.create(RequestFixtures.BATCH_ID);

    // then
    Optional<RequestEntity> requestOpt = requestRepository.findByBatchId(RequestFixtures.BATCH_ID);
    Assertions.assertThat(requestOpt).isPresent();
    RequestEntity request = requestOpt.get();
    assertThat(request.getBatchId()).isEqualTo(RequestFixtures.BATCH_ID);
  }
}
