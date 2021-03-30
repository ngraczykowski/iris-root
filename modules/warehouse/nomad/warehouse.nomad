variable "warehouse_artifact" {
  type = string
  description = "The name of file containing Warehouse artifact"
}

variable "warehouse_artifact_checksum" {
  type = string
  description = "Artifact SHA256 checksum should be provided"
}

variable "memory" {
  default = 1500
}

variable "namespace" {
  type    = string
  default = "dev"
}

variable "http_tags" {
  type = list(string)
  default = []
}

locals {
  jvm_memory = ceil(var.memory * 0.7)
  perm_memory = ceil(var.memory * 0.2)
  database_node_destination = "eu2"
  database_volume = "/srv/sep-cluster/postgres/${var.namespace}-warehouse"
}

job "warehouse" {
  type = "service"

  namespace = "${var.namespace}"

  datacenters = [
    "dc1"
  ]

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
        data        = "{{ key \"database/${var.namespace}-warehouse/secrets\" }}"
        destination = "secrets/warehouse.env"
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
        name = "${var.namespace}-warehouse-db"

        port = "tcp"

        check {
          type     = "tcp"
          interval = "30s"
          timeout  = "5s"
        }
      }

      resources {
        cpu    = 750
        memory = 1024
      }
    }
  }


  group "warehouse" {
    count = 1

    network {
      port "http" {
      }
      port "grpc" {
      }
    }

    service {
      name = "${var.namespace}-warehouse"
      port = "http"
      tags = concat([
        "http",
        "traefik.enable=true",
        # Those 2 tags are required to gather metrics by prometheus
        "traefik.protocol=http",
        "prometheus.metrics.path=/rest/warehouse/management/prometheus",
        # FIXME(ahaczewski): Remove when Consul Discovery can filter through results based on tags.
        "gRPC.port=${NOMAD_PORT_grpc}",
      ], var.http_tags)

      check_restart {
        limit = 3
        grace = "90s"
        ignore_warnings = false
      }

      check {
        name = "Warehouse HTTP Health Check"
        type = "http"
        path = "/rest/warehouse/management/health"
        method = "GET"
        interval = "10s"
        timeout = "2s"
      }
    }

    service {
      name = "${var.namespace}-warehouse"
      port = "grpc"
      tags = [
        "grpc",
        # FIXME(ahaczewski): Remove when Consul Discovery can filter through results based on tags.
        "gRPC.port=${NOMAD_PORT_grpc}",
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
      name = "${var.namespace}-grpc-warehouse"
      port = "grpc"
      tags = [
        "grpc",
        "gRPC.port=${NOMAD_PORT_grpc}",
      ]
    }

    task "warehouse" {
      driver = "raw_exec"

      artifact {
        source = var.warehouse_artifact
        options {
          checksum = "${var.warehouse_artifact_checksum}"
        }
        mode = "file"
        destination = "local/warehouse-app.jar"
      }

      template {
        data        = "{{ key \"${var.namespace}/warehouse/secrets\" }}"
        destination = "secrets/warehouse.env"
        env = true
      }

      template {
        data = file("./conf/application.yml")
        destination = "local/conf/application.yml"
        change_mode = "restart"
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
          "-jar",
          "local/warehouse-app.jar",
          "--spring.profiles.active=linux,warehouse,rabbitmq,messaging",
          "--spring.config.additional-location=file:local/conf/"
        ]
      }

      logs {
        max_files = 10
        max_file_size = 20
      }

      resources {
        cpu = 1000
        memory = var.memory
      }
    }
  }
}
