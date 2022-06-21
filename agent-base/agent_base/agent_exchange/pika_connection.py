from typing import Callable

import aio_pika
from s8_python_network.pika_connection import BasePikaConnection, RabbitMQConfig

from agent_base.agent.exception import AgentException
from agent_base.utils.config.application_config import (
    ConfigurationException,
    MessagingConfig,
)


class PikaConnection(BasePikaConnection):
    def __init__(
        self,
        messaging_configuration: MessagingConfig,
        connection_configuration: RabbitMQConfig,
        callback: Callable,
        max_requests_to_worker: int,
        use_ssl: bool = False,
    ):
        super().__init__(
            connection_configuration=connection_configuration,
            max_requests_to_worker=max_requests_to_worker,
            use_ssl=use_ssl,
        )
        self.messaging_configuration = messaging_configuration
        self.request_callback = callback
        self.request_queue, self.callback_exchange, self.request_queue_tag = None, None, None

    async def start(self) -> None:
        await self.set_connection()
        await self.set_channel()
        await self.set_queue_and_exchanges()

    async def set_queue_and_exchanges(self):
        try:
            self.request_queue = await self.channel.get_queue(
                name=self.messaging_configuration.request.queue_name,
                ensure=True,
            )
            self.logger.info(f"Got an existing (request) queue: {self.request_queue}")
        except aio_pika.exceptions.ChannelNotFoundEntity as err:
            self.logger.debug(f"Queue doesn't exits: {err!r}")
            if self.messaging_configuration.request.all_required:

                # not sure why but on error close callbacks are called in aiormq,
                # and more exceptions happens
                await self.channel.close()
                channel: aio_pika.Channel = await self.connection.channel()
                await channel.set_qos(prefetch_count=self.max_requests_to_worker)

                self.request_queue: aio_pika.queue.Queue = await channel.declare_queue(
                    name=self.messaging_configuration.request.queue_name,
                    durable=self.messaging_configuration.request.queue_durable,
                    arguments=self.messaging_configuration.request.queue_arguments,
                )
                request_exchange = self.messaging_configuration.request.exchange

                await self.get_or_create_exchange(request_exchange)

                await self.request_queue.bind(
                    exchange=request_exchange,
                    routing_key=self.messaging_configuration.request.routing_key,
                )
                self.logger.info(
                    f"Created queue {self.request_queue} and bind to exchange {request_exchange}"
                )
            else:
                self.logger.error(
                    "Queue not exists and cannot be created - check messaging config 'request"
                )
                raise

        if not self.messaging_configuration.response.all_required:
            raise ConfigurationException("Messaging config not valid - check 'response")

        self.callback_exchange = await self.get_or_create_exchange(
            self.messaging_configuration.response.exchange
        )
        self.request_queue_tag = await self.request_queue.consume(
            callback=self.on_request, no_ack=False
        )

    async def get_or_create_exchange(self, exchange_name: str) -> aio_pika.Exchange:
        try:
            exchange = await self.channel.get_exchange(name=exchange_name, ensure=True)
            self.logger.info(f"Got an existing exchange {exchange}")
        except Exception as err:
            channel: aio_pika.Channel = await self.connection.channel()
            self.logger.debug(f"Exchange doesn't exits: {err!r}")
            exchange = await channel.declare_exchange(
                name=exchange_name, durable=True, type="topic"
            )
            self.logger.info(f"Created exchange {exchange}")
        return exchange

    async def stop(self) -> None:
        # not sure whenever order is important, so doing it without gather
        if self.request_queue_tag:
            await self.request_queue.cancel(self.request_queue_tag)
        if self.channel:
            await self.channel.close()
        if self.connection:
            await self.connection.close()

    async def on_request(self, message: aio_pika.IncomingMessage) -> None:
        self.logger.debug(f"Received message id: {message.message_id}")

        try:
            response_message = await self.request_callback(message)
        except AgentException as err:
            self.logger.warning(f"{err!r} on message id: {message.message_id}")
            await message.nack(requeue=err.retry)
            return
        except Exception as err:
            self.logger.warning(f"{err!r} on message id: {message.message_id}")
            await message.nack()
            return

        await self.callback_exchange.publish(
            routing_key=self.messaging_configuration.response.routing_key,
            message=response_message,
        )
        await message.ack()
        self.logger.debug(f"acknowledged message id: {message.message_id}")
