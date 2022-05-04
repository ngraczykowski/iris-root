apiVersion: v1
kind: Service
metadata:
  name: {{ include "sear.componentName" . }}
  labels:
    {{- include "sear.componentLabels" . | nindent 4 }}
spec:
  type: {{ .component.service.type }}
  ports:
    - port: {{ .component.service.http.port }}
      targetPort: http
      protocol: TCP
      name: http
    {{- if .component.service.grpc.enabled }}
    - port: {{ .component.service.grpc.port }}
      targetPort: grpc
      protocol: TCP
      name: grpc
    {{- end }}
  selector:
    {{- include "sear.componentSelectorLabels" . | nindent 4 }}
