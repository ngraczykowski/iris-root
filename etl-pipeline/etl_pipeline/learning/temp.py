# import lz4

# response_body = lz4.frame.compress(
#             response.SerializeToString(),
#             block_size=lz4.frame.BLOCKSIZE_MAX64KB,
#             block_linked=False,
#             compression_level=lz4.frame.COMPRESSIONLEVEL_MINHC,
#             content_checksum=True,
#             store_size=False,
#         )

#         return aio_pika.Message(
#             response_body,
#             content_encoding="lz4",
#             content_type="application/x-protobuf",
#             delivery_mode=message.delivery_mode,
#             headers=message.headers,
#             priority=message.priority,
#             timestamp=int(time.time()),
#             type="silenteight.agents.v1.api.exchange.AgentExchangeResponse",
#         )
