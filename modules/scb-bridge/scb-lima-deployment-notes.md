# Intro

This writeup describes basics of deployment of `sear` with `scb-bridge` and `scb-core` components
to `lima` environment which is one of S8's test environments.

Some information here is generic, but this doc was written from the point of view of `scb-bridge`
component and `lima` environment.

# Prerequisites

- Nomad and other infrastructure services must be accessed via VPN: you must have WireGuard running.

# Infrastructure services

- Components' artifacts (`jar` files) are build by [Jenkins](https://jenkins.silenteight.com/). In
  our case, the most interesting job
  is [scb-bridge](https://jenkins.silenteight.com/view/all/job/sens/job/sens%252Fscb-bridge).
- Components' artifacts released to be deployed are kept
  in [Minio](https://console.minio.silenteight.com)'s `artifacts` bucket.
- Deployment of `sear` with `scb-bridge` and `scb-core` is done by [Nomad](http://10.8.0.1:4646/ui/)
  . Note, after logging-in to the UI change `Namespace` to `lima`.
- [Consul](http://10.8.0.1:8500/ui/dc1/) is used to host secrets (mostly credentials as key-value
  pairs) needed by the components to access other components, and also as a grpc services
  registration & discovery. It does also health checks of the services.
- [RabbitMQ](http://10.8.0.2:31131/rabbitmq/) is used as a queueing middleware.
- [Traefik](https://doc.traefik.io/traefik/v1.4/) is an Api Gateway. Maps Frontends urls to Backend
  urls and does Load Balancing as well. Integrates with Consul for dynamic mapping. Checks for tags
  in Consul and dynamically creates Frontend urls. Thanks to this, for example, you may
  access `scb-bridge` on `https://lima.silenteight.com/rest/scb-bridge/management/info` even though
  the ports can change between deployment.

TODO: It would be nice to have credentials to the above in team's vault in bit warden.

TODO: Add monitoring services ? But this is probably of scope for deployment.

# Nomad deployment

After login to [Nomad](http://10.8.0.1:4646/ui/) UI and choosing `lima` Namespace there should be
lots of Jobs visible (around 16), each representing different component (like `scb-bridge`
, `core-bridge`, `warehouse`, etc). Those Jobs combined makes up the whole system which is `sear`

+ `scb-bridge` and `core-bridge`.

Under single Nomad Job there can be multiple Tasks Groups, e.x.: Job `scb-bridge` has two Task
Groups:

- `scb-bridge`:
    - runs two sidecar tasks: [`fluentbit`](https://fluentbit.io/)
      and [`grpcui`](https://github.com/fullstorydev/grpcui)
    - runs main task: `scb-bridge` artefact as a java service
- `database`: runs postgres db inside docker

// TODO: what is `/srv/s8cluster` directory ? Is it just a directory on a host, or some mapped
folder backed by other FS ?

Make sure all of the tasks groups are running.

To see how deployment (so-called Allocation in Nomad's terms) went and view the component logs, go
to: `Nomad/Jobs/<click on a job name>/<find Recent Allocations and click>/<find Tasks and click>/Logs`

Note: Nodes - visible in Nomad `Topology` tab (e.x. `eu2`) - can be accessed via `ssh`, just make
request via helpdesk, need to give them your public `ssh` key. In `serp` this was used to
use `serp cli` scripts for testing.

# Release artifact to Minio and single component deployment

`scb-bridge` artefacts are copied to Minio during invocation of `deployOnNomad` stage (
inside `additionalStages`).

To release project's artefact to Minio, go
to [scb-bridge jenkins job](https://jenkins.silenteight.com/view/all/job/sens/job/sens%252Fscb-bridge/job/master/)
and click `Build with Parameters`. Do one of the following:

- Select `release` checkbox and do not select any `env`. The build will eventually fail, but it will
  copy the artefacts to Minio.
- No not select `release` but choose lima `env` - in that case, artifact will be copied to Minio and
  Nomad deployment will be attempted.

Be careful when selecting `env` as it should deploy to the selected environment the current version
which may not be compatible with other services, that's why it is safer to deploy via Nomad
deployment project as you may adjust versions of other components as well.

# Deployment via Nomad deployment project

Nomad Jobs descriptions used for deployment of the whole system to `lima` environment are defined
in [scb-nomad-lima-deployment](https://gitlab.silenteight.com/scb/scb-nomad-lima-deployment)
repository.

One can
find [there](https://gitlab.silenteight.com/scb/scb-nomad-lima-deployment/-/tree/master/nomad)
directories which corresponds to the components that have to be deployed and are copied from
specific projects (
like [scb-bridge nomad directory](https://gitlab.silenteight.com/sens/scb-bridge/-/tree/master/nomad))
, adjusted for `lima` environment.

Special attention deserves `version.vars` file which lists versions of components' artifacts that
needs to be deployed. The available versions can be checked
in [Minio](https://console.minio.silenteight.com/object-browser/artifacts), just enter the directory
that corresponds to the component and check artefacts inside. Only artefacts with `-exec.jar` suffix
are available as candidates to be deployed by Nomad.

If you have a problem with artifact's checksum during Nomad deployment, you can copy the checksum
from Nomad error log (visible in Nomad UI) and put it into `version.vars` and repeat the deployment
process.

Artefacts are downloaded by Nomad from Minio, so they must be there, and they are put there by
Jenkins as explained in above section.

## Changes to Nomad deployment and Deployment itself

If you wish to make changes to the deployment, create a branch
in [scb-nomad-lima-deployment](https://gitlab.silenteight.com/scb/scb-nomad-lima-deployment) and MR
with label `On-Hold`.

When jenkins builds this branch it will invoke `bin/deploy-all.sh` script which deploys all defined
there Nomad Jobs to `lima`. The script just invokes each service's `deploy.sh` which basically
do `nomad job run`.

Just push your changes to that branch and deployment will happen
via [Jenkins job](https://jenkins.silenteight.com/job/scb/job/scb%2Fscb-nomad-lima-deployment/view/change-requests/).

Our current branch: `feature/ALFA-91/add-bridge-deployment-configuration`

## Description of important Nomad files

```
nomad
├── conf
│   ├── application.yml
│   ├── fluent-bit.conf
│   ├── fluent-parsers.conf
│   └── logback.xml
├── deploy.sh
├── lima.vars
└── scb-bridge.nomad
```

- inside `conf` there are application specific configurations which are uploaded to `local`
  or `secrets` directory of Nomad allocation. Thanks to this you may have for example specific
  config for spring which is aware of lima environment.
- `scb-bridge.nomad` - description of the Nomad Job which should not change between envs
- `lima.vars` - parameters (variables) for the Nomad Job for `lima` env
- `deploy.sh` - glues together getting the artifact from Minio and deploying Job

## Consul

In order the components to see each other they must be configured in Consul.

Before you deploy any new component, go to: `Consule/Key/Value tab/lima` and create directory for
specific component. There you define secrets which are key-value pairs, which will be available for
the running component as an ENV variables.

### Rabbitmq

You can log in to [RabbitMQ](http://10.8.0.2:31131/rabbitmq/) and investigate queues and exchanges
and see some stats.

Note, you will have to create separate user for each new component deployed on the environment e.x.:
Name: `lima-scb-bridge`
Tags: `monitoring, management`
Can access virtual hosts: `/lima`

# Tips

- health checks - in nomad configuration file, we need to define check section for service:

    ```  
    check { name = "SCB Bridge HTTP Health Check"
      type = "http"
      path = "/rest/scb-bridge/management/health"
      method = "GET"
      interval = "30s"
      timeout = "10s"
    }
    ```

  Endpoint ```/rest/scb-bridge/management/health``` must be exposed in service. How to achieve that:

    - in application.yaml add section:
  ``` 
  management:
    endpoint:
      health.show-details: always 
    endpoints:
      web:
        base-path: /management 
        exposure:
          include: ["health", "info", "liquibase", "metrics", "prometheus", "loggers"]
  ```  

    - in nomad config file, in section where we start .jar file, add the following parameters to set
      context path for specific service:
  ```  
  "--server.servlet.context-path=/rest/scb-bridge",
  "--spring.webflux.base-path=/rest/scb-bridge",
  ```  

- port to http server & grpc server - in application.yaml file, override server.port &
  grpc.server.port properties in microservice to NOMAD_PORT_http & NOMAD_PORT_grpc.

  ```
  server:
    port: {{ env "NOMAD_PORT_http" }}

  grpc:
    server:
      port: {{ env "NOMAD_PORT_grpc" }}
  ```

- microservice cannot authorize in rabbitmq - sometimes we need to update password for user in
  rabbitmq when microservice cannot connect to rabbit server.

    - go to consul --> Key/Value --> lima (or which env you use) --> scb-bridge --> secrets --> copy
      property from SPRING_RABBITMQ_PASSWORD
    - got to rabbitmq UI --> Admin --> lima-scb-bridge --> Update this user --> set copied password

- traefik mapping to scb-bridge - in lima.vars file, set PathPrefix to '/rest/scb-bridge' in
  http_tags section like that:
  ```
  http_tags = [
    "traefik.http.routers.lima-scb-bridge.rule=Host(`lima.silenteight.com`) && PathPrefix(`/rest/scb-bridge`)",
    "traefik.http.routers.lima-scb-bridge.tls.certResolver=letsencrypt",
    "traefik.http.routers.lima-scb-bridge.service=lima-scb-bridge",
  ]
  ```

- consul settings - we need to provide settings for registration in consul properly to discover
  addresses to another services to communicate based on service name.

  ```
  spring:
    cloud:
      consul:
        enabled: true 
        host: localhost 
        port: 8500 
        scheme: http 
        discovery:
          enabled: true 
          register: false 
          register-health-check: false 
          catalog-services-watch:
            enabled: true
        service-registry:
          auto-registration:
            enabled: false
  ```

# Useful links

- [Into to Nomad](https://drive.google.com/file/d/1aZSuwcLeBYiUe93CGzNDbRaFQ0m8HbVG/view?usp=sharing)
  by Michael
- [SERP Component Overview](https://drive.google.com/drive/folders/1s4n17tH55ryBYUgqHoAfrpSRFoLUl_WU)
  by Andrzej

EOF
