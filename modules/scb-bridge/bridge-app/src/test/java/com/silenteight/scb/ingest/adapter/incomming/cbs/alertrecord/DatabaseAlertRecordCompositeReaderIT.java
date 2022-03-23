package com.silenteight.scb.ingest.adapter.incomming.cbs.alertrecord;

import lombok.RequiredArgsConstructor;

import com.silenteight.proto.serp.scb.v1.ScbAlertIdContext;
import com.silenteight.scb.ingest.adapter.incomming.cbs.alertid.AlertId;
import com.silenteight.scb.ingest.adapter.incomming.cbs.alertrecord.DatabaseAlertRecordCompositeReaderIT.TestConfiguration;
import com.silenteight.scb.ingest.adapter.incomming.cbs.batch.ScbBridgeConfigProperties;
import com.silenteight.scb.ingest.adapter.incomming.cbs.domain.CbsHitDetails;
import com.silenteight.scb.ingest.adapter.incomming.cbs.domain.NeoFlag;
import com.silenteight.scb.ingest.adapter.incomming.cbs.metrics.CbsOracleMetrics;
import com.silenteight.scb.ingest.adapter.incomming.common.SyncTestInitializer;
import com.silenteight.scb.ingest.adapter.incomming.common.batch.DateConverter;
import com.silenteight.scb.ingest.adapter.incomming.common.config.SyncDataSourcesConfiguration;
import com.silenteight.sep.base.testing.BaseJdbcTest;

import io.micrometer.core.instrument.simple.SimpleMeterRegistry;
import org.assertj.core.api.AbstractListAssert;
import org.assertj.core.api.ObjectAssert;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.Sql.ExecutionPhase;
import org.springframework.test.context.jdbc.SqlConfig;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import javax.sql.DataSource;

import static com.silenteight.scb.ingest.adapter.incomming.cbs.alertrecord.InvalidAlert.Reason.ABSENT;
import static com.silenteight.scb.ingest.adapter.incomming.cbs.alertrecord.InvalidAlert.Reason.WRONG_BATCH_ID;
import static java.util.Arrays.asList;
import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;
import static org.springframework.transaction.annotation.Propagation.NEVER;

//TODO (iwnek) We should create integration tests for each reader separately as we did for
// DatabaseCbsHitDetailsReader
@ContextConfiguration(initializers = SyncTestInitializer.class, classes = TestConfiguration.class)
@Sql(scripts = "testReader.sql")
@Sql(scripts = "clearReaderData.sql", executionPhase = ExecutionPhase.AFTER_TEST_METHOD)
@SqlConfig(dataSource = "externalDataSource", transactionManager = "externalTransactionManager")
@Transactional(propagation = NEVER, transactionManager = "externalTransactionManager")
@Disabled
class DatabaseAlertRecordCompositeReaderIT extends BaseJdbcTest {

  @Autowired
  @Qualifier("externalDataSource")
  private DataSource externalDataSource;
  @MockBean
  private DatabaseCbsHitDetailsReader cbsHitDetailsReader;
  @MockBean
  private GnsSolutionMapper gnsSolutionMapper;

  private DatabaseAlertRecordCompositeReader classUnderTest;

  private CbsOracleMetrics cbsOracleMetrics;

  @BeforeEach
  void setUp() {
    cbsOracleMetrics = new CbsOracleMetrics();
    cbsOracleMetrics.bindTo(new SimpleMeterRegistry());

    JdbcTemplate jdbcTemplate = new JdbcTemplate(externalDataSource);
    jdbcTemplate.setQueryTimeout(1000);
    DecisionRecordRowMapper decisionMapper = new DecisionRecordRowMapper(
        new DateConverter("Asia/Hong_Kong"), gnsSolutionMapper);

    classUnderTest = new DatabaseAlertRecordCompositeReader(
        new DatabaseAlertRecordReader(jdbcTemplate),
        new DatabaseDecisionRecordReader(jdbcTemplate, decisionMapper),
        cbsHitDetailsReader,
        cbsOracleMetrics);
  }

