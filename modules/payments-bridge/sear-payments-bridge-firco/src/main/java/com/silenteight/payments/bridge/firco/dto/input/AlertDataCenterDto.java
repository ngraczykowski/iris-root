package com.silenteight.payments.bridge.firco.dto.input;

import lombok.*;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AlertDataCenterDto {

  @NonNull
  private AlertMessageDto alertMessageDto;

  @NonNull
  private String dataCenter;
}
