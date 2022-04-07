package com.silenteight.scb.feeding.domain.featureinput;

import com.silenteight.scb.ingest.adapter.incomming.common.model.alert.Alert;
import com.silenteight.scb.ingest.adapter.incomming.common.model.alert.AlertedParty;
import com.silenteight.scb.ingest.adapter.incomming.common.model.match.Match;
import com.silenteight.scb.ingest.adapter.incomming.common.model.match.MatchedParty;
import com.silenteight.universaldatasource.api.library.Feature;
import com.silenteight.universaldatasource.api.library.date.v1.DateFeatureInputOut;
import com.silenteight.universaldatasource.api.library.date.v1.EntityTypeOut;
import com.silenteight.universaldatasource.api.library.date.v1.SeverityModeOut;

import org.apache.commons.collections4.CollectionUtils;

import java.util.ArrayList;
import java.util.List;

public class DateFeatureInputFactory implements FeatureInputFactory {

  @Override
  public Feature create(Alert alert, Match match) {
    return DateFeatureInputOut.builder()
                .feature("features/date")
                .alertedPartyDates(getAlertedPartyDates(alert.alertedParty()))
                .watchlistDates(getWatchlistDates(match.matchedParty()))
                .alertedPartyType(determineApType(match.matchedParty().apType()))
                .mode(SeverityModeOut.NORMAL)
                .build();
  }

  private List<String> getAlertedPartyDates(AlertedParty alertedParty) {
    List<String> alertedPartyDates = new ArrayList<>();
    CollectionUtils.addIgnoreNull(alertedPartyDates, alertedParty.apDobDoi());
    return alertedPartyDates;
  }

  private List<String> getWatchlistDates(MatchedParty matchedParty) {
    List<String> watchlistDates = new ArrayList<>();
    CollectionUtils.addIgnoreNull(watchlistDates, matchedParty.wlDob());
    return watchlistDates;
  }

  private static EntityTypeOut determineApType(String apType) {
    if (apType.equals("I")) {
      return EntityTypeOut.INDIVIDUAL;
    } else if (apType.equals("C")) {
      return EntityTypeOut.ORGANIZATION;
    }
    return EntityTypeOut.ENTITY_TYPE_UNSPECIFIED;
  }
}
