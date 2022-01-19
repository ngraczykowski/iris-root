package com.silenteight.warehouse.report.reporting;

import com.silenteight.sep.auth.token.UserAwareTokenProvider;
import com.silenteight.warehouse.common.domain.country.CountryPermissionService;
import com.silenteight.warehouse.indexer.query.streaming.DataProvider;
import com.silenteight.warehouse.indexer.query.streaming.FetchDataRequest;
import com.silenteight.warehouse.indexer.query.streaming.FieldDefinition;
import com.silenteight.warehouse.indexer.query.streaming.ReportFieldDefinitions;
import com.silenteight.warehouse.report.sql.SqlExecutor;
import com.silenteight.warehouse.report.sql.dto.SqlExecutorDto;
import com.silenteight.warehouse.report.storage.ReportStorage;
import com.silenteight.warehouse.report.storage.temporary.TemporaryFileStorage;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.Collections;
import java.util.List;
import java.util.Set;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ReportGenerationServiceTest {

  private static final String EXISTING_GROUP_UUID_2 = "123e4567-e89b-42d3-a456-556642441235";
  private static final String NOT_EXISTING_GROUP_UUID = "123e4567-e89b-42d3-a456-556642441111";

  @Mock
  private DataProvider dataProvider;
  @Mock
  private TemporaryFileStorage temporaryFileStorage;
  @Mock
  private ReportStorage reportStorage;
  @Mock
  private SqlExecutor sqlExecutor;
  @Mock
  private UserAwareTokenProvider userAwareTokenProvider;
  @Mock
  private CountryPermissionService countryPermissionService;

  @Captor
  ArgumentCaptor<SqlExecutorDto> sqlDtoArgumentCaptor;

  private ReportGenerationService underTest;

  @BeforeEach
  void setUp() {
    underTest = new ReportGenerationService(dataProvider,
        temporaryFileStorage, reportStorage, sqlExecutor,
        userAwareTokenProvider, countryPermissionService);
  }

  @Test
  public void shouldHandleSqlReportAndAddParameters() {
    when(userAwareTokenProvider.getRolesForGroup("kibana-sso"))
        .thenReturn(Set.of(EXISTING_GROUP_UUID_2));
    when(countryPermissionService.getCountries(Set.of(EXISTING_GROUP_UUID_2)))
        .thenReturn(Set.of("MX"));

    //given:
    FetchDataRequest request = FetchDataRequest.builder()
        .useSqlReports(true)
        .sqlTemplates(
            List.of("select1 * from warehouse_alert "
                    + "where created_at BETWEEN DATE '${from}' AND DATE '${to}'",
                "select2 * from warehouse_alert "
                    + "where created_at BETWEEN DATE '${from}' AND DATE '${to}'"))
        .selectSqlQuery("select * from warehouse_alert "
            + "where created_at BETWEEN DATE '${from}' AND DATE '${to}' "
            + "AND countryGroup IN (${countries})")
        .fieldsDefinitions(ReportFieldDefinitions.builder()
            .fieldDefinitions(List.of(FieldDefinition.builder()
                .name("name")
                .label("label")
                .build()))
            .build())
        .dateField("")
        .from(OffsetDateTime.of(2021, 11, 1, 12,
            12, 12, 12, ZoneOffset.UTC))
        .to(OffsetDateTime.of(2021, 12, 1, 12,
            12, 12, 12, ZoneOffset.UTC))
        .indexes(Collections.emptyList())
        .name("name")
        .build();
    //when:
    underTest.generate(request);

    //then:
    verify(sqlExecutor).execute(sqlDtoArgumentCaptor.capture(), any());
    assertThat(sqlDtoArgumentCaptor.getValue()
        .getSelectSqlStatement())
        .isEqualTo("select * from warehouse_alert "
            + "where created_at BETWEEN DATE '2021-11-01' AND DATE '2021-12-01' "
            + "AND countryGroup IN ('MX')");
    assertThat(sqlDtoArgumentCaptor.getValue()
        .getPrepareDataSqlStatements()
        .get(0))
        .isEqualTo("select1 * from warehouse_alert "
            + "where created_at BETWEEN DATE '2021-11-01' AND DATE '2021-12-01'");
    assertThat(sqlDtoArgumentCaptor.getValue()
        .getPrepareDataSqlStatements()
        .get(1))
        .isEqualTo("select2 * from warehouse_alert "
            + "where created_at BETWEEN DATE '2021-11-01' AND DATE '2021-12-01'");
  }

  @Test
  public void shouldHandleSqlReportAndAddEmptyParameters() {
    when(userAwareTokenProvider.getRolesForGroup("kibana-sso"))
        .thenReturn(Set.of(NOT_EXISTING_GROUP_UUID));
    when(countryPermissionService.getCountries(Set.of(NOT_EXISTING_GROUP_UUID)))
        .thenReturn(Collections.emptySet());

    //given:
    FetchDataRequest request = FetchDataRequest.builder()
        .useSqlReports(true)
        .selectSqlQuery("select * from warehouse_alert "
            + "where created_at BETWEEN DATE '${from}' AND DATE '${to}' "
            + "AND countryGroup IN (${countries})")
        .fieldsDefinitions(ReportFieldDefinitions.builder()
            .fieldDefinitions(List.of(FieldDefinition.builder()
                .name("name")
                .label("label")
                .build()))
            .build())
        .dateField("")
        .from(OffsetDateTime.of(2021, 11, 1, 12,
            12, 12, 12, ZoneOffset.UTC))
        .to(OffsetDateTime.of(2021, 12, 1, 12,
            12, 12, 12, ZoneOffset.UTC))
        .indexes(Collections.emptyList())
        .name("name")
        .build();
    //when:
    underTest.generate(request);

    //then:
    verify(sqlExecutor).execute(sqlDtoArgumentCaptor.capture(), any());
    assertThat(sqlDtoArgumentCaptor.getValue()
        .getSelectSqlStatement())
        .isEqualTo("select * from warehouse_alert "
            + "where created_at BETWEEN DATE '2021-11-01' AND DATE '2021-12-01' "
            + "AND countryGroup IN ('')");
  }

  @Test
  public void shouldHandleEsReports() {
    //given:
    FetchDataRequest request = FetchDataRequest.builder()
        .useSqlReports(false)
        .sqlTemplates(
            List.of("select1 * from warehouse_alert "
                    + "where created_at BETWEEN DATE '${from}' AND DATE '${to}'",
                "select2 * from warehouse_alert "
                    + "where created_at BETWEEN DATE '${from}' AND DATE '${to}'"))
        .selectSqlQuery("select * from warehouse_alert "
            + "where created_at BETWEEN DATE '${from}' AND DATE '${to}'")
        .fieldsDefinitions(ReportFieldDefinitions.builder()
            .fieldDefinitions(List.of(FieldDefinition.builder()
                .name("name")
                .label("label")
                .build()))
            .build())
        .dateField("")
        .from(OffsetDateTime.of(2021, 11, 1, 12,
            12, 12, 12, ZoneOffset.UTC))
        .to(OffsetDateTime.of(2021, 12, 1, 12,
            12, 12, 12, ZoneOffset.UTC))
        .indexes(Collections.emptyList())
        .name("report name")
        .build();
    //when:
    underTest.generate(request);

    //then:
    verify(temporaryFileStorage).doOnTempFile(any(), any());
  }
}