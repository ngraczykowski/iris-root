package com.silenteight.fab.dataprep.domain;

import lombok.extern.slf4j.Slf4j;

import com.silenteight.fab.dataprep.domain.model.ParsedMessageData;

import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class MessageDataTokenizer implements Converter<String, ParsedMessageData> {

  private static final String SEPARATOR = ";";

  static final int NUMBER_OF_SEGMENTS = 28;

  @Override
  public ParsedMessageData convert(String source) {
    String[] parts = source.split(SEPARATOR, -1);
    if (parts.length != NUMBER_OF_SEGMENTS) {
      throw new IllegalArgumentException(
          "Alert payload should contains " + NUMBER_OF_SEGMENTS + "segments separated with"
              + SEPARATOR);
    }
    return ParsedMessageData.builder()
        .salutation(parts[0])
        .name(parts[1])
        .shortName(parts[2])
        .customerType(parts[3])
        .dob(parts[4])
        .dateOfEstablishment(parts[5])
        .gender(parts[6])
        .swiftBic(parts[7])
        .address1(parts[8])
        .address2(parts[9])
        .city(parts[10])
        .state(parts[11])
        .country(parts[12])
        .countryOfIncorporation(parts[13])
        .countryOfDomicile(parts[14])
        .countryOfBirth(parts[15])
        .customerSegment(parts[16])
        .profession(parts[17])
        .passportNum(parts[18])
        .nationalId(parts[19])
        .tradeLicPlaceOfIssue(parts[20])
        .groupOrCompanyName(parts[21])
        .source(parts[22])
        .sourceSystemId(parts[23])
        .customerNumber(parts[24])
        .alternate(parts[25])
        .latestCustomerNumber(parts[26])
        .lastUpdateTime(parts[27])
        .build();
  }
}
