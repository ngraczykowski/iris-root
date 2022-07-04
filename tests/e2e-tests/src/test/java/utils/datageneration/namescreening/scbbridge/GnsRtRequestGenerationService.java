/*
 * Copyright (c) 2022 Silent Eight Pte. Ltd. All rights reserved.
 */

package utils.datageneration.namescreening.scbbridge;

import utils.CommonUtils;

public class GnsRtRequestGenerationService {

  public GnsRtRequest generate(String recommendationType) {
    String template = switch (recommendationType) {
      case "INVESTIGATE" ->
          CommonUtils.getJsonTemplate("scbbridge/rtrequests/lima", "investigateRtRequestTemplate");
      case "FALSE_POSITIVE" ->
          CommonUtils.getJsonTemplate("scbbridge/rtrequests/lima", "fpRtRequestTemplate");
      case "POTENTIAL_TRUE_POSITIVE" ->
          CommonUtils.getJsonTemplate("scbbridge/rtrequests/lima", "ptpRtRequestTemplate");
      default -> throw new IllegalArgumentException("Wrong recommendation type");
    };
    return new GnsRtRequest(template);
  }
}
