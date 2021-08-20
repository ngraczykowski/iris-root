package com.silenteight.hsbc.bridge.jenkins;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import com.silenteight.hsbc.bridge.model.dto.ModelInfo;
import com.silenteight.hsbc.bridge.model.dto.ModelStatusUpdatedDto;
import com.silenteight.hsbc.bridge.model.transfer.ModelClient;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.http.client.utils.URIBuilder;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.net.http.HttpResponse.BodyHandlers;
import java.util.Base64;

@RequiredArgsConstructor
@Slf4j
public class JenkinsModelClient implements ModelClient {

  private static final String AUTHORIZATION = "Authorization";

  private final ObjectMapper objectMapper;
  private final HttpClient httpClient;
  private final JenkinsApiProperties jenkinsApiProperties;

  @Override
  public void updateModel(ModelInfo modelInfo) {
    var crumbResponse = getCrumb();
    var statusCode = getUpdatedModel(crumbResponse, modelInfo);
    log.info("Status code of getting model from Jenkins is: " + statusCode);
  }

  @Override
  public void sendModelStatus(ModelStatusUpdatedDto modelStatusUpdated) {
    var crumbResponse = getCrumb();
    var statusCode = sendUpdateModelStatus(crumbResponse, modelStatusUpdated);
    log.info("Status code of sending model status to Jenkins is: " + statusCode);
  }

  private CrumbResponse getCrumb() {
    var crumbRequest = createCrumbHttpRequest();
    return receiveCrumbResponse(crumbRequest);
  }

  private HttpRequest createCrumbHttpRequest() {
    return HttpRequest.newBuilder()
        .GET()
        .header(
            AUTHORIZATION,
            encodeCredentialsToStringForBasicAuth(
                jenkinsApiProperties.getUsername(),
                jenkinsApiProperties.getPassword()))
        .uri(URI.create(jenkinsApiProperties.getCrumbUri()))
        .build();
  }

  private static String encodeCredentialsToStringForBasicAuth(String username, String password) {
    return "Basic " + Base64.getEncoder().encodeToString((username + ":" + password).getBytes());
  }

  private CrumbResponse receiveCrumbResponse(HttpRequest crumbRequest) {
    var crumbResponse = sendRequest(crumbRequest);
    try {
      return objectMapper.readValue(crumbResponse.body(), new TypeReference<>() {});
    } catch (JsonProcessingException e) {
      log.error("Exception occurred on receiving CrumbResponse: ", e);
      throw new ModelNotReceivedException(
          "Exception occurred on receiving CrumbResponse: " + e.getMessage(), e);
    }
  }

  private int getUpdatedModel(CrumbResponse crumbResponse, ModelInfo modelInfo) {
    var updateModelRequest =
        createUpdateModelHttpRequest(crumbResponse, modelInfo);
    var statusCode = sendRequest(updateModelRequest).statusCode();
    if (statusCode == 201) {
      return statusCode;
    } else {
      log.error("Unable to get updated model with status code: " + statusCode);
      throw new ModelNotReceivedException(
          "Unable to get updated model with status code: " + statusCode);
    }
  }

  private int sendUpdateModelStatus(
      CrumbResponse crumbResponse, ModelStatusUpdatedDto modelStatusUpdated) {
    var updateModelStatusRequest =
        createUpdateModelStatusHttpRequest(crumbResponse, modelStatusUpdated);
    int statusCode = sendRequest(updateModelStatusRequest).statusCode();
    if (statusCode == 201) {
      return statusCode;
    } else {
      log.error("Unable to send update model status with code: " + statusCode);
      throw new ModelNotReceivedException(
          "Unable to send update model status with code: " + statusCode);
    }
  }

  private HttpRequest createUpdateModelStatusHttpRequest(
      CrumbResponse crumbResponse, ModelStatusUpdatedDto modelStatusUpdated) {
    return HttpRequest.newBuilder()
        .POST(HttpRequest.BodyPublishers.noBody())
        .header(
            AUTHORIZATION,
            encodeCredentialsToStringForBasicAuth(
                jenkinsApiProperties.getUsername(),
                jenkinsApiProperties.getPassword()))
        .header(crumbResponse.getCrumbRequestField(), crumbResponse.getCrumb())
        .uri(createModelStatusUpdatedUri(
            jenkinsApiProperties.getUpdateModelStatusUri(), modelStatusUpdated))
        .build();
  }

  private URI createModelStatusUpdatedUri(
      String updateModelStatusUri, ModelStatusUpdatedDto modelStatusUpdated) {
    var uri = URI.create(updateModelStatusUri);
    var uriWithParams = new URIBuilder()
        .setScheme(uri.getScheme())
        .setHost(uri.getHost())
        .setPath(uri.getPath())
        .addParameter("name", modelStatusUpdated.getName())
        .addParameter("url", modelStatusUpdated.getUrl())
        .addParameter("type", modelStatusUpdated.getType())
        .addParameter("status", modelStatusUpdated.getStatus())
        .toString();

    return URI.create(uriWithParams);
  }

  private HttpRequest createUpdateModelHttpRequest(
      CrumbResponse crumbResponse, ModelInfo modelInfo) {
    return HttpRequest.newBuilder()
        .POST(HttpRequest.BodyPublishers.noBody())
        .header(
            AUTHORIZATION,
            encodeCredentialsToStringForBasicAuth(
                jenkinsApiProperties.getUsername(),
                jenkinsApiProperties.getPassword()))
        .header(crumbResponse.getCrumbRequestField(), crumbResponse.getCrumb())
        .uri(createModelInfoUri(jenkinsApiProperties.getUpdateModelUri(), modelInfo))
        .build();
  }

  private URI createModelInfoUri(String updateModelUri, ModelInfo modelInfo) {
    var uri = URI.create(updateModelUri);
    var uriWithParams = new URIBuilder()
        .setScheme(uri.getScheme())
        .setHost(uri.getHost())
        .setPath(uri.getPath())
        .addParameter("name", modelInfo.getName())
        .addParameter("url", modelInfo.getUrl())
        .addParameter("type", modelInfo.getType())
        .addParameter("changeType", modelInfo.getChangeType())
        .toString();

    return URI.create(uriWithParams);
  }

  private HttpResponse<String> sendRequest(HttpRequest httpRequest) {
    try {
      return httpClient.send(httpRequest, BodyHandlers.ofString());
    } catch (IOException e) {
      log.error("Exception occurred on receiving HttpResponse", e);
      throw new ModelNotReceivedException(
          "Exception occurred during sending request to Jenkins: " + e.getMessage(), e);
    } catch (InterruptedException e) {
      log.error("Exception occurred on receiving HttpResponse", e);
      Thread.currentThread().interrupt();
      throw new ModelNotReceivedException(
          "Exception occurred during sending request to Jenkins: " + e.getMessage(), e);
    }
  }

  private static class ModelNotReceivedException extends RuntimeException {

    private static final long serialVersionUID = 330005480374608070L;

    public ModelNotReceivedException(String message, Throwable cause) {
      super(message, cause);
    }

    public ModelNotReceivedException(String message) {
      super(message);
    }
  }
}
