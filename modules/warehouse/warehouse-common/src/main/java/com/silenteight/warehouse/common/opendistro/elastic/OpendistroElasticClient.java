package com.silenteight.warehouse.common.opendistro.elastic;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import net.minidev.json.JSONObject;
import org.apache.http.RequestLine;
import org.apache.http.StatusLine;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.entity.StringEntity;
import org.elasticsearch.client.*;

import java.io.IOException;
import java.util.Map;
import java.util.Set;

import static java.nio.charset.StandardCharsets.UTF_8;
import static java.util.Map.of;
import static org.apache.http.client.methods.HttpGet.METHOD_NAME;
import static org.elasticsearch.client.RequestOptions.DEFAULT;
import static org.springframework.web.util.UriComponentsBuilder.fromUriString;

@Slf4j
@RequiredArgsConstructor
public class OpendistroElasticClient {

  static final String HEADER_SECURITY_TENANT = "securitytenant";
  private final RestClient restLowLevelClient;
  private final ObjectMapper objectMapper;
  private static final String ROLE_PARAM = "role";
  private static final String ROLEMAPPING_PARAM = "rolemapping";
  private static final String LIST_REPORTS_INSTANCES_ENDPOINT = "/_opendistro/_reports/instances";
  private static final String TENANT_ENDPOINT = "/_opendistro/_security/api/tenants/";
  private static final String SQL_ENDPOINT = "/_opendistro/_sql";
  private static final String ROLES_ENDPOINT =
      "/_opendistro/_security/api/roles/{" + ROLE_PARAM + "}";
  private static final String ROLES_MAPPING_ENDPOINT =
      "/_opendistro/_security/api/rolesmapping/{" + ROLEMAPPING_PARAM + "}";
  private static final String TENANT_DESCRIPTION = "description";
  private static final String CONTENT_TYPE = "Content-type";
  private static final String APPLICATION_JSON = "application/json";

  public Set<String> getTenantsList() {
    Request request = new Request(METHOD_NAME, TENANT_ENDPOINT);

    TypeReference<Map<String, Object>> typeRef = new TypeReference<>() {};

    try {
      return doGetTenantsList(request, typeRef);
    } catch (IOException e) {
      throw handle("getTenantsList", e);
    }
  }

  private Set<String> doGetTenantsList(
      Request request, TypeReference<Map<String, Object>> typeRef) throws IOException {

    Response response = restLowLevelClient.performRequest(request);
    Map<String, Object> tenantInstanceMap = objectMapper.readValue(
        response.getEntity().getContent(), typeRef);

    Set<String> tenantsNames = tenantInstanceMap.keySet();

    log.debug("OpendistroElasticClient method=GET, endpoint={}, statusCode={}, parsedBody={}",
        request.getEndpoint(), response.getStatusLine(), tenantsNames);

    return tenantsNames;
  }

  public void createTenant(String name, String description) {
    try {
      doCreateTenant(name, description);
    } catch (IOException e) {
      throw handle("createTenant", e);
    }
  }

  private void doCreateTenant(String name, String description) throws IOException {
    Request request = createRequest(name, description);
    Response response = restLowLevelClient.performRequest(request);

    log.debug("OpendistroElasticClient method=GET, endpoint={}, statusCode={}",
        request.getEndpoint(), response.getStatusLine());
  }

  public ListReportsInstancesResponse getReportInstances(
      ListReportsInstancesRequest listReportsInstancesRequest) {

    Request request = new Request(METHOD_NAME, LIST_REPORTS_INSTANCES_ENDPOINT);
    String securityTenant = listReportsInstancesRequest.getTenant();
    request.setOptions(getRequestOptions(securityTenant));

    try {
      return getListReportsInstancesResponse(request);
    } catch (IOException e) {
      throw handle("getReportInstances", e);
    }
  }

  private ListReportsInstancesResponse getListReportsInstancesResponse(Request request)
      throws IOException {

    Response response = restLowLevelClient.performRequest(request);

    ListReportsInstancesResponse listReportsInstancesResponse =
        objectMapper.readValue(
            response.getEntity().getContent(),
            ListReportsInstancesResponse.class);
    log.debug("OpendistroElasticClient method=GET, endpoint={}, statusCode={}, parsedBody={}",
        request.getEndpoint(), response.getStatusLine(), listReportsInstancesResponse);
    return listReportsInstancesResponse;
  }


  public RoleDto getCurrentRole(String roleId) {
    try {
      return doGetCurrentRole(roleId);
    } catch (IOException e) {
      throw handle("getCurrentRole", e);
    }
  }

