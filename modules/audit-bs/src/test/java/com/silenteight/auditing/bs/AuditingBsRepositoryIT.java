package com.silenteight.auditing.bs;

import com.silenteight.auditing.bs.AuditingBsRepositoryIT.AuditingBsRepositoryITConfiguration;
import com.silenteight.testing.containers.PostgresContainer.PostgresTestInitializer;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.ContextConfiguration;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.UUID;
import javax.sql.DataSource;

import static org.assertj.core.api.Assertions.*;

@DataJpaTest
@EntityScan
@EnableJpaRepositories
@AutoConfigureTestDatabase(replace = Replace.NONE)
@ContextConfiguration(
    classes = {
        AuditingBsRepositoryITConfiguration.class,
        AuditingConfiguration.class },
    initializers = PostgresTestInitializer.class)
class AuditingBsRepositoryIT {

  @Qualifier("auditingLogger")
  @Autowired
  private AuditingLogger logger;

  @Qualifier("dataSource")
  @Autowired
  private DataSource dataSource;

  @Test
  public void shouldSaveAuditInfo() {
    //given
    AuditDataDto auditLog = AuditDataDto.builder()
        .correlationId(new UUID(0, 0))
        .details("Test")
        .entityAction("Action")
        .entityClass("com.silenteight.Example")
        .entityId("1234567890")
        .eventId(new UUID(1, 1))
        .principal("Owner")
        .timestamp(Timestamp.valueOf(LocalDateTime.now()))
        .type("ImportantAudit")
        .build();

    //when
    logger.log(auditLog);

    //then
    JdbcTemplate reader = new JdbcTemplate(dataSource);
    Integer result = reader.queryForObject("SELECT count(*) FROM audit", Integer.class);
    assertThat(result).isEqualTo(1);
  }

  @Configuration
  static class AuditingBsRepositoryITConfiguration {
  }
}
