package com.silenteight.customerbridge.common.gnsparty;

import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Optional;

import static com.silenteight.customerbridge.common.gnsparty.PartyCreatorFixtures.*;
import static com.silenteight.customerbridge.common.gnsparty.PartyCreatorProvider.GROUP_TWO_TOKEN_COUNT;
import static com.silenteight.customerbridge.common.gnsparty.PartyCreatorProvider.countFields;
import static com.silenteight.customerbridge.common.gnsparty.PartyCreatorProvider.getCreatorsByColumnCount;
import static org.assertj.core.api.Assertions.*;

public class PartyCreatorProviderTest {

  @Test
  void shouldProperlyCountFields() {
    assertThat(countFields(null, SEPARATOR)).isEqualTo(0);
    assertThat(countFields("", SEPARATOR)).isEqualTo(0);
    assertThat(countFields("A", SEPARATOR)).isEqualTo(1);
    assertThat(countFields("~", SEPARATOR)).isEqualTo(2);
    assertThat(countFields("A~A", SEPARATOR)).isEqualTo(2);
    assertThat(countFields("~A", SEPARATOR)).isEqualTo(2);
    assertThat(countFields("A~", SEPARATOR)).isEqualTo(2);
  }

  @Test
  void testDataShouldContainProperFieldCounts() {
    assertThat(countFields(RECORD_SCB_ADV, SEPARATOR)).isEqualTo(GROUP_TWO_TOKEN_COUNT);
    assertThat(countFields(RECORD_SCB_EDMP_ADVM, SEPARATOR)).isEqualTo(GROUP_TWO_TOKEN_COUNT);
    assertThat(countFields(RECORD_SCB_DENY, SEPARATOR)).isEqualTo(GROUP_TWO_TOKEN_COUNT);
    assertThat(countFields(RECORD_SCB_DD, SEPARATOR)).isEqualTo(GROUP_TWO_TOKEN_COUNT);
    assertThat(countFields(RECORD_SCB_EDMP_DENY, SEPARATOR)).isEqualTo(GROUP_TWO_TOKEN_COUNT);
    assertThat(countFields(RECORD_SCB_EDMP_DUED, SEPARATOR)).isEqualTo(GROUP_TWO_TOKEN_COUNT);
  }

  @Test
  void shouldProvideCorrectCreatorByRecordFieldsCount() {
    Optional<List<GnsPartyCreator>> creatorList =
        getCreatorsByColumnCount(RECORD_SCB_ADV, SEPARATOR);
    assertThat(creatorList.isPresent());
    assertThat(creatorList.get().size()).isEqualTo(1);
    assertThat(creatorList.get().get(0)).isInstanceOf(GnsPartyGroupTwoCreator.class);
  }
}
