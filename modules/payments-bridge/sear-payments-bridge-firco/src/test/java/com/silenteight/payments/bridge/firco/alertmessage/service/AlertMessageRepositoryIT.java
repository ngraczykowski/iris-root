package com.silenteight.payments.bridge.firco.alertmessage.service;

import com.silenteight.payments.bridge.testing.RepositoryTestConfiguration;
import com.silenteight.sep.base.testing.BaseDataJpaTest;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Import;

@Import(RepositoryTestConfiguration.class)
class AlertMessageRepositoryIT extends BaseDataJpaTest {

  @Autowired
  AlertMessageRepository repository;

  @Test
  void exampleRepositoryTest() {
  }
}
