package com.silenteight.connector.ftcc.ingest.adapter.rest;

import com.silenteight.connector.ftcc.common.testing.rest.BaseRestControllerTest;
import com.silenteight.connector.ftcc.ingest.dto.input.RequestDto;
import com.silenteight.sep.base.common.support.jackson.JsonConversionHelper;

import org.jetbrains.annotations.NotNull;
import org.junit.jupiter.api.Test;
import org.springframework.context.annotation.Import;

import java.io.IOException;
import java.io.InputStream;

import static org.hamcrest.CoreMatchers.is;
import static org.springframework.http.HttpStatus.OK;

@Import({
    IngestRestController.class
})
class IngestRestControllerTest extends BaseRestControllerTest {

  private static final String INGEST_URL = "/v1/alert";

  @Test
  void its200_whenAlertSent() throws IOException {
    post(INGEST_URL, getResourceAsObject("requests/SendMessage.json"))
        .statusCode(OK.value())
        .body("Body.msg_Acknowledgement.faultcode", is("0"))
        .body("Body.msg_Acknowledgement.faultstring", is("OK"));
  }

  @NotNull
  private RequestDto getResourceAsObject(String file) throws IOException {
    return JsonConversionHelper
        .INSTANCE
        .objectMapper()
        .readValue(getResourceAsString(file), RequestDto.class);
  }

  private String getResourceAsString(String file) throws IOException {
    InputStream inputStream = getClass().getClassLoader().getResourceAsStream(file);
    return new String(inputStream.readAllBytes());
  }
}
