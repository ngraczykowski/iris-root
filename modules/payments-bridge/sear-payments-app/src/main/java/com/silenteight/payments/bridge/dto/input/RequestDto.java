package com.silenteight.payments.bridge.dto.input;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;
import java.util.List;
import java.util.stream.Collectors;
import javax.annotation.Nullable;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RequestDto implements Serializable {

  private static final long serialVersionUID = -2318468779346806189L;

  @JsonProperty("Header")
  String header;

  @JsonProperty("Body")
  @NotNull
  @Valid
  RequestBodyDto body;

  @JsonProperty("DataCenter")
  @Nullable
  String dataCenter;

  public List<AlertDataCenterDto> getAlertDataCenters() {
    return body.getAlerts()
        .stream()
        .map(alert -> AlertDataCenterDto.builder()
            .alertMessageDto(alert)
            .dataCenter(dataCenter)
            .build())
        .collect(Collectors.toList());
  }
}
