package com.silenteight.warehouse.migration.backupmessage;

import com.silenteight.sep.base.testing.containers.PostgresContainer.PostgresTestInitializer;
import com.silenteight.sep.base.testing.containers.RabbitContainer.RabbitTestInitializer;

import org.junit.jupiter.api.Test;
import org.springframework.amqp.rabbit.listener.SimpleMessageListenerContainer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.AutoConfigureDataJpa;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.integration.dsl.IntegrationFlow;
import org.springframework.integration.dsl.StandardIntegrationFlow;
import org.springframework.integration.test.context.SpringIntegrationTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.*;

@Sql
@SpringBootTest(classes = MessageMigrationTestConfiguration.class)
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

  @Test
  void shouldMigrateExistingRecordsToNewDbStructureAndStartRabbit() {
    //given
    assertThat(getPausedDuringInitialization()).isTrue();
    assertThat(fetchAlertNames()).isEmpty();

    //when
    cut.migration();

    //then
    assertThat(query.notMigratedRecordExist()).isFalse();
    assertThat(getQueues())
        .extracting(SimpleMessageListenerContainer::isRunning)
        .containsExactly(true, true, true, true);
    assertThat(fetchAlertNames()).isNotEmpty();

  }

  List<String> fetchAlertNames() {
    return jdbcTemplate.query(
        "SELECT * FROM warehouse_alert",
        (rs, rowNum) -> rs.getString(NAME_COLUMN));
  }

  AtomicBoolean getPausedDuringInitialization() {
    return (AtomicBoolean) ReflectionTestUtils.getField(
        rabbitMessageContainerLifecycle, "pausedDuringInitialization");
  }

  @SuppressWarnings("unchecked")
  List<SimpleMessageListenerContainer> getQueues() {
    List<IntegrationFlow> queueBeans =
        (List<IntegrationFlow>) ReflectionTestUtils.getField(
            rabbitMessageContainerLifecycle, "queueBeans");

    return queueBeans.stream()
        .filter(StandardIntegrationFlow.class::isInstance)
        .map(StandardIntegrationFlow.class::cast)
        .map(StandardIntegrationFlow::getIntegrationComponents)
        .flatMap(e -> e.keySet().stream())
        .filter(SimpleMessageListenerContainer.class::isInstance)
        .map(SimpleMessageListenerContainer.class::cast)
        .collect(Collectors.toList());
  }
}
