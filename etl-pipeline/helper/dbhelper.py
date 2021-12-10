from configparser import ConfigParser

config = ConfigParser()
config.read("config.ini")


class DbHelper:
    def __init__(self, spark, DB_PLATFORM):
        self.spark = spark
        self.DB_PLATFORM = DB_PLATFORM

    def read_from_db(self, table_name):
        def _read_from_pg(pg_table):
            if "select" in pg_table.lower():
                query = """
                select * from (%(pg_table)s)a
                """ % {
                    "pg_table": pg_table
                }
            else:
                query = """
                select * from %(pg_table)s
                """ % {
                    "pg_table": pg_table
                }

            df = (
                self.spark.read.format("jdbc")
                .option(
                    "url",
                    "jdbc:postgresql://%(postgresIP)s:%(postgresPort)s/%(postgresDB)s"
                    "?currentSchema=%(postgresSchema)s"
                    % {
                        "postgresIP": config["DATABASE"]["host"],
                        "postgresPort": config["DATABASE"]["port"],
                        "postgresDB": config["DATABASE"]["database"],
                        "postgresSchema": config["DATABASE"]["schema"],
                    },
                )
                .option("user", config["DATABASE"]["user"])
                .option("password", config["DATABASE"]["password"])
                .option("dbtable", "(%s) b" % query)
                .option("driver", "org.postgresql.Driver")
                .load()
            )
            return df

        def _read_from_oracle(oracle_table):
            query = """
                select * from (%(oracle_table)s)a
                """ % {
                "oracle_table": oracle_table
            }

            df = (
                self.spark.read.format("jdbc")
                .option(
                    "url",
                    "jdbc:oracle:thin:%(oracleUser)s/%(oraclePassword)s@//%(oracleHost)s:"
                    "%(oraclePort)s/%(oracleService)s"
                    % {
                        "oracleUser": config["DATABASE"]["ORACLEUSER"],
                        "oraclePassword": config["DATABASE"]["ORACLEPASSWORD"],
                        "oracleHost": config["DATABASE"]["ORACLEHOST"],
                        "oraclePort": config["DATABASE"]["ORACLEPORT"],
                        "oracleService": config["DATABASE"]["ORACLESERVICE"],
                    },
                )
                .option("dbtable", "(%s) b" % query)
                .option("driver", "oracle.jdbc.driver.OracleDriver")
                .load()
            )
            return df

        if self.DB_PLATFORM == "POSTGRES":
            return _read_from_pg(table_name)
        elif self.DB_PLATFORM == "ORACLE":
            return _read_from_oracle(table_name)
        else:
            raise ValueError(
                "Please specify which db platform you want to access, possible values:" ' "POSTGRES", "ORACLE"'
            )
