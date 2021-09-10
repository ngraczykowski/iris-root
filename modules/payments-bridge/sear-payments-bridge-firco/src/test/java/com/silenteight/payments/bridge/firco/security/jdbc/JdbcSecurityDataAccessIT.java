package com.silenteight.payments.bridge.firco.security.jdbc;

import com.silenteight.payments.bridge.testing.JdbcTestConfiguration;
import com.silenteight.sep.base.testing.BaseJdbcTest;

import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.jdbc.Sql;

@Sql
@ContextConfiguration(classes = {
    JdbcTestConfiguration.class,
    JdbcSecurityDataAccess.class,
    SelectByCredentialsQuery.class
})
class JdbcSecurityDataAccessIT extends BaseJdbcTest {
  /*
  @Autowired
  JdbcSecurityDataAccess jdbcSecurityDataAccess;

  @Test
  void shouldFindUser() {
    assertThat(jdbcSecurityDataAccess
        .findByCredentials("username", "password"))
        .isTrue();
  }

  @Test
  void shouldNotFindUser() {
    assertThat(jdbcSecurityDataAccess
        .findByCredentials("usernasdame", "passwasdord"))
        .isFalse();
  }
  */
}
