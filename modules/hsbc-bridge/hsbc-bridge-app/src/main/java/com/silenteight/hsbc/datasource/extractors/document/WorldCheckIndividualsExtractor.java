package com.silenteight.hsbc.datasource.extractors.document;

import com.silenteight.hsbc.datasource.datamodel.WorldCheckIndividual;

import java.util.List;

class WorldCheckIndividualsExtractor extends IndividualDocumentExtractor {

  WorldCheckIndividualsExtractor(Document document) {
    super(document);
  }

  void extract(List<WorldCheckIndividual> worldCheckIndividuals) {

    worldCheckIndividuals.forEach(
        w -> {
          var passportNumber = w.getPassportNumber();

          extractIdNumbers(w.getIdNumbers());
          extractPassportNumbers(passportNumber);
          extractPassportCountries(passportNumber);
        });
  }
}
