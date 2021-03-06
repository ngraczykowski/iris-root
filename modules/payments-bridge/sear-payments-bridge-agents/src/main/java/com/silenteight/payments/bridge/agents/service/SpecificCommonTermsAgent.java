package com.silenteight.payments.bridge.agents.service;

import lombok.NonNull;

import com.silenteight.payments.bridge.agents.model.SpecificCommonTermsAgentResponse;
import com.silenteight.payments.bridge.agents.model.SpecificCommonTermsRequest;
import com.silenteight.payments.bridge.agents.port.SpecificCommonTermsUseCase;

import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
class SpecificCommonTermsAgent implements SpecificCommonTermsUseCase {

  private static final String COMPANY_REG = "(?i)((\\s|^)CO[\\s\\r\\n]+([.A-Z]+|2/))";
  private static final Pattern COMPANY_PATTERN = Pattern.compile(COMPANY_REG);
  private static final String CHAR_TO_BE_REPLACED = "(\\W|\\r|\\n|/.,]*)";
  private static final Pattern CHAR_TO_BE_REPLACED_PATTERN = Pattern.compile(CHAR_TO_BE_REPLACED);
  private static final String LINE_BREAKERS = "\r\n|\n|\r";
  private static final Pattern LINE_BREAKERS_PATTERN = Pattern.compile(LINE_BREAKERS);


  private static final List<String> COMPANY_NAMES =
      Arrays.asList("LIMITED", "LIM", "L", "LT", "LTD", "LLC", "2",
          "WLL", "KG", "AND", "PTE", "PTY", "PHAN", "POBOX", "MPANY", "INC", "P", "NO", "RIYADH",
          "UNICEF", "MAIN",
          "MMERCIAL", "BLDG");

  @NonNull
  public SpecificCommonTermsAgentResponse invoke(SpecificCommonTermsRequest request) {
    return checkIfFieldValueContainsSpecificCommonTerms(
        request.getAllMatchFieldsValue(), request.getIsAccountNumberFlagInMatchingField());
  }

  static SpecificCommonTermsAgentResponse checkIfFieldValueContainsSpecificCommonTerms(
      @NonNull String fieldValue, boolean isAccountNumberFlagInMatchingField) {
    return checkForCompany(fieldValue, isAccountNumberFlagInMatchingField ? 2 : 1);
  }

  @NonNull
  private static SpecificCommonTermsAgentResponse checkForCompany(String fieldValue, int i) {
    List<String> stringListAfterRemovingLineBreakers =
        convertStringToListOnLineBreakers(fieldValue);

    if (stringListAfterRemovingLineBreakers.size() <= i)
      return SpecificCommonTermsAgentResponse.NO;

    Matcher linerMatcher = getMatcher(stringListAfterRemovingLineBreakers.get(i));
    return linerMatcher.find() ? matchIsCompanyRelated(linerMatcher)
                               : SpecificCommonTermsAgentResponse.NO;
  }

  private static SpecificCommonTermsAgentResponse matchIsCompanyRelated(Matcher linerMatcher) {
    return secondTokenExtractor(linerMatcher) ? SpecificCommonTermsAgentResponse.NO
                                              : SpecificCommonTermsAgentResponse.YES;
  }

  @NonNull
  private static Matcher getMatcher(@NonNull String fieldValue) {
    return COMPANY_PATTERN.matcher(fieldValue);
  }

  private static List<String> convertStringToListOnLineBreakers(String fieldValue) {
    return Arrays.asList(LINE_BREAKERS_PATTERN.split(fieldValue));
  }

  private static boolean secondTokenExtractor(Matcher matcher) {
    String extracted = matcher.group(1);
    extracted = CHAR_TO_BE_REPLACED_PATTERN.matcher(extracted).replaceAll(" ")
        .strip().toUpperCase();
    extracted = extracted.substring(extracted.indexOf(' ') + 1);
    extracted = CHAR_TO_BE_REPLACED_PATTERN.matcher(extracted).replaceAll("").strip();
    return COMPANY_NAMES.contains(extracted);
  }
}
