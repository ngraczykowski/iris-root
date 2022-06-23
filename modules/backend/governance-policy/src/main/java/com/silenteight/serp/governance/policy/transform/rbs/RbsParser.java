package com.silenteight.serp.governance.policy.transform.rbs;

import lombok.RequiredArgsConstructor;

import com.silenteight.serp.governance.policy.transform.rbs.StepsData.Feature;
import com.silenteight.serp.governance.policy.transform.rbs.StepsData.Step;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVFormat.Builder;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.List;
import javax.validation.Valid;

import static com.silenteight.serp.governance.policy.domain.dto.Solution.valueOf;
import static java.util.stream.Collectors.toList;
import static java.util.stream.Collectors.toUnmodifiableList;

@RequiredArgsConstructor
class RbsParser {

  private static final String CATEGORIES_PREFIX = "categories/";
  private static final String FEATURE_PREFIX = "features/";
  private static final String SOLUTION_HEADER = "recommended_action";
  private static final String REASONING_BRANCH_ID_HEADER = "reasoning_branch_id";
  private static final char DELIMITER = ',';

  private static final CSVFormat CSV_FORMAT = Builder.create()
      .setDelimiter(DELIMITER)
      .setHeader()
      .setSkipHeaderRecord(true)
      .setTrim(true)
      .build();

  @Valid
  private final RbsImportProperties rbsImportProperties;

  StepsData parse(InputStream inputStream, String fileName) {
    try {
      Reader reader = new InputStreamReader(inputStream);
      CSVParser csvParser = CSV_FORMAT.parse(reader);

      List<String> featureHeaders = getFeatureHeaders(csvParser.getHeaderNames());
      if (featureHeaders.isEmpty()) {
        throw new RbsParserException("Missing feature headers");
      }

      return StepsData.builder()
          .name(fileName)
          .steps(createSteps(csvParser, featureHeaders))
          .build();

    } catch (IOException e) {
      throw new RbsParserException(e);
    }
  }

  private static List<String> getFeatureHeaders(List<String> allHeaders) {
    return allHeaders.stream()
        .filter(header -> header.startsWith(CATEGORIES_PREFIX) || header.startsWith(FEATURE_PREFIX))
        .collect(toUnmodifiableList());
  }

  private List<Step> createSteps(CSVParser csvParser, List<String> featureHeaders) {
    return csvParser.stream()
        .filter(record -> isSupportedSolution(record.get(SOLUTION_HEADER)))
        .map(record -> Step.builder()
            .solution(valueOf(record.get(SOLUTION_HEADER)))
            .reasoningBranchId(record.get(REASONING_BRANCH_ID_HEADER))
            .features(createFeatures(record, featureHeaders))
            .build())
        .collect(toList());
  }

  private static List<Feature> createFeatures(CSVRecord record, List<String> featureHeaders) {
    return featureHeaders.stream()
        .map(header -> new Feature(header, record.get(header)))
        .collect(toList());
  }

  private boolean isSupportedSolution(String solution) {
    return rbsImportProperties.getSolutions().contains(solution);
  }
}
