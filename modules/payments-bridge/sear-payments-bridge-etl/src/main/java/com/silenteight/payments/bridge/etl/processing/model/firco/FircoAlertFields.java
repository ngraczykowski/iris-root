package com.silenteight.payments.bridge.etl.processing.model.firco;

import lombok.Builder;
import lombok.Value;

import com.silenteight.payments.bridge.etl.processing.model.MessageData;

@Value
@Builder
public class FircoAlertFields {

  String unit;
  String businessUnit;
  String messageId;
  String systemId;
  boolean bypass;
  boolean command;
  String businessType;
  String messageType;
  String ioIndicator;
  String senderReference;
  String currency;
  String amount;
  String applicationCode;
  String toApplication;
  MessageData messageData;
  String senderCode;
  String receiverCode;
  String applicationPriority;
  String cutoffTime;
  String normalizedAmount;
  String lastComment;
  String lastOperator;
  String valueDate;
  String filteredDate;
  String relatedReference;
  String createdDate;
  String priority;
  String confidentiality;
  int blocking;
  int nonBlocking;
  String copyService;
}
