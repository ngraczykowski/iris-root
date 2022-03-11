package com.silenteight.scb.ingest.domain.model;

import lombok.Value;

import java.util.List;

@Value
public class StopDescriptorsItem {

  List<StopDescriptor> descriptors;

  @Value
  public static class StopDescriptor {

    String name;
    String totalMatchScore;
    List<StopDescriptorDetail> stopDescriptorDetail;

    @Value
    public static class StopDescriptorDetail {

      String inputToken;
      String inputSynonym;
      String sdToken;
      String matchScore;
    }
  }
}
