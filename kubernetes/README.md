# Iris Kubernetes Deployment

This directory contains [values files](https://helm.sh/docs/chart_template_guide/values_files/) for deploying Iris in different configurations.

## Overview

The values required to deploy a working system are:

- Core
- Ingress
- Environment-specific bridge and agents

### Core

The core of the product is deployed with `values.core.yaml`. Deploying this configuration is necessary for the system to work.

### Ingress

Besides the Core, system must have ingress configured. Right now there is only single ingress type, `values.ingress-internal.yaml`, which grants access to the environment via ingress controller available through Silent Eight VPN.

### Bridge and agents

The system needs a bridge, which is responsible for receiving and ETLing alerts, as well as environment-specific set of agents. Sets of these are pre-configured in `values.*.yaml` files.

## Deploying environment

### Creating new namespace

To deploy new environment, you should create an individual namespace and set current context namespace to it, so all subsequent `kubectl` or `helm` commands run in that namespace:

```bash
kubectl create namespace dev-<your username>
kubectl config set-context --current --namespace=dev-<your username>
```

### Installing chart

Before installing the chart, run the following command to grab chart dependencies:

```bash
helm dependency build ../charts/sear
```

Install required plugin

```bash
helm plugin install https://github.com/jkroepke/helm-secrets
```

Install `vals` from https://github.com/variantdev/vals (by fetching the binary)

```bash
wget -qO- https://github.com/variantdev/vals/releases/download/v0.18.0/vals_0.18.0_linux_amd64.tar.gz | tar xvz vals
```

To install the chart, run the following command, replacing `<variables>` as documented below the command:

```bash
#!/bin/bash

ENV=<environment> \
RELEASE_NAME=<release-name> \
NAMESPACE=<namespace> \
HELM_SECRETS_VALS_PATH="\$(which vals | grep . || echo ./vals)" \
helm secrets -d vals upgrade --install \
  --namespace $NAMESPACE \
  --values values.core.yaml \
  --values values.ingress-internal.yaml \
  --values values.$ENV.yaml \
  --set keycloak.ingress.hostname=$RELEASE_NAME-$NAMESPACE.prv.dev.s8ops.com \
  --set keycloak.externalDatabase.host=$RELEASE_NAME-postgres.$NAMESPACE.svc \
  --set keycloak.externalDatabase.existingSecret=keycloak.$RELEASE_NAME-postgres.credentials.postgresql.acid.zalan.do \
  $RELEASE_NAME \
  ../charts/sear
```

For `<environment>` choose one of the provided environment values files (e.g., `sierra`, `hotel`, `foxtrot`).

For `<release-name>` you can put any name you want (have to be unique in the namespace, e.g., `sear-sierra`). It allows you to install multiple releases of the chart to a single namespace.

For `<namespace>` put your individual namespace name.

### Watching the deployment progress

You can observe the deployment progress watching pods:

```bash
kubectl get pods --watch
```

### Uninstalling chart

The running environment costs real world money. If you do not need the environment, consider uninstalling it.

To uninstall the chart, run the following command:

```bash
helm uninstall <release name>
```

### Upgrading chart

When something changes in the chart, you can upgrade your environment by running the command in [Installing chart](#installing-chart).

### Secret values

with secrets plugin ("...secrets -d vals...") helm now supports secret rendering. SSM example:

```yaml
myValue: ref+awsssm://pp/test/param?region=eu-central-1
```

more info: https://github.com/variantdev/vals#cli ()

In order for given secret type to work, one has to make sure the evnironment is contains proper credentials.
EG: `VAULT_TOKEN` for vault, `AWS_*` for awsssm etc...
