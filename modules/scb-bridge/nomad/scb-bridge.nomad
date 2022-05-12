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

locals {
  jvm_memory = ceil(var.memory * 0.7)
  perm_memory = ceil(var.memory * 0.2)
  database_node_destination = "eu3"
  database_volume = "/srv/sep-cluster/postgres12/${var.namespace}-scb-bridge"
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
        image   = "postgres:12"
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
        interval = "60s"
        timeout  = "10s"
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
        change_mode = "noop"
      }

      template {
        data = file("./conf/qco_config_file.csv")
        destination = "local/conf/qco_config_file.csv"
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
          "--spring.profiles.active=consul",
          "--spring.cloud.consul.config.prefix=${var.namespace}/config",
          "--spring.config.additional-location=file:local/conf/",
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
        cpu = 150
        memory = 200
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
