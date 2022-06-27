# Deploying locally built images

## Deploy own docker registry with following command:

`helm install --namespace=<MY_NAMESPACE> --values=<values.yaml> twuni/docker-registry --generate-name`

with following values file:

```yaml
image:
  tag: latest

ingress:
  enabled: true
  className: ""
  annotations:
    kubernetes.io/ingress.class: nginx-internal
    nginx.ingress.kubernetes.io/proxy-body-size: 0
  hosts:
    - <my_repo>.prv.dev.s8ops.com

```

<my_repo> should be unique in the cluster

## Use the registry when deploying docker:

```
gradlew -DjibRegistry=<my_repo>.prv.dev.s8ops.com :<my>:<module>:jib
```

3. Refer to the image when deploying image:

```yaml
...
image: <my_repo>.prv.dev.s8ops.com/iris/the/image
tag: "HEAD"
```
