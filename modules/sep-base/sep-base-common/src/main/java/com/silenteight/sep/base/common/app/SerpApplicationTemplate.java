package com.silenteight.sep.base.common.app;

import lombok.NonNull;

import com.silenteight.commons.app.spring.SpringApplicationTemplate;

public class SerpApplicationTemplate extends SpringApplicationTemplate {

  private final String appName;
  private String[] profiles = new String[0];
  private boolean web = true;

  public SerpApplicationTemplate(
      @NonNull String appName,
      @NonNull String[] args,
      @NonNull Class<?>... sources) {

    super(args, sources);

    new ApplicationBootstrapper(appName, "serp.home", new HomeDirectoryDiscoverer("SERP_HOME"))
        .bootstrapApplication();

    this.appName = appName;
  }

  public SerpApplicationTemplate profiles(@NonNull String... profiles) {
    this.profiles = profiles;
    return this;
  }

  public SerpApplicationTemplate web(boolean web) {
    this.web = web;
    return this;
  }

  @Override
  public void runAndExit() {
    super.runAndExit(new SerpApplicationConfigurer(appName, web, profiles));
  }
}
