package com.silenteight.hsbc.datasource.extractors.document;

import lombok.RequiredArgsConstructor;

import com.silenteight.hsbc.datasource.datamodel.CustomerIndividual;

import java.util.List;
import java.util.Objects;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@RequiredArgsConstructor
class CustomerIndividualsExtractor {

  private static final String PASSPORT_CODE = "P";
  private static final String NATIONAL_ID_CODE = "NID";
  private static final Pattern BASE_PATTERN = Pattern.compile("\"(.*)\"");
  private static final Pattern OTHER_DOCUMENT_PATTERN = Pattern.compile("(?i)[\\w\\s]+ number");
  private final Document document;

  void extract(List<CustomerIndividual> customerIndividuals) {
    customerIndividuals.stream()
        .flatMap(customerIndividual -> Stream.of(
            customerIndividual.getIdentificationDocument1(),
            customerIndividual.getIdentificationDocument2(),
            customerIndividual.getIdentificationDocument3(),
            customerIndividual.getIdentificationDocument4(),
            customerIndividual.getIdentificationDocument5(),
            customerIndividual.getIdentificationDocument6(),
            customerIndividual.getIdentificationDocument7(),
            customerIndividual.getIdentificationDocument8(),
            customerIndividual.getIdentificationDocument9(),
            customerIndividual.getIdentificationDocument10()))
        .filter(Objects::nonNull)
        .forEach(this::extractCustomerIndividualsDocuments);
  }

  private void extractCustomerIndividualsDocuments(String identificationDocument) {
    var matcher = BASE_PATTERN.matcher(identificationDocument);

    if (matcher.find()) {
      var filteredData = matcher.group(0).replace("\"", "");
      var documentsData = Stream.of(filteredData.split(","))
          .map(String::trim)
          .collect(Collectors.toList());
      if (documentsData.size() > 1) {
        addToDocument(documentsData);
      }
    }
  }

  private void addToDocument(List<String> data) {
    var documentType = data.get(0);
    var documentValue = data.get(1);

    switch (documentType) {
      case PASSPORT_CODE: {
        document.addPassportNumber(documentValue);
        break;
      }
      case NATIONAL_ID_CODE: {
        document.addNationalIdNumber(documentValue);
        if (data.size() > 2) {
          document.addDocumentIdentificationCountry(data.get(2));
        }
        break;
      }
      default: {
        extractOtherDocument(documentValue);
      }
    }
  }

  private void extractOtherDocument(String otherDocument) {
    var matcher = OTHER_DOCUMENT_PATTERN.matcher(otherDocument);
    if (matcher.find()) {
      var result = matcher.group(0);
      document.addOtherDocumentNumber(result);
    }
  }
}
