package com.silenteight.fab.dataprep.domain;

import com.silenteight.proto.fab.api.v1.AlertHeader;
import com.silenteight.proto.fab.api.v1.MessageAlertAndMatchesStored;

import org.springframework.core.convert.converter.Converter;

public class MessageAlertAndMatchesStoredToAlertHeaderConverter
    implements Converter<MessageAlertAndMatchesStored, AlertHeader> {

  @Override
  public AlertHeader convert(MessageAlertAndMatchesStored source) {
    return AlertHeader
        .newBuilder()
        .setAlertId(source.getAlertId())
        .setBatchId(source.getBatchId())
        .build();
  }
}
