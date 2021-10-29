package com.silenteight.warehouse.indexer.alert.indexing;

import lombok.AllArgsConstructor;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;

import org.elasticsearch.action.bulk.BulkRequest;
import org.elasticsearch.action.bulk.BulkResponse;
import org.elasticsearch.action.update.UpdateRequest;
import org.elasticsearch.client.RestHighLevelClient;
import org.springframework.retry.annotation.Retryable;

import java.io.IOException;
import java.net.SocketTimeoutException;
import java.util.List;

import static org.apache.commons.collections4.ListUtils.partition;
import static org.elasticsearch.action.support.WriteRequest.RefreshPolicy.WAIT_UNTIL;
import static org.elasticsearch.client.RequestOptions.DEFAULT;

@Slf4j
@AllArgsConstructor
public class AlertIndexService {

  @NonNull
  private final RestHighLevelClient restHighLevelClient;

  private final int updateRequestBatchSize;

  private final int retryOnConflictCount;

  @Retryable(value = { SocketTimeoutException.class, AlertNotIndexedExceptions.class })
  public void saveAlerts(List<MapWithIndex> alerts) {
    partition(alerts, updateRequestBatchSize).stream()
        .map(this::asBulkRequest)
        .forEach(this::sendBulkRequest);
  }

  private BulkRequest asBulkRequest(List<MapWithIndex> alerts) {
    BulkRequest bulkRequest = new BulkRequest();

    alerts.stream()
        .map(alert -> asUpdateRequest(alert))
        .forEach(bulkRequest::add);

    return bulkRequest;
  }

  private UpdateRequest asUpdateRequest(MapWithIndex alert) {
    UpdateRequest updateRequest = new UpdateRequest(alert.getIndexName(), alert.getDocumentId());
    updateRequest.doc(alert.getPayload());
    updateRequest.docAsUpsert(true);
    updateRequest.retryOnConflict(retryOnConflictCount);

    return updateRequest;
  }

  private void sendBulkRequest(BulkRequest bulkRequest) {
    try {
      doSendBulkRequest(bulkRequest);
    } catch (IOException e) {
      throw new AlertNotIndexedExceptions("Alert has not been indexed", e);
    }
  }

  private void doSendBulkRequest(BulkRequest bulkRequest) throws IOException {
    log.debug("Attempting to send a bulk request, alertCount={}, estimatedSizeInBytes={}",
        bulkRequest.requests().size(), bulkRequest.estimatedSizeInBytes());

    bulkRequest.setRefreshPolicy(WAIT_UNTIL);
    BulkResponse bulk = restHighLevelClient.bulk(bulkRequest, DEFAULT);
    if (bulk.hasFailures())
      log.error(
          "There were some errors while indexing data: {}. "
              + "Other data in the bulk request may have been indexed successfully.",
          bulk.buildFailureMessage());
  }
}
