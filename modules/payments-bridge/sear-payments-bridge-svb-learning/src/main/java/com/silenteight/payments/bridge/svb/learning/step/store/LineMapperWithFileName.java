package com.silenteight.payments.bridge.svb.learning.step.store;

import lombok.RequiredArgsConstructor;

import org.springframework.batch.item.file.LineMapper;
import org.springframework.batch.item.file.mapping.FieldSetMapper;
import org.springframework.batch.item.file.transform.LineTokenizer;

@RequiredArgsConstructor
public class LineMapperWithFileName implements LineMapper<LearningCsvRowEntity> {

  private final String fileName;
  private final LineTokenizer tokenizer;
  private final FieldSetMapper<LearningCsvRowEntity> fieldSetMapper;

  @Override
  public LearningCsvRowEntity mapLine(String line, int lineNumber) throws Exception {
    LearningCsvRowEntity csvRowEntity = fieldSetMapper.mapFieldSet(tokenizer.tokenize(line));
    csvRowEntity.setFileName(fileName);
    return csvRowEntity;
  }

}
