package com.silenteight.qco.domain.model;

import com.google.common.collect.Maps;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
public class QcoConfigurationHolder {

  public Map<ChangeCondition, QcoParams> getConfiguration() {
    //TODO: implement
    return Maps.newHashMap();
  }
}