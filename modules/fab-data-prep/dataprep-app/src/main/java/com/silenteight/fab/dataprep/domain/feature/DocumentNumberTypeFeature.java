package com.silenteight.fab.dataprep.domain.feature;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import com.silenteight.fab.dataprep.domain.model.ParsedMessageData;
import com.silenteight.universaldatasource.api.library.Feature;
import com.silenteight.universaldatasource.api.library.isofgivendocumenttype.v1.IsOfGivenDocumentTypeFeatureInputOut;

import static java.util.List.of;

@Slf4j
@RequiredArgsConstructor
public class DocumentNumberTypeFeature implements FabFeature {

  static final String FEATURE_NAME = "features/isUaeNationalId";
  static final String DOCUMENT_TYPE = "UAE_NATIONAL_ID";

  @Override
  public Feature buildFeature(BuildFeatureCommand buildFeatureCommand) {
    return IsOfGivenDocumentTypeFeatureInputOut.builder()
        .feature(FEATURE_NAME)
        .documentTypes(of(DOCUMENT_TYPE))
        .documentNumber(getAlertedPart(buildFeatureCommand.getParsedMessageData()))
        .build();
  }

  private static String getAlertedPart(ParsedMessageData parsedMessageData) {
    return parsedMessageData.getNationalId();
  }
}
