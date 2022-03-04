package com.silenteight.customerbridge.common.recommendation.alertinfo;

import com.silenteight.customerbridge.common.protocol.AlertWrapper;
import com.silenteight.proto.serp.scb.v1.*;
import com.silenteight.proto.serp.v1.alert.Alert;
import com.silenteight.proto.serp.v1.alert.Match;
import com.silenteight.proto.serp.v1.reporter.AlertInfo;
import com.silenteight.proto.serp.v1.reporter.MatchInfo;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static com.silenteight.customerbridge.common.recommendation.alertinfo.AlertInfosMapperTestFactory.createAlert;
import static com.silenteight.sep.base.common.protocol.AnyUtils.maybeUnpack;
import static org.assertj.core.api.Assertions.*;

class AlertInfoMapperTest {

  private AlertInfoMapper alertInfoMapper;

  @BeforeEach
  void setUp() {
    alertInfoMapper = new AlertInfoMapper();
  }

  @Test
  void mappingTest() {
    Alert alert = createAlert();

    var infos = alertInfoMapper.map(alert);

    assertInfo(infos.getAlertInfo(), alert);
    assertScbInfo(infos.getScbAlertInfo(), alert);
  }

  private static void assertInfo(AlertInfo info, Alert alert) {
    assertThat(info.getAlertId()).isEqualTo(alert.getId());
    assertThat(info.getGeneratedAt()).isEqualTo(alert.getGeneratedAt());
    assertThat(info.getDecisionGroup()).isEqualTo(alert.getDecisionGroup());
    assertLatestDecision(info, alert);
    assertMatches(info, alert);
  }

  private static void assertLatestDecision(AlertInfo info, Alert alert) {
    new AlertWrapper(alert)
        .getLatestAnalystDecision()
        .ifPresent(d -> assertThat(info.getLatestAnalystDecision())
            .satisfies(ld -> assertThat(ld.getSolution()).isEqualTo(d.getSolution().name()))
            .satisfies(ld -> assertThat(ld.getComment()).isEqualTo(d.getComment())));
  }

  private static void assertMatches(AlertInfo info, Alert alert) {
    assertThat(info.getMatchesCount()).isEqualTo(alert.getMatchesCount());
    for (int i = 0; i < info.getMatchesCount(); i++) {
      MatchInfo matchInfo = info.getMatches(i);
      Match match = alert.getMatches(i);
      assertThat(matchInfo.getMatchId()).isEqualTo(match.getId());
      assertThat(matchInfo.getIndex()).isEqualTo(match.getIndex());
    }
  }

  private static void assertScbInfo(ScbAlertInfo info, Alert alert) {
    AlertWrapper accessor = new AlertWrapper(alert);
    accessor.unpackDetails(ScbAlertDetails.class).ifPresent(d -> {
      assertThat(info.getAlertId()).isEqualTo(alert.getId());
      assertThat(info.getBatchId()).isEqualTo(d.getBatchId());
      assertThat(info.getUnit()).isEqualTo(d.getUnit());
      assertThat(info.getSystemId()).isEqualTo(d.getSystemId());
    });

    var firstDecision = alert.getDecisionsList().get(0);
    maybeUnpack(firstDecision.getDetails(), ScbDecisionDetails.class)
        .ifPresent(
            d -> assertThat(info.getLastIntermediateStateName()).isEqualTo(d.getStateName()));

    accessor.unpackAlertedPartyDetails(ScbAlertedPartyDetails.class).ifPresent(d -> {
      assertThat(info.getApDbCountry()).isEqualTo(d.getApDbCountry());
      assertThat(info.getApSrcSysId()).isEqualTo(d.getApSrcSysId());
    });

    accessor.iterateMatches((index, matchAccessor) -> {
      HitDetails hitDetails = info.getHitDetails(index);
      assertThat(hitDetails.getMatchIndex()).isEqualTo(index);
      matchAccessor.unpackMatchedPartyDetails(ScbWatchlistPartyDetails.class)
          .ifPresent(d -> assertThat(hitDetails)
              .satisfies(hd -> assertThat(hd.getWlHitTypeList()).isEqualTo(d.getWlHitTypeList()))
              .satisfies(hd -> assertThat(hd.getWlId()).isEqualTo(d.getWlId())));
    });
  }
}
