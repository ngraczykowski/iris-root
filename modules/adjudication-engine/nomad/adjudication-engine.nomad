variable "adjudication_engine_artifact" {
  type        = string
  description = "The name of file containing Adjudication Engine artifact"
}

variable "adjudication_engine_artifact_checksum" {
  type        = string
  description = "Artifact SHA256 checksum should be provided"
}

variable "namespace" {
  type    = string
  default = "dev"
}

variable "memory" {
  default = 1500
}

variable "http_tags" {
  type    = list(string)
  default = []
}

locals {
  jvm_memory                = ceil(var.memory * 0.7)
  perm_memory               = ceil(var.memory * 0.2)
  database_node_destination = "eu2"
  database_volume           = "/srv/sep-cluster/postgres/${var.namespace}-adjudication-engine"
}

job "adjudication-engine" {
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
        data        = "{{ key \"database/${var.namespace}-adjudication-engine/secrets\" }}"
        destination = "secrets/adjudication-engine-db.env"
        env         = true
      }

      config {
        image   = "postgres:10"
        ports   = [
          "tcp"]
        volumes = [
          "${local.database_volume}:/var/lib/postgresql/data"
        ]
      }

      service {
        name = "${var.namespace}-adjudication-engine-db"

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

  group "adjudication-engine" {
    count = 2

    network {
      port "http" {
      }
      port "grpc" {
      }
    }

    service {
      name = "${var.namespace}-adjudication-engine"
      port = "http"
      tags = concat([
        "http",
        "traefik.enable=true",
        # Those 2 tags are required to gather metrics by prometheus
        "traefik.protocol=http",
        "prometheus.metrics.path=/rest/ae/management/prometheus",
        # FIXME(ahaczewski): Remove when Consul Discovery can filter through results based on tags.
        "gRPC.port=${NOMAD_PORT_grpc}",
      ], var.http_tags)

      check_restart {
        limit           = 3
        grace           = "90s"
        ignore_warnings = false
      }

      check {
        name     = "Adjudication Engine HTTP Health Check"
        type     = "http"
        path     = "/rest/ae/management/health"
        method   = "GET"
        interval = "10s"
        timeout  = "2s"
      }
    }

    service {
      name = "${var.namespace}-adjudication-engine"
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
      name = "${var.namespace}-grpc-adjudication-engine"
      port = "grpc"
      tags = [
        "grpc",
        "gRPC.port=${NOMAD_PORT_grpc}",
      ]
    }

    task "adjudication-engine" {
      driver = "raw_exec"

      artifact {
        source      = var.adjudication_engine_artifact
        options {
          checksum = "${var.adjudication_engine_artifact_checksum}"
        }
        mode        = "file"
        destination = "local/adjudication-engine-app.jar"
      }

      template {
        data        = "{{ key \"${var.namespace}/adjudication-engine/secrets\" }}"
        destination = "secrets/adjudication-engine.env"
        env         = true
      }

      template {
        data        = file("./conf/application.yml")
        destination = "local/conf/application.yml"
        change_mode = "noop"
      }

      config {
        command = "java"
        args    = [
          format("-Xms%dm", local.jvm_memory),
          format("-Xmx%dm", local.jvm_memory),
          format("-XX:MaxPermSize=%dm", local.perm_memory),
          "-Dfile.encoding=UTF-8",
          "-Dsun.jnu.encoding=UTF-8",
          "-Djava.net.preferIPv4Stack=true",
          "-Djava.io.tmpdir=${meta.silenteight.home}/tmp",
          "-jar",
          "local/adjudication-engine-app.jar",
          "--spring.profiles.active=linux,adjudication-engine,database,rabbitmq,messaging",
          "--spring.config.additional-location=file:local/conf/"
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
