package com.silenteight.customerbridge.common;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class WlName {

  private String name;
  private WlNameType type;
}
