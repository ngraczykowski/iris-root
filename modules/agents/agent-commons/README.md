## Agent Commons

The Agent Commons project consists of 8 modules that can be used directly in agents as separate
dependencies.

These modules are:

1. agent-autoconfigure
2. agent-config-loader
3. agent-starter
4. agent-starter-test
5. agent-dependencies-common
6. agent-dictionaries
7. agent-monitoring
8. file-reading-commons
9. agent-facade-core

### agent-autoconfigure

Adds following dependencies for the agent.

    org.springframework.cloud:spring-cloud-starter-sleuth
    org.springframework.cloud:spring-cloud-consul-discovery
    org.springframework.cloud:spring-cloud-starter-consul-config

With config resource bundle:

    agent-autoconfigure/src/main/resources/config/application.yml
    agent-autoconfigure/src/main/resources/config/application-consul.yml
    agent-autoconfigure/src/main/resources/config/application-tls.yml
    agent-autoconfigure/src/main/resources/config/bootstrap.yml
    agent-autoconfigure/src/main/resources/config/bootstrap-consul.yml

When added to the classpath, based on resources will automatically configure:

1. grpc server, like number of used threads
2. consul
3. tls
4. prometheus
5. health check endpoints

It uses default behaviour of spring boot autoconfigure based on profiles.

Example usage:

    libraries.silenteight_agent_autoconfig = "com.silenteight.agent.commons:agent-autoconfigure:$s8agentCommonsVersion"

and then in `build.gradle` add:

    implementation libraries.silenteight_agent_autoconfig

Agent-autoconfigure library is used in following projects:

- gender agent
- linguistics
- learning engine
- allow-list agent
- country agent
- document agent
- date agent
- geo agent
- historical decisions agent
- is-pep agent
- freetext agent
- region agent

#### Overwrite default values

You can overwrite the default settings. If f.eg you want to limit the number of used threads or
consul port in your `bootstrap.properties` you have to add the following values:

    agent.consul.port=24125
    agent.rpc.threads=4

### agent-config-loader

Adds the default ConfigLoader to load properties from files and ConfigsPathFinder to detect files
and directories. By default, it searches for the config files in `AGENT_HOME` directory.

Example usage:

    libraries.silenteight_agent_config_loader = "com.silenteight.agent.commons:agent-config-loader:$s8agentCommonsVersion"

and then in `build.gradle` add:

    implementation libraries.silenteight_agent_config_loader

ConfigLoader in class:

    import com.silenteight.agent.configloader.AgentConfigsLoader;


    ComparerConfigProperties loadProperties(String fileName) throws IOException {
        var loader = new ConfigLoader<>(APPLICATION_NAME, PREFIX, ComparerConfigProperties.class);
        var agentConfig = loader.load();
        return agentConfig.agentConfigs().get(fileName);
    }

ConfigPathFinder in class:

    import static com.silenteight.agent.configloader.ConfigsPathFinder.findFile;

    private static final String FILE_NAME = "administratively-related-countries.csv";
    private final String configDir;

    public List<AdministrativelyRelatedCountries> provideCountries() {
        var file = findFile(configDir, FILE_NAME).toString();
        return FileReader.readLinesAsStream(file)
            .filter(line -> !line.startsWith(COMMENTED_LINE))
            .map(AdministrativelyRelatedCountriesProvider::mapCountries)
            .map(AdministrativelyRelatedCountries::new)
            .collect(toList());
    }

Agent-config-loader library is used in following projects:

- allow-list agent
- document agent
- date agent
- geo agent
- historical decisions agent
- is-pep agent
- linguistics
- freetext agent
- region agent

### agent-starter

Adds the starter dependencies for the agent.

```
commons
spring
grpc
reactor grpc
consul
micrometer

```

Example usage:

    libraries.silenteight_agent_starter = "com.silenteight.agent.commons:agent-starter:$s8agentCommonsVersion"

and then in `build.gradle` add:

    implementation libraries.silenteight_agent_starter

Agent-starter library is used in following projects:

- allow-list agent
- country agent
- document agent
- geo agent
- linguistics
- agent facade
- freetext agent

### agent-starter-test

Adds the starter dependencies for agent tests.

```
reactor_test
spock_core
groovy_all

```

