package com.silenteight.serp.governance.bulkchange;

import com.silenteight.proto.serp.v1.api.BulkBranchChangeView;
import com.silenteight.serp.governance.bulkchange.BulkBranchChange.State;
import com.silenteight.serp.governance.bulkchange.BulkBranchChangeViewMapper.UnsupportedStateException;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertThrows;

@DisplayName("Branch BulkChangeCommands View Mapper test suite")
class BulkBranchChangeViewMapperTest {

  @Nested
  @DisplayName("Tests for status mapping")
  class StatusMappingTests {

    @Test
    @DisplayName("Proto State properly converted to the Java State")
    void shouldReturnJavaStates() {
      //Given
      List<State> result = new ArrayList<>();
      //When
      ListBulkBranchChangeViewTestFixtures.protoStates().forEach(
          state -> result.add(BulkBranchChangeStateMapper.mapToJava(state)));
      //Then
      assertThat(result).isEqualTo(ListBulkBranchChangeViewTestFixtures.javaStates());
    }

    @Test
    @DisplayName("Java State properly converted to the Proto State")
    void shouldReturnProtoStates() {
      //Given
      List<BulkBranchChangeView.State> result = new ArrayList<>();
      //When
      ListBulkBranchChangeViewTestFixtures.javaStates().forEach(
          state -> result.add(BulkBranchChangeStateMapper.mapToProto(state)));
      //Then
      assertThat(result).isEqualTo(ListBulkBranchChangeViewTestFixtures.protoStates());
    }

    @Test
    @DisplayName("Exception is thrown on an unrecognised Proto state")
    void shouldThrowExceptionOnUnrecognisedState() {
      //When, Then
      assertThrows(
          UnsupportedStateException.class,
          () -> BulkBranchChangeStateMapper.mapToJava(BulkBranchChangeView.State.UNRECOGNIZED));
    }
  }

  @Nested
  @DisplayName("Tests for branch bulk change mapping")
  class BranchBulkChangeMappingTests {

    @Test
    @DisplayName("Proper proto BulkBranchChangeView is returned for the Enablement Change")
    void shouldReturnProtoBulkBranchChangeView_enablement() {
      //When, Then
      assertThat(BulkBranchChangeViewMapper.mapToProtoBulkBranchChangeView(
          ListBulkBranchChangeViewTestFixtures.JAVA_CHANGE_ENABLEMENT)).isEqualTo(
          ListBulkBranchChangeViewTestFixtures.PROTO_CHANGE_ENABLEMENT);
    }

    @Test
    @DisplayName("Proper proto BulkBranchChangeView is returned for the Solution Change")
    void shouldReturnProtoBulkBranchChangeView_solution() {
      //When, Then
      assertThat(BulkBranchChangeViewMapper.mapToProtoBulkBranchChangeView(
          ListBulkBranchChangeViewTestFixtures.JAVA_CHANGE_SOLUTION))
          .isEqualTo(ListBulkBranchChangeViewTestFixtures.PROTO_CHANGE_SOLUTION);
    }
  }
}
