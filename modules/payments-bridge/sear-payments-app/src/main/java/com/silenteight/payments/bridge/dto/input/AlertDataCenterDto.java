package com.silenteight.payments.bridge.dto.input;

import lombok.*;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AlertDataCenterDto {

  @NonNull private AlertMessageDto alertMessageDto;
  @NonNull private String dataCenter;

}
