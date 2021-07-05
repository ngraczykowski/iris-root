variable "governance_artifact" {
  type = string
  description = "The name of file containing Governance artifact"
}

variable "governance_artifact_checksum" {
  type = string
  description = "Artifact SHA256 checksum should be provided"
}

variable "namespace" {
  type = string
  default = "dev"
}

variable "memory" {
  default = 1500
}

variable "http_tags" {
  type = list(string)
  default = []
}

variable "grpcui_tags" {
  type = list(string)
  default = []
}

locals {
  jvm_memory = ceil(var.memory * 0.7)
  perm_memory = ceil(var.memory * 0.2)
}

job "governance" {
  type = "service"

  datacenters = [
    "dc1"
  ]

  namespace = "${var.namespace}"

  update {
    auto_revert = true
  }

  group "governance" {
    count = 2

    network {
      port "http" {
      }
      port "grpc" {
      }
      port "grpcui" {
      }
    }

    service {
      name = "${var.namespace}-governance"
      port = "http"
      tags = concat([
        "http",
        "traefik.enable=true",
        # Those 2 tags are required to gather metrics by prometheus
        "traefik.protocol=http",
        "prometheus.metrics.path=/rest/governance/management/prometheus",
        # FIXME(ahaczewski): Remove when Consul Discovery can filter through results based on tags.
        "gRPC.port=${NOMAD_PORT_grpc}",
        "gRPC_port=${NOMAD_PORT_grpc}",
      ], var.http_tags)

      check_restart {
        limit = 3
        grace = "90s"
        ignore_warnings = false
      }

      check {
        name = "Governance HTTP Health Check"
        type = "http"
        path = "/rest/governance/management/health"
        method = "GET"
        interval = "10s"
        timeout = "2s"
      }
    }

    service {
      name = "${var.namespace}-governance"
      port = "grpc"
      tags = [
        "grpc",
        # FIXME(ahaczewski): Remove when Consul Discovery can filter through results based on tags.
        "gRPC.port=${NOMAD_PORT_grpc}",
        "gRPC_port=${NOMAD_PORT_grpc}",
      ]

      check_restart {
        limit = 3
        grace = "90s"
        ignore_warnings = false
      }

      check {
        name = "gRPC Port Alive Check"
        type = "tcp"
        interval = "10s"
        timeout = "2s"
      }
    }

    # Dummy registration of a service required for Spring Consul Discovery.
    # FIXME(ahaczewski): Remove when Consul Discovery can filter through results based on tags.
    service {
      name = "${var.namespace}-grpc-governance"
      port = "grpc"
      tags = [
        "grpc",
        "gRPC.port=${NOMAD_PORT_grpc}",
        "gRPC_port=${NOMAD_PORT_grpc}",
      ]
    }

        service {
          name = "${var.namespace}-grpcui-governance"
          port = "grpcui"
          tags = concat([
            "grpcui",
            "traefik.enable=true",
            "traefik.protocol=http",
          ], var.grpcui_tags)
        }

        task "grpcui" {
          driver = "raw_exec"

          lifecycle {
            hook = "poststart"
            sidecar = true
          }

          artifact {
            source = "https://github.com/fullstorydev/grpcui/releases/download/v1.1.0/grpcui_1.1.0_linux_x86_64.tar.gz"
            options {
              checksum = "sha256:41b9b606a025561f7df892e78a8ac1819597ed74d2300183797ab8caa7b290a6"
            }
          }

          config {
            command = "grpcui"
            args = [
              "-plaintext",
              "-bind=${NOMAD_IP_grpcui}",
              "-port=${NOMAD_PORT_grpcui}",
              "-open-browser=false",
              "${NOMAD_ADDR_grpc}"
            ]
          }

          resources {
            cpu = 50
            memory = 100
          }
        }

    task "governance" {
      driver = "raw_exec"

      artifact {
        source = var.governance_artifact
        options {
          checksum = "${var.governance_artifact_checksum}"
        }
        mode = "file"
        destination = "local/governance-app.jar"
      }

      template {
        data = "{{ key \"${var.namespace}/governance/secrets\" }}"
        destination = "secrets/governance.env"
        env = true
      }

      template {
        data = file("./conf/application.yml")
        destination = "local/conf/application.yml"
        change_mode = "noop"
      }

      template {
        data = file("./conf/application-database.yml")
        destination = "local/conf/application-database.yml"
        change_mode = "noop"
      }

      template {
        data = file("./conf/application-messaging.yml")
        destination = "local/conf/application-messaging.yml"
        change_mode = "noop"
      }

      template {
        data = file("./conf/logback.xml")
        destination = "secrets/conf/logback.xml"
        change_mode = "noop"
      }

      env {
        LOG_PATH = "${NOMAD_ALLOC_DIR}/logs"
      }

      config {
        command = "java"
        args = [
          format("-Xms%dm", local.jvm_memory),
          format("-Xmx%dm", local.jvm_memory),
          format("-XX:MaxPermSize=%dm", local.perm_memory),
          "-Dfile.encoding=UTF-8",
          "-Dsun.jnu.encoding=UTF-8",
          "-Djava.net.preferIPv4Stack=true",
          "-Djava.io.tmpdir=${meta.silenteight.home}/tmp",
          "-Dlogging.config=secrets/conf/logback.xml",
          "-jar",
          "local/governance-app.jar",
          "--spring.profiles.active=linux,governance,database,rabbitmq,messaging",
          "--spring.config.additional-location=file:local/conf/"
        ]
      }

      logs {
        max_files = 10
        max_file_size = 20
      }

      resources {
        cpu = 400
        memory = var.memory
      }
    }

    task "fluentbit" {
      driver = "docker"

      lifecycle {
        hook = "prestart"
        sidecar = true
      }

      config {
        image = "fluent/fluent-bit:1.7.7"
        network_mode = "host"
        volumes = [
          "secrets/fluent-bit.conf:/fluent-bit/etc/fluent-bit.conf",
          "local/fluent-parsers.conf:/fluent-bit/etc/fluent-parsers.conf",
        ]
      }

      resources {
        cpu = 50
        memory = 100
      }

      template {
        data = file("./conf/fluent-bit.conf")
        destination = "secrets/fluent-bit.conf"
      }

      template {
        data = file("./conf/fluent-parsers.conf")
        destination = "local/fluent-parsers.conf"
      }
    }
  }
}
