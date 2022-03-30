package com.silenteight.scb.ingest.adapter.incomming.cbs.alertunderprocessing;

import com.silenteight.proto.serp.scb.v1.ScbAlertIdContext;
import com.silenteight.scb.ingest.adapter.incomming.cbs.alertid.AlertId;
import com.silenteight.scb.ingest.adapter.incomming.cbs.alertunderprocessing.AlertUnderProcessing.State;
import com.silenteight.sep.base.testing.transaction.RunWithoutTransactionManager;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Collection;
import java.util.List;

import static java.util.Arrays.asList;
import static java.util.Collections.singletonList;
import static org.assertj.core.api.Assertions.*;

@RunWithoutTransactionManager
class AlertInFlightServiceIT {

  private static ScbAlertIdContext alertIdContext = ScbAlertIdContext.newBuilder().build();
  private AlertUnderProcessingRepository repository = new InMemoryAlertUnderProcessingRepository();
  private AlertInFlightService classUnderTest;

  @BeforeEach
  void setUp() {
    classUnderTest = new AlertsUnderProcessingService(repository);
  }

  @Test
  void shouldStoreOnlyNewAlertsToBeProcessed() {
    // given
    var internalBatchId = "5a209f6d-cb00-44e6-a603-2057bc63da4c";
    AlertId alertId1 = createAlertId("system_id_1");
    AlertId alertId2 = createAlertId("system_id_2");
    AlertId alertId3 = createAlertId("system_id_3");
    List<AlertId> alertsToBeSaved = asList(alertId1, alertId3);
    List<AlertId> alertIds = asList(alertId1, alertId2);
    repository.saveAll(alertIds.stream()
        .map(e -> toEntity(e, internalBatchId))
        .toList());

    // when
    classUnderTest.saveUniqueAlerts(alertsToBeSaved, internalBatchId, alertIdContext);
    Collection<AlertUnderProcessing> alertsUnderProcessing = repository.findAll();

    // then
    assertThat(alertsUnderProcessing.size()).isEqualTo(3);
    Assertions.assertThat(alertsUnderProcessing)
        .extracting(AlertUnderProcessing::getSystemId)
        .containsExactlyInAnyOrder(
            alertId1.getSystemId(), alertId2.getSystemId(), alertId3.getSystemId());
  }

  @Test
  void shouldDeleteAlertsBySystemIdAndBatchId() {
    //given
    var internalBatchId = "5a209f6d-cb00-44e6-a603-2057bc63da4c";
    AlertId alertId1 = createAlertId("system_id_1", "batch_id_1");
    AlertId alertId2 = createAlertId("system_id_1", "batch_id_2");
    AlertId alertId3 = createAlertId("system_id_3", "batch_id_1");
    List<AlertId> alertsToBeSaved = asList(alertId1, alertId2, alertId3);
    repository.saveAll(alertsToBeSaved.stream()
        .map(e -> toEntity(e, internalBatchId))
        .toList());

    //when
    classUnderTest.delete(alertId3);

    //then
    assertThat(repository.findAll().size()).isEqualTo(2);

    //when
    classUnderTest.delete(alertId2);

    //then
    assertThat(repository.findAll().size()).isEqualTo(1);

    //when
    classUnderTest.delete(alertId1);

    //then
    assertThat(repository.findAll().isEmpty()).isTrue();
  }

  @Test
  void shouldUpdateState() {
    //given
    var internalBatchId = "5a209f6d-cb00-44e6-a603-2057bc63da4c";
    AlertId alertId = createAlertId("system_id_1", "batch_id_1");
    repository.saveAll(singletonList(toEntity(alertId, internalBatchId)));

    //when
    classUnderTest.update(alertId, State.ERROR);

    //then
    Collection<AlertUnderProcessing> results = repository.findAll();
    assertThat(results.size()).isEqualTo(1);
    for (AlertUnderProcessing alertUnderProcessing : results) {
      assertThat(alertUnderProcessing.getState()).isEqualTo(State.ERROR);
    }
  }

  @Test
  void shouldUpdateStateAndError() {
    //given
    var internalBatchId = "5a209f6d-cb00-44e6-a603-2057bc63da4c";
    AlertId alertId = createAlertId("system_id_1", "batch_id_1");
    repository.saveAll(singletonList(toEntity(alertId, internalBatchId)));
    String someTooLongError =
        "Lorem ipsum dolor sit amet, consectetur adipiscing elit. In ullamcorper tincidunt ipsum, "
            + "ullamcorper sagittis odio suscipit sit amet. Mauris hendrerit, nulla fringilla "
            + "maximus fringilla, risus ex sagittis lacus, vitae bibendum augue turpis rutrum leo. "
            + "Etiam eu vehicula erat. Integer commodo tellus quis congue pharetra. Cras commodo "
            + "purus faucibus nisi blandit imperdiet. Aliquam rutrum elementum ultricies. "
            + "Lorem ipsum dolor sit amet, consectetur adipiscing elit. Maecenas auctor ante velit,"
            + "non scelerisque leo efficitur fermentum. Integer eleifend euismod eros, ut euismod "
            + "diam pellentesque non. Duis et laoreet ligula, eget lobortis enim. Mauris "
            + "sollicitudin ultricies vestibulum. Nunc tincidunt, elit vitae posuere pellentesque, "
            + "mi tellus accumsan sapien, eget dapibus tellus risus nec neque. Donec ut est "
            + "bibendum, vehicula nibh quis, auctor elit. Mauris ut neque tincidunt, posuere orci "
            + "ac, elementum nisl. Nam molestie, leo a ultricies mollis, diam nulla facilisis urna,"
            + " ut tristique nisl amet.";

    //when
    classUnderTest.update(alertId, State.ERROR, someTooLongError);

    //then
    Collection<AlertUnderProcessing> results = repository.findAll();
    assertThat(results.size()).isEqualTo(1);
    for (AlertUnderProcessing alertUnderProcessing : results) {
      assertThat(alertUnderProcessing.getState()).isEqualTo(State.ERROR);
      assertThat(alertUnderProcessing.getError().length()).isEqualTo(1000);
    }
  }

  private static AlertUnderProcessing toEntity(AlertId alertId, String internalBatchId) {
    return new AlertUnderProcessing(
        alertId.getSystemId(), alertId.getBatchId(), internalBatchId, alertIdContext);
  }

  private static AlertId createAlertId(String systemId) {
    return createAlertId(systemId, "some_batch_id");
  }

  private static AlertId createAlertId(String systemId, String batchId) {
    return AlertId.builder().systemId(systemId).batchId(batchId).build();
  }
}