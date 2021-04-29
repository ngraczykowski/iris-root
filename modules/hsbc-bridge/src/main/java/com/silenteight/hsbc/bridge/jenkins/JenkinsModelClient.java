package com.silenteight.hsbc.bridge.jenkins;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import com.silenteight.hsbc.bridge.transfer.ModelClient;
import com.silenteight.hsbc.bridge.transfer.ModelInfo;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

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

  private final ObjectMapper objectMapper;
  private final HttpClient httpClient;
  private final JenkinsApiProperties jenkinsApiProperties;

  @Override
  public Model getModel(ModelInfo modelInfo) {
    var crumbResponse = getCrumb();
    return getUpdatedModel(crumbResponse, modelInfo);
  }

  private CrumbResponse getCrumb() {
    var crumbRequest = createCrumbHttpRequest();
    return receiveCrumbResponse(crumbRequest);
  }

  private HttpRequest createCrumbHttpRequest() {
    return HttpRequest.newBuilder()
        .GET()
        .header(
            "Authorization",
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
      throw new ModelNotReceivedException(e.getMessage());
    }
  }

  private Model getUpdatedModel(CrumbResponse crumbResponse, ModelInfo modelInfo) {
    var updateModelRequest =
        createUpdateModelHttpRequest(crumbResponse, modelInfo);
    return receiveModelUpdateResponse(updateModelRequest);
  }

  private HttpRequest createUpdateModelHttpRequest(
      CrumbResponse crumbResponse, ModelInfo modelInfo) {
    return HttpRequest.newBuilder()
        .POST(HttpRequest.BodyPublishers.ofString(mapModelInfoToJsonAsString(modelInfo)))
        .header(crumbResponse.getCrumbRequestField(), crumbResponse.getCrumb())
        .uri(URI.create(jenkinsApiProperties.getUpdateModelUri()))
        .build();
  }

  private String mapModelInfoToJsonAsString(ModelInfo modelInfo) {
    try {
      return objectMapper.writeValueAsString(modelInfo);
    } catch (JsonProcessingException e) {
      throw new ModelNotReceivedException(e.getMessage());
    }
  }

  private Model receiveModelUpdateResponse(HttpRequest updateModelRequest) {
    var updateModelResponse = sendRequest(updateModelRequest);
    try {
      return objectMapper.readValue(updateModelResponse.body(), new TypeReference<>() {});
    } catch (JsonProcessingException e) {
      log.error("Exception occurred on receiving ModelUpdateResponse: ", e);
      throw new ModelNotReceivedException(e.getMessage());
    }
  }

  private HttpResponse<String> sendRequest(HttpRequest httpRequest) {
    try {
      return httpClient.send(httpRequest, BodyHandlers.ofString());
    } catch (IOException e) {
      log.error("Exception occurred on receiving HttpResponse", e);
      throw new ModelNotReceivedException(e.getMessage());
    } catch (InterruptedException e) {
      log.error("Exception occurred on receiving HttpResponse", e);
      Thread.currentThread().interrupt();
      throw new ModelNotReceivedException(e.getMessage());
    }
  }

  private static class ModelNotReceivedException extends RuntimeException {

    public ModelNotReceivedException(String message) {
      super(message);
    }
  }
}
