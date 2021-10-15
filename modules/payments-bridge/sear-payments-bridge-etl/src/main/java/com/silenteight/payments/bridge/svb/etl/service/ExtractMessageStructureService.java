package com.silenteight.payments.bridge.svb.etl.service;

import lombok.RequiredArgsConstructor;

import com.silenteight.payments.bridge.svb.etl.model.*;
import com.silenteight.payments.bridge.svb.etl.port.ExtractMessageStructureUseCase;
import com.silenteight.payments.bridge.svb.etl.response.SourceSystem;

import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ExtractMessageStructureService implements ExtractMessageStructureUseCase {

  private final FieldValueExtractor fieldValueExtractor;

  @Override
  public AbstractMessageStructure extractMessageStructure(
      ExtractAlertedPartyDataRequest request) {

    String messageType = request.getMessageType();
    String tag = request.getTag();
    String messageData = request.getMessageData();
    var sourceSystemId = request.getApplicationCode();

    if (sourceSystemId.startsWith("MTS"))
      return messageStructureForMts(messageType, tag, messageData, SourceSystem.MTS);
    if (sourceSystemId.startsWith("NBP"))
      return messageStructureForNbp(messageType, tag, messageData);
    if (sourceSystemId.startsWith("STAR") || sourceSystemId.startsWith("AMH"))
      return messageStructureForScstar(messageType, tag, messageData);
    if (sourceSystemId.startsWith("STS"))
      return messageStructureForSts(messageType, tag, messageData);
    if (sourceSystemId.startsWith("DTP"))
      return messageStructureForDtp(request.getMatchingText(), messageType, tag, messageData);

    return defaultMessageStructure(messageType, tag, messageData);
  }

  @NotNull
  private static MessageStructureDefault defaultMessageStructure(
      String messageType, String tag, String messageData) {
    return new MessageStructureDefault(messageType, tag, messageData);
  }

  private static MessageStructureDtp messageStructureForDtp(
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

  private static MessageStructureSts messageStructureForSts(
      String messageType, String tag, String messageData) {
    return new MessageStructureSts(messageType, tag, messageData);
  }

  private static MessageStructureNbp messageStructureForNbp(
      String messageType, String tag, String messageData) {
    return new MessageStructureNbp(messageType, tag, messageData);
  }

  private MessageStructureMts messageStructureForMts(
      String messageType, String tag, String messageData, SourceSystem sourceSystem) {
    return new MessageStructureMts(
        messageType, tag, messageData, fieldValueExtractor.extractFieldValue(
        ExtractFieldStructureValue
            .builder()
            .sourceSystem(sourceSystem.name())
            .tag(tag)
            .messageData(messageData)
            .build()));
  }

  private static MessageStructureScstar messageStructureForScstar(
      String messageType, String tag, String messageData) {
    return new MessageStructureScstar(messageType, tag, messageData);
  }
}
