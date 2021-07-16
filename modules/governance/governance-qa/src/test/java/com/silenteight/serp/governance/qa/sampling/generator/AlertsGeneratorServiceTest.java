package com.silenteight.serp.governance.qa.sampling.generator;

import com.silenteight.model.api.v1.AlertsDistributionServiceProto.AlertDistribution;
import com.silenteight.model.api.v1.AlertsDistributionServiceProto.Distribution;
import com.silenteight.model.api.v1.SampleAlertServiceProto.RequestedAlertsFilter;
import com.silenteight.serp.governance.qa.manage.analysis.create.CreateAlertWithDecisionUseCase;
import com.silenteight.serp.governance.qa.manage.domain.dto.CreateDecisionRequest;
import com.silenteight.serp.governance.qa.sampling.domain.AlertSamplingService;
import com.silenteight.serp.governance.qa.sampling.domain.dto.DateRangeDto;
import com.silenteight.serp.governance.qa.sampling.generator.dto.AlertDistributionDto;
import com.silenteight.serp.governance.qa.sampling.generator.dto.DistributionDto;
import com.silenteight.serp.governance.qa.sampling.generator.dto.GetAlertsSampleRequest;
import com.silenteight.serp.governance.qa.send.SendAlertMessageCommand;
import com.silenteight.serp.governance.qa.send.SendAlertMessageUseCase;
import com.silenteight.serp.governance.qa.send.dto.AlertDto;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

import static com.silenteight.serp.governance.qa.AlertFixture.generateDiscriminator;
import static com.silenteight.serp.governance.qa.manage.domain.DecisionLevel.ANALYSIS;
import static com.silenteight.serp.governance.qa.manage.domain.DecisionState.NEW;
import static com.silenteight.serp.governance.qa.sampling.generator.RiskType.PEP;
import static com.silenteight.serp.governance.qa.sampling.generator.RiskType.RISK_TYPE;
import static com.silenteight.serp.governance.qa.sampling.generator.RiskType.SANCTION;
import static com.silenteight.serp.governance.qa.sampling.generator.dto.DistributionDtoFixture.getDistributionDto;
import static java.time.OffsetDateTime.parse;
import static java.util.List.of;
import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

@SpringBootTest(classes = {TestSamplingGeneratorConfiguration.class})
@RunWith(SpringRunner.class)
class AlertsGeneratorServiceTest {

  @MockBean
  private DistributionProvider distributionProvider;

  @MockBean
  private AlertProvider alertProvider;

  @MockBean
  private CreateAlertWithDecisionUseCase createAlertWithDecisionUseCase;

  @Autowired
  ApplicationEventPublisher eventPublisher;

  private final List<String> groupingFields = GroupingFields.valuesAsStringList();

  @Autowired
  private AlertsGeneratorService underTest;

  private DateRangeDto dateRangeDto;

  @MockBean
  private AlertSamplingService alertSamplingService;

  @Autowired
  private SendAlertMessageUseCase sendAlertMessageUseCase;

  @Captor
  private ArgumentCaptor<List<String>> groupingFieldsCaptor;

  @Captor
  private ArgumentCaptor<List<AlertDistributionDto>> alertDistributionsCaptor;

  @Captor
  private ArgumentCaptor<SendAlertMessageCommand> messageCommandCaptor;

  @BeforeEach
  void setUp() {
    dateRangeDto = new DateRangeDto(parse("2021-05-01T01:00:00Z"), parse("2021-05-31T01:00:00Z"));
  }

  @Test
  void generateAlertsShouldCallDistributionProviderWithDateRangeAndGroupingFields() {
    //given
    ArgumentCaptor<DateRangeDto> dateRangeDtoCaptor = ArgumentCaptor.forClass(DateRangeDto.class);
    when(distributionProvider.getDistribution(dateRangeDto, groupingFields))
        .thenReturn(of(getAlertDistribution(1, RISK_TYPE, PEP)));
    //when
    underTest.generateAlerts(dateRangeDto, 1L);
    //then
    verify(distributionProvider, times(1))
        .getDistribution(dateRangeDtoCaptor.capture(), groupingFieldsCaptor.capture());
    assertThat(dateRangeDtoCaptor.getValue()).isEqualTo(dateRangeDto);
    assertThat(groupingFieldsCaptor.getAllValues()).containsExactly(groupingFields);
  }

