## Agent Commons

The Agent Commons project consists of 4 modules that can be used directly in agents as separate dependencies.

These modules are:
1. agent-autoconfigure
2. agent-config-loader
3. agent-starter
4. agent-starter-test

### agent-autoconfigure

When added to the classpath, will automatically configure:
1. grpc server, like number of used threads
2. consul
3. tls
4. prometheus
5. health check endpoints

It uses default behaviour of spring boot autoconfigure based on profiles. 

Example usage:

    libraries.agent_autoconfigure = [group: 'com.silenteight.agent.commons', name: 'agent-autoconfigure]

and then in `build.gradle` add:

    implementation libraries.agent_autoconfigure


#### Overwrite default values
You can overwrite the default settings.
If f.eg you want to limit the number of used threads or consul port in your `bootstrap.properties` you have to add the following values:

    agent.consul.port=24125
    agent.rpc.threads=4

### agent-config-loader

Adds the default loader of the config files.
By default, it searches for the config files in `AGENT_HOME` directory.

Example usage:

    import com.silenteight.agent.configloader.AgentConfigsLoader;


    ComparerConfigProperties loadProperties(String fileName) throws IOException {
        var loader = new ConfigLoader<>(APPLICATION_NAME, PREFIX, ComparerConfigProperties.class);
        var agentConfig = loader.load();
        return agentConfig.agentConfigs().get(fileName);
    }

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

    libraries.agent_starter = [group: 'com.silenteight.agent.commons', name: 'agent-starter]

and then in `build.gradle` add:

    implementation libraries.agent_starter

### agent-starter-test

Adds the starter dependencies for agent tests.

Example usage:

    libraries.agent_starter_test = [group: 'com.silenteight.agent.commons', name: 'agent-starter-test]

and then in `build.gradle` add:

    testImplementation libraries.agent_starter_test

### Building JARs

Build JARs with command:

    ./gradlew clean build

The command will build JAR files:

- `agent-autoconfigure` JAR in `agent-autoconfigure/build/libs/agent-autoconfigure-*.jar`.
- `agent-config-loader` JAR in `agent-config-loader/build/libs/agent-config-loader-*.jar`.
- `agent-starter` JAR in `agent-starter/build/libs/agent-starter-*.jar`.
- `agent-starter-test` JAR in `agent-starter-test/build/libs/agent-starter-test-*.jar`.
