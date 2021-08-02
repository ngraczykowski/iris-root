package com.silenteight.payments.bridge.firco.dto.input;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RequestDto implements Serializable {

  private static final long serialVersionUID = -2318468779346806189L;

  @JsonProperty("Header")
  private Map<String, Object> header;

  @JsonProperty("Body")
  @NotNull
  @Valid
  private RequestBodyDto body;

  public List<AlertDataCenterDto> getAlertDataCenters(@NonNull String dataCenter) {
    return body.getAlerts()
        .stream()
        .map(alert -> AlertDataCenterDto.builder()
            .alertMessageDto(alert)
            .dataCenter(dataCenter)
            .build())
        .collect(Collectors.toList());
  }
}