  private RoleDto doGetCurrentRole(String roleId) throws IOException {
    TypeReference<Map<String, RoleDto>> typeRef = new TypeReference<>() {};

    String path = fromUriString(ROLES_ENDPOINT)
        .buildAndExpand(of(ROLE_PARAM, roleId))
        .toUriString();

    Request request = new Request(METHOD_NAME, path);
    Response response = restLowLevelClient.performRequest(request);

    Map<String, RoleDto> role = objectMapper.readValue(
        response.getEntity().getContent(), typeRef);

    log.debug("OpendistroElasticClient method=GET, endpoint={}, statusCode={}, parsedBody={}",
        request.getEndpoint(), response.getStatusLine(), role);

    return role.get(roleId);
  }

  public void setRole(String roleId, RoleDto roleDto) {
    try {
      doSetRole(roleId, roleDto);
    } catch (IOException e) {
      throw handle("setRole", e);
    }
  }

  private void doSetRole(String roleId, RoleDto roleDto) throws IOException {
    String path = fromUriString(ROLES_ENDPOINT)
        .buildAndExpand(of(ROLE_PARAM, roleId))
        .toUriString();

    Request request = new Request(HttpPut.METHOD_NAME, path);
    request.setJsonEntity(objectMapper.writeValueAsString(roleDto));
    Response response = restLowLevelClient.performRequest(request);

    log.debug("OpendistroElasticClient method=PUT, endpoint={}, statusCode={}, parsedBody={}",
        request.getEndpoint(), response.getStatusLine(), response.getEntity().getContent());
  }

  public RoleMappingDto getRoleMapping(String roleId) {
    try {
      return doGetRoleMapping(roleId);
    } catch (IOException e) {
      throw handle("getRoleMapping", e);
    }
  }

  private RoleMappingDto doGetRoleMapping(String roleId) throws IOException {
    TypeReference<Map<String, RoleMappingDto>> typeRef = new TypeReference<>() {};

    String path = fromUriString(ROLES_MAPPING_ENDPOINT)
        .buildAndExpand(of(ROLEMAPPING_PARAM, roleId))
        .toUriString();

    Request request = new Request(METHOD_NAME, path);
    Response response = restLowLevelClient.performRequest(request);

    Map<String, RoleMappingDto> role = objectMapper.readValue(
        response.getEntity().getContent(), typeRef);

    log.debug("OpendistroElasticClient method=GET, endpoint={}, statusCode={}, parsedBody={}",
        request.getEndpoint(), response.getStatusLine(), role);

    return role.get(roleId);
  }

  public void setRoleMapping(String roleId, RoleMappingDto roleDto) {
    try {
      doSetRoleMapping(roleId, roleDto);
    } catch (IOException e) {
      throw handle("setRoleMapping", e);
    }
  }

  private void doSetRoleMapping(String roleId, RoleMappingDto roleDto) throws IOException {
    String path = fromUriString(ROLES_MAPPING_ENDPOINT)
        .buildAndExpand(of(ROLEMAPPING_PARAM, roleId))
        .toUriString();

    Request request = new Request(HttpPut.METHOD_NAME, path);
    request.setJsonEntity(objectMapper.writeValueAsString(roleDto));
    Response response = restLowLevelClient.performRequest(request);

    log.debug("OpendistroElasticClient method=PUT, endpoint={}, statusCode={}, parsedBody={}",
        request.getEndpoint(), response.getStatusLine(), response.getEntity().getContent());
  }

  public QueryResultDto executeSql(QueryDto queryDto) {
    try {
      return doExecuteSql(queryDto);
    } catch (IOException e) {
      throw handle("executeSql", e);
    }
  }

  private QueryResultDto doExecuteSql(QueryDto queryDto) throws IOException {
    String path = fromUriString(SQL_ENDPOINT).toUriString();

    Request request = new Request(HttpPost.METHOD_NAME, path);
    request.setJsonEntity(objectMapper.writeValueAsString(queryDto));
    Response response = restLowLevelClient.performRequest(request);

    QueryResultDto queryResultDto =
        objectMapper.readValue(response.getEntity().getContent(), QueryResultDto.class);

    log.debug("OpendistroElasticClient method=POST, endpoint={}, statusCode={}, parsedBody={}",
        request.getEndpoint(), response.getStatusLine(), queryResultDto);

    return queryResultDto;
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

  RequestOptions.Builder getRequestOptions(String tenant) {
    return DEFAULT.toBuilder().addHeader(HEADER_SECURITY_TENANT, tenant);
  }

  OpendistroElasticClientException handle(String context, IOException e) {
    if (e instanceof ResponseException) {
      ResponseException responseException = (ResponseException) e;
      StatusLine statusLine = responseException.getResponse().getStatusLine();
      RequestLine requestLine = responseException.getResponse().getRequestLine();

      return new OpendistroElasticClientException(
          statusLine.getStatusCode(),
          statusLine.getReasonPhrase(),
          requestLine.getUri(),
          context, e);
    }

    return new OpendistroElasticClientException(500, e.getMessage(), null, context, e);
  }
}
