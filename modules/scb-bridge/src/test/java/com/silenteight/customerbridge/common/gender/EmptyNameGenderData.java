package com.silenteight.customerbridge.common.gender;

import java.util.Collections;
import java.util.List;

class EmptyNameGenderData implements NameGenderData {

  @Override
  public List<String> getMaleNames() {
    return Collections.emptyList();
  }

  @Override
  public List<String> getFemaleNames() {
    return Collections.emptyList();
  }
}
