package com.silenteight.payments.bridge.svb.etl.model;

import com.silenteight.payments.bridge.svb.etl.response.MessageFieldStructure;

import java.util.List;
import java.util.Optional;

import static java.util.Arrays.asList;

public class MessageStructureScstar extends MessageStructureDefault {

  public MessageStructureScstar(String type, String apTag, String messageData) {
    super(type, apTag, messageData);
  }

  @Override
  public boolean checkMessageWithoutAccountNum() {
    return "700".equals(getType()) && "50".equals(getApTag());
  }

  @Override
  public boolean checkMessageFormatF() {
    return "103".equals(getType()) && asList("50F", "59F").contains(getApTag());
  }

  @Override
  public boolean checkMessageFormatUnstructured() {
    return ("103".equals(getType()) && asList("50K", "59", "57D").contains(getApTag()))
        || ("700".equals(getType()) && "50".equals(getApTag()));
  }

  @Override
  public Optional<String> getAccountNumber(GetAccountNumberRequest request) {
    if (List
        .of(
            MessageFieldStructure.NAMEADDRESS_FORMAT_F,
            MessageFieldStructure.NAMEADDRESS_FORMAT_UNSTRUCTURED)
        .contains(getMessageFieldStructure())) {
      return Optional.of(request.getMessage().split("\n")[0]);
    }
    return Optional.empty();
  }
}
