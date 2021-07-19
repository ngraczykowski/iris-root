variable "simulator_artifact" {
  type = string
  description = "The name of file containing Simulator artifact"
}

variable "simulator_artifact_checksum" {
  type = string
  description = "Artifact SHA256 checksum should be provided"
}

variable "memory" {
  default = 1500
}

variable "namespace" {
  type = string
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
  database_volume           = "/srv/sep-cluster/postgres/${var.namespace}-simulator"
}

job "simulator" {
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
        data        = "{{ key \"database/${var.namespace}-simulator/secrets\" }}"
        destination = "secrets/simulator-db.env"
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
        name = "${var.namespace}-simulator-db"

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

  group "simulator" {
    count = 2

    network {
      port "http" {
      }
    }

    service {
      name = "${var.namespace}-simulator"
      port = "http"
      tags = concat([
        "http",
        "traefik.enable=true"
      ], var.http_tags)

      check_restart {
        limit = 3
        grace = "90s"
        ignore_warnings = false
      }

      check {
        name = "Simulator HTTP Health Check"
        type = "http"
        path = "/rest/simulator/management/health"
        method = "GET"
        interval = "10s"
        timeout = "2s"
      }
    }


    task "simulator" {
      driver = "raw_exec"

      artifact {
        source = var.simulator_artifact
        options {
          checksum = "${var.simulator_artifact_checksum}"
        }
        mode = "file"
        destination = "local/simulator-app.jar"
      }

      template {
        data = "{{ key \"${var.namespace}/simulator/secrets\" }}"
        destination = "secrets/simulator.env"
        env = true
      }

      template {
        data = file("./conf/application.yml")
        destination = "local/conf/application.yml"
        change_mode = "restart"
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
          "local/simulator-app.jar",
          "--spring.profiles.active=linux,simulator,messaging",
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
