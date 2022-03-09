package com.silenteight.scb.ingest.adapter.incomming.cbs.alertrecord;

import com.silenteight.scb.ingest.adapter.incomming.cbs.alertid.AlertId;
import com.silenteight.scb.ingest.adapter.incomming.cbs.domain.CbsHitDetails;
import com.silenteight.scb.ingest.adapter.incomming.cbs.domain.NeoFlag;
import com.silenteight.scb.ingest.adapter.incomming.common.alertrecord.AlertRecord;
import com.silenteight.scb.ingest.adapter.incomming.common.alertrecord.DecisionRecord;

import org.assertj.core.api.AbstractListAssert;
import org.assertj.core.api.ObjectAssert;
import org.junit.jupiter.api.Test;

import java.time.Instant;
import java.util.HashMap;
import java.util.List;

import static com.silenteight.scb.ingest.adapter.incomming.cbs.alertrecord.InvalidAlert.Reason.ABSENT;
import static com.silenteight.scb.ingest.adapter.incomming.cbs.alertrecord.InvalidAlert.Reason.WRONG_BATCH_ID;
import static java.util.Arrays.asList;
import static java.util.Collections.emptyList;
import static java.util.Collections.emptyMap;
import static java.util.Collections.singletonList;
import static org.assertj.core.api.Assertions.*;

class AlertRecordCompositeCollectionTest {

  @Test
  void shouldReturnFetchedAlerts() {
    // given
    List<AlertId> alertIds = asList(
        createAlertId("system-id-1", "batch-id-1"),
        createAlertId("system-id-2", "batch-id-2"),
        createAlertId("system-id-3", "batch-id-3"));

    AlertRecord alert1 = createAlertRecord("system-id-1", "batch-id-1");
    AlertRecord alert2 = createAlertRecord("system-id-2", "batch-id-2");
    AlertRecord alert3 = createAlertRecord("system-id-3", "batch-id-3");

    DecisionRecord decision1 = createDecisionRecord("system-id-1");
    DecisionRecord decision2 = createDecisionRecord("system-id-2");
    DecisionRecord decision3 = createDecisionRecord("system-id-3");

    CbsHitDetails hitDetails11 = createHitDetails(alert1, 0);
    CbsHitDetails hitDetails12 = createHitDetails(alert1, 1);
    CbsHitDetails hitDetails21 = createHitDetails(alert2, 0);

    // when
    var alertRecordComposite = new AlertRecordCompositeCollection(
        alertIds,
        asList(alert1, alert2, alert3),
        asList(decision1, decision2, decision3),
        new HashMap<>() {
          {
            put(alert1, asList(hitDetails11, hitDetails12));
            put(alert2, singletonList(hitDetails21));
          }
        });

    // then
    assertThat(alertRecordComposite.getInvalidAlerts()).isEmpty();
    assertThat(alertRecordComposite.getAlerts())
        .hasSize(3)
        .satisfies(alerts -> assertThat(alerts.get(0))
            .satisfies(a -> assertDecisions(a).containsExactly(decision1))
            .satisfies(a -> assertHitDetails(a).containsExactly(hitDetails11, hitDetails12)))
        .satisfies(alerts -> assertThat(alerts.get(1))
            .satisfies(a -> assertDecisions(a).containsExactly(decision2))
            .satisfies(a -> assertHitDetails(a).containsExactly(hitDetails21)))
        .satisfies(alerts -> assertThat(alerts.get(2))
            .satisfies(a -> assertDecisions(a).containsExactly(decision3))
            .satisfies(a -> assertHitDetails(a).isEmpty()));
  }

  @Test
  void shouldReturnFetchedAlertsAndAbsentSystemIds() {
    // given
    List<AlertId> alertIds = asList(
        createAlertId("system-id-1", "batch-id-1"),
        createAlertId("system-id-2", "batch-id-2"),
        createAlertId("system-id-3", "batch-id-3"));

    List<AlertRecord> alerts = singletonList(createAlertRecord("system-id-1", "batch-id-1"));

    // when
    var alertRecordComposite = new AlertRecordCompositeCollection(
        alertIds, alerts, emptyList(), emptyMap());

    // then
    assertThat(alertRecordComposite.getInvalidSystemIdsWithReason(ABSENT))
        .containsExactly("system-id-2", "system-id-3");
    assertThat(alertRecordComposite.getInvalidSystemIdsWithReason(WRONG_BATCH_ID)).isEmpty();
    assertThat(alertRecordComposite.getAlerts()).hasSize(1);
  }

  @Test
  void shouldReturnFetchedAlertsAndSystemIdsWithDifferentBatchId() {
    // given
    List<AlertId> alertIds = asList(
        createAlertId("system-id-1", "batch-id-1"),
        createAlertId("system-id-2", "batch-id-2"),
        createAlertId("system-id-3", "batch-id-3"));

    List<AlertRecord> alerts = asList(
        createAlertRecord("system-id-1", "batch-id-1"),
        createAlertRecord("system-id-2", "batch-id-1"),
        createAlertRecord("system-id-3", "batch-id-5"));

    // when
    var alertRecordComposite = new AlertRecordCompositeCollection(
        alertIds, alerts, emptyList(), emptyMap());

    // then
    assertThat(alertRecordComposite.getInvalidSystemIdsWithReason(ABSENT)).isEmpty();
    assertThat(alertRecordComposite.getInvalidSystemIdsWithReason(WRONG_BATCH_ID))
        .containsExactly("system-id-2", "system-id-3");
    assertThat(alertRecordComposite.getAlerts()).hasSize(1);
  }

  private static AlertId createAlertId(String systemId, String batchId) {
    return AlertId.builder()
        .systemId(systemId)
        .batchId(batchId)
        .build();
  }

  private static AlertRecord createAlertRecord(String systemId, String batchId) {
    return AlertRecord.builder()
        .systemId(systemId)
        .batchId(batchId)
        .build();
  }

  private static DecisionRecord createDecisionRecord(String systemId) {
    return DecisionRecord.builder()
        .systemId(systemId)
        .comments("some comments")
        .type(1)
        .operator("operator")
        .decisionDate(Instant.now())
        .build();
  }

  private static CbsHitDetails createHitDetails(AlertRecord record, int seqNo) {
    return CbsHitDetails.builder()
        .systemId(record.getSystemId())
        .batchId(record.getBatchId())
        .seqNo(seqNo)
        .hitNeoFlag(NeoFlag.NEW)
        .build();
  }

  private static AbstractListAssert<?, List<?>, Object, ObjectAssert<Object>> assertDecisions(
      AlertRecordComposite actual) {

    return assertThat(actual)
        .extracting(AlertRecordComposite::getDecisions)
        .asList();
  }

  private static AbstractListAssert<?, List<?>, Object, ObjectAssert<Object>> assertHitDetails(
      AlertRecordComposite alert) {

    return assertThat(alert)
        .extracting(AlertRecordComposite::getCbsHitDetails)
        .asList();
  }
}