Example usage:

    libraries.silenteight_agent_starter_test = "com.silenteight.agent.commons:agent-starter-test:$s8agentCommonsVersion"

and then in `build.gradle` add:

    testImplementation libraries.silenteight_agent_starter_test

Agent-starter-test library is used in following projects:

- learning-engine
- allow-list agent
- country agent
- document agent
- geo agent
- historical decisions agent
- is-pep agent
- linguistics
- agent facade

### agent-dependencies-common

Adds the dependencies which are required for agent starter and facade. Provides miscellaneous
dependencies which are used directly by agent.

Example usage:

    dependencies {
        implementation platform("com.silenteight.agent.commons:agent-dependencies-common:$s8agentCommonsVersion")
        annotationProcessor platform("com.silenteight.agent.commons:agent-dependencies-common:$s8agentCommonsVersion")
    }

Agent-dependencies-common library is used in every agent project.

### agent-dictionaries

Adds dependencies for agent to provide an abstraction for reading and loading all kind of resources
that you need for developing agent purposes. Further information about this library is provided in
README.md file placed in agent-dictionaries module.

Example usage:

    libraries.s8_agents_dictionaries = "com.silenteight.agent.commons:agent-dictionaries:$s8agentCommonsVersion"

and then in `build.gradle` add:

    implementation libraries.s8_agents_dictionaries

in class:

    import com.silenteight.agent.common.dictionary.DictionarySource;
    import com.silenteight.agent.common.dictionary.UniqueValuesDictionary;
    import com.silenteight.solvers.freetext.extractor.WordsRepository;

    import static lombok.AccessLevel.PRIVATE;

    @RequiredArgsConstructor(access = PRIVATE)
    class RowsBasedWordsRepository implements WordsRepository {

    public static RowsBasedWordsRepository fromSource(DictionarySource source) {
        return new RowsBasedWordsRepository(UniqueValuesDictionary.fromSource(source));
    }

    private final UniqueValuesDictionary uniqueValuesDictionary;

    @Override
    public boolean exists(String word) {
        return uniqueValuesDictionary.contains(word);
    }
    }

Agent-dictionaries library is used in following projects:

- linguistics
- freetext agent
- region agent

### agent-monitoring

Adds dependencies for agent to provide monitoring using Sentry. Further information about this
library is provided in README.md file placed in agent-monitoring module.

Example usage:

    libraries.agent_monitoring = "com.silenteight.agent.commons:agent-monitoring:$s8agentCommonsVersion"

and then in `build.gradle` add:

    implementation libraries.agent_monitoring

Agent-monitoring library is used in following projects:

- learning-engine
- linguistics
- agent facade
- freetext agent
- region agent

### file-reading-commons

Adds dependencies for agent to provide File Readers.

Example usage:

    libraries.s8_agents_file_reading_commons = "com.silenteight.agent.commons:file-reading-commons:$s8agentCommonsVersion"

and then in `build.gradle` add:

    implementation libraries.s8_agents_file_reading_commons

in class:

    import com.silenteight.agent.common.io.FileReader;

    public List<AdministrativelyRelatedCountries> provideCountries() {
        var file = findFile(configDir, FILE_NAME).toString();
        return FileReader.readLinesAsStream(file)
            .filter(line -> !line.startsWith(COMMENTED_LINE))
            .map(AdministrativelyRelatedCountriesProvider::mapCountries)
            .map(AdministrativelyRelatedCountries::new)
            .collect(toList());
    }

} }

File-reading-commons library is used in following projects:

- linguistics
- geo-agent
- gender-agent

### agent-facade-core

Adds dependencies for agent to provide amqp facade. Further information about this library is
provided in README.md file placed in agent-facade-core module.

### Building JARs

Build JARs with command:

    ./gradlew clean build

The command will build JAR files:

- `agent-autoconfigure` JAR in `agent-autoconfigure/build/libs/agent-autoconfigure-*.jar`.
- `agent-config-loader` JAR in `agent-config-loader/build/libs/agent-config-loader-*.jar`.
- `agent-starter` JAR in `agent-starter/build/libs/agent-starter-*.jar`.
- `agent-starter-test` JAR in `agent-starter-test/build/libs/agent-starter-test-*.jar`.
