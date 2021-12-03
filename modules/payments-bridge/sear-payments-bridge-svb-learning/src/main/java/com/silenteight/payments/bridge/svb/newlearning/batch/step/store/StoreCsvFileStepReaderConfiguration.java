
package com.silenteight.payments.bridge.svb.newlearning.batch.step.store;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import com.silenteight.payments.bridge.svb.learning.reader.domain.LearningRequest;
import com.silenteight.payments.bridge.svb.newlearning.batch.step.LoadCsvJobProperties;
import com.silenteight.payments.bridge.svb.newlearning.port.CsvFileResourceProvider;

import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.LineMapper;
import org.springframework.batch.item.file.builder.FlatFileItemReaderBuilder;
import org.springframework.batch.item.file.mapping.BeanWrapperFieldSetMapper;
import org.springframework.batch.item.file.mapping.DefaultLineMapper;
import org.springframework.batch.item.file.mapping.FieldSetMapper;
import org.springframework.batch.item.file.separator.DefaultRecordSeparatorPolicy;
import org.springframework.batch.item.file.transform.DelimitedLineTokenizer;
import org.springframework.batch.item.file.transform.LineTokenizer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.Resource;

import static com.silenteight.payments.bridge.svb.newlearning.batch.LearningJobConstants.BUCKET_NAME_PARAMETER;
import static com.silenteight.payments.bridge.svb.newlearning.batch.LearningJobConstants.FILE_NAME_PARAMETER;


@Configuration
@RequiredArgsConstructor
@EnableConfigurationProperties(LoadCsvJobProperties.class)
@Slf4j
class StoreCsvFileStepReaderConfiguration {

  private final LoadCsvJobProperties properties;
  private final CsvFileResourceProvider csvFileResourceProvider;

  @SuppressWarnings("SpringElInspection")
  @Bean
  @StepScope
  FlatFileItemReader<LearningCsvRowEntity> storeCsvFileStepItemReader(
      @Value("#{stepExecution}") StepExecution stepExecution) {
    var fileName =
        stepExecution.getJobParameters().getString(FILE_NAME_PARAMETER);
    var bucketName =
        stepExecution.getJobParameters().getString(BUCKET_NAME_PARAMETER);
    log.info("Step:{} file:{}", stepExecution.getStepName(), fileName);

    return new FlatFileItemReaderBuilder<LearningCsvRowEntity>()
        .name("csvFileItemReader")
        .encoding(properties.getFileEncoding())
        .resource(getFileResource(fileName, bucketName))
        // Default policy helps to parse multiline CSV files.
        .recordSeparatorPolicy(new DefaultRecordSeparatorPolicy())
        // First line is a header.
        .linesToSkip(1)
        .lineMapper(lineMapper())
        .build();
  }


  private Resource getFileResource(String fileName, String bucketName) {
    return csvFileResourceProvider.getResource(
        LearningRequest.builder().object(fileName)
            .bucket(bucketName)
            .build());
  }

  private static LineMapper<LearningCsvRowEntity> lineMapper() {
    DefaultLineMapper<LearningCsvRowEntity> lineMapper = new DefaultLineMapper<>();
    lineMapper.setLineTokenizer(lineTokenizer());
    lineMapper.setFieldSetMapper(informationMapper());
    return lineMapper;
  }

  private static FieldSetMapper<LearningCsvRowEntity> informationMapper() {
    BeanWrapperFieldSetMapper<LearningCsvRowEntity> informationMapper =
        new BeanWrapperFieldSetMapper<>();
    informationMapper.setTargetType(LearningCsvRowEntity.class);
    return informationMapper;
  }

  private static LineTokenizer lineTokenizer() {
    DelimitedLineTokenizer lineTokenizer = new DelimitedLineTokenizer();
    lineTokenizer.setDelimiter(",");
    lineTokenizer.setQuoteCharacter('"');
    lineTokenizer.setNames(CSV_HEADERS);
    return lineTokenizer;
  }

  private static final String[] CSV_HEADERS = {
      "FKCO_ID",
      "FKCO_V_SYSTEM_ID",
      "FKCO_V_FORMAT",
      "FKCO_V_TYPE",
      "FKCO_V_TRANSACTION_REF",
      "FKCO_V_RELATED_REF",
      "FKCO_V_SENS",
      "FKCO_V_BUSINESS_UNIT",
      "FKCO_V_APPLICATION",
      "FKCO_V_CURRENCY",
      "FKCO_F_AMOUNT",
      "FKCO_V_CONTENT",
      "FKCO_B_HIGHLIGHT_ALL",
      "FKCO_V_VALUE_DATE",
      "FKCO_UNIT",
      "FKCO_I_MSG_FML_PRIORITY",
      "FKCO_I_MSG_FML_CONFIDENTIALITY",
      "FKCO_D_APP_DEADLINE",
      "FKCO_I_APP_PRIORITY",
      "FKCO_I_NORMAMOUNT",
      "FKCO_V_MESSAGEID",
      "FKCO_V_COPY_SERVICE",
      "FKCO_V_ACTION_COMMENT",
      "FKCO_ACTION_DATE",
      "FKCO_D_FILTERED_DATETIME",
      "FKCO_D_ACTION_DATETIME",
      "FKCO_OPERATOR",
      "FKCO_STATUS",
      "FKCO_I_TOTAL_ACTION",
      "FKCO_MESSAGES",
      "FKCO_B_HIGHLIGHT_HIT",
      "FKCO_V_NAME_MATCHED_TEXT",
      "FKCO_V_ADDRESS_MATCHED_TEXT",
      "FKCO_V_CITY_MATCHED_TEXT",
      "FKCO_V_STATE_MATCHED_TEXT",
      "FKCO_V_COUNTRY_MATCHED_TEXT",
      "FKCO_V_LIST_MATCHED_NAME",
      "FKCO_V_FML_TYPE",
      "FKCO_I_FML_PRIORITY",
      "FKCO_I_FML_CONFIDENTIALITY",
      "FKCO_V_HIT_MATCH_LEVEL",
      "FKCO_V_HIT_TYPE",
      "FKCO_I_NONBLOCKING",
      "FKCO_I_BLOCKING",
      "FKCO_LISTED_RECORD",
      "FKCO_FILTERED_DATE",
      "FKCO_D_FILTERED_DATETIME_1",
      "FKCO_V_MATCHED_TAG",
      "FKCO_V_MATCHED_TAG_CONTENT",
      "FKCO_I_SEQUENCE",
      "FKCO_V_LIST_FMM_ID",
      "FKCO_V_LIST_OFFICIAL_REF",
      "FKCO_V_LIST_TYPE",
      "FKCO_V_LIST_ORIGIN",
      "FKCO_V_LIST_DESIGNATION",
      "FKCO_V_LIST_PEP",
      "FKCO_V_LIST_FEP",
      "FKCO_V_LIST_NAME",
      "FKCO_V_LIST_CITY",
      "FKCO_V_LIST_STATE",
      "FKCO_V_LIST_COUNTRY",
      "FKCO_V_LIST_USERDATA1",
      "FKCO_V_LIST_USERDATA2",
      "FKCO_V_LIST_KEYWORD",
      "FKCO_V_LIST_ADD_INFO",
      "FKCO_V_STATUS_NAME",
      "FKCO_V_STATUS_BEHAVIOR",
      "FKCO_I_BLOCKINGHITS"
  };
}
