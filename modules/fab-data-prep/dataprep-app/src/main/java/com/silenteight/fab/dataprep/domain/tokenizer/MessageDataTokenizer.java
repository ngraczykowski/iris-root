package com.silenteight.fab.dataprep.domain.tokenizer;

import lombok.extern.slf4j.Slf4j;

import com.silenteight.fab.dataprep.domain.ex.DataPrepException;
import com.silenteight.fab.dataprep.domain.model.ParsedMessageData;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.dataformat.csv.CsvMapper;
import com.fasterxml.jackson.dataformat.csv.CsvSchema;
import com.google.common.base.CharMatcher;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Service;

import static com.silenteight.fab.dataprep.domain.tokenizer.TokenizerConfiguration.SEPARATOR;

@Slf4j
@Service
public class MessageDataTokenizer implements Converter<String, ParsedMessageData> {

  static final int NUMBER_OF_SEGMENTS = 28;
  private final CsvMapper mapper = new CsvMapper();

  @Override
  public ParsedMessageData convert(String source) {
    int numberOfSegments = CharMatcher.is(SEPARATOR).countIn(source) + 1;
    CsvSchema schema = getConfiguration(numberOfSegments).getConfiguration();

    try {
      return mapper.readerFor(ParsedMessageData.class)
          .with(schema)
          .readValue(source);
    } catch (JsonProcessingException e) {
      throw new DataPrepException(e);
    }
  }

  private static TokenizerConfiguration getConfiguration(int numberOfSegments) {
    if (numberOfSegments == NUMBER_OF_SEGMENTS) {
      return new AllFieldsTokenizerConfiguration();
    } else if (numberOfSegments == NUMBER_OF_SEGMENTS - 1) {
      return new WithoutShortNameTokenizerConfiguration();
    } else {
      throw new IllegalArgumentException(
          "Alert payload should contains " + NUMBER_OF_SEGMENTS + " segments separated with "
              + SEPARATOR);
    }
  }
}
