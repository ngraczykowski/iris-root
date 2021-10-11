package com.silenteight.payments.bridge.common.dto.input;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import com.fasterxml.jackson.databind.PropertyNamingStrategy.UpperCamelCaseStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;

import java.io.Serializable;
import java.util.List;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;

@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonNaming(UpperCamelCaseStrategy.class)
public class RequestDto implements Serializable {

  private static final long serialVersionUID = -2318468779346806189L;

  private transient String header;

  @NotNull
  @Valid
  private RequestBodyDto body;

  public UsernamePasswordAuthenticationToken getAuthenticationToken() {
    var authentication = getAuthentication();

    return new UsernamePasswordAuthenticationToken(
        authentication.getUserLogin(), authentication.getUserPassword());
  }

  public String getAuthenticationRealm() {
    return getAuthentication().getUserRealm();
  }

  public List<AlertMessageDto> getAlerts() {
    return body.getAlerts();
  }

  public CaseManagerAuthenticationDto getAuthentication() {
    return getBody().getCaseManagerAuthentication();
  }
}
