package com.silenteight.warehouse.common.opendistro.kibana;

import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;

import com.silenteight.warehouse.common.opendistro.kibana.dto.KibanaReportDto;
import com.silenteight.warehouse.common.opendistro.kibana.dto.SavedObjectDto;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpRequest.BodyPublisher;
import java.net.http.HttpRequest.Builder;
import java.net.http.HttpResponse;
import java.util.List;
import java.util.Objects;
import java.util.function.Supplier;

import static java.net.URI.create;
import static java.net.http.HttpRequest.BodyPublishers.noBody;
import static java.net.http.HttpRequest.BodyPublishers.ofInputStream;
import static java.net.http.HttpResponse.BodyHandlers.ofString;
import static java.util.Map.of;
import static java.util.Optional.ofNullable;
import static java.util.stream.Collectors.toList;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import static org.springframework.web.util.UriComponentsBuilder.fromUriString;

@Slf4j
@RequiredArgsConstructor
public class OpendistroKibanaClient {

  private static final String HEADER_SECURITY_TENANT = "securitytenant";
  private static final String GENERATE_REPORT = "/api/reporting/generateReport/{reportInstanceId}";
  private static final String FIND_SAVED_OBJECT = "/api/saved_objects/_find";
  private static final String CREATE_SAVED_OBJECT = "/api/saved_objects/{objectType}/{objectId}";

  private final HttpClient client;
  private final String hostname;
  private final ObjectMapper objectMapper;
  private final Supplier<Builder> defaultHttpRequestBuilder;
  private final String timezone;

  public KibanaReportDto getReportContent(String tenant, String reportInstanceId) {
    TypeReference<ReportContentResponse> typeRef = new TypeReference<>() {};

    String path = fromUriString(GENERATE_REPORT)
        .queryParam("timezone", timezone)
        .buildAndExpand(of("reportInstanceId", reportInstanceId))
        .toUriString();

    GetHttpRequest request = new GetHttpRequest(path, tenant);
    ReportContentResponse report = this.get(request, typeRef);

    String data = ofNullable(report.getData())
        .orElseThrow(() -> new KibanaReportGenerationFailedException(reportInstanceId));
    String filename = ofNullable(report.getFilename())
        .orElseThrow(() -> new KibanaReportGenerationFailedException(reportInstanceId));

    return KibanaReportDto.builder()
        .content(data)
        .filename(filename)
        .build();
  }

  public List<SavedObjectDto> listSavedObjects(
      String tenant, SavedObjectType savedObjectType, Integer maxItemCount) {

    TypeReference<SavedObjectResponsePaginatedList> typeRef = new TypeReference<>() {};

    String path = fromUriString(FIND_SAVED_OBJECT)
        .queryParam("type", savedObjectType.getId())
        .queryParam("per_page", maxItemCount)
        .build()
        .toUriString();

    GetHttpRequest request = new GetHttpRequest(path, tenant);
    SavedObjectResponsePaginatedList savedObjectResponsePaginatedList = this.get(request, typeRef);

    return savedObjectResponsePaginatedList.getSavedObjects().stream()
        .map(SavedObject::toDto)
        .collect(toList());
  }

  @SneakyThrows
  public SavedObject createSavedObjects(
      String tenant, SavedObjectType savedObjectType, String id, SavedObject savedObject) {

    TypeReference<SavedObject> typeRef = new TypeReference<>() {};

    String path = fromUriString(CREATE_SAVED_OBJECT)
        .buildAndExpand(of(
            "objectType", savedObjectType.getId(),
            "objectId", id))
        .toUriString();

    return this.post(PostHttpRequest.builder()
        .endpoint(path)
        .tenant(tenant)
        .payload(objectMapper.writeValueAsBytes(savedObject))
        .build(), typeRef);
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

    String loggedResponse = intoLoggedResponse(response, parsedResponse);
    log.debug("OpendistroKibanaClient method={}, endpoint={}, statusCode={}, body={}",
        httpRequest.method(), httpRequest.uri(), response.statusCode(), loggedResponse);

    return parsedResponse;
  }

  @SneakyThrows
  private <T> String intoLoggedResponse(HttpResponse<String> response, T parsedResponse) {
    boolean isError = response.statusCode() >= 400;

    return isError
           ? response.body()
           : ofNullable(parsedResponse).map(Objects::toString).orElse("<empty>");
  }
}
