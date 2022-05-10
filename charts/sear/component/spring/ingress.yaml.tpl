{{- if .component.ingress.enabled -}}
{{- $fullName := include "sear.componentName" . -}}
{{- $svcPort := .component.service.http.port -}}
{{- if and .component.ingress.className (not (semverCompare ">=1.18-0" .Capabilities.KubeVersion.GitVersion)) }}
  {{- if not (hasKey .component.ingress.annotations "kubernetes.io/ingress.class") }}
  {{- $_ := set .component.ingress.annotations "kubernetes.io/ingress.class" .component.ingress.className}}
  {{- end }}
{{- end }}
{{- if semverCompare ">=1.19-0" .Capabilities.KubeVersion.GitVersion -}}
apiVersion: networking.k8s.io/v1
{{- else if semverCompare ">=1.14-0" .Capabilities.KubeVersion.GitVersion -}}
apiVersion: networking.k8s.io/v1beta1
{{- else -}}
apiVersion: extensions/v1beta1
{{- end }}
kind: Ingress
metadata:
  name: {{ $fullName }}
  labels:
    {{- include "sear.componentLabels" . | nindent 4 }}
  {{- with .Values.ingress.annotations }}
  annotations:
    {{- toYaml . | nindent 4 }}
  {{- end }}
spec:
  {{- if and .component.ingress.className (semverCompare ">=1.18-0" .Capabilities.KubeVersion.GitVersion) }}
  ingressClassName: {{ .component.ingress.className }}
  {{- end }}
  {{- if .Values.ingress.tls }}
  tls:
    {{- range .Values.ingress.tls }}
    - hosts:
        {{- range .hosts }}
        - {{ . | quote }}
        {{- end }}
      secretName: {{ .secretName }}
    {{- end }}
  {{- end }}
  rules:
    {{- $hosts := .Values.ingress.hosts | default (list (include "sear.defaultHost" .)) }}
    {{- range $hosts }}
    - host: {{ . | quote }}
      http:
        paths:
          - path: /rest/{{ $.component.webPath }}
            {{- if semverCompare ">=1.18-0" $.Capabilities.KubeVersion.GitVersion }}
            pathType: Prefix
            {{- end }}
            backend:
              {{- if semverCompare ">=1.19-0" $.Capabilities.KubeVersion.GitVersion }}
              service:
                name: {{ $fullName }}
                port:
                  number: {{ $svcPort }}
              {{- else }}
              serviceName: {{ $fullName }}
              servicePort: {{ $svcPort }}
              {{- end }}
    {{- end }}
{{- end }}
