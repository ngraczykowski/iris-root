package com.silenteight.warehouse.common.testing.elasticsearch;

import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;

import org.elasticsearch.ElasticsearchException;
import org.elasticsearch.action.admin.indices.alias.Alias;
import org.elasticsearch.action.admin.indices.delete.DeleteIndexRequest;
import org.elasticsearch.action.admin.indices.template.delete.DeleteIndexTemplateRequest;
import org.elasticsearch.action.get.GetRequest;
import org.elasticsearch.action.get.GetResponse;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.support.WriteRequest.RefreshPolicy;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.client.core.CountRequest;
import org.elasticsearch.client.indices.PutIndexTemplateRequest;

import java.util.Map;

import static java.util.List.of;
import static org.elasticsearch.client.RequestOptions.DEFAULT;

@RequiredArgsConstructor
public class SimpleElasticTestClient {

  private static final String INDEX_TEMPLATE = "itest-template";
  private final RestHighLevelClient restHighLevelClient;

  @SneakyThrows
  public Map<String, Object> getSource(String index, String documentId) {
    GetRequest getRequest = new GetRequest(index, documentId);
    GetResponse getResponse = restHighLevelClient.get(getRequest, DEFAULT);
    return getResponse.getSource();
  }

  @SneakyThrows
  public void storeData(String index, String id, Map<String, Object> attributes) {
    IndexRequest indexRequest = new IndexRequest(index);
    indexRequest.id(id);
    indexRequest.source(attributes);

    indexRequest.setRefreshPolicy(RefreshPolicy.WAIT_UNTIL);
    restHighLevelClient.index(indexRequest, DEFAULT);
  }

  @SneakyThrows
  public void removeIndex(String index) {
    DeleteIndexRequest deleteIndexRequest = new DeleteIndexRequest(index);

    restHighLevelClient.indices().delete(deleteIndexRequest, DEFAULT);
  }

  @SneakyThrows
  public long getDocumentCount(String index) {
    CountRequest countRequest = new CountRequest(index);

    return restHighLevelClient.count(countRequest, DEFAULT)
        .getCount();
  }

  @SneakyThrows
  public void createIndexTemplate(String indexPattern, String aliasName) {
    // FIXME: there should be no index present
    try {
      removeIndex(aliasName);
    } catch (ElasticsearchException e) {
      // do nothing, proceed
    }

    PutIndexTemplateRequest request = new PutIndexTemplateRequest(INDEX_TEMPLATE)
        .patterns(of(indexPattern))
        .alias(new Alias(aliasName));

    restHighLevelClient.indices().putTemplate(request, DEFAULT);
  }

  @SneakyThrows
  public void removeIndexTemplate() {
    DeleteIndexTemplateRequest request = new DeleteIndexTemplateRequest(INDEX_TEMPLATE);

    restHighLevelClient.indices().deleteTemplate(request, DEFAULT);
  }
}
