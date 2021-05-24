package com.silenteight.hsbc.datasource.provider;

import lombok.RequiredArgsConstructor;

import com.silenteight.hsbc.bridge.match.MatchFacade;
import com.silenteight.hsbc.datasource.common.DataSourceInputProvider;
import com.silenteight.hsbc.datasource.dto.country.CountryInputResponse;
import com.silenteight.hsbc.datasource.dto.date.DateInputResponse;
import com.silenteight.hsbc.datasource.dto.document.DocumentInputResponse;
import com.silenteight.hsbc.datasource.dto.event.EventInputResponse;
import com.silenteight.hsbc.datasource.dto.gender.GenderInputResponse;
import com.silenteight.hsbc.datasource.dto.location.LocationInputResponse;
import com.silenteight.hsbc.datasource.dto.name.NameInputResponse;
import com.silenteight.hsbc.datasource.dto.nationalid.NationalIdInputResponse;
import com.silenteight.hsbc.datasource.dto.transaction.TransactionInputResponse;
import com.silenteight.hsbc.datasource.extractors.name.NameInformationServiceClient;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@RequiredArgsConstructor
class DataProvidersConfiguration {

  private final MatchFacade matchFacade;
  private final NameInformationServiceClient nameInformationServiceClient;

  @Bean
  DataSourceInputProvider<CountryInputResponse> countryInputProvider() {
    return new CountryInputProvider(matchFacade);
  }

  @Bean
  DataSourceInputProvider<DateInputResponse> dateInputProvider() {
    return new DateInputProvider(matchFacade);
  }

  @Bean
  DataSourceInputProvider<DocumentInputResponse> documentInputProvider() {
    return new DocumentInputProvider(matchFacade);
  }

  @Bean
  DataSourceInputProvider<EventInputResponse> eventInputProvider() {
    return new EventInputProvider(matchFacade);
  }

  @Bean
  DataSourceInputProvider<GenderInputResponse> genderInputProvider() {
    return new GenderInputProvider(matchFacade);
  }

  @Bean
  IsPepInputProvider isPepInputProvider() {
    return new IsPepInputProvider(matchFacade);
  }

  @Bean
  DataSourceInputProvider<LocationInputResponse> locationInputProvider() {
    return new LocationInputProvider(matchFacade);
  }

  @Bean
  DataSourceInputProvider<NameInputResponse> nameInputProvider() {
    return new NameInputProvider(matchFacade, nameInformationServiceClient);
  }

  @Bean
  DataSourceInputProvider<NationalIdInputResponse> nationalIdInputProvider() {
    return new NationalIdInputProvider(matchFacade);
  }

  @Bean
  DataSourceInputProvider<TransactionInputResponse> transactionInputProvider() {
    return new TransactionInputProvider(matchFacade);
  }
}