  @Test
  void shouldReadCompositeCollectionWithCbsHitDetailsCorrectly() {
    // given
    CbsHitDetails hitDetails1 = createCbsHitDetails("system-id-1", "batch-id-1");
    CbsHitDetails hitDetails2 = createCbsHitDetails("system-id-3", "batch-id-3");
    AlertId alertId1 = AlertId.builder().batchId("batch-id-1").systemId("system-id-1").build();
    AlertId alertId2 = AlertId.builder().batchId("batch-id-2").systemId("system-id-2").build();
    AlertId alertId3 = AlertId.builder().batchId("batch-id-3").systemId("system-id-3").build();
    ScbAlertIdContext context = createScbAlertIdContext("fff_records", "cbs_hit_details");
    doReturn(List.of(hitDetails1, hitDetails2))
        .when(cbsHitDetailsReader)
        .read("cbs_hit_details", List.of(alertId1, alertId2, alertId3));

    List<AlertId> alertIds = asList(
        createAlertId("system-id-1", "batch-id-1"),
        createAlertId("system-id-2", "batch-id-7"),
        createAlertId("system-id-3", "batch-id-3"),
        createAlertId("system-id-4", "batch-id-4"),
        createAlertId("system-id-5", "batch-id-5"));

    // when
    AlertRecordCompositeCollection compositeCollection =
        classUnderTest.read(context, alertIds);

    // then
    assertThat(compositeCollection.getInvalidSystemIdsWithReason(ABSENT))
        .containsExactlyInAnyOrder("system-id-4", "system-id-5");
    assertThat(compositeCollection.getInvalidSystemIdsWithReason(WRONG_BATCH_ID))
        .containsExactlyInAnyOrder("system-id-2");
    assertThat(compositeCollection.getAlerts())
        .hasSize(2)
        .satisfies(a -> assertHitDetails(a.get(0)).containsExactly(hitDetails1))
        .satisfies(a -> assertHitDetails(a.get(1)).containsExactly(hitDetails2));
  }

  private static ScbAlertIdContext createScbAlertIdContext(
      String sourceView, String hitDetailsView) {
    return ScbAlertIdContext.newBuilder()
        .setSourceView(sourceView)
        .setHitDetailsView(hitDetailsView)
        .build();
  }

  private static AlertId createAlertId(String systemId, String batchId) {
    return AlertId.builder()
        .systemId(systemId)
        .batchId(batchId)
        .build();
  }

  private static CbsHitDetails createCbsHitDetails(String systemId, String batchId) {
    return CbsHitDetails.builder()
        .systemId(systemId)
        .batchId(batchId)
        .seqNo(1)
        .hitNeoFlag(NeoFlag.NEW)
        .build();
  }

  private static AbstractListAssert<?, List<?>, Object, ObjectAssert<Object>> assertHitDetails(
      AlertRecordComposite alert) {

    return assertThat(alert)
        .extracting(AlertRecordComposite::getCbsHitDetails)
        .asList();
  }

  @Test
  void shouldReadCompositeCollectionCorrectly() {
    // given
    List<AlertId> alertIds = asList(
        createAlertId("system-id-1", "batch-id-1"),
        createAlertId("system-id-2", "batch-id-7"),
        createAlertId("system-id-3", "batch-id-3"),
        createAlertId("system-id-4", "batch-id-4"),
        createAlertId("system-id-5", "batch-id-5"));
    ScbAlertIdContext context = createScbAlertIdContext("fff_records", "");

    // when
    AlertRecordCompositeCollection compositeCollection =
        classUnderTest.read(context, alertIds);

    // then
    assertThat(compositeCollection.getInvalidSystemIdsWithReason(ABSENT))
        .containsExactlyInAnyOrder("system-id-4", "system-id-5");
    assertThat(compositeCollection.getInvalidSystemIdsWithReason(WRONG_BATCH_ID))
        .containsExactlyInAnyOrder("system-id-2");
    assertThat(compositeCollection.getAlerts()).hasSize(2);
  }

  @Configuration
  @Import({ ScbBridgeConfigProperties.class, SyncDataSourcesConfiguration.class })
  @RequiredArgsConstructor
  static class TestConfiguration {

  }
}
