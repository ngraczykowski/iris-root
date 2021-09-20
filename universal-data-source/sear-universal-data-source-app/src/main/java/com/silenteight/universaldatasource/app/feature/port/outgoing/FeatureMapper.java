package com.silenteight.universaldatasource.app.feature.port.outgoing;


import com.silenteight.universaldatasource.app.feature.model.MatchFeatureOutput;
import com.silenteight.universaldatasource.app.feature.port.incoming.BatchFeatureInputResponse;

import com.google.protobuf.Any;
import com.google.protobuf.InvalidProtocolBufferException;
import com.google.protobuf.Message;

public interface FeatureMapper {

  String getType();

  BatchFeatureInputResponse map(MatchFeatureOutput matchFeatureOutput);

  Message unpackAnyMessage(Any featureInput) throws InvalidProtocolBufferException;

}
