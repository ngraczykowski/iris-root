package com.silenteight.warehouse.common.opendistro.roles;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import com.silenteight.warehouse.common.elastic.DlsQueryProcessor;
import com.silenteight.warehouse.common.opendistro.elastic.OpendistroElasticClient;
import com.silenteight.warehouse.common.opendistro.elastic.RoleDto;
import com.silenteight.warehouse.common.opendistro.elastic.RoleDto.IndexPermission;

import org.elasticsearch.index.query.TermsQueryBuilder;

import java.util.*;

import static com.silenteight.warehouse.common.opendistro.roles.RolesMappedConstants.COUNTRY_KEY;
import static com.silenteight.warehouse.common.opendistro.utils.OpendistroUtils.getRawField;
import static java.util.Collections.emptyList;
import static java.util.List.of;
import static java.util.Optional.ofNullable;
import static java.util.stream.Collectors.toList;

@Slf4j
@RequiredArgsConstructor
public class RoleService {

  private final OpendistroElasticClient opendistroElasticClient;
  private final DlsQueryProcessor dlsQueryProcessor;

  public void setCountries(
      UUID countryGroupId, Collection<String> countries, String productionIndexQueryPattern) {

    TermsQueryBuilder queryBuilder = new TermsQueryBuilder(getRawField(COUNTRY_KEY), countries);
    String terms = dlsQueryProcessor.serialize(queryBuilder);

    IndexPermission indexPermission = IndexPermission.builder()
        .indexPatterns(of(productionIndexQueryPattern))
        .dls(terms)
        .build();

    RoleDto roleDto = RoleDto.builder()
        .indexPermissions(of(indexPermission))
        .build();

    opendistroElasticClient.setRole(countryGroupId.toString(), roleDto);
  }

  public List<String> getCountries(UUID countryGroupId) {
    RoleDto currentRole = opendistroElasticClient.getCurrentRole(countryGroupId.toString());

    if (currentRole.getIndexPermissionCount() < 1) {
      return emptyList();
    }

    if (currentRole.getIndexPermissionCount() > 1) {
      log.warn(
          "CountryGroup={} has more then one index permission. Only one index is supported.",
          countryGroupId);
    }

    return ofNullable(currentRole.getFirstIndexPermissionDls())
        .filter(dls -> !dls.isBlank())
        .map(dlsQueryProcessor::deserialize)
        .map(TermsQueryBuilder::values)
        .orElseGet(Collections::emptyList)
        .stream()
        .map(Objects::toString)
        .collect(toList());
  }
}
