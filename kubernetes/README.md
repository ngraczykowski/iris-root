# Iris Kubernetes Deployment

This directory contains [values files](https://helm.sh/docs/chart_template_guide/values_files/) for deploying Iris in different configurations.

## Overview

The minimum values required to deploy working system are:

- Core
- Ingress

### Core

The core of the product is deployed with `values.core.yaml`. Deploying this configuration is necessary for the system to work.

### Ingress

Besides the Core, system must have ingress configured. Right now there is only single ingress type, `values.ingress-internal.yaml`, which grants access to the environment via ingress controller available through Silent Eight VPN.

## Deploying environment

### Creating new namespace

To deploy new environment, you should create an individual namespace and set current context namespace to it, so all subsequent `kubectl` or `helm` commands run in that namespace:

```bash
kubectl create namespace dev-<your username>
kubectl config set-context --current --namespace=dev-<your username>
```

### Installing chart

To install the chart, run the following command, replacing `<environment>` with the name of environment you want to deploy:

```bash
helm upgrade --install --namespace=dev-<your username> \
  --values values.core.yaml \
  --values values.ingress-internal.yaml \
  --values values.<environment>.yaml \
  --set ingress.hosts[0]=<your environment url>.dev.s8ops.com \
  <release name> \
  ../charts/sear
```

For `<environment>` choose one of the provided environment values files.

For `<release name>` you can put any name you want (e.g., `sear`). It allows you to install multiple releases of the chart to a single namespace.

For `<your environment url>` you can put any DNS-compatible name. This will be the URL you access your environment with.

### Watching the deployment progress

You can observe the deployment progress watching pods:

```bash
kubectl get pods --watch
```

### Uninstalling chart

The running environment costs real world money. If you do not need the environment, consider uninstalling it.

To uninstall the chart, run the following command:

```bash
helm uninstall --namespace=dev-<your username> <release name>
```

### Upgrading chart

When something changes in the chart, you can upgrade your environment by running the command in [Installing chart](#installing-chart).
