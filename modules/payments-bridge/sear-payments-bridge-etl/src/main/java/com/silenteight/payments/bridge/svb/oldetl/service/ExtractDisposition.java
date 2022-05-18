package com.silenteight.payments.bridge.svb.oldetl.service;

import lombok.Value;

import com.silenteight.payments.bridge.etl.processing.model.MessageData;

@Value
public class ExtractDisposition {

  String applicationCode;
  String messageFormat;
  MessageData messageData;
  String hitTag;
}
