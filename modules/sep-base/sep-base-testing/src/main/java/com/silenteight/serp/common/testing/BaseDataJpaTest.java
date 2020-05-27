package com.silenteight.serp.common.testing;

import com.silenteight.serp.common.database.HibernateCacheAutoConfiguration;
import com.silenteight.serp.common.support.hibernate.SilentEightNamingConventionConfiguration;
import com.silenteight.serp.common.testing.containers.PostgresContainer.PostgresTestInitializer;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.ImportAutoConfiguration;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.ContextConfiguration;

@DataJpaTest
@ContextConfiguration(initializers = { PostgresTestInitializer.class })
@ImportAutoConfiguration({
    HibernateCacheAutoConfiguration.class,
    SilentEightNamingConventionConfiguration.class,
})
@SuppressWarnings("squid:S1694")
public abstract class BaseDataJpaTest {

  @Autowired
  protected TestEntityManager entityManager;
}
