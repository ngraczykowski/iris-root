package com.silenteight.warehouse.indexer.alert;

import lombok.AllArgsConstructor;
import lombok.NonNull;

import com.silenteight.data.api.v1.Alert;
import com.silenteight.data.api.v1.DataIndexRequest;

import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.support.WriteRequest.RefreshPolicy;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;

import java.io.IOException;

import static com.silenteight.warehouse.indexer.alert.NameResource.getId;

@AllArgsConstructor
public class AlertService {

  @NonNull
  private final RestHighLevelClient restHighLevelClient;

  @NonNull
  private final AlertMapper alertMapper;

  public void indexAlert(DataIndexRequest dataIndexRequest) {
    dataIndexRequest
        .getAlertsList()
        .stream()
        .map(alert -> prepareRequest(alert, getId(dataIndexRequest.getAnalysisName())))
        .forEach(this::attemptToSaveAlert);
  }

  private IndexRequest prepareRequest(Alert alert, String indexName) {
    IndexRequest indexRequest = new IndexRequest(indexName);
    indexRequest.id(getId(alert.getName()));
    indexRequest.source(alertMapper.convertAlertToAttributes(alert));
    indexRequest.setRefreshPolicy(RefreshPolicy.WAIT_UNTIL);
    return indexRequest;
  }

  private void attemptToSaveAlert(IndexRequest indexRequest) {
    try {
      restHighLevelClient.index(indexRequest, RequestOptions.DEFAULT);
    } catch (IOException e) {
      throw new AlertNotIndexedExceptions("Alert has not been indexed", e);
    }
  }
}
