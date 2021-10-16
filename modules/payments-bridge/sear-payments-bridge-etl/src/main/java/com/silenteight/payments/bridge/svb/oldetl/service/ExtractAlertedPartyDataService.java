package com.silenteight.payments.bridge.svb.oldetl.service;

import lombok.RequiredArgsConstructor;

import com.silenteight.payments.bridge.svb.oldetl.model.AbstractMessageStructure;
import com.silenteight.payments.bridge.svb.oldetl.model.ExtractAlertedPartyDataRequest;
import com.silenteight.payments.bridge.svb.oldetl.port.ExtractAlertedPartyDataUseCase;
import com.silenteight.payments.bridge.svb.oldetl.port.ExtractMessageStructureUseCase;
import com.silenteight.payments.bridge.svb.oldetl.response.AlertedPartyData;
import com.silenteight.payments.bridge.svb.oldetl.response.MessageFieldStructure;
import com.silenteight.payments.bridge.svb.oldetl.service.MessageFieldExtractor.MessageNameAddressResult;

import org.springframework.stereotype.Service;

import java.util.List;

@RequiredArgsConstructor
@Service
class ExtractAlertedPartyDataService implements ExtractAlertedPartyDataUseCase {

  private final ExtractMessageStructureUseCase extractMessageStructureUseCase;
  private final FieldValueExtractor fieldValueExtractor;

  @Override
  public AlertedPartyData extractAlertedPartyData(ExtractAlertedPartyDataRequest request) {
    AbstractMessageStructure messageStructure =
        extractMessageStructureUseCase.extractMessageStructure(request);
    List<List<String>> fieldValues =
        fieldValueExtractor.extractFieldValues(
            request.getApplicationCode(),
            messageStructure);

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
}
