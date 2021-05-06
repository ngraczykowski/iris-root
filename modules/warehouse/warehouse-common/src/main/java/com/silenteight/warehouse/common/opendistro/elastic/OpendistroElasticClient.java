package com.silenteight.warehouse.common.opendistro.elastic;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.elasticsearch.client.Request;
import org.elasticsearch.client.RequestOptions.Builder;
import org.elasticsearch.client.Response;
import org.elasticsearch.client.RestClient;

import java.io.IOException;

import static org.apache.http.client.methods.HttpGet.METHOD_NAME;
import static org.elasticsearch.client.RequestOptions.DEFAULT;

@Slf4j
@RequiredArgsConstructor
public class OpendistroElasticClient {

  static final String HEADER_SECURITY_TENANT = "securitytenant";

  private final RestClient restLowLevelClient;
  private final ObjectMapper objectMapper;

  private static final String LIST_REPORTS_INSTANCES_ENDPOINT = "/_opendistro/_reports/instances";

  public ListReportsInstancesResponse getReportInstance(
      ListReportsInstancesRequest listReportsInstancesRequest) {

    Request request = new Request(METHOD_NAME, LIST_REPORTS_INSTANCES_ENDPOINT);
    String securityTenant = listReportsInstancesRequest.getTenant();
    request.setOptions(getRequestOptions(securityTenant));

    try {
      Response response = restLowLevelClient.performRequest(request);

      ListReportsInstancesResponse listReportsInstancesResponse =
          objectMapper.readValue(
              response.getEntity().getContent(),
              ListReportsInstancesResponse.class);
      log.debug("OpendistroElasticClient method=GET, endpoint={}, statusCode={}, parsedBody={}",
          request.getEndpoint(), response.getStatusLine(), listReportsInstancesResponse);

      return listReportsInstancesResponse;
    } catch (IOException e) {
      throw new OpendistroElasticClientException("Method getReportInstance failed, request="
          + listReportsInstancesRequest, e);
    }
  }

  Builder getRequestOptions(String tenant) {
    return DEFAULT.toBuilder().addHeader(HEADER_SECURITY_TENANT, tenant);
  }
}
