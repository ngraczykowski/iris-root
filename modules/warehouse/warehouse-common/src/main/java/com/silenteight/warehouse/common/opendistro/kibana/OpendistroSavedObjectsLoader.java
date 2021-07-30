package com.silenteight.warehouse.common.opendistro.kibana;

import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.entity.mime.content.InputStreamBody;
import org.apache.http.impl.client.CloseableHttpClient;

import java.io.InputStream;
import javax.validation.Valid;

import static java.lang.String.join;
import static java.util.Base64.getEncoder;
import static org.apache.http.entity.ContentType.DEFAULT_BINARY;
import static org.apache.http.entity.mime.HttpMultipartMode.BROWSER_COMPATIBLE;
import static org.springframework.web.util.UriComponentsBuilder.fromUriString;

@Slf4j
@RequiredArgsConstructor
public class OpendistroSavedObjectsLoader {

  @Valid
  private final KibanaProperties kibanaProperties;
  private final CloseableHttpClient httpclient;
  private final ObjectMapper objectMapper;

  private static final String URL_IMPORT = "/api/saved_objects/_import";

  @SneakyThrows
  public void loadAll(String tenant, InputStream payload) {
    String path = fromUriString(URL_IMPORT)
        .queryParam("overwrite", "true")
        .toUriString();

    HttpPost post = new HttpPost(kibanaProperties.getUrl() + path);
    post.setHeader("Authorization", getAdminAuthorizationToken());
    post.setHeader("kbn-xsrf", "true");
    post.setHeader("Origin", kibanaProperties.getUrl());
    post.setHeader("securitytenant", tenant);
    InputStreamBody inputStreamBody =
        new InputStreamBody(payload, DEFAULT_BINARY, "file.ndjson");

    MultipartEntityBuilder builder = MultipartEntityBuilder.create();
    builder.setMode(BROWSER_COMPATIBLE);
    builder.addPart("file", inputStreamBody);

    HttpEntity entity = builder.build();
    post.setEntity(entity);
    HttpResponse response = httpclient.execute(post);

    TypeReference<ImportResult> typeRef = new TypeReference<>() {};
    ImportResult parsedResponse =
        objectMapper.readValue(response.getEntity().getContent(), typeRef);

    int statusCode = response.getStatusLine().getStatusCode();
    log.debug("OpendistroKibanaImportClient method=POST, endpoint={}, statusCode={}, body={}",
        path, statusCode, parsedResponse);
    if (statusCode > 200 || parsedResponse.isError()) {
      throw new OpendistroKibanaClientException(statusCode, parsedResponse.toString(), path);
    }
  }

  private String getAdminAuthorizationToken() {
    String username = kibanaProperties.getUsername();
    String password = kibanaProperties.getPassword();

    String plain = join(":", username, password);
    String base64 = getEncoder().encodeToString(plain.getBytes());

    return join(" ", "Basic", base64);
  }
}
