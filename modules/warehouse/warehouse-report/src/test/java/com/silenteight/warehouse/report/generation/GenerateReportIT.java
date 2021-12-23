package com.silenteight.warehouse.report.generation;

import lombok.SneakyThrows;

import com.silenteight.sep.base.testing.BaseDataJpaTest;
import com.silenteight.sep.filestorage.api.StorageManager;
import com.silenteight.sep.filestorage.api.dto.FileDto;
import com.silenteight.sep.filestorage.minio.container.MinioContainer.MinioContainerInitializer;
import com.silenteight.warehouse.report.generation.dto.GenerateReportDto;
import com.silenteight.warehouse.report.sql.dto.SqlExecutorDto;
import com.silenteight.warehouse.report.storage.ReportStorage;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;

import java.sql.Connection;
import java.sql.Statement;
import java.util.List;
import javax.sql.DataSource;

import static com.silenteight.warehouse.report.generation.GenerateReportFixtures.*;
import static java.nio.charset.StandardCharsets.UTF_8;
import static java.util.stream.Collectors.toList;
import static java.util.stream.Stream.of;
import static org.assertj.core.api.Assertions.*;

@ContextConfiguration(classes = { GenerateReportTestConfiguration.class },
    initializers = { MinioContainerInitializer.class }
)
class GenerateReportIT extends BaseDataJpaTest {

  @Autowired
  private StorageManager storageManager;

  @Autowired
  private ReportStorage reportStorage;

  @Autowired
  private GenerateReportService generateReportService;

  @Autowired
  private DataSource dataSource;

  @BeforeEach
  public void init() {
    prepareData();
    createMinioBucket();
  }

  @AfterEach
  public void cleanup() {
    clearMinioBucket();
    clearDatabase();
  }

  @SneakyThrows
  @Test
  void shouldCopyOutReportToMinio() {
    // given
    GenerateReportDto generateReportDto = toGenerateReportDto(REPORT_STORAGE_NAME);

    // when
    generateReportService.generate(generateReportDto);

    // then
    List<String> report = getReportContentFromMinio(REPORT_STORAGE_NAME);
    assertThat(report).hasSize(6);
  }

  @SneakyThrows
  private List<String> getReportContentFromMinio(String reportName) {
    FileDto report = reportStorage.getReport(reportName);
    byte[] responseBytes = report.getContent().readAllBytes();
    return of(new String(responseBytes, UTF_8).split("\\n"))
        .map(String::trim)
        .collect(toList());
  }

  private GenerateReportDto toGenerateReportDto(String reportStorageName) {
    return GenerateReportDto.builder()
        .sqlExecutorDto(toSqlExecutorDto())
        .reportName(reportStorageName)
        .build();
  }

  private SqlExecutorDto toSqlExecutorDto() {
    return SqlExecutorDto.builder()
        .prepareDataSqlStatements(PREPARE_DATA_SQL_STATEMENTS)
        .selectSqlStatement(SELECT_SQL)
        .build();
  }

  private void prepareData() {
    executeSqlStatement(CREATE_TABLE_WITH_RANDOM_DATA_SQL);
  }

  private void createMinioBucket() {
    storageManager.create(TEST_BUCKET);
  }

  private void clearMinioBucket() {
    reportStorage.removeReport(REPORT_STORAGE_NAME);
    storageManager.remove(TEST_BUCKET);
  }

  private void clearDatabase() {
    executeSqlStatement(DROP_TABLE_SQL);
  }

  @SneakyThrows
  private void executeSqlStatement(String sqlStatement) {
    Connection connection = dataSource.getConnection();
    Statement statement = connection.createStatement();
    statement.execute(sqlStatement);
  }
}
