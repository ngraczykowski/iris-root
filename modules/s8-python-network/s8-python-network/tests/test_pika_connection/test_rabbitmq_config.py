import pytest

from s8_python_network.pika_connection import RabbitMQConfig

DEFAULT_FIELDS = {"login": "dev", "password": "dev", "virtualhost": "/"}


def test_generate_from_host_and_port():
    rmq_config = RabbitMQConfig(**DEFAULT_FIELDS, host="local", port=1234)
    generated = list(rmq_config.generate())
    assert len(generated) == 1
    assert generated[0] == RabbitMQConfig(**DEFAULT_FIELDS, host="local", port=1234)


def test_generate_from_addresses():
    addresses = "host_one:1234,host_two:997,third:3"
    rmq_config = RabbitMQConfig(**DEFAULT_FIELDS, addresses=addresses)
    generated = list(rmq_config.generate())
    assert len(generated) == 3
    assert generated[0] == RabbitMQConfig(
        **DEFAULT_FIELDS, host="host_one", port=1234, addresses=addresses
    )
    assert generated[1] == RabbitMQConfig(
        **DEFAULT_FIELDS, host="host_two", port=997, addresses=addresses
    )
    assert generated[2] == RabbitMQConfig(
        **DEFAULT_FIELDS, host="third", port=3, addresses=addresses
    )


def test_generate_from_none():
    rmq_config = RabbitMQConfig(**DEFAULT_FIELDS, host=None, port=None, addresses=None)
    with pytest.raises(StopIteration):
        next(rmq_config.generate())


def test_rmq_config_validation():
    with pytest.raises(ValueError):
        RabbitMQConfig(**DEFAULT_FIELDS, host="local", port="abc")
