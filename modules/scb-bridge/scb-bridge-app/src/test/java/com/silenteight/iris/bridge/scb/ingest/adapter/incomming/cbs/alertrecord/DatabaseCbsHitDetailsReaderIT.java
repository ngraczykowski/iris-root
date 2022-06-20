/*
 * Copyright (c) 2022 Silent Eight Pte. Ltd. All rights reserved.
 */

package com.silenteight.iris.bridge.scb.ingest.adapter.incomming.cbs.alertrecord;

import lombok.RequiredArgsConstructor;

import com.silenteight.iris.bridge.scb.ingest.adapter.incomming.cbs.alertid.AlertId;
import com.silenteight.iris.bridge.scb.ingest.adapter.incomming.cbs.alertrecord.DatabaseCbsHitDetailsReaderIT.TestConfiguration;
import com.silenteight.iris.bridge.scb.ingest.adapter.incomming.cbs.batch.ScbBridgeConfigProperties;
import com.silenteight.iris.bridge.scb.ingest.adapter.incomming.cbs.domain.CbsHitDetails;
import com.silenteight.iris.bridge.scb.ingest.adapter.incomming.cbs.domain.NeoFlag;
import com.silenteight.iris.bridge.scb.ingest.adapter.incomming.common.SyncTestInitializer;
import com.silenteight.iris.bridge.scb.ingest.adapter.incomming.common.config.SyncDataSourcesConfiguration;
import com.silenteight.sep.base.testing.BaseJdbcTest;

import org.assertj.core.api.ListAssert;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.dao.NonTransientDataAccessException;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.Sql.ExecutionPhase;
import org.springframework.test.context.jdbc.SqlConfig;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import javax.validation.ConstraintViolationException;

import static java.util.List.of;
import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.springframework.transaction.annotation.Propagation.NEVER;

@ContextConfiguration(initializers = SyncTestInitializer.class, classes = TestConfiguration.class)
@Sql(scripts = "cbsHitDetailsReaderBefore.sql")
@Sql(scripts = "cbsHitDetailsReaderAfter.sql", executionPhase = ExecutionPhase.AFTER_TEST_METHOD)
@SqlConfig(dataSource = "externalDataSource", transactionManager = "externalTransactionManager")
@Transactional(propagation = NEVER, transactionManager = "externalTransactionManager")
class DatabaseCbsHitDetailsReaderIT extends BaseJdbcTest {

  private final Fixtures fixtures = new Fixtures();

  private DatabaseCbsHitDetailsReader reader;

  @BeforeEach
  void setUp() {
    jdbcTemplate.setQueryTimeout(Integer.MAX_VALUE);
    reader = new DatabaseCbsHitDetailsReader(jdbcTemplate);
  }

  @Test
  void shouldThrowNonTransientDataAccessException_whenNonExistingView() {
    var alertIds = of(fixtures.alertId);
    assertThrows(
        NonTransientDataAccessException.class,
        () -> reader.read(fixtures.notExistingView, alertIds));
  }

  @Test
  @Disabled("It throws different exception than in SERP - BadSqlGrammarException")
  void shouldThrowConstraintViolationException_whenPassViewWithSqlInjection() {
    var alertIds = of(fixtures.alertId);
    assertThrows(
        ConstraintViolationException.class,
        () -> reader.read(fixtures.viewWithSqlInjection, alertIds));
  }

  @Test
  void shouldReturnEmptyListIfNoRecords() {
    List<CbsHitDetails> result = read(fixtures.existingView, of());

    assertThat(result).isEmpty();
  }

  private List<CbsHitDetails> read(String view, List<AlertId> alertIds) {
    return reader.read(view, alertIds);
  }

  @Test
  void shouldReadSingleEntryCorrectly() {
    assertSingleEntryFoundCorrectly(fixtures.withoutNeoFlag);
    assertSingleEntryFoundCorrectly(fixtures.newNeoFlag);
    assertSingleEntryFoundCorrectly(fixtures.existingNeoFlag);
    assertSingleEntryFoundCorrectly(fixtures.obsoleteNeoFlag);
  }

