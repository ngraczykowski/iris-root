variable "webapp_artifact" {
  type = string
}

variable "webapp_artifact_checksum" {
  type = string
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
  type = list(string)
  default = []
}

locals {
  jvm_memory = ceil(var.memory * 0.7)
  perm_memory = ceil(var.memory * 0.2)
  database_node_destination = "eu2"
  database_volume           = "/srv/sep-cluster/postgres/${var.namespace}-webapp"
}

job "webapp" {
  type = "service"

  datacenters = [
    "dc1"
  ]

  namespace = "${var.namespace}"

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
        data        = "{{ key \"database/${var.namespace}-webapp/secrets\" }}"
        destination = "secrets/webapp-db.env"
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
        name = "${var.namespace}-webapp-db"

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

  group "webapp" {
    count = 1

    network {
      port "http" {
      }
    }

    update {
      auto_revert = true
    }

    service {
      name = "${var.namespace}-webapp"
      port = "http"
      tags = concat([
        "http",
        "traefik.enable=true",
        # Those 2 tags are required to gather metrics by prometheus
        "traefik.protocol=http",
        "prometheus.metrics.path=/rest/webapp/management/prometheus"
      ], var.http_tags)

      check_restart {
        limit = 3
        grace = "90s"
        ignore_warnings = false
      }

      check {
        name = "http"
        type = "http"
        path = "/rest/webapp/management/health"
        method = "GET"
        interval = "30s"
        timeout = "2s"
      }
    }

    task "webapp" {
      driver = "raw_exec"

      artifact {
        source = var.webapp_artifact
        options {
            checksum = "${var.webapp_artifact_checksum}"
        }
        mode = "file"
        destination = "local/sens-webapp-backend.jar"
      }

      template {
        data = "{{ key \"${var.namespace}/webapp/secrets\" }}"
        destination = "secrets/webapp.env"
        env = true
      }

      template {
        data = file("conf/application.yml")
        destination = "local/conf/application.yml"
        change_mode = "noop"
      }

      template {
        data = file("conf/application-database.yml")
        destination = "local/conf/application-database.yml"
        change_mode = "noop"
      }

      template {
        data = file("conf/application-messaging.yml")
        destination = "local/conf/application-messaging.yml"
        change_mode = "noop"
      }

      template {
        data = file("./conf/application-consul.yml")
        destination = "local/conf/application-consul.yml"
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
          "-Dserp.show-env=true",
          "-jar",
          "local/sens-webapp-backend.jar",
          "--spring.profiles.active=linux,webapp,database,rabbitmq,messaging,consul,debug",
          "--spring.config.additional-location=file:${NOMAD_TASK_DIR}/conf/",
          # todo tkleszcz: enable consul for SD, in future change to envoy
          "--spring.cloud.consul.discovery.register=false",
          "--spring.cloud.consul.config.enabled=false",
          "--spring.cloud.consul.port=8500",
          "--spring.rabbitmq.virtual-host=/${var.namespace}",
        ]
      }

      logs {
        max_files = 10
        max_file_size = 20
      }

      resources {
        cpu = 800
        memory = var.memory
      }
    }
  }
}
