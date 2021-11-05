package com.silenteight.warehouse.common.opendistro.elastic;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import com.silenteight.warehouse.common.opendistro.elastic.exception.OpendistroElasticClientException;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import net.minidev.json.JSONObject;
import org.apache.http.RequestLine;
import org.apache.http.StatusLine;
import org.apache.http.client.methods.HttpDelete;
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

  private static final String ACTUAL_SIZE_LIMIT = "\"size\":1000,";
  private static final String DESIRED_SIZE_LIMIT = "\"size\":65000,";
  private static final String ROLE_PARAM = "role";
  private static final String ROLEMAPPING_PARAM = "rolemapping";
  private static final String TENANT_ENDPOINT = "/_opendistro/_security/api/tenants/";
  private static final String SQL_ENDPOINT = "/_opendistro/_sql";
  private static final String SQL_EXPLAIN_ENDPOINT = "/_opendistro/_sql/_explain";
  private static final String SEARCH_ENDPOINT = "_search";
  private static final String ROLES_ENDPOINT =
      "/_opendistro/_security/api/roles/{" + ROLE_PARAM + "}";

  private static final String ROLES_MAPPING_ENDPOINT =
      "/_opendistro/_security/api/rolesmapping/{" + ROLEMAPPING_PARAM + "}";

  private static final String TENANT_DESCRIPTION = "description";
  private static final String CONTENT_TYPE = "Content-type";
  private static final String APPLICATION_JSON = "application/json";

  private final RestClient restLowLevelClient;
  private final ObjectMapper objectMapper;

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

  public void removeRole(String id) {
    try {
      doRemoveRole(id);
    } catch (IOException e) {
      throw handle("removeRole", e);
    }
  }

  private void doRemoveRole(String id) throws IOException {
    String path = fromUriString(ROLES_ENDPOINT)
        .buildAndExpand(of(ROLE_PARAM, id))
        .toUriString();

    Request request = new Request(HttpDelete.METHOD_NAME, path);
    Response response = restLowLevelClient.performRequest(request);

    log.debug(
        "OpendistroElasticClient method=DELETE, endpoint={}, statusCode={}", request.getEndpoint(),
        response.getStatusLine());
  }

  public void removeRoleMapping(String id) {
    try {
      doRemoveMapping(id);
    } catch (IOException e) {
      throw handle("removeRoleMapping", e);
    }
  }

  private void doRemoveMapping(String id) throws IOException {
    String path = fromUriString(ROLES_MAPPING_ENDPOINT)
        .buildAndExpand(of(ROLEMAPPING_PARAM, id))
        .toUriString();

    Request request = new Request(HttpDelete.METHOD_NAME, path);
    Response response = restLowLevelClient.performRequest(request);

    log.debug(
        "OpendistroElasticClient method=DELETE, endpoint={}, statusCode={}", request.getEndpoint(),
        response.getStatusLine());
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

  public SearchResultDto executeGroupingSearch(QueryDto queryDto, String index) {
    try {
      return doExecuteGroupingSearch(queryDto, index);
    } catch (IOException e) {
      throw handle("executeGroupingSearch", e);
    }
  }

  // Przemek L.: cause of ES limitations (1000 rows), there was need to implement some work around
  // first send sql explain request to ES, then change response
  // 'composite_buckets -> composite -> size': from 1000 to 65000
  // and send it as _search request
  private SearchResultDto doExecuteGroupingSearch(QueryDto queryDto, String index) throws
      IOException {

    String dlsQuery = getDlsQuery(queryDto);
    String dlsQueryWithHigherLimit = dlsQuery.replace(ACTUAL_SIZE_LIMIT, DESIRED_SIZE_LIMIT);
    return doGroupingSearch(dlsQueryWithHigherLimit, index);
  }

  private String getDlsQuery(QueryDto queryDto) throws IOException {
    String explainPath = fromUriString(SQL_EXPLAIN_ENDPOINT).toUriString();
    String query = objectMapper.writeValueAsString(queryDto);
    log.info("Query to explain: {}", query);

    Request request = new Request(HttpPost.METHOD_NAME, explainPath);
    request.setJsonEntity(query);
    Response response = restLowLevelClient.performRequest(request);

    ExplainResultDto explainResultDto =
        objectMapper.readValue(response.getEntity().getContent(), ExplainResultDto.class);

    log.debug("OpendistroElasticClient method=POST, endpoint={}, statusCode={}, parsedBody={}",
        request.getEndpoint(), response.getStatusLine(), explainResultDto);

    return extractDlsQuery(explainResultDto.getRequest());
  }

  private String extractDlsQuery(String request) {
    return request.substring(
        request.lastIndexOf("sourceBuilder={") + 14,
        request.lastIndexOf(","));
  }

  private SearchResultDto doGroupingSearch(String dlsQuery, String index) throws IOException {
    String searchPath = fromUriString(index).pathSegment(SEARCH_ENDPOINT).toUriString();

    Request request = new Request(HttpPost.METHOD_NAME, searchPath);
    request.setJsonEntity(dlsQuery);
    Response response = restLowLevelClient.performRequest(request);

    SearchResultDto searchResultDto = objectMapper.readValue(
        response.getEntity().getContent(), SearchResultDto.class);

    log.debug("OpendistroElasticClient method=POST, endpoint={}, statusCode={}, parsedBody={}",
        request.getEndpoint(), response.getStatusLine(), searchResultDto);

    return searchResultDto;
  }
}
