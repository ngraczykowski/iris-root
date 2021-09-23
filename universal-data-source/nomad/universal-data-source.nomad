variable "universal_data_source_artifact" {
  type        = string
  description = "The name of file containing Universal Data Source artifact"
}

variable "universal_data_source_artifact_checksum" {
  type        = string
  description = "Artifact SHA256 checksum should be provided"
    default = "sha256:7819fc442a13302a38ba29dd7d36b7da7db5f14ee157545e19578eb70866959d"
}

variable "namespace" {
  type    = string
  default = "default"
}

variable "memory" {
  default = 1500
}

variable "http_tags" {
  type    = list(string)
  default = []
}

variable "grpcui_tags" {
  type    = list(string)
  default = []
}

locals {
  jvm_memory                = ceil(var.memory * 0.7)
  perm_memory               = ceil(var.memory * 0.2)
    database_node_destination = "eu2"
    database_volume           = "/srv/sep-cluster/postgres/${var.namespace}-universal-data-source"
}

job "universal-data-source" {
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
          data        = "{{ key \"database/${var.namespace}-universal-data-source/secrets\" }}"
          destination = "secrets/universal-data-source-db.env"
          env         = true
        }

        config {
          image   = "postgres:10.18"
          ports   = [
            "tcp"
          ]
          volumes = [
            "${local.database_volume}:/var/lib/postgresql/data"
          ]
        }

        service {
          name = "${var.namespace}-universal-data-source-db"

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
          memory = 4096
          # MB
        }
      }
    }

  group "universal-data-source" {
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
      name = "${var.namespace}-universal-data-source"
      port = "http"
      tags = concat([
        "http",
        "traefik.enable=true",
        # Those 2 tags are required to gather metrics by prometheus
        "traefik.protocol=http",
        "prometheus.metrics.path=/management/c",
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
        name     = "Universal Data Source HTTP Health Check"
        type     = "http"
        path     = "/management/health"
        method   = "GET"
        interval = "30s"
        timeout  = "2s"
      }
    }

    service {
      name = "${var.namespace}-universal-data-source"
      port = "grpc"
      tags = [
        "grpc",
        # FIXME(ahaczewski): Remove when Consul Discovery can filter through results based on tags.
        "gRPC.port=${NOMAD_PORT_grpc}",
      ]

      check_restart {
        limit           = 3
        grace           = "90s"
        ignore_warnings = false
      }

      check {
        name     = "gRPC Port Alive Check"
        type     = "tcp"
        interval = "10s"
        timeout  = "2s"
      }
    }

    # Dummy registration of a service required for Spring Consul Discovery.
    # FIXME(ahaczewski): Remove when Consul Discovery can filter through results based on tags.
    service {
      name = "${var.namespace}-grpc-universal-data-source"
      port = "grpc"
      tags = [
        "grpc",
        "gRPC.port=${NOMAD_PORT_grpc}",
      ]
    }

    task "universal-data-source" {
      driver = "raw_exec"

      artifact {
        source      = var.universal_data_source_artifact
        options {
          checksum = "${var.universal_data_source_artifact_checksum}"
        }
        mode        = "file"
        destination = "local/universal-data-source-app.jar"
      }

      template {
        data        = "{{ key \"${var.namespace}/universal-data-source/secrets\" }}"
        destination = "secrets/universal-data-source.env"
        env         = true
      }

      template {
        data = "{{ keyOrDefault \"${var.namespace}/universal-data-source/environment\" \"\" }}"
        destination = "local/universal-data-source.env"
        env = true
      }

      template {
        data = file("./conf/application.yml")
        destination = "local/conf/application.yml"
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
          "-Dlogging.config=secrets/conf/logback.xml",
          "-jar",
          "local/universal-data-source-app.jar",
          "--spring.config.additional-location=file:local/conf/",
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

    service {
      name = "${var.namespace}-universal-data-source-grpcui"
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
  }
}
