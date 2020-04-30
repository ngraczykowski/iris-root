package com.silenteight.auditing.bs;

import com.silenteight.auditing.bs.AuditingBsRepositoryIT.AuditingBsRepositoryITConfiguration;
import com.silenteight.testing.containers.PostgresContainer.PostgresTestInitializer;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.ContextConfiguration;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.util.Collection;
import java.util.UUID;
import javax.sql.DataSource;

import static java.util.UUID.randomUUID;
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

  @Autowired
  private AuditingLogger logger;

  @Autowired
  private AuditingFinder finder;

  @Autowired
  private DataSource dataSource;

  @Autowired
  private TestEntityManager entityManager;

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

  @Test
  public void shouldFindAuditInfo() {
    //given
    UUID insideBoundsEventId1 = UUID.fromString("de1afe98-0b58-4941-9791-4e081f9b8139");
    UUID insideBoundsEventId2 = UUID.fromString("30131be0-7405-41f1-b79e-fe109a5d2a41");
    UUID beforeLowerBoundEventId = UUID.fromString("5367c5a5-5e55-45db-b14f-9e3808096d18");
    UUID afterUpperBoundEventId = UUID.fromString("ecbadfff-164b-4751-b131-7177438e9903");
    insertAuditData(insideBoundsEventId1, Timestamp.valueOf("2020-04-12 12:14:32.456"));
    insertAuditData(insideBoundsEventId2, Timestamp.valueOf("2020-04-17 21:56:18.971"));
    insertAuditData(beforeLowerBoundEventId, Timestamp.valueOf("2020-04-05 11:32:45.123"));
    insertAuditData(afterUpperBoundEventId, Timestamp.valueOf("2020-04-25 08:11:21.748"));
    OffsetDateTime from = OffsetDateTime.parse("2020-04-10T12:00:00Z");
    OffsetDateTime to = OffsetDateTime.parse("2020-04-20T12:00:00Z");

    //when
    Collection<AuditDataDto> result = finder.find(from, to);

    //then
    assertThat(result.size()).isEqualTo(2);
    assertThat(result).extracting(AuditDataDto::getEventId)
        .containsExactly(insideBoundsEventId1, insideBoundsEventId2);
  }

  private void insertAuditData(UUID eventId, Timestamp timestamp) {
    entityManager.getEntityManager().createNativeQuery(
        "INSERT INTO audit (event_id, correlation_id, timestamp, type) VALUES (?,?,?,?)")
        .setParameter(1, eventId)
        .setParameter(2, randomUUID())
        .setParameter(3, timestamp)
        .setParameter(4, "ImportantAudit")
        .executeUpdate();
  }

  @Configuration
  static class AuditingBsRepositoryITConfiguration {
  }
}
