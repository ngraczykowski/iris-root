variable "connector_artifact" {
  type = string
  description = "The name of file containing date Agent artifact"
}

variable "connector_artifact_version" {
  type = string
  description = "The name of file containing Adjudication Engine artifact"
}

variable "connector_artifact_checksum" {
  type = string
  description = "Artifact SHA256 checksum should be provided"
}

variable "namespace" {
  type    = string
  default = "foxtrot"
}

variable "memory" {
  default = 1500
}

variable "http_tags" {
  type    = list(string)
  default = []
}

variable "firco_connector_http_port" {
  type = number
  default = 30844
}

variable "grpcui_tags" {
  type    = list(string)
  default = []
}

variable "node_destination" {
  type    = string
  default = "eu3"
}

locals {
  jvm_memory                = ceil(var.memory * 0.7)
  perm_memory               = ceil(var.memory * 0.2)
  database_node_destination = "eu3"
  database_volume           = "/srv/sep-cluster/postgres12/${var.namespace}-firco-trust-cmapi-connector"
}

job "firco-trust-cmapi-connector" {
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
        data        = "{{ key \"database/${var.namespace}-firco-trust-cmapi-connector/secrets\" }}"
        destination = "secrets/firco-trust-cmapi-connector-db.env"
        env         = true
      }

      config {
        image = "postgres:12"
        ports = [
          "tcp"
        ]
        volumes = [
          "${local.database_volume}:/var/lib/postgresql/data"
        ]
      }

      service {
        name = "${var.namespace}-firco-trust-cmapi-connector-db"

        port = "tcp"

        check {
          type     = "tcp"
          interval = "30s"
          timeout  = "5s"
        }
      }

      resources {
        cpu    = 1024
        # MHz
        memory = 1024
        # MB
      }
    }
  }

  group "firco-trust-cmapi-connector" {
    count = 1

    constraint {
      attribute = "${node.unique.name}"
      value     = var.node_destination
    }

    network {
      port "http" {
        static = var.firco_connector_http_port
      }
      port "grpc" {
      }
      port "grpcui" {
      }
    }

    service {
      name = "${var.namespace}-firco-trust-cmapi-connector"
      port = "http"
      tags = concat([
        "http",
        "traefik.enable=true",
        # Those 2 tags are required to gather metrics by prometheus
        "traefik.protocol=http",
        # FIXME(ahaczewski): Remove when Consul Discovery can filter through results based on tags.
        "gRPC.port=${NOMAD_PORT_grpc}",
        "gRPC_port=${NOMAD_PORT_grpc}",
      ], var.http_tags)

      check_restart {
        limit           = 3
        grace           = "90s"
        ignore_warnings = false
      }

      check {
        name     = "Core Bridge HTTP Health Check"
        type     = "http"
        path     = "/rest/ftcc/management/health"
        method   = "GET"
        interval = "30s"
        timeout  = "10s"
      }
    }

    service {
      name = "${var.namespace}-firco-trust-cmapi-connector"
      port = "grpc"
      tags = [
        "grpc",
        # FIXME(ahaczewski): Remove when Consul Discovery can filter through results based on tags.
        "gRPC.port=${NOMAD_PORT_grpc}",
        "gRPC_port=${NOMAD_PORT_grpc}",
      ]

      check_restart {
        limit           = 3
        grace           = "90s"
        ignore_warnings = false
      }

      check {
        name     = "gRPC Port Alive Check"
        type     = "tcp"
        interval = "30s"
        timeout  = "10s"
      }
    }

    # Dummy registration of a service required for Spring Consul Discovery.
    # FIXME(ahaczewski): Remove when Consul Discovery can filter through results based on tags.
    service {
      name = "${var.namespace}-grpc-firco-trust-cmapi-connector"
      port = "grpc"
      tags = [
        "grpc",
        "gRPC.port=${NOMAD_PORT_grpc}",
        "gRPC_port=${NOMAD_PORT_grpc}",
      ]
    }

    service {
      name = "${var.namespace}-grpcui-firco-trust-cmapi-connector"
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
        hook    = "poststart"
        sidecar = true
      }

      restart {
        interval = "1m"
        attempts = 5
        delay    = "20s"
        mode     = "delay"
      }

      artifact {
        source = "https://github.com/fullstorydev/grpcui/releases/download/v1.1.0/grpcui_1.1.0_linux_x86_64.tar.gz"
        options {
          checksum = "sha256:41b9b606a025561f7df892e78a8ac1819597ed74d2300183797ab8caa7b290a6"
        }
      }

      config {
        command = "grpcui"
        args    = [
          "-plaintext",
          "-bind=${NOMAD_IP_grpcui}",
          "-port=${NOMAD_PORT_grpcui}",
          "-open-browser=false",
          "${NOMAD_ADDR_grpc}"
        ]
      }

      resources {
        cpu    = 50
        memory = 100
      }
    }

    task "connector" {
      driver = "raw_exec"

      artifact {
        source = var.connector_artifact
        options {
          checksum = "${var.connector_artifact_checksum}"
        }
        destination = "local"
      }

      template {
        data        = "{{ key \"${var.namespace}/firco-trust-cmapi-connector/secrets\" }}"
        destination = "secrets/firco-trust-cmapi-connector.env"
        env         = true
      }

      template {
        data        = file("./conf/${var.namespace}-application.yml")
        destination = "local/conf/application.yml"
        change_mode = "restart"
      }

      env {
        LOG_PATH = "${NOMAD_ALLOC_DIR}/logs"
      }

      config {
        command = "java"
        args    = [
          format("-Xms%dm", local.jvm_memory),
          format("-Xmx%dm", local.jvm_memory),
          format("-XX:MaxMetaspaceSize=%dm", local.perm_memory),
          "-Dfile.encoding=UTF-8",
          "-Dsun.jnu.encoding=UTF-8",
          "-Djava.net.preferIPv4Stack=true",
          "-Djava.io.tmpdir=${meta.silenteight.home}/tmp",
          "-jar",
          "local/ftcc-app-${var.connector_artifact_version}-exec.jar",
          "--spring.profiles.active=linux,consul,mockcallback",
          "--spring.config.additional-location=file:local/conf/",
        ]
      }

      logs {
        max_files     = 10
        max_file_size = 20
      }

      resources {
        cpu    = 400
        memory = var.memory
      }
    }
  }
}
