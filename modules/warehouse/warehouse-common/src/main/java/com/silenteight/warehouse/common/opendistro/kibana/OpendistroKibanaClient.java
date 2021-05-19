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
import java.util.List;
import java.util.Objects;
import java.util.function.Supplier;

import static com.silenteight.warehouse.common.opendistro.kibana.SavedObjectType.KIBANA_INDEX_PATTERN;
import static com.silenteight.warehouse.common.opendistro.kibana.SavedObjectType.SEARCH;
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
  private static final String HEADER_CONTENT_TYPE = "Content-Type";
  private static final String HEADER_ORIGIN = "Origin";
  private static final String URL_REPORT_DEFINITION_LIST =
      "/api/reporting/reportDefinitions";
  private static final String URL_CREATE_REPORT_DEFINITION =
      "/api/reporting/reportDefinition";
  private static final String URL_GENERATE_REPORT =
      "/api/reporting/generateReport/{reportInstanceId}";
  private static final String URL_FIND_SAVED_OBJECT = "/api/saved_objects/_find";
  private static final String URL_SAVED_OBJECT = "/api/saved_objects/{objectType}/{objectId}";
  private static final String PARAM_OBJECT_TYPE = "objectType";
  private static final String PARAM_OBJECT_ID = "objectId";
  private static final String QUERY_PARAM_TIMEZONE = "timezone";
  private static final String QUERY_PARAM_TYPE = "type";
  private static final String QUERY_PARAM_PER_PAGE = "per_page";

  private final HttpClient client;
  private final String hostname;
  private final ObjectMapper objectMapper;
  private final Supplier<Builder> defaultHttpRequestBuilder;
  private final String timezone;

  public KibanaReportDto getReportContent(String tenant, String reportInstanceId) {
    TypeReference<ReportContentResponse> typeRef = new TypeReference<>() {};

    String path = fromUriString(URL_GENERATE_REPORT)
        .queryParam(QUERY_PARAM_TIMEZONE, timezone)
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

  public List<KibanaIndexPatternDto> listKibanaIndexPattern(String tenant, Integer maxItemCount) {

    TypeReference<SavedObjectPaginatedList<KibanaIndexPatternAttributes>> typeRef =
        new TypeReference<>() {};

    String path = fromUriString(URL_FIND_SAVED_OBJECT)
        .queryParam(QUERY_PARAM_TYPE, KIBANA_INDEX_PATTERN.getId())
        .queryParam(QUERY_PARAM_PER_PAGE, maxItemCount)
        .build()
        .toUriString();

    GetHttpRequest request = new GetHttpRequest(path, tenant);
    return get(request, typeRef).getSavedObjects()
        .stream()
        .map(OpendistroKibanaClient::toKibanaIndexPatternDto)
        .collect(toList());
  }

  private static KibanaIndexPatternDto toKibanaIndexPatternDto(
      SavedObject<KibanaIndexPatternAttributes> savedObject) {

    return KibanaIndexPatternDto.builder()
        .id(savedObject.getId())
        .attributes(savedObject.getAttributes())
        .references(savedObject.getReferences())
        .build();
  }

  @SneakyThrows
  public void createKibanaIndexPattern(
      String tenant, KibanaIndexPatternDto kibanaIndexPatternDto) {

    TypeReference<SavedObject<KibanaIndexPatternAttributes>> typeRef =
        new TypeReference<>() {};

    String path = fromUriString(URL_SAVED_OBJECT)
        .buildAndExpand(of(
            PARAM_OBJECT_TYPE, KIBANA_INDEX_PATTERN.getId(),
            PARAM_OBJECT_ID, kibanaIndexPatternDto.getId()))
        .toUriString();

    SavedObject<KibanaIndexPatternAttributes> requestBody =
        SavedObject.<KibanaIndexPatternAttributes>builder()
            .attributes(kibanaIndexPatternDto.getAttributes())
            .references(kibanaIndexPatternDto.getReferences())
            .build();

    PostHttpRequest request = PostHttpRequest.builder()
        .endpoint(path)
        .tenant(tenant)
        .payload(objectMapper.writeValueAsBytes(requestBody))
        .build();
    post(request, typeRef);
  }

  public List<SearchDto> listSavedSearchDefinitions(
      String tenant, Integer maxItemCount) {

    TypeReference<SavedObjectPaginatedList<SearchAttributes>> typeRef = new TypeReference<>() {};

    String path = fromUriString(URL_FIND_SAVED_OBJECT)
        .queryParam(QUERY_PARAM_TYPE, SEARCH.getId())
        .queryParam(QUERY_PARAM_PER_PAGE, maxItemCount)
        .build()
        .toUriString();

    GetHttpRequest request = new GetHttpRequest(path, tenant);
    return get(request, typeRef)
        .getSavedObjects()
        .stream()
        .map(OpendistroKibanaClient::toSearchDto)
        .collect(toList());
  }

  private static SearchDto toSearchDto(
      SavedObject<SearchAttributes> savedObject) {

    return SearchDto.builder()
        .id(savedObject.getId())
        .attributes(savedObject.getAttributes())
        .references(savedObject.getReferences())
        .build();
  }

  @SneakyThrows
  public void createSavedSearchObjects(String tenant, SearchDto searchDto) {
    TypeReference<SavedObject<SearchAttributes>> typeRef = new TypeReference<>() {};

    String path = fromUriString(URL_SAVED_OBJECT)
        .buildAndExpand(of(
            PARAM_OBJECT_TYPE, SEARCH.getId(),
            PARAM_OBJECT_ID, searchDto.getId()))
        .toUriString();

    SavedObject<SearchAttributes> requestBody =
        SavedObject.<SearchAttributes>builder()
            .attributes(searchDto.getAttributes())
            .references(searchDto.getReferences())
            .build();

    PostHttpRequest request = PostHttpRequest.builder()
        .endpoint(path)
        .tenant(tenant)
        .payload(objectMapper.writeValueAsBytes(requestBody))
        .build();
    post(request, typeRef);
  }

  public List<ReportDefinitionDto> listReportDefinitions(String tenant) {
    TypeReference<ReportDefinitionList> typeRef = new TypeReference<>() {};

    GetHttpRequest request = new GetHttpRequest(URL_REPORT_DEFINITION_LIST, tenant);
    return get(request, typeRef)
        .getData()
        .stream()
        .map(ReportDefinition::toDto)
        .collect(toList());
  }

  @SneakyThrows
  public String createReportDefinition(String tenant, ReportDefinitionDto reportDefinitionDto) {
    TypeReference<ReportDefinitionCreated> typeRef = new TypeReference<>() {};

    reportDefinitionDto.clearOrigin();

    PostHttpRequest request = PostHttpRequest.builder()
        .endpoint(URL_CREATE_REPORT_DEFINITION)
        .tenant(tenant)
        .origin(reportDefinitionDto.getOrigin())
        .payload(objectMapper.writeValueAsBytes(reportDefinitionDto.getReportDefinitionDetails()))
        .build();

    return post(request, typeRef)
        .getSchedulerResponse()
        .getReportDefinitionId();
  }

  public void deleteSavedObjects(String tenant, SavedObjectType type, String objectId) {
    TypeReference<Void> typeRef = new TypeReference<>() {};

    String path = fromUriString(URL_SAVED_OBJECT)
        .buildAndExpand(of(
            PARAM_OBJECT_TYPE, type.getId(),
            PARAM_OBJECT_ID, objectId))
        .toUriString();

    DeleteHttpRequest request = new DeleteHttpRequest(path, tenant);
    delete(request, typeRef);
  }

  public boolean isExistingReportDefinition(String tenant, String objectId) {
    return listReportDefinitions(tenant).stream()
        .map(ReportDefinitionDto::getId)
        .anyMatch(id -> id.equals(objectId));
  }

  @SneakyThrows
  <T> T delete(DeleteHttpRequest deleteHttpRequest, TypeReference<T> type) {
    HttpRequest request = defaultHttpRequestBuilder.get()
        .uri(create(hostname + deleteHttpRequest.getEndpoint()))
        .header(HEADER_SECURITY_TENANT, deleteHttpRequest.getTenant())
        .DELETE()
        .build();

    return execute(request, type);
  }

  @SneakyThrows
  <T> T post(PostHttpRequest postHttpRequest, TypeReference<T> type) {
    BodyPublisher bodyPublisherInUse = postHttpRequest.hasPayload()
                                       ? ofInputStream(postHttpRequest::getPayload)
                                       : noBody();

    Builder httpRequestBuilder = defaultHttpRequestBuilder.get()
        .uri(create(hostname + postHttpRequest.getEndpoint()))
        .header(HEADER_CONTENT_TYPE, APPLICATION_JSON_VALUE)
        .header(HEADER_SECURITY_TENANT, postHttpRequest.getTenant())
        .POST(bodyPublisherInUse);

    ofNullable(postHttpRequest.getOrigin())
        .ifPresent(origin -> httpRequestBuilder.header(HEADER_ORIGIN, origin));

    return execute(httpRequestBuilder.build(), type);
  }

  @SneakyThrows
  <T> T get(GetHttpRequest getHttpRequest, TypeReference<T> type) {
    HttpRequest request = defaultHttpRequestBuilder.get()
        .uri(create(hostname + getHttpRequest.getEndpoint()))
        .header(HEADER_CONTENT_TYPE, APPLICATION_JSON_VALUE)
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

    if (isError(response)) {
      throw new OpendistroKibanaClientException(response.statusCode(), loggedResponse);
    }

    return parsedResponse;
  }

  private <T> String intoLoggedResponse(HttpResponse<String> response, T parsedResponse) {
    return isError(response)
           ? response.body()
           : ofNullable(parsedResponse).map(Objects::toString).orElse("<empty>");
  }

  private boolean isError(HttpResponse<?> response) {
    return response.statusCode() >= 400;
  }
}
