package com.silenteight.warehouse.common.opendistro.elastic;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import com.silenteight.warehouse.common.opendistro.elastic.exception.OpendistroElasticClientException;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.http.RequestLine;
import org.apache.http.StatusLine;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpPut;
import org.elasticsearch.client.*;

import java.io.IOException;
import java.util.Map;

import static java.util.Map.of;
import static org.apache.http.client.methods.HttpGet.METHOD_NAME;
import static org.springframework.web.util.UriComponentsBuilder.fromUriString;

@Slf4j
@RequiredArgsConstructor
public class OpendistroElasticClient {

  private static final String ROLE_PARAM = "role";
  private static final String ROLES_ENDPOINT =
      "/_opendistro/_security/api/roles/{" + ROLE_PARAM + "}";

  private final RestClient restLowLevelClient;
  private final ObjectMapper objectMapper;

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