  private void assertSingleEntryFoundCorrectly(CbsHitDetails expected) {
    var alertId = new AlertId(expected.getSystemId(), expected.getBatchId());
    assertFoundEntries(fixtures.existingView, List.of(alertId))
        .hasSize(1)
        .first()
        .isEqualTo(expected);
  }

  private ListAssert<CbsHitDetails> assertFoundEntries(
      String existingView, List<AlertId> alertIds) {

    return assertThat(read(existingView, alertIds));
  }

  @Test
  void shouldReadEntriesWithDifferentBatchIdsCorrectly() {
    assertSingleEntryFoundCorrectly(fixtures.withBatchId1);
    assertSingleEntryFoundCorrectly(fixtures.withBatchId2);
  }

  @Test
  void shouldReadMultipleEntriesForSingleSystemIdAndBatchId() {
    assertFoundEntries(fixtures.existingView, List.of(fixtures.multiHitAlertId))
        .containsExactly(fixtures.hit1, fixtures.hit2, fixtures.hit3);
  }

  private static class Fixtures {

    AlertId alertId = new AlertId("systemId", "batchId");
    String existingView = "gns.cbs_hit_details_test_view";
    String notExistingView = "gns.not_existing_view";
    String viewWithSqlInjection = "gns.not_existing_view WHERE 1=1 OR ";

    CbsHitDetails withoutNeoFlag = CbsHitDetails.builder()
        .systemId("system_id_0")
        .batchId("batch_id")
        .seqNo(1)
        .hitNeoFlag(null)
        .build();

    CbsHitDetails newNeoFlag = CbsHitDetails.builder()
        .systemId("system_id_1")
        .batchId("batch_id")
        .seqNo(1)
        .hitNeoFlag(NeoFlag.NEW)
        .build();

    CbsHitDetails existingNeoFlag = CbsHitDetails.builder()
        .systemId("system_id_2")
        .batchId("batch_id")
        .seqNo(1)
        .hitNeoFlag(NeoFlag.EXISTING)
        .build();

    CbsHitDetails obsoleteNeoFlag = CbsHitDetails.builder()
        .systemId("system_id_3")
        .batchId("batch_id")
        .seqNo(1)
        .hitNeoFlag(NeoFlag.OBSOLETE)
        .build();

    CbsHitDetails withBatchId1 = CbsHitDetails.builder()
        .systemId("system_id_4")
        .batchId("batch_id_1")
        .seqNo(1)
        .hitNeoFlag(NeoFlag.NEW)
        .build();

    CbsHitDetails withBatchId2 = CbsHitDetails.builder()
        .systemId("system_id_4")
        .batchId("batch_id_2")
        .seqNo(1)
        .hitNeoFlag(NeoFlag.EXISTING)
        .build();

    String multiHitSystemId = "system_id_5";
    String multiHitBatchId = "batch_id";

    CbsHitDetails hit1 = CbsHitDetails.builder()
        .systemId(multiHitSystemId)
        .batchId(multiHitBatchId)
        .seqNo(1)
        .hitNeoFlag(NeoFlag.NEW)
        .build();

    CbsHitDetails hit2 = CbsHitDetails.builder()
        .systemId(multiHitSystemId)
        .batchId(multiHitBatchId)
        .seqNo(2)
        .hitNeoFlag(NeoFlag.EXISTING)
        .build();

    CbsHitDetails hit3 = CbsHitDetails.builder()
        .systemId(multiHitSystemId)
        .batchId(multiHitBatchId)
        .seqNo(3)
        .hitNeoFlag(NeoFlag.OBSOLETE)
        .build();

    AlertId multiHitAlertId = new AlertId(multiHitSystemId, multiHitBatchId);
  }

  @Configuration
  @Import({ ScbBridgeConfigProperties.class, SyncDataSourcesConfiguration.class })
  @RequiredArgsConstructor
  static class TestConfiguration {

  }
}
