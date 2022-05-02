package com.silenteight.connector.ftcc.app


import com.silenteight.recommendation.api.library.v1.RecommendationsIn

import lombok.AccessLevel
import lombok.NoArgsConstructor

@NoArgsConstructor(access = AccessLevel.PRIVATE)
final class Fixtures {
  def static IMPORT_RESPONSE_JSON = '''{
  "Header": null,
  "Body": {
    "msg_Acknowledgement": {
      "faultcode": "0",
      "faultstring": "OK",
      "faultactor": "cmapi.send.message"
    }
  }
}'''

  def static CALLBACK_REQUEST = '''{
  "Header": null,
  "Body": {
    "msg_ReceiveStatusUpdate": {
      "VersionTag": "1",
      "Authentication": {
        "TrustLogin": "user",
        "TrustPassword": "password"
      },
      "Messages": [
        {
          "Message": {
            "Unit": "TRAINING",
            "BusinessUnit": "",
            "MessageID": "00000005",
            "SystemID": "SAN!60C2ED1B-58A1D68E-0326AE78-A8C7CC79",
            "Status": {
              "Name": "Level 2-, TRUE1",
              "RoutingCode": "12",
              "Checksum": "dc44ae12825d619154b8a1dc8e09223b",
              "ID": "6"
            },
            "Comment": "Comment",
            "Operator": "S8 SEAR"
          }
        }
      ]
    }
  }
}
'''

  def static CALLBACK_RESPONSE = '''{
  "Header": null,
  "Body": {
    "msg_Acknowledgement": {
      "faultcode": "0",
      "faultstring": "OK",
      "faultactor": "cmapi.send.message"
    }
  }
}'''

  static def ANALYSIS_NAME = 'analysis'

  static RecommendationsIn RECOMMENDATIONS_IN = RecommendationsIn.builder()
      .analysisName(ANALYSIS_NAME)
      .alertNames([])
      .build()
}
