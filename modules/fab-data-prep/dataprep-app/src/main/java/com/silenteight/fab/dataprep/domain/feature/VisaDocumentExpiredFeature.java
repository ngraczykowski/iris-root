package com.silenteight.fab.dataprep.domain.feature;

import lombok.RequiredArgsConstructor;

import com.silenteight.fab.dataprep.domain.model.ParsedMessageData;
import com.silenteight.universaldatasource.api.library.Feature;
import com.silenteight.universaldatasource.api.library.comparedates.v1.CompareDatesFeatureInputOut;

@RequiredArgsConstructor
public class VisaDocumentExpiredFeature implements FabFeature {

  static final String FEATURE_NAME = "features/visaExpiryDateVsToday";

  @Override
  public Feature buildFeature(BuildFeatureCommand buildFeatureCommand) {
    ParsedMessageData parsedMessageData = buildFeatureCommand.getParsedMessageData();
    return CompareDatesFeatureInputOut.builder()
        .feature(FEATURE_NAME)
        .dateToCompare(getAlertedPart(parsedMessageData))
        .build();
  }

  private static String getAlertedPart(ParsedMessageData parsedMessageData) {
    return parsedMessageData.getTradeLicPlaceOfIssue();
  }
}
