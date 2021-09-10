package com.silenteight.payments.bridge.firco.dto.input;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.PropertyNamingStrategy.UpperCamelCaseStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;

import java.io.Serializable;
import java.util.List;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;

@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonNaming(UpperCamelCaseStrategy.class)
public class RequestBodyDto implements Serializable {

  private static final long serialVersionUID = -1093820420334687526L;

  @JsonProperty("msg_SendMessage")
  @NotNull
  @Valid
  private RequestSendMessageDto sendMessageDto;

  CaseManagerAuthenticationDto getCaseManagerAuthentication() {
    return sendMessageDto.getAuthentication();
  }

  List<AlertMessageDto> getAlerts() {
    return sendMessageDto.getAlerts();
  }
}
