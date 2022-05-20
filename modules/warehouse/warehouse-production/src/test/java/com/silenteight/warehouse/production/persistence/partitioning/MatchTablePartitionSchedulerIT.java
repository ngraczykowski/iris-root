package com.silenteight.warehouse.production.persistence.partitioning;

import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;

import com.silenteight.sep.base.common.time.TimeSource;
import com.silenteight.sep.base.testing.containers.PostgresContainer.PostgresTestInitializer;
import com.silenteight.warehouse.production.persistence.ProductionPersistenceModule;
import com.silenteight.warehouse.production.persistence.insert.PersistenceService;
import com.silenteight.warehouse.production.persistence.mapping.alert.AlertDefinition;
import com.silenteight.warehouse.production.persistence.mapping.match.MatchDefinition;
import com.silenteight.warehouse.production.persistence.partitioning.MatchTablePartitionSchedulerIT.TablePartitionSchedulerTestConfiguration;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.integration.test.context.SpringIntegrationTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.transaction.annotation.Transactional;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.Map;

import static com.silenteight.sep.base.testing.time.MockTimeSource.ARBITRARY_INSTANCE;
import static org.assertj.core.api.Assertions.*;

@Slf4j
@SpringBootTest(classes = TablePartitionSchedulerTestConfiguration.class)
@SpringIntegrationTest
@ContextConfiguration(initializers = {
    PostgresTestInitializer.class
})
@ActiveProfiles("jpa-test")
@Transactional
public class MatchTablePartitionSchedulerIT {

  private static final String PAYLOAD = "{\"test\": \"value1\"}";
  @Autowired
  MatchTablePartitionScheduler underTest;

  @Autowired
  JdbcTemplate jdbcTemplate;

  @Autowired
  PersistenceService persistenceService;


  @Test
  public void shouldCreatePartitionedTables() {
    //create initial table:
    underTest.checkAndCreatePartitions();

    prepareData();

    assertThat(underTest.getLastPartitionRange()).isNotEmpty();

    assertThat(underTest.extractMaxRange("FOR VALUES FROM ('1') TO ('1500000')"))
        .isEqualTo(1500000);

    underTest.checkAndCreatePartitions();

    List<String> partNames = extractPartitions();
    assertThat(partNames.size()).isEqualTo(2);
    assertThat(partNames.contains("warehouse_match_1500000")).isTrue();
    assertThat(partNames.contains("warehouse_match_3000000")).isTrue();
  }

  @Test
  public void shouldNotCreatePartitionedTables() {
    //given:
    underTest.checkAndCreatePartitions();

    List<String> partNames = extractPartitions();
    assertThat(partNames.size()).isEqualTo(1);
    assertThat(partNames.contains("warehouse_match_1500000")).isTrue();
  }


  @SneakyThrows
  public void prepareData() {
    persistenceService.insert(AlertDefinition.builder()
        .discriminator("disc1")
        .name("name1")
        .payload(PAYLOAD)
        .recommendationDate(OffsetDateTime.now())
        .labels(Map.of())
        .matchDefinitions(List.of(MatchDefinition
            .builder()
            .matchId("match1")
            .name("match1name")
            .payload(PAYLOAD)
            .build()))
        .build());


  }

  private List<String> extractPartitions() {
    return jdbcTemplate.query("select pt.relname as partition_name,"
        + "                      pg_get_expr(pt.relpartbound, pt.oid, true) as partition_expression"
        + "                      from pg_class base_tb"
        + "                               join pg_inherits i on i.inhparent = base_tb.oid"
        + "                               join pg_class pt on pt.oid = i.inhrelid"
        + "                      where base_tb.oid = 'public.warehouse_match'::regclass",
        (rs, rsNum) -> rs.getString("partition_name"));
  }

  @ComponentScan(basePackageClasses = {
      ProductionPersistenceModule.class
  })
  @JdbcTest
  @EnableTransactionManagement
  public static class TablePartitionSchedulerTestConfiguration {
    @Bean
    TimeSource timeSource() {
      return ARBITRARY_INSTANCE;
    }
  }
}