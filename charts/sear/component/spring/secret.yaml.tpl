{{- if and .component.db.enabled .component.db.external }}
apiVersion: v1
kind: Secret
metadata:
  name: {{ include "sear.postgresqlSecretName" . }}
  labels:
    {{- include "sear.componentLabels" . | nindent 4 }}
data:
  password: {{ .component.db.password | b64enc }}
  username: {{ .component.db.username | b64enc }}
type: Opaque
---
{{- end }}
{{- if and .component.rabbit.enabled .component.rabbit.external }}

apiVersion: v1
kind: Secret
metadata:
  name: {{ include "sear.rabbitmqSecretName" . }}
  labels:
    {{- include "sear.componentLabels" . | nindent 4 }}
data:
  password: {{ .component.rabbit.password | b64enc }}
  username: {{ .component.rabbit.username | b64enc }}
  port: {{ .component.rabbit.port | b64enc }}
  host: {{ .component.rabbit.host | b64enc }}
type: Opaque
---
{{- end }}
