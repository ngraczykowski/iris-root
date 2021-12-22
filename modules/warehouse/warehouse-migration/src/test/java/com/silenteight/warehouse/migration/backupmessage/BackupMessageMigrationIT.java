package com.silenteight.warehouse.migration.backupmessage;

import com.silenteight.sep.base.testing.containers.PostgresContainer.PostgresTestInitializer;
import com.silenteight.sep.base.testing.containers.RabbitContainer.RabbitTestInitializer;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.autoconfigure.orm.jpa.AutoConfigureDataJpa;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.Lifecycle;
import org.springframework.integration.test.context.SpringIntegrationTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

import static org.assertj.core.api.Assertions.*;

@Sql
@SpringBootTest(classes = MigrationTestConfiguration.class)
@SpringIntegrationTest
@ContextConfiguration(initializers = {
    RabbitTestInitializer.class,
    PostgresTestInitializer.class
})
@AutoConfigureDataJpa
@ActiveProfiles("jpa-test")
class BackupMessageMigrationIT {

  private static final String NAME_COLUMN = "name";

  @Autowired
  private JdbcTemplate jdbcTemplate;

  @Autowired
  private BackupMessageMigration cut;

  @Autowired
  private BackupMessageQuery query;

  @Autowired
  private RabbitMessageContainerLifecycle rabbitMessageContainerLifecycle;

  @Autowired
  @Qualifier("backupQueueToChannelIntegrationFlow")
  Lifecycle amqpInboundChannelAdapter;

  @Test
  void shouldMigrateExistingRecordsToNewDbStructureAndStartRabbit() {
    //given
    assertThat(fetchAlertNames()).isEmpty();

    //when
    cut.migration();

    //then
    AtomicBoolean pausedDuringInitialization =
        (AtomicBoolean) ReflectionTestUtils.getField(
            rabbitMessageContainerLifecycle, "pausedDuringInitialization");

    assertThat(pausedDuringInitialization).isTrue();
    assertThat(amqpInboundChannelAdapter.isRunning()).isTrue();
    assertThat(query.notMigratedRecordExist()).isFalse();
    assertThat(fetchAlertNames()).isNotEmpty();

  }

  private List<String> fetchAlertNames() {
    return jdbcTemplate.query(
        "SELECT * FROM warehouse_alert",
        (rs, rowNum) -> rs.getString(NAME_COLUMN));
  }
}
