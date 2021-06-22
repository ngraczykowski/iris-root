package com.silenteight.warehouse.indexer.alert;

import lombok.AllArgsConstructor;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;

import com.silenteight.data.api.v1.Alert;
import com.silenteight.data.api.v1.Match;

import org.elasticsearch.action.bulk.BulkRequest;
import org.elasticsearch.action.bulk.BulkResponse;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;

import java.io.IOException;
import java.util.List;
import java.util.stream.Stream;

import static com.silenteight.warehouse.indexer.alert.NameResource.getSplitName;
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
        .flatMap(alert -> convertAlertToDocument(indexName, alert))
        .forEach(bulkRequest::add);

    attemptToSaveAlert(bulkRequest);
  }

  private Stream<IndexRequest> convertAlertToDocument(String indexName, Alert alert) {
    List<Match> matchesList = alert.getMatchesList();
    if (matchesList.isEmpty()) {
      throw new ZeroMatchesException("There are no matches in this alert.");
    }
    return matchesList.stream()
        .map(match -> prepareDocument(alert, indexName, match));
  }

  private IndexRequest prepareDocument(Alert alert, String indexName, Match match) {
    IndexRequest indexRequest = new IndexRequest(indexName);
    indexRequest.id(getSplitName(alert.getName() + ":" + getSplitName(match.getName())));
    indexRequest.source(alertMapper.convertAlertAndMatchToAttributes(alert, match));
    return indexRequest;
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
