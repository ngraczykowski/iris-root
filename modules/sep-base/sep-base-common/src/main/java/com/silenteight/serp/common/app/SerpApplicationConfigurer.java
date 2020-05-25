package com.silenteight.serp.common.app;

import lombok.extern.slf4j.Slf4j;

import com.silenteight.commons.app.spring.ApplicationBuilderConfigurer;
import com.silenteight.commons.app.spring.ConfigurableApplicationBuilder;

import org.springframework.boot.Banner.Mode;
import org.springframework.boot.WebApplicationType;
import org.springframework.boot.builder.SpringApplicationBuilder;

import java.util.ArrayList;
import java.util.List;

import static java.util.Arrays.asList;

@Slf4j
public class SerpApplicationConfigurer implements ApplicationBuilderConfigurer {

  private final String appName;
  private final boolean web;
  private final List<String> profiles;

  public SerpApplicationConfigurer(String appName, boolean web, String... profiles) {
    this.appName = appName;
    this.web = web;
    this.profiles = new ArrayList<>();
    this.profiles.add(appName);
    this.profiles.addAll(asList(profiles));
  }

  @Override
  public ConfigurableApplicationBuilder configure(ConfigurableApplicationBuilder builder) {
    return builder
        .bootstrapProperties("spring.application.name=" + appName)
        .properties(
            "serp.logging.path=${serp.home:${java.io.tmpdir:/tmp}}/log/${spring.application.name}",
            "serp.logging.name=${spring.application.name}")
        .configName("application", "serp")
        .profiles(profiles.toArray(String[]::new));
  }

  @Override
  public SpringApplicationBuilder customize(SpringApplicationBuilder springBuilder) {
    if (!web)
      springBuilder.web(WebApplicationType.NONE);

    return springBuilder.bannerMode(Mode.OFF);
  }
}
