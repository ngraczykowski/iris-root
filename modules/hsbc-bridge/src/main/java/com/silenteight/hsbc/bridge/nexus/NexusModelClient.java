package com.silenteight.hsbc.bridge.nexus;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import com.silenteight.hsbc.bridge.model.transfer.RepositoryClient;

import org.apache.commons.io.IOUtils;

import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.net.http.HttpResponse.BodyHandlers;
import java.util.Base64;

@Slf4j
@RequiredArgsConstructor
public class NexusModelClient implements RepositoryClient {

  private final HttpClient httpClient;
  private final NexusApiProperties nexusApiProperties;

  @Override
  public byte[] updateModel(String uri) throws IOException {
    var nexusRequest = createNexusHttpRequest(uri);
    return receiveNexusResponse(nexusRequest);
  }

  private HttpRequest createNexusHttpRequest(String uri) {
    return HttpRequest.newBuilder()
        .GET()
        .header(
            "Authorization",
            encodeCredentialsToStringForBasicAuth(
                nexusApiProperties.getUsername(),
                nexusApiProperties.getPassword()))
        .uri(URI.create(uri))
        .build();
  }

  private static String encodeCredentialsToStringForBasicAuth(String username, String password) {
    return "Basic " + Base64.getEncoder().encodeToString((username + ":" + password).getBytes());
  }

  private byte[] receiveNexusResponse(HttpRequest nexusRequest) throws IOException {
    var nexusResponse = sendRequest(nexusRequest);
    if (nexusResponse.statusCode() == 200) {
      return IOUtils.toByteArray(nexusResponse.body());
    } else {
      throw new NexusResponseNotReceivedException(
          "Unable to receive model, status code from Nexus is: " + nexusResponse.statusCode());
    }
  }

  private HttpResponse<InputStream> sendRequest(HttpRequest httpRequest) {
    try {
      return httpClient.send(httpRequest, BodyHandlers.ofInputStream());
    } catch (IOException e) {
      log.error("Exception occurred on receiving HttpResponse", e);
      throw new NexusResponseNotReceivedException(e.getMessage());
    } catch (InterruptedException e) {
      log.error("Exception occurred on receiving HttpResponse", e);
      Thread.currentThread().interrupt();
      throw new NexusResponseNotReceivedException(e.getMessage());
    }
  }

  private static class NexusResponseNotReceivedException extends RuntimeException {

    private static final long serialVersionUID = 7105701841811530177L;

    public NexusResponseNotReceivedException(String message) {
      super(message);
    }
  }
}
