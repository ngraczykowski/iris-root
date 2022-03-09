package com.silenteight.payments.bridge.svb.oldetl.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import com.silenteight.payments.bridge.etl.processing.model.MessageData;
import com.silenteight.payments.bridge.svb.oldetl.model.UnsupportedMessageException;
import com.silenteight.payments.bridge.svb.oldetl.response.AlertedPartyData;

import org.springframework.stereotype.Service;

import java.util.List;

import static com.silenteight.payments.bridge.common.dto.common.CommonTerms.*;

@RequiredArgsConstructor
@Slf4j
@Service
public class AlertParserService {

  private static final List<String> SUPPORTED_FIRCO_FORMATS =
      List.of(
          FIRCO_FORMAT_INT,
          FIRCO_FORMAT_FED,
          FIRCO_FORMAT_IAT_I,
          FIRCO_FORMAT_IAT_O,
          FIRCO_FORMAT_O_F);

  public static AlertedPartyData extractAlertedPartyData(
      final MessageData messageData, final String hitTag,
      final String fircoFormat, final String applicationCode) {

    final boolean tagValueInFormatF = ifTagValueInFormatF(messageData.getLines(hitTag));

    return ExtractOriginatorStrategy
        .choose(hitTag, tagValueInFormatF)
        .extract(new ExtractDisposition(applicationCode, fircoFormat, messageData, hitTag));
  }


  private static boolean ifTagValueInFormatF(List<String> tagValueLines) {
    var formatFPrefixes = List.of(ADDRESS_ROW_PREFIX, COUNTRY_ROW_PREFIX);
    var numberOfLinesContainsFormatFPrefixes = tagValueLines.stream()
        .filter(line -> line.length() > 1)
        .filter(line -> formatFPrefixes.contains(
            line.substring(0, 2)))
        .count();

    return numberOfLinesContainsFormatFPrefixes >= 2;
  }

  public static String extractFircoFormat(String applicationCode, MessageData messageData) {
    if (isSwiftFormat(applicationCode)) {
      return FIRCO_FORMAT_SWF;
    }

    var type = messageData.getValue("TYPE");

    for (var format : SUPPORTED_FIRCO_FORMATS) {
      if (type.contains(format))
        return format;
    }

    if (type.contains("BOO"))
      return FIRCO_FORMAT_IAT_O;

    throw new UnsupportedMessageException(
        "Unable to map unknown TYPE " + type + " to FKCO_V_FORMAT");
  }

  private static boolean isSwiftFormat(String applicationCode) {
    return APPLICATION_CODE_GTEX.equals(applicationCode) || APPLICATION_CODE_H_R_GTEX.equals(
        applicationCode);
  }
}
