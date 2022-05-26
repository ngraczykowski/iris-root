package com.silenteight.scb.ingest.adapter.incomming.common.rest;

import lombok.extern.slf4j.Slf4j;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.actuate.endpoint.annotation.Endpoint;
import org.springframework.boot.actuate.endpoint.annotation.ReadOperation;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Component
@Endpoint(id = "globalhealth")
@Slf4j
public class GlobalHealthEndpoint {

  private record Service(String id, String contextPath) {}

  private final ObjectMapper mapper = new ObjectMapper();

  private final List<Service> servicesToCheck = List.of(
      new Service("scb-bridge", "/rest/scb-bridge"),
      new Service("core-bridge", "/rest/core-bridge"),
      new Service("adjudication-engine", "/rest/ae"),
      new Service("universal-data-source", "/rest/uds"),
      new Service("governance", "/rest/governance"),
      new Service("warehouse", "/rest/warehouse"),
      new Service("date-agent", "/rest/date-agent"),
      new Service("name-agent", "/rest/name-agent"));

  private final RestTemplate restTemplate;

  private final DiscoveryClient discoveryClient;

  private final String namespace;

  @Autowired
  GlobalHealthEndpoint(
      DiscoveryClient discoveryClient, @Value("${namespace:}") String namespace) {
    this(
        new RestTemplate(httpComponentsClientHttpRequestFactory()),
        discoveryClient,
        namespace);
  }

  GlobalHealthEndpoint(
      RestTemplate restTemplate,
      DiscoveryClient discoveryClient,
      String namespace) {
    this.restTemplate = restTemplate;
    this.discoveryClient = discoveryClient;
    this.namespace = namespace;
  }

  @ReadOperation
  public Map<String, List<Map<String, String>>> health() {

    log.info(
        "Getting statuses of known services for namespace: {} from: {}", namespace,
        discoveryClient.description());

    var serviceIds = validServiceIds();
    var servicesMap = new LinkedHashMap<String, List<Map<String, String>>>();

    servicesToCheck.forEach(service -> {

      log.info("Looking for serviceIds for service: {}", service);

      serviceIds.stream()
          .filter(sid -> sid.endsWith(service.id))
          .findFirst()
          .ifPresent(serviceId -> {
            log.info("Found serviceId: {} for service: {}", serviceId, service);
            servicesMap.put(service.id(), serviceInstances(serviceId, service.contextPath()));
          });

    });

    return servicesMap;
  }

  private List<String> validServiceIds() {
    return discoveryClient.getServices()
        .stream()
        .filter(this::validServiceId)
        .toList();
  }

  private boolean validServiceId(String serviceName) {
    if (!serviceName.startsWith(namespace)) {
      return false;
    }
    if (serviceName.contains("grpc")) {
      return false;
    }
    return true;
  }

  private List<Map<String, String>> serviceInstances(String serviceName, String contextPath) {
    var instances = new ArrayList<Map<String, String>>();
    for (var instance : discoveryClient.getInstances(serviceName)) {
      try {
        instances.add(serviceInfo(instance.getUri() + contextPath + "/management"));
      } catch (Exception e) {
        log.warn(
            "Can't get instances for service: {}: {}/{}", serviceName, e.getClass().getName(),
            e.getMessage());
      }
    }
    return instances;
  }

  private Map<String, String> serviceInfo(String actuatorUri) throws JsonProcessingException {
    var infoMap = deserializeMap(get(actuatorUri + "/info"));
    var healthMap = deserializeMap(get(actuatorUri + "/health"));

    var ans = new LinkedHashMap<String, String>();
    ans.put("status", healthMap.get("status").toString());
    ans.put("actuator_uri", actuatorUri);
    ans.put("artefact", makeArtefact((Map<String, Object>) infoMap.get("build")));
    ans.put("commit_time", makeCommitTime((Map<String, Object>) infoMap.get("git")));
    return ans;
  }

  private Map<String, Object> deserializeMap(String json) throws JsonProcessingException {
    var mapTypeRef = new TypeReference<Map<String, Object>>() {};
    return mapper.readValue(json, mapTypeRef);
  }

  private static String makeArtefact(Map<String, Object> build) {
    return build.get("group") + ":" + build.get("artifact") + ":" + build.get("version");
  }

  private static String makeCommitTime(Map<String, Object> git) {
    return ((Map<String, Object>) git.get("commit")).get("time").toString();
  }

  private String get(String uri) {
    log.info("GET from uri: {}", uri);
    return restTemplate.getForEntity(uri, String.class).getBody();
  }

  private static HttpComponentsClientHttpRequestFactory httpComponentsClientHttpRequestFactory() {
    var factory = new HttpComponentsClientHttpRequestFactory();
    factory.setConnectionRequestTimeout(1000);
    factory.setConnectTimeout(1000);
    factory.setReadTimeout(5000);
    return factory;
  }

}