  @Test
  void generateAlertsShouldCallAlertProviderWithGetAlertsSampleRequest() {
    //given
    ArgumentCaptor<GetAlertsSampleRequest> alertsSampleRequestCaptor =
        ArgumentCaptor.forClass(GetAlertsSampleRequest.class);
    when(distributionProvider.getDistribution(dateRangeDto, groupingFields))
        .thenReturn(of(
            getAlertDistribution(1400, RISK_TYPE, PEP),
            getAlertDistribution(1200, RISK_TYPE, SANCTION)));
    //when
    underTest.generateAlerts(dateRangeDto, 1L);
    //then
    verify(alertProvider, times(2))
        .getAlerts(alertsSampleRequestCaptor.capture());
    assertThat(alertsSampleRequestCaptor.getValue().getDateRangeDto()).isEqualTo(dateRangeDto);
    assertThat(alertsSampleRequestCaptor.getAllValues().get(0).getLimit()).isEqualTo(245);
    assertThat(alertsSampleRequestCaptor.getAllValues().get(1).getLimit()).isEqualTo(210);
    assertThat(alertsSampleRequestCaptor.getAllValues().get(0).getRequestedAlertsFilters())
        .containsExactly(getRequestedAlertsFilter(RISK_TYPE, PEP));
    assertThat(alertsSampleRequestCaptor.getAllValues().get(1).getRequestedAlertsFilters())
        .containsExactly(getRequestedAlertsFilter(RISK_TYPE, SANCTION));
  }

  private RequestedAlertsFilter getRequestedAlertsFilter(String fieldName, String fieldValue) {
    return RequestedAlertsFilter
        .newBuilder()
        .setFieldName(fieldName)
        .setFieldValue(fieldValue)
        .build();
  }

  private AlertDistribution getAlertDistribution(
      Integer alertCount, String fieldName, String fieldValue) {

    return AlertDistribution
        .newBuilder()
        .addGroupingFields(getDistribution(fieldName, fieldValue))
        .setAlertCount(alertCount)
        .build();
  }

  private AlertDistributionDto getAlertDistributionDto(
      int alertsCount, List<DistributionDto> distributions) {

    return AlertDistributionDto.builder()
        .alertsCount(alertsCount)
        .distributions(distributions)
        .build();
  }

  private Distribution getDistribution(String fieldName, String fieldValue) {
    return Distribution
        .newBuilder()
        .setFieldName(fieldName)
        .setFieldValue(fieldValue)
        .build();
  }

