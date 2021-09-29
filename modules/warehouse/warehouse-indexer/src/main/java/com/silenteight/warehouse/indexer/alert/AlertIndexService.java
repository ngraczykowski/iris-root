package com.silenteight.warehouse.indexer.alert;

import lombok.AllArgsConstructor;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;

import com.silenteight.data.api.v1.Alert;

import org.elasticsearch.action.bulk.BulkRequest;
import org.elasticsearch.action.bulk.BulkResponse;
import org.elasticsearch.action.update.UpdateRequest;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;

import java.io.IOException;
import java.util.List;

import static org.apache.commons.collections4.ListUtils.partition;
import static org.elasticsearch.action.support.WriteRequest.RefreshPolicy.WAIT_UNTIL;

@Slf4j
@AllArgsConstructor
public class AlertIndexService {

  @NonNull
  private final RestHighLevelClient restHighLevelClient;

  @NonNull
  private final AlertMapper alertMapper;

  private final int updateRequestBatchSize;

  public void indexAlerts(List<Alert> alerts, String indexName) {
    partition(alerts, updateRequestBatchSize)
        .forEach(alertsBatch -> indexAlertsBatch(alertsBatch, indexName));
  }

  void indexAlertsBatch(List<Alert> alerts, String indexName) {
    BulkRequest bulkRequest = new BulkRequest();

    alerts.stream()
        .map(alert -> convertAlertToDocument(indexName, alert))
        .forEach(bulkRequest::add);

    trySaveAlert(bulkRequest);
  }

  private UpdateRequest convertAlertToDocument(String indexName, Alert alert) {
    UpdateRequest updateRequest = new UpdateRequest(indexName, alert.getDiscriminator());
    updateRequest.doc(alertMapper.convertAlertToAttributes(alert));
    updateRequest.docAsUpsert(true);

    return updateRequest;
  }

  private void trySaveAlert(BulkRequest indexRequest) {
    try {
      saveAlert(indexRequest);
    } catch (IOException e) {
      throw new AlertNotIndexedExceptions("Alert has not been indexed", e);
    }
  }

  private void saveAlert(BulkRequest indexRequest) throws IOException {
    indexRequest.setRefreshPolicy(WAIT_UNTIL);
    BulkResponse bulk = restHighLevelClient.bulk(indexRequest, RequestOptions.DEFAULT);
    if (bulk.hasFailures())
      log.error(
          "There were some errors while attempting to index data: {}",
          bulk.buildFailureMessage());
  }
}
