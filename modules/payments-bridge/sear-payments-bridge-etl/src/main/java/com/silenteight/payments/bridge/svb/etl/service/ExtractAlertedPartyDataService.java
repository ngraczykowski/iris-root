package com.silenteight.payments.bridge.svb.etl.service;

import lombok.RequiredArgsConstructor;

import com.silenteight.payments.bridge.svb.etl.model.AbstractMessageStructure;
import com.silenteight.payments.bridge.svb.etl.model.ExtractAlertedPartyDataRequest;
import com.silenteight.payments.bridge.svb.etl.port.ExtractAlertedPartyDataUseCase;
import com.silenteight.payments.bridge.svb.etl.port.ExtractMessageStructureUseCase;
import com.silenteight.payments.bridge.svb.etl.response.AlertedPartyData;
import com.silenteight.payments.bridge.svb.etl.response.MessageFieldStructure;
import com.silenteight.payments.bridge.svb.etl.service.MessageFieldExtractor.MessageNameAddressResult;

import org.springframework.stereotype.Service;

import java.util.List;

@RequiredArgsConstructor
@Service
class ExtractAlertedPartyDataService implements ExtractAlertedPartyDataUseCase {

  private final ExtractMessageStructureUseCase extractMessageStructureUseCase;

  @Override
  public AlertedPartyData extractAlertedPartyData(ExtractAlertedPartyDataRequest request) {
    AbstractMessageStructure messageStructure =
        extractMessageStructureUseCase.extractMessageStructure(request);
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
}
