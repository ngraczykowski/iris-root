package com.silenteight.warehouse.common.opendistro.roles;

import com.silenteight.warehouse.common.elastic.DlsQueryProcessor;
import com.silenteight.warehouse.common.opendistro.elastic.OpendistroElasticClient;
import com.silenteight.warehouse.common.opendistro.elastic.RoleDto;
import com.silenteight.warehouse.common.opendistro.elastic.RoleDto.IndexPermission;

import org.elasticsearch.index.query.TermsQueryBuilder;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static com.silenteight.warehouse.common.opendistro.roles.RolesFixtures.COUNTRY_GROUP_ID;
import static com.silenteight.warehouse.common.opendistro.roles.RolesMappedConstants.COUNTRY_KEY;
import static java.util.List.of;
import static org.assertj.core.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class RoleServiceTest {

  private static final String SAMPLE_DLS = "<sample query>";
  private static final List<Object> VALUES = of("ES", "DE");

  @Mock
  private OpendistroElasticClient opendistroElasticClient;

  @Mock
  private DlsQueryProcessor dlsQueryProcessor;

  @InjectMocks
  private RoleService underTest;

  @Test
  void shouldReturnEmptyListWhenRoleEmpty() {
    Mockito.when(opendistroElasticClient.getCurrentRole(COUNTRY_GROUP_ID.toString()))
        .thenReturn(RoleDto.builder().build());

    List<String> countries = underTest.getCountries(COUNTRY_GROUP_ID);

    assertThat(countries).isEmpty();
  }

  @Test
  void shouldReturnEmptyListWhenDlsNotDefined() {
    IndexPermission emptyIndexPermission = IndexPermission.builder().build();
    Mockito.when(opendistroElasticClient.getCurrentRole(COUNTRY_GROUP_ID.toString()))
        .thenReturn(RoleDto.builder()
            .indexPermissions(of(emptyIndexPermission))
            .build());

    List<String> countries = underTest.getCountries(COUNTRY_GROUP_ID);

    assertThat(countries).isEmpty();
  }

  @Test
  void shouldReturnCountryList() {
    IndexPermission indexPermission = IndexPermission.builder().dls(SAMPLE_DLS).build();
    RoleDto roleDto = RoleDto.builder().indexPermissions(of(indexPermission)).build();
    Mockito.when(opendistroElasticClient.getCurrentRole(COUNTRY_GROUP_ID.toString()))
        .thenReturn(roleDto);
    Mockito.when(dlsQueryProcessor.deserialize(SAMPLE_DLS))
        .thenReturn(new TermsQueryBuilder(COUNTRY_KEY, VALUES));

    List<String> countries = underTest.getCountries(COUNTRY_GROUP_ID);

    assertThat(countries).containsExactlyInAnyOrder("ES", "DE");
  }
}
