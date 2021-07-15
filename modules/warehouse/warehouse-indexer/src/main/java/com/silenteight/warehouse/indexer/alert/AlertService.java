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

import static org.elasticsearch.action.support.WriteRequest.RefreshPolicy.WAIT_UNTIL;

@Slf4j
@AllArgsConstructor
public class AlertService {

  @NonNull
  private final RestHighLevelClient restHighLevelClient;

  @NonNull
  private final AlertMapper alertMapper;

  public void indexAlerts(List<Alert> alerts, String indexName) {
    BulkRequest bulkRequest = new BulkRequest();

    alerts.stream()
        .map(alert -> convertAlertToDocument(indexName, alert))
        .forEach(bulkRequest::add);

    attemptToSaveAlert(bulkRequest);
  }

  private UpdateRequest convertAlertToDocument(String indexName, Alert alert) {
    UpdateRequest updateRequest = new UpdateRequest(indexName, alert.getDiscriminator());
    updateRequest.doc(alertMapper.convertAlertToAttributes(alert));
    updateRequest.docAsUpsert(true);

    return updateRequest;
  }

  private void attemptToSaveAlert(BulkRequest indexRequest) {
    try {
      doAttemptToSaveAlert(indexRequest);
    } catch (IOException e) {
      throw new AlertNotIndexedExceptions("Alert has not been indexed", e);
    }
  }

  private void doAttemptToSaveAlert(BulkRequest indexRequest) throws IOException {
    indexRequest.setRefreshPolicy(WAIT_UNTIL);
    BulkResponse bulk = restHighLevelClient.bulk(indexRequest, RequestOptions.DEFAULT);
    if (bulk.hasFailures())
      log.error(
          "There were some errors while attempting to index data: {}",
          bulk.buildFailureMessage());
  }
}