  @Test
  void generateAlertsShouldSaveTwoAlertsInAnalysisLevelAndNewState() {
    //given
    final ArgumentCaptor<CreateDecisionRequest> createDecisionRequestCaptor
        = ArgumentCaptor.forClass(CreateDecisionRequest.class);
    final ArgumentCaptor<Long> alertSamplingId = ArgumentCaptor.forClass(Long.class);
    final ArgumentCaptor<Integer> alertsCount = ArgumentCaptor.forClass(Integer.class);
    final String discriminatorFirst = generateDiscriminator();
    final String discriminatorSecond = generateDiscriminator();
    final List<AlertDistribution> distributions = of(
        getAlertDistribution(1, RISK_TYPE, PEP),
        getAlertDistribution(1, RISK_TYPE, SANCTION));
    final List<AlertDistributionDto> distributionDtos = of(
        getAlertDistributionDto(1, of(getDistributionDto(RISK_TYPE, PEP))),
        getAlertDistributionDto(1, of(getDistributionDto(RISK_TYPE, SANCTION)))
    );
    when(distributionProvider.getDistribution(dateRangeDto, groupingFields))
        .thenReturn(distributions);
    when(alertProvider.getAlerts(getAlertsSampleRequest(PEP)))
        .thenReturn(of(discriminatorFirst));
    when(alertProvider.getAlerts(getAlertsSampleRequest(SANCTION)))
        .thenReturn(of(discriminatorSecond));
    //when
    underTest.generateAlerts(dateRangeDto, 1L);
    //then
    verify(createAlertWithDecisionUseCase, times(2))
        .activate(createDecisionRequestCaptor.capture());
    assertThat(createDecisionRequestCaptor.getAllValues().get(0).getDiscriminator())
        .isEqualTo(discriminatorFirst);
    assertThat(createDecisionRequestCaptor.getAllValues().get(1).getDiscriminator())
        .isEqualTo(discriminatorSecond);
    assertThat(createDecisionRequestCaptor.getValue().getCreatedBy())
        .isEqualTo(underTest.getClass().getSimpleName());
    assertThat(createDecisionRequestCaptor.getValue().getLevel())
        .isEqualTo(ANALYSIS);
    assertThat(createDecisionRequestCaptor.getValue().getState())
        .isEqualTo(NEW);
    verify(alertSamplingService, times(1))
        .saveAlertDistribution(
            alertSamplingId.capture(), alertDistributionsCaptor.capture(), alertsCount.capture());
    assertThat(alertSamplingId.getValue()).isEqualTo(1L);
    assertThat(alertDistributionsCaptor.getValue()).isEqualTo(distributionDtos);
    assertThat(alertsCount.getValue()).isEqualTo(2);
  }

  private GetAlertsSampleRequest getAlertsSampleRequest(String fieldValue) {
    return GetAlertsSampleRequest.of(
        dateRangeDto, of(getDistribution(RISK_TYPE, fieldValue)), 228L);
  }

  @Test
  void generateAlertsShouldNotCallGetAlertsWhenTotalAlertCountIsZero() {
    when(distributionProvider.getDistribution(dateRangeDto, groupingFields))
        .thenReturn(of(
            getAlertDistribution(0, RISK_TYPE, PEP),
            getAlertDistribution(0, RISK_TYPE, SANCTION)));
    //when
    underTest.generateAlerts(dateRangeDto, 1L);
    //then
    verify(alertProvider, never()).getAlerts(any());
    verify(createAlertWithDecisionUseCase, never()).activate(any());
    verify(alertSamplingService, never()).saveAlertDistribution(any(), any(), any());
  }

  @Test
  void generateAlertsShouldSendSendAlertMessageCommandWithTwoAlerts() {
    //given
    when(distributionProvider.getDistribution(dateRangeDto, groupingFields))
        .thenReturn(of(
            getAlertDistribution(1, RISK_TYPE, PEP),
            getAlertDistribution(1, RISK_TYPE, SANCTION)));
    String discriminatorFirst = generateDiscriminator();
    String discriminatorSecond = generateDiscriminator();
    when(alertProvider.getAlerts(getAlertsSampleRequest(PEP)))
        .thenReturn(of(discriminatorFirst));
    when(alertProvider.getAlerts(getAlertsSampleRequest(SANCTION)))
        .thenReturn(of(discriminatorSecond));
    //when
    underTest.generateAlerts(dateRangeDto, 1L);
    //then
    verify(sendAlertMessageUseCase, times(1))
        .activate(messageCommandCaptor.capture());
    assertThat(messageCommandCaptor.getValue().getAlertDtos().size()).isEqualTo(2);
    assertThat(messageCommandCaptor.getValue().getAlertDtos())
        .isEqualTo(of(getAlertDto(discriminatorFirst), getAlertDto(discriminatorSecond)));
  }

  private AlertDto getAlertDto(String discriminator) {
    return AlertDto.builder()
        .discriminator(discriminator)
        .level(ANALYSIS)
        .state(NEW)
        .build();
  }
}