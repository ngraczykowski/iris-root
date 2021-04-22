package com.silenteight.warehouse.common.opendistro.kibana;

import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpRequest.BodyPublisher;
import java.net.http.HttpRequest.Builder;
import java.net.http.HttpResponse;
import java.util.function.Supplier;

import static java.net.URI.create;
import static java.net.http.HttpRequest.BodyPublishers.noBody;
import static java.net.http.HttpRequest.BodyPublishers.ofInputStream;
import static java.net.http.HttpResponse.BodyHandlers.ofString;
import static java.util.Optional.ofNullable;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@Slf4j
@RequiredArgsConstructor
public class OpendistroKibanaClient {

  private static final String HEADER_SECURITY_TENANT = "securitytenant";

  private final HttpClient client;
  private final String hostname;
  private final ObjectMapper objectMapper;
  private final Supplier<Builder> defaultHttpRequestBuilder;
  private final String timezone;

  public String getReportContent(String tenant, String reportInstanceId) {
    TypeReference<ReportContentResponse> typeRef = new TypeReference<>() {};

    ReportContentResponse report = this.get(GetHttpRequest.builder()
        .endpoint("/api/reporting/generateReport/" + reportInstanceId + "?timezone=" + timezone)
        .tenant(tenant)
        .build(), typeRef);

    return ofNullable(report.getData())
        .orElseThrow(() -> new KibanaReportGenerationFailedException(reportInstanceId));
  }

  @SneakyThrows
  <T> T post(PostHttpRequest postHttpRequest, TypeReference<T> type) {
    BodyPublisher bodyPublisherInUse = postHttpRequest.hasPayload()
                                       ? ofInputStream(postHttpRequest::getPayload)
                                       : noBody();

    HttpRequest request = defaultHttpRequestBuilder.get()
        .uri(create(hostname + postHttpRequest.getEndpoint()))
        .header("Content-Type", APPLICATION_JSON_VALUE)
        .header(HEADER_SECURITY_TENANT, postHttpRequest.getTenant())
        .POST(bodyPublisherInUse)
        .build();

    return execute(request, type);
  }

  @SneakyThrows
  <T> T get(GetHttpRequest getHttpRequest, TypeReference<T> type) {
    HttpRequest request = defaultHttpRequestBuilder.get()
        .uri(create(hostname + getHttpRequest.getEndpoint()))
        .header("Content-Type", APPLICATION_JSON_VALUE)
        .header(HEADER_SECURITY_TENANT, getHttpRequest.getTenant())
        .GET()
        .build();

    return execute(request, type);
  }

  @SneakyThrows
  private <T> T execute(HttpRequest httpRequest, TypeReference<T> typeRef) {
    HttpResponse<String> response = client.send(httpRequest, ofString());
    T parsedResponse = objectMapper.readValue(response.body(), typeRef);

    log.info("OpendistroKibanaClient method={}, endpoint={}, statusCode={}, body={}",
        httpRequest.method(), httpRequest.uri(), response.statusCode(), parsedResponse);

    return parsedResponse;
  }
}
