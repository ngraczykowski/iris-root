variable "scb_bridge_artifact" {
  type = string
  description = "The name of file containing SCB-Bridge artifact"
}

variable "scb_bridge_artifact_checksum" {
  type = string
  description = "Artifact SHA256 checksum should be provided"
}

variable "namespace" {
  type = string
  default = "lima"
}

variable "memory" {
  default = 4096
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
  database_node_destination = "eu3"
  database_volume = "/srv/sep-cluster/postgres/${var.namespace}-scb-bridge"
}

job "scb-bridge" {
  type = "service"

  datacenters = [
    "dc1"
  ]

  namespace = "${var.namespace}"

  update {
    auto_revert = true
  }

  group "database" {
    count = 1

    constraint {
      attribute = "${node.unique.name}"
      value     = "${local.database_node_destination}"
    }

    network {
      port "tcp" {
        to = 5432
      }
    }

    task "postgres" {
      driver = "docker"

      template {
        data        = "{{ key \"database/${var.namespace}-scb-bridge/secrets\" }}"
        destination = "secrets/scb-bridge-db.env"
        env         = true
      }

      config {
        image   = "postgres:10"
        ports   = [
          "tcp"
        ]
        volumes = [
          "${local.database_volume}:/var/lib/postgresql/data"
        ]
      }

      service {
        name = "${var.namespace}-scb-bridge-db"

        port = "tcp"

        check {
          type     = "tcp"
          interval = "30s"
          timeout  = "5s"
        }
      }

      resources {
        cpu    = 2048
        # MHz
        memory = 2048
        # MB
      }
    }
  }

  group "scb-bridge" {
    count = 1

    network {
      port "http" {
      }
      port "grpc" {
      }
      port "grpcui" {
      }
    }

    service {
      name = "${var.namespace}-scb-bridge"
      port = "http"
      tags = concat([
        "http",
        "traefik.enable=true",
        # Those 2 tags are required to gather metrics by prometheus
        "traefik.protocol=http",
        "prometheus.metrics.path=/rest/scb-bridge/management/prometheus",
        # FIXME(ahaczewski): Remove when Consul Discovery can filter through results based on tags.
        "gRPC.port=${NOMAD_PORT_grpc}",
        "gRPC_port=${NOMAD_PORT_grpc}",
      ], var.http_tags)

      check_restart {
        limit = 5
        grace = "180s"
        ignore_warnings = true
      }

      check {
        name     = "SCB Bridge HTTP Health Check"
        type     = "http"
        path     = "/rest/scb-bridge/management/health"
        method   = "GET"
        interval = "30s"
        timeout  = "10s"
      }
    }

    service {
      name = "${var.namespace}-scb-bridge"
      port = "grpc"
      tags = [
        "grpc",
        # FIXME(ahaczewski): Remove when Consul Discovery can filter through results based on tags.
        "gRPC.port=${NOMAD_PORT_grpc}",
        "gRPC_port=${NOMAD_PORT_grpc}",
      ]

#     check_restart {
#       limit           = 3
#       grace           = "90s"
#       ignore_warnings = false
#     }

      check {
        name     = "gRPC Port Alive Check"
        type     = "tcp"
        interval = "30s"
        timeout  = "10s"
      }
    }

    service {
      name = "${var.namespace}-grpcui-scb-bridge"
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

      restart {
        interval = "5m"
        attempts = 10
        delay = "20s"
        mode = "delay"
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

    task "scb-bridge" {
      driver = "raw_exec"

      artifact {
        source = var.scb_bridge_artifact
        options {
          checksum = "${var.scb_bridge_artifact_checksum}"
        }
        mode = "file"
        destination = "local/scb-bridge.jar"
      }

      template {
        data = "{{ key \"${var.namespace}/scb-bridge/secrets\" }}"
        destination = "secrets/scb-bridge.env"
        env = true
      }

      template {
        data = file("./conf/application.yaml")
        destination = "local/conf/application.yaml"
        change_mode = "restart"
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
        command = "/srv/s8cluster/java17/bin/java"
        args    = [
          format("-Xms%dm", local.jvm_memory),
          format("-Xmx%dm", local.jvm_memory),
          format("-XX:MaxMetaspaceSize=%dm", local.perm_memory),
          "-Dfile.encoding=UTF-8",
          "-Dsun.jnu.encoding=UTF-8",
          "-Djava.net.preferIPv4Stack=true",
          "-Djava.io.tmpdir=${meta.silenteight.home}/tmp",
          "-Dlogging.config=secrets/conf/logback.xml",
          "-jar",
          "local/scb-bridge.jar",
          "--spring.profiles.active=linux",
          "--spring.config.additional-location=file:local/conf/",
          "--server.servlet.context-path=/rest/scb-bridge",
          "--spring.webflux.base-path=/rest/scb-bridge",
          "--management.endpoints.web.base-path=/management",
          "--spring.rabbitmq.virtual-host=/${var.namespace}",
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
