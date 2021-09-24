package com.silenteight.hsbc.datasource.extractors.document;

import com.silenteight.hsbc.datasource.datamodel.CustomerIndividual;
import com.silenteight.hsbc.datasource.datamodel.IndividualComposite;

public class DocumentExtractor {

  public Document convertAlertedPartyDocumentNumbers(CustomerIndividual customerIndividual) {
    var document = new Document();
    var customerIndividualExtractor = new CustomerIndividualsExtractor(document);

    customerIndividualExtractor.extract(customerIndividual);
    return document;
  }

  public Document convertMatchedPartyDocumentNumbers(IndividualComposite individualComposite) {
    var document = new Document();

    if (individualComposite.hasWorldCheckIndividuals()) {
      var worldCheckIndividualsExtractor = new WorldCheckIndividualsExtractor(document);
      worldCheckIndividualsExtractor.extract(individualComposite.getWorldCheckIndividuals());
    }
    if (individualComposite.hasPrivateListIndividuals()) {
      var privateListIndividualsExtractor = new PrivateListIndividualsExtractor(document);
      privateListIndividualsExtractor.extract(individualComposite.getPrivateListIndividuals());
    }
    return document;
  }
}
