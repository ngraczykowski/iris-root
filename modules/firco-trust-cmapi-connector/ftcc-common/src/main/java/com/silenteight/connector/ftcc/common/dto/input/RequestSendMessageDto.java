package com.silenteight.connector.ftcc.common.dto.input;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.PropertyNamingStrategies.UpperCamelCaseStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;

import java.io.Serializable;
import java.util.List;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonNaming(UpperCamelCaseStrategy.class)
public class RequestSendMessageDto implements Serializable {

  private static final long serialVersionUID = -7868232254808751898L;

  private String versionTag;

  /**
   * List of parameters used to connect to Case Manager, it is Payments Bridge in our case.
   */
  @Valid
  private CaseManagerAuthenticationDto authentication;

  @Size(min = 1)
  @NotNull
  @Valid
  private List<JsonNode> messages;

  long getMessagesCount() {
    return messages.size();
  }
}