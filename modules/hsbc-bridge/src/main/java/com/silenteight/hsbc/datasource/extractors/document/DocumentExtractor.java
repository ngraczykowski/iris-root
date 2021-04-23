package com.silenteight.hsbc.datasource.extractors.document;

import com.silenteight.hsbc.bridge.domain.CustomerIndividuals;
import com.silenteight.hsbc.bridge.domain.IndividualComposite;
import com.silenteight.hsbc.bridge.domain.PrivateListIndividuals;
import com.silenteight.hsbc.bridge.domain.WorldCheckIndividuals;
import com.silenteight.hsbc.datasource.extractors.common.SimpleRegexBasedExtractor;
import com.silenteight.hsbc.datasource.extractors.country.NationalIdNumberFieldCountryExtractor;
import com.silenteight.hsbc.datasource.extractors.country.PassportNumberFieldCountryExtractor;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class DocumentExtractor {

  //FIXE (mmrowka) this should be configurable from properties
  private static final String PASSPORT_CODE = "\"P\"";
  private static final String NATIONAL_ID_CODE = "\"NID\"";
  private static final Pattern DOCUMENT_ID_PATTERN = Pattern.compile(".*ID$");
  private static final Pattern OTHER_DOCUMENT_ID_PATTERN = Pattern.compile(".*(?<!ID)$");
  private static final Pattern BASE_PATTERN = Pattern.compile("\"(.*)\"");
  private static final Pattern DOCUMENT_GROUP = Pattern.compile("^(.*) \\((.*)\\)$");
  private static final Pattern EDQ_SUFFIX = Pattern.compile("\"^\\s*[^0-9]+\\s*$\"");

  public Document convertAlertedPartyDocumentNumbers(CustomerIndividuals customerIndividuals) {
    var document = new Document();

    extractCustomerIndividualsIdentificationDocument(
        customerIndividuals, document);

    return document;
  }

  public Document convertMatchedPartyDocumentNumbers(IndividualComposite individualComposite) {
    var document = new Document();

    extractWorldCheckIndividualsDocuments(
        individualComposite.getWorldCheckIndividuals(), document);

    extractPrivateListIndividualsDocument(
        individualComposite.getPrivateListIndividuals(), document);

    return document;
  }

  private void extractCustomerIndividualsIdentificationDocument(
      CustomerIndividuals customerIndividuals, Document document) {

    Stream.of(
        customerIndividuals.getIdentificationDocument1(),
        customerIndividuals.getIdentificationDocument2(),
        customerIndividuals.getIdentificationDocument3(),
        customerIndividuals.getIdentificationDocument4(),
        customerIndividuals.getIdentificationDocument5(),
        customerIndividuals.getIdentificationDocument6(),
        customerIndividuals.getIdentificationDocument7(),
        customerIndividuals.getIdentificationDocument8(),
        customerIndividuals.getIdentificationDocument9(),
        customerIndividuals.getIdentificationDocument10())
        .filter(Objects::nonNull)
        .forEach(i -> extractCustomerIndividualsDocuments(document, i));
  }

  private void extractCustomerIndividualsDocuments(Document document, String i) {
    var matcher = BASE_PATTERN.matcher(i);
    if (matcher.find()) {
      var group = matcher.group(0);

      var split = group.split(",");

      if (split[0].equals(PASSPORT_CODE)) {
        document.addPassportNumber(split[1].replace("\"", ""));
      } else if (split[0].equals(NATIONAL_ID_CODE)) {
        document.addNationalIdNumber(split[1].replace("\"", ""));
      } else if (!Arrays.asList(PASSPORT_CODE, NATIONAL_ID_CODE).contains(split[0])) {
        document.addOtherDocumentNumber(split[1].replace("\"", ""));
      }
    }
  }

  private void extractWorldCheckIndividualsDocuments(
      List<WorldCheckIndividuals> worldCheckIndividuals, Document document) {

    worldCheckIndividuals.forEach(w -> {
      var splitPassportNumbers = w.getPassportNumber().split(";");

      Arrays.stream(splitPassportNumbers)
          .map(PassportNumberFieldCountryExtractor::new)
          .map(SimpleRegexBasedExtractor::extract)
          .filter(Optional::isPresent)
          .map(Optional::get)
          .forEach(document::addPassportCountry);

      Arrays.stream(splitPassportNumbers)
          .map(this::extractDocumentId)
          .forEach(document::addPassportNumber);

      var splitIdNumbers = w.getIdNumbers().split("\\|");

      Arrays.stream(splitIdNumbers)
          .map(NationalIdNumberFieldCountryExtractor::new)
          .map(SimpleRegexBasedExtractor::extract)
          .filter(Optional::isPresent)
          .map(Optional::get)
          .forEach(document::addNationalIdCountry);

      var extractedIdNumbers = Arrays.stream(splitIdNumbers)
          .map(this::extractDocumentId)
          .collect(Collectors.toList());

      extractedIdNumbers
          .stream()
          .filter(i -> DOCUMENT_ID_PATTERN.matcher(i).find())
          .forEach(document::addNationalIdNumber);

      extractedIdNumbers
          .stream()
          .filter(i -> OTHER_DOCUMENT_ID_PATTERN.matcher(i).find())
          .forEach(document::addOtherDocumentNumber);
    });
  }

  private void extractPrivateListIndividualsDocument(
      List<PrivateListIndividuals> privateListIndividuals, Document document) {
    privateListIndividuals.forEach(x -> extractPrivateIndividual(x, document));
  }

  private void extractPrivateIndividual(
      PrivateListIndividuals individual, Document document) {

    var edqSuffix = extractEdqSuffix(individual.getEdqSuffix());

    Stream.of(individual.getPassportNumber()
        .split("[,;]"))
        .map(v -> v.split("[,;]"))
        .flatMap(Stream::of)
        .map(this::extractDocumentId)
        .map(k -> k.split(";")[0])
        .filter(e -> edqSuffix.isPresent() && !edqSuffix.get().equals(e))
        .forEach(document::addPassportNumber);

    Stream.of(individual.getNationalId()
        .split(","))
        .filter(e -> edqSuffix.isPresent() && !edqSuffix.get().equals(e))
        .forEach(document::addNationalIdNumber);

    document.addOtherDocumentNumber(individual.getEdqDrivingLicence());
    document.addOtherDocumentNumber(individual.getEdqTaxNumber());
    edqSuffix.ifPresent(document::addOtherDocumentNumber);

  }

  private String extractDocumentId(String documentId) {
    var matcher = DOCUMENT_GROUP.matcher(documentId);
    if (matcher.find()) {
      return matcher.group(1);
    }
    return documentId;
  }

  private Optional<String> extractEdqSuffix(String documentId) {
    var matcher = EDQ_SUFFIX.matcher(documentId);
    if (!matcher.find()) {
      return Optional.of(documentId);
    }
    return Optional.empty();
  }
}


