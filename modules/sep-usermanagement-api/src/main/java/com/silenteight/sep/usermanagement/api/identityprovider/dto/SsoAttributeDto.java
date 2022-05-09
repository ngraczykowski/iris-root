package com.silenteight.sep.usermanagement.api.identityprovider.dto;

import lombok.*;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class SsoAttributeDto {

  @NonNull
  String key;
  @NonNull
  String value;
}
