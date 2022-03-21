package com.silenteight.scb

import com.silenteight.sep.base.common.database.HibernateCacheAutoConfiguration
import com.silenteight.sep.base.common.support.hibernate.SilentEightNamingConventionConfiguration
import com.silenteight.sep.base.testing.containers.PostgresContainer.PostgresTestInitializer

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.autoconfigure.ImportAutoConfiguration
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager
import org.springframework.test.context.ContextConfiguration
import spock.lang.Specification

// Note: this is a spock version of com.silenteight.sep.base.testing.BaseDataJpaTest class

@DataJpaTest
@ContextConfiguration(initializers = [PostgresTestInitializer.class])
@ImportAutoConfiguration([
    HibernateCacheAutoConfiguration,
    SilentEightNamingConventionConfiguration
])
abstract class BaseDataJpaSpec extends Specification {

  @Autowired
  protected TestEntityManager entityManager;

}
