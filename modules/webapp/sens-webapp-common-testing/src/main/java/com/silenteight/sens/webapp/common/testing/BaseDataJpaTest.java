package com.silenteight.sens.webapp.common.testing;

import com.silenteight.sens.webapp.common.database.HibernateAutoConfiguration;
import com.silenteight.sens.webapp.common.testing.containers.PostgresContainer.PostgresTestInitializer;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.ImportAutoConfiguration;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.ContextConfiguration;

@DataJpaTest
@ContextConfiguration(initializers = { PostgresTestInitializer.class })
@ImportAutoConfiguration(HibernateAutoConfiguration.class)
@SuppressWarnings("squid:S1694")
public abstract class BaseDataJpaTest {

  @Autowired
  protected TestEntityManager entityManager;
}
