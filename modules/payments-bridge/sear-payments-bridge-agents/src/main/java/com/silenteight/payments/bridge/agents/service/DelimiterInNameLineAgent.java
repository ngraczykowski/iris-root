package com.silenteight.payments.bridge.agents.service;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import com.silenteight.payments.bridge.agents.model.DelimiterInNameLineAgentRequest;
import com.silenteight.payments.bridge.agents.model.DelimiterInNameLineAgentResponse;
import com.silenteight.payments.bridge.agents.port.DelimiterInNameLineUseCase;

import org.springframework.stereotype.Service;

import java.util.List;
import java.util.regex.Pattern;

import static com.silenteight.payments.bridge.agents.model.DelimiterInNameLineAgentResponse.NO;
import static com.silenteight.payments.bridge.agents.model.DelimiterInNameLineAgentResponse.YES;

@RequiredArgsConstructor
@Service
class DelimiterInNameLineAgent implements DelimiterInNameLineUseCase {

  private static final List<String> DELIMITERS =
      List.of("\\s-\\s", "--", "\\s-[A-Za-z]{3}", "\\sOR\\s");
  private static final List<String> UNSTRUCTURED_NAME_ADDRESS_FORMATS =
      List.of("NAMEADDRESS_FORMAT_UNSTRUCTURED");

  @NonNull
  public DelimiterInNameLineAgentResponse invoke(
      DelimiterInNameLineAgentRequest delimiterInNameLineAgentRequest) {
    return isDelimiterInName(
        delimiterInNameLineAgentRequest.getAllMatchingFieldsValue(),
        delimiterInNameLineAgentRequest.getMessageFieldStructureText()) ? YES : NO;
  }

  private static boolean isDelimiterInName(
      @NonNull String nameLine,
      @NonNull String messageFieldStructure) {
    Pattern delimitersPattern = Pattern.compile(String.join("|", DELIMITERS));

    return UNSTRUCTURED_NAME_ADDRESS_FORMATS.contains(messageFieldStructure) && delimitersPattern
        .matcher(nameLine)
        .find();
  }
}
