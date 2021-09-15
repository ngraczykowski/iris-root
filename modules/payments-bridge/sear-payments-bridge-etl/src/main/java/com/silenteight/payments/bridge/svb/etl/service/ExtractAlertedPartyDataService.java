package com.silenteight.payments.bridge.svb.etl.service;

import com.silenteight.payments.bridge.svb.etl.model.AbstractMessageStructure;
import com.silenteight.payments.bridge.svb.etl.model.AbstractMessageStructure.*;
import com.silenteight.payments.bridge.svb.etl.model.AlertedPartyData;
import com.silenteight.payments.bridge.svb.etl.model.ExtractAlertedPartyDataRequest;
import com.silenteight.payments.bridge.svb.etl.model.MessageFieldStructure;
import com.silenteight.payments.bridge.svb.etl.port.ExtractAlertedPartyDataUseCase;
import com.silenteight.payments.bridge.svb.etl.service.MessageFieldExtractor.MessageNameAddressResult;

import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
class ExtractAlertedPartyDataService implements ExtractAlertedPartyDataUseCase {

  @Override
  public AlertedPartyData extractAlertedPartyData(ExtractAlertedPartyDataRequest request) {
    AbstractMessageStructure messageStructure = extractMessageStructure(request);
    List<List<String>> fieldValues =
        FieldValueExtractor.extractFieldValues(
            request.getApplicationCode(),
            messageStructure,
            request.getMessageData());

    MessageFieldStructure messageFieldStructure = messageStructure.getMessageFieldStructure();
    MessageNameAddressResult messageNameAddressResult = MessageFieldExtractor.extractNameAddress(
        fieldValues,
        messageFieldStructure,
        messageStructure.checkMessageWithoutAccountNum());

    return AlertedPartyData.builder()
        .names(messageNameAddressResult.getNames())
        .addresses(messageNameAddressResult.getAddresses())
        .ctryTowns(messageNameAddressResult.getCtryTowns())
        .nameAddresses(messageNameAddressResult.getNameAddresses())
        .noAcctNumFlag(messageNameAddressResult.isNoAcctNumFlag())
        .numOfLines(messageNameAddressResult.getNumOfLines())
        .messageLength(messageNameAddressResult.getLength())
        .messageFieldStructure(messageFieldStructure)
        .build();
  }

  private AbstractMessageStructure extractMessageStructure(
      ExtractAlertedPartyDataRequest request) {

    String messageType = request.getMessageType();
    String tag = request.getTag();
    String messageData = request.getMessageData();
    var sourceSystem =
        AbstractMessageStructure.extractSourceSystem(request.getApplicationCode());
    switch (sourceSystem) {
      case SCSTAR:
        return messageStructureForScstar(messageType, tag, messageData);
      case MTS:
        return messageStructureForMts(messageType, tag, messageData, sourceSystem);
      case NBP:
        return messageStructureForNbp(messageType, tag, messageData);
      case STS:
        return messageStructureForSts(messageType, tag, messageData);
      case DTP:
        return messageStructureForDtp(request.getMatchingText(), messageType, tag, messageData);
      default:
        return defaultMessageStructure(messageType, tag, messageData);
    }
  }

  @NotNull
  private AbstractMessageStructure.MessageStructureDefault defaultMessageStructure(
      String messageType, String tag, String messageData) {
    return new MessageStructureDefault(messageType, tag, messageData);
  }

  @NotNull
  private AbstractMessageStructure.MessageStructureDtp messageStructureForDtp(
      String matchingText, String messageType, String tag, String messageData) {
    String matchText = null;
    List<String> mainTagFieldValues = null;
    List<String> nextTagFieldValues = null;
    if (MessageStructureDtp.DTP_PAIRS_SCOPE.contains(tag)) {
      matchText = matchingText;
      List<List<String>> components =
          ComponentsExtractor.getComponents(
              messageData, tag, matchText);
      mainTagFieldValues = ComponentExtractorHelper.getElementsFromComponents(components, 0);
      nextTagFieldValues = ComponentExtractorHelper.getElementsFromComponents(components, 2);
    }
    return new MessageStructureDtp(messageType, tag, messageData, matchText, mainTagFieldValues,
        nextTagFieldValues);
  }

  @NotNull
  private AbstractMessageStructure.MessageStructureSts messageStructureForSts(
      String messageType, String tag, String messageData) {
    return new MessageStructureSts(messageType, tag, messageData);
  }

  @NotNull
  private AbstractMessageStructure.MessageStructureNbp messageStructureForNbp(
      String messageType, String tag, String messageData) {
    return new MessageStructureNbp(messageType, tag, messageData);
  }

  @NotNull
  private AbstractMessageStructure.MessageStructureMts messageStructureForMts(
      String messageType, String tag, String messageData, SourceSystem sourceSystem) {
    return new MessageStructureMts(
        messageType, tag, messageData, FieldValueExtractor.extractFieldValue(
        sourceSystem.name(), tag, messageData));
  }

  private MessageStructureScstar messageStructureForScstar(
      String messageType, String tag, String messageData) {
    return new MessageStructureScstar(messageType, tag, messageData);
  }
}
