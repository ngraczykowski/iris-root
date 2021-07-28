import asyncio
import logging
from typing import Callable, Mapping

import aio_pika

from company_name.base.exception import AgentException


class PikaConnection:
    def __init__(
        self,
        messaging_configuration: Mapping,
        connection_configuration: Mapping,
        callback: Callable,
    ):
        self.messaging_configuration = messaging_configuration
        self.connection_configuration = connection_configuration
        self.request_callback = callback
        (
            self.connection,
            self.request_queue,
            self.callback_exchange,
            self.request_queue_tag,
        ) = (None, None, None, None)
        self.logger = logging.getLogger("PikaConnection")

    async def start(self) -> None:
        self.connection: aio_pika.RobustConnection = await aio_pika.connect_robust(
            **self.connection_configuration
        )
        channel: aio_pika.Channel = await self.connection.channel()

        self.callback_exchange = await channel.get_exchange(
            self.messaging_configuration["response"]["exchange"]
        )

        try:
            self.request_queue = await channel.get_queue(
                name=self.messaging_configuration["request"].get("queue-name"),
                ensure=True,
            )
        except aio_pika.exceptions.ChannelClosed as err:
            self.logger.debug(f"Queue doesn't exits: {err!r}")
            if "exchange" in self.messaging_configuration["request"]:

                self.request_queue = await channel.declare_queue(
                    name=self.messaging_configuration["request"].get("queue-name", ""),
                    durable=True,
                )

                await self.request_queue.bind(
                    exchange=self.messaging_configuration["request"]["exchange"],
                    routing_key=self.messaging_configuration["request"].get(
                        "routing-key"
                    ),
                )
            else:
                raise

        self.request_queue_tag = await self.request_queue.consume(
            callback=self.on_request, no_ack=False
        )

    async def stop(self) -> None:
        if self.request_queue_tag:
            await self.request_queue.cancel(self.request_queue_tag)
        if self.connection:
            await self.connection.close()

    async def on_request(self, message: aio_pika.IncomingMessage) -> None:
        self.logger.debug(f"received {message}")

        try:
            response_message = await self.request_callback(message)
        except AgentException as err:
            self.logger.warning(f"{err!r} on {message}")
            await asyncio.sleep(10)  # TODO
            message.nack(requeue=err.retry)
            return
        except Exception as err:
            self.logger.warning(f"{err!r} on {message}")
            await asyncio.sleep(10)  # TODO
            message.nack()
            return

        await self.callback_exchange.publish(
            routing_key="",
            message=response_message,
        )
        message.ack()
        self.logger.debug(f"acknowledged {message.message_id}")
