import asyncio
import logging
import time
import uuid
from typing import Dict

import aio_pika
import lz4.frame
from silenteight.agents.v1.api.exchange.exchange_pb2 import (
    AgentExchangeRequest,
    AgentExchangeResponse,
)


class AdjudicationEngineMock:
    def __init__(self, config, consume_responses=True):
        self.config = config
        self._events: Dict[str, asyncio.Event] = {}
        self._responses = {}
        self._received = asyncio.Queue()
        (
            self._connection,
            self._exchange,
            self._callback_queue,
            self._callback_queue_tag,
        ) = (None, None, None, None)
        self.consume_responses = consume_responses
        self.logger = logging.getLogger("AdjudicationEngineMock")

    async def start(self):
        self._connection = await aio_pika.connect_robust(**self.config["rabbitmq"])

        channel: aio_pika.Channel = await self._connection.channel()
        self._exchange = await channel.get_exchange(
            self.config["agent"]["agent-exchange"]["request"]["exchange"]
        )

        if self.consume_responses:
            self._callback_queue = await channel.declare_queue(name="", exclusive=True)
            await self._callback_queue.bind(
                exchange=self.config["agent"]["agent-exchange"]["response"]["exchange"],
                routing_key="#",
            )
        self.logger.info("AEMock started")
        if self.consume_responses:
            self._callback_queue_tag = await self._callback_queue.consume(
                callback=self.on_response, no_ack=True
            )

    async def __aenter__(self):
        await self.start()
        return self

    async def send(
        self,
        message: AgentExchangeRequest,
        correlation_id: str = None,
        routing_key: str = None,
    ):
        if not correlation_id:
            correlation_id = str(uuid.uuid4())
        if routing_key is None:
            routing_key = self.config["agent"]["agent-exchange"]["request"]["routing-key"]
        assert correlation_id not in self._events
        self._events[correlation_id] = asyncio.Event()

        await self._exchange.publish(
            routing_key=routing_key,
            message=aio_pika.Message(
                lz4.frame.compress(message.SerializeToString()),
                content_encoding="lz4",
                content_type="application/x-protobuf",
                delivery_mode=aio_pika.DeliveryMode.PERSISTENT,
                headers={
                    "correlationId": correlation_id,
                    "agentConfig": routing_key.replace(".", "/").replace("_", "."),
                    "priority": 5,
                },
                priority=5,
                timestamp=int(time.time()),
                type="silenteight.agents.v1.api.exchange.AgentExchangeRequest",
            ),
        )
        self.logger.debug(f"Published with {correlation_id}")
        return correlation_id

    async def on_response(self, message: aio_pika.IncomingMessage):
        correlation_id = message.headers["correlationId"]
        self._responses[correlation_id] = lz4.frame.decompress(message.body)
        if correlation_id in self._events:
            self._events[correlation_id].set()
        await self._received.put(correlation_id)

    async def wait_for(self, correlation_id, timeout: int = 10):
        response = self._responses.get(correlation_id)
        if not response:
            event = self._events.get(correlation_id)
            if not event:
                raise Exception("Message not yet send, cannot wait")
            await asyncio.wait_for(event.wait(), timeout)
            response = self._responses.get(correlation_id)
        return AgentExchangeResponse.FromString(response)

    async def get(self, timeout: int = 10):
        correlation_id = await asyncio.wait_for(self._received.get(), timeout)
        return correlation_id, AgentExchangeResponse.FromString(self._responses[correlation_id])

    async def stop(self):
        if self._callback_queue:
            await self._callback_queue.cancel(self._callback_queue_tag)
            await self._callback_queue.unbind(
                exchange=self.config["agent"]["agent-exchange"]["response"]["exchange"],
            )
            await self._callback_queue.delete()

        if self._connection:
            await self._connection.close()

        self.logger.info("Mock stopped")

    async def __aexit__(self, exc_type, exc, tb):
        await self.stop()
