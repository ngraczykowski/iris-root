package com.silenteight.customerbridge.common.recommendation.alertinfo;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import com.silenteight.proto.serp.v1.alert.Alert;

import reactor.core.publisher.Mono;

import java.util.List;

import static java.util.stream.Collectors.toList;

@RequiredArgsConstructor
public class AlertInfoService {

  private final AlertInfoMapper mapper;
  private final AlertInfoBroadcaster broadcaster;

  public Mono<Void> sendAlertInfo(@NonNull List<Alert> alerts) {
    var infoList = alerts
        .stream()
        .map(mapper::map)
        .collect(toList());

    return broadcaster.send(infoList);
  }

  public void sendAlertInfo(@NonNull Alert alert) {
    broadcaster.send(mapper.map(alert));
  }
}
