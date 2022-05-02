package com.silenteight.connector.ftcc.ingest.adapter.incoming.rest;

import com.silenteight.connector.ftcc.common.testing.rest.BaseRestControllerTest;
import com.silenteight.connector.ftcc.ingest.domain.BatchIdGenerator;
import com.silenteight.connector.ftcc.ingest.domain.IngestFacade;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.io.FileUtils;
import org.jetbrains.annotations.NotNull;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.util.ResourceUtils;

import java.io.IOException;

import static java.nio.charset.StandardCharsets.UTF_8;
import static org.hamcrest.CoreMatchers.is;
import static org.mockito.Mockito.*;
import static org.springframework.http.HttpStatus.OK;

@Import({ IngestRestController.class, RestConfiguration.class })
class IngestRestControllerTest extends BaseRestControllerTest {

  private static final String INGEST_URL = "/v1/alert";

  @MockBean
  private IngestFacade ingestFacade;

  @MockBean
  private ObjectMapper objectMapper;

  @MockBean
  private BatchIdGenerator batchIdGenerator;

  @Test
  void its200_whenAlertSent() throws IOException {
    when(batchIdGenerator.generate()).thenCallRealMethod();
    post(INGEST_URL, readFile("classpath:requests/SendMessage.json"))
        .status(OK)
        .body("Body.msg_Acknowledgement.faultcode", is("0"))
        .body("Body.msg_Acknowledgement.faultstring", is("OK"));
  }

  @NotNull
  private String readFile(String file) throws IOException {
    return FileUtils.readFileToString(ResourceUtils.getFile(file), UTF_8);
  }
}
