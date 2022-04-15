package com.silenteight.connector.ftcc.ingest.adapter.incoming.rest;

import com.silenteight.connector.ftcc.common.dto.input.RequestDto;
import com.silenteight.connector.ftcc.common.testing.rest.BaseRestControllerTest;
import com.silenteight.connector.ftcc.ingest.domain.IngestFacade;
import com.silenteight.sep.base.common.support.jackson.JsonConversionHelper;

import org.jetbrains.annotations.NotNull;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.util.ResourceUtils;
import org.testcontainers.shaded.com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;

import static org.hamcrest.CoreMatchers.is;
import static org.springframework.http.HttpStatus.OK;

@Import(IngestRestController.class)
class IngestRestControllerTest extends BaseRestControllerTest {

  private static final String INGEST_URL = "/v1/alert";

  @MockBean
  private IngestFacade ingestFacade;

  @MockBean
  private ObjectMapper objectMapper;

  //@Test
  void its200_whenAlertSent() throws IOException {
    post(INGEST_URL, getResourceAsObject("classpath:requests/SendMessage.json"))
        .statusCode(OK.value())
        .body("Body.msg_Acknowledgement.faultcode", is("0"))
        .body("Body.msg_Acknowledgement.faultstring", is("OK"));
  }

  @NotNull
  private RequestDto getResourceAsObject(String file) throws IOException {
    return JsonConversionHelper
        .INSTANCE
        .objectMapper()
        .readValue(ResourceUtils.getFile(file), RequestDto.class);
  }
}
