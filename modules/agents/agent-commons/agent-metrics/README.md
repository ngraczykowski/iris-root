# Agent metrics

Agent metrics module simplify using metrics in agents. It handles configuration and provide api
which helps to add new record metrics.

### Content:

- [Adding metrics module](#adding-metrics-module-to-agent-source-code)
- [Configuration](#configuration)
- [Usage](#usage)
- [Testing](#testing)
- [Publishing](#publishing-dashboards)

### Adding metrics module to agent source code

Create new metrics sub-module in agent project.

Standard file structure:

```
. metrics
   └── grafana
   |    └─── dashboards
   |            {{agent-name}}}.json
   └── provisioning
   |    └─── dashboards
   |    |       silenteigt.yml
   |    └─── datasources
   |            prometheus.yml
   └── prometheus
   |         prometheus.yml
   └── src
        -main...  
```

grafana/dashboard/{{agent-name}}.json - json dashboard file generated in grafana.

provisioning/dashboards/silenteight.yml - common dashboard settings. Example:

```yaml
apiVersion: 1

providers:
  - name: Default
    folder: 'Silent Eight Provided'
    type: file
    disableDeletion: true
    editable: true
    options:
      path: /etc/grafana/dashboards/default
```

provisioning/dashboards/prometheus.yml - data source configuration:

```yaml
apiVersion: 1

datasources:
  - name: Prometheus
    type: prometheus
    access: proxy
    url: http://prometheus:9090/prometheus
    isDefault: true
    editable: true
```

prometheus/prometheus.yml - prometheus app settings:

```yaml
global:
  scrape_interval: 10s

scrape_configs:
  - job_name: '{{agent name}}'
    scheme: http
    metrics_path: /rest/{{agent-name}}/management/prometheus
    static_configs:
      - targets: [ '172.253.0.1:24310' ]

```

*IP address in targets should be same as set for docker network and port should be set to agent http
port.
(on mac computers please use: **host.docker.internal** as ip address but do not push it to
repository)

src - standard java project structure, put MetriceTemplates and metric code here([usage](#usage))

### Configuration

In agent project:

Add those two properties to agent configuration file( bootstrap.properties or application.yml)

{{prefix_value}} - prefix that will be used for stored metrics.

```bash
agents.commons.metrics.enabled=true
agents.commons.metrics.prefix={{prefix_value}}
```

## Usage

### Adding new record type:

1. Create new class which will implement **MetricsTemplate** interface. It must override build
   method which returns **RecordMetrics** class.

Example:

```java

@Value
public class DecisionMetrics implements MetricsTemplate {

  private static final String NAME = "decision";

  @NonNull
  String decision;

  @Override
  public RecordMetrics build() {
    return new RecordCounterMetrics(NAME, new MetricTag(NAME, decision));
  }
}
```

### Store record:

In order to store record use: com.silenteight.agent.common.metrics.Metrics.record method with custom
class implementing **MetricsTemplate** as an argument:

```java
record(new IncomingGenders(gender,sourceTag))
```

That's all aspects which is set on record method will sore it for us.

## Testing

### Run Graphana and Prometheus on local env

In agent source code dockerfile with grafana and prometheus should be available.

1. Go to agent project directory and run:

```bash
docker-compose -f metrics/docker-compose.yml --compatibility up --detach
```

2. Go to localhost:24099 and login as dev user (dev/dev by default).
3. Run Agent Application.

To stop docker services run:

```bash
docker-compose -f metrics/docker-compose.yml --compatibility down
```

## Publishing dashboards

In order to publish dashboard please modify build.gradle file in your agent project:

1. Add configuration:

```
configurations {
  dashboardsZip
}
```

2. Add task:

```
def zipDashboardsTask = tasks.register("zipDashboards", Zip) {
  it.group "Build"
  it.description "Zips {{Agent Name}} dashboards"

  it.from "$projectDir/metrics/grafana/dashboards"

  it.archiveFileName = "{{agent-name}}-dashboards-${project.version}.zip"
  it.destinationDirectory = file("$buildDir/distributions")
}
```

3. Add dashboard publish to publishing:

```
publishing {
  publications {
    mavenDashboardsZip(MavenPublication) {
      artifactId = '{{agent-name}}-dashboards'
      artifact source: zipDashboardsTask.get(), extension: 'zip'
    }
  }
}
```
