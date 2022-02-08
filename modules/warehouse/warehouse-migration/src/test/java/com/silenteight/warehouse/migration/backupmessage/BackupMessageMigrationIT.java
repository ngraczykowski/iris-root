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
    assertThat(fetchAlertNames()).contains("alerts/9bd8404f-939f-4804-bc5a-fa9543898543",
        "alerts/d1e22e2a-415a-4600-a42c-c65d8ed21ea9",
        "alerts/aa59b3d0-5414-42fe-94d9-60c607720781",
        "alerts/a5fb880f-d0ff-44ce-bccf-8edd278fa64f",
        "alerts/296ffd3e-dca7-4094-9fc7-f65355038867",
        "alerts/18a93aaf-dc62-4957-9052-5aaab869ea66",
        "alerts/4ecdc38a-58ac-454d-bed7-baf04bfa61bd",
        "alerts/58485906-bdbc-4cce-8a7f-9aeb4213b2cb",
        "alerts/eeb7f088-1c42-494a-945f-bd5e484e8f90",
        "alerts/30d1fef2-c011-4a38-acd5-3b7102f85793");
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
