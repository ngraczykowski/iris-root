/*
 * Copyright (c) 2022 Silent Eight Pte. Ltd. All rights reserved.
 */

package com.silenteight.hsbc.datasource.extractors.document;

import com.silenteight.hsbc.datasource.datamodel.NegativeNewsScreeningIndividuals;

import java.util.List;

public class NnsIndividualsExtractor extends IndividualDocumentExtractor {

  NnsIndividualsExtractor(Document document) {
    super(document);
  }

  void extract(List<NegativeNewsScreeningIndividuals> nnsIndividuals) {
    nnsIndividuals.forEach(
        nns -> {
          var passportNumber = nns.getPassportNumber();

          extractIdNumbers(nns.getIdNumbers());
          extractPassportNumbers(passportNumber);
          extractPassportCountries(passportNumber);
        });
  }
}
