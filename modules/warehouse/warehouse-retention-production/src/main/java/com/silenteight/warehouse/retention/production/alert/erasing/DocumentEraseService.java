package com.silenteight.warehouse.retention.production.alert.erasing;

import lombok.AllArgsConstructor;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;

import org.elasticsearch.action.bulk.BulkRequest;
import org.elasticsearch.action.bulk.BulkResponse;
import org.elasticsearch.action.delete.DeleteRequest;
import org.elasticsearch.client.RestHighLevelClient;

import java.io.IOException;
import java.util.List;

import static org.apache.commons.collections4.ListUtils.partition;
import static org.elasticsearch.action.support.WriteRequest.RefreshPolicy.WAIT_UNTIL;
import static org.elasticsearch.client.RequestOptions.DEFAULT;

@Slf4j
@AllArgsConstructor
public class DocumentEraseService {

  @NonNull
  private final RestHighLevelClient restHighLevelClient;

  private final int eraseRequestBatchSize;

  public void eraseDocuments(List<DocumentDto> alerts) {
    partition(alerts, eraseRequestBatchSize).stream()
        .map(DocumentEraseService::asBulkRequest)
        .forEach(this::sendBulkRequest);
  }

  private static BulkRequest asBulkRequest(List<DocumentDto> alerts) {
    BulkRequest bulkRequest = new BulkRequest();

    alerts.stream()
        .map(DocumentEraseService::asDeleteRequest)
        .forEach(bulkRequest::add);

    return bulkRequest;
  }

  private static DeleteRequest asDeleteRequest(DocumentDto alert) {
    return new DeleteRequest(alert.getIndexName(), alert.getDocumentId());
  }

  private void sendBulkRequest(BulkRequest bulkRequest) {
    try {
      doSendBulkRequest(bulkRequest);
    } catch (IOException e) {
      throw new DocumentNotErasedException("Alert has not been erased", e);
    }
  }

  private void doSendBulkRequest(BulkRequest bulkRequest) throws IOException {
    log.debug("Attempting to send a bulk request, alertCount={}, estimatedSizeInBytes={}",
        bulkRequest.requests().size(), bulkRequest.estimatedSizeInBytes());

    bulkRequest.setRefreshPolicy(WAIT_UNTIL);
    BulkResponse bulk = restHighLevelClient.bulk(bulkRequest, DEFAULT);
    if (bulk.hasFailures())
      log.error(
          "There were some errors while erasing data: {}. "
              + "Other data in the bulk request may have been erased successfully.",
          bulk.buildFailureMessage());
  }
}
