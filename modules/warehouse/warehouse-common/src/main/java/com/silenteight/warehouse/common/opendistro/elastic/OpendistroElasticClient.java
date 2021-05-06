package com.silenteight.warehouse.common.opendistro.elastic;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import net.minidev.json.JSONObject;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.entity.StringEntity;
import org.elasticsearch.client.Request;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.Response;
import org.elasticsearch.client.RestClient;

import java.io.IOException;
import java.util.Map;
import java.util.Set;

import static java.nio.charset.StandardCharsets.UTF_8;
import static org.apache.http.client.methods.HttpGet.METHOD_NAME;
import static org.elasticsearch.client.RequestOptions.DEFAULT;

@Slf4j
@RequiredArgsConstructor
public class OpendistroElasticClient {

  static final String HEADER_SECURITY_TENANT = "securitytenant";
  private final RestClient restLowLevelClient;
  private final ObjectMapper objectMapper;
  private static final String LIST_REPORTS_INSTANCES_ENDPOINT = "/_opendistro/_reports/instances";
  private static final String TENANT_ENDPOINT = "/_opendistro/_security/api/tenants/";
  private static final String TENANT_DESCRIPTION = "description";
  private static final String CONTENT_TYPE = "Content-type";
  private static final String APPLICATION_JSON = "application/json";

  public Set<String> getTenantsList() {
    Request request = new Request(METHOD_NAME, TENANT_ENDPOINT);

    TypeReference<Map<String, Object>> typeRef = new TypeReference<>() {};

    try {
      return doGetTenantsList(request, typeRef);
    } catch (IOException e) {
      throw new OpendistroElasticClientException("Method getTenantsList failed", e);
    }
  }

  public void createTenant(String name, String description) {
    try {
      doCreateTenant(name, description);
    } catch (IOException e) {
      throw new OpendistroElasticClientException("Method createTenant failed.", e);
    }
  }

  private void doCreateTenant(String name, String description) throws IOException {
    Request request = createRequest(name, description);
    Response response = restLowLevelClient.performRequest(request);

    log.debug("OpendistroElasticClient method=GET, endpoint={}, statusCode={}",
        request.getEndpoint(), response.getStatusLine());
  }

  private Set<String> doGetTenantsList(
      Request request, TypeReference<Map<String, Object>> typeRef) throws
      IOException {
    Response response = restLowLevelClient.performRequest(request);
    Map<String, Object> tenantInstanceMap = objectMapper.readValue(
        response.getEntity().getContent(), typeRef);

    Set<String> tenantsNames = tenantInstanceMap.keySet();

    log.debug("OpendistroElasticClient method=GET, endpoint={}, statusCode={}, parsedBody={}",
        request.getEndpoint(), response.getStatusLine(), tenantsNames);

    return tenantsNames;
  }

  private Request createRequest(String name, String description) {
    Request request = new Request(HttpPut.METHOD_NAME, TENANT_ENDPOINT + name);
    request.setEntity(getEntity(description));
    request.setOptions(getRequestOptions());
    return request;
  }

  private StringEntity getEntity(String description) {
    JSONObject tenantAsJson = new JSONObject();
    tenantAsJson.put(TENANT_DESCRIPTION, description);
    return new StringEntity(tenantAsJson.toString(), UTF_8);
  }

  private RequestOptions.Builder getRequestOptions() {
    return DEFAULT.toBuilder().addHeader(CONTENT_TYPE, APPLICATION_JSON);
  }

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

  RequestOptions.Builder getRequestOptions(String tenant) {
    return DEFAULT.toBuilder().addHeader(HEADER_SECURITY_TENANT, tenant);
  }
}
