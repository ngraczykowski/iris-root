from typing import Union

import grpc

from s8_python_network.ssl_credentials import SSLCredentials


def get_channel(
    address: str,
    asynchronous: bool = False,
    ssl: bool = False,
    ssl_credentials: SSLCredentials = None,
) -> Union[grpc.Channel, grpc.aio.Channel]:

    if ssl:
        if not ssl_credentials:
            raise AttributeError("When ssl=True, ssl_credentials must be provided")
        with open(ssl_credentials.ca_filename, "rb") as f:
            ca = f.read()
        with open(ssl_credentials.client_private_key_filename, "rb") as f:
            private_key = f.read()
        with open(ssl_credentials.client_public_key_chain_filename, "rb") as f:
            certificate_chain = f.read()
        server_credentials = grpc.ssl_channel_credentials(ca, private_key, certificate_chain)
        if asynchronous:
            return grpc.aio.secure_channel(address, server_credentials)
        else:
            return grpc.secure_channel(address, server_credentials)

    else:
        if asynchronous:
            return grpc.aio.insecure_channel(address)
        else:
            return grpc.insecure_channel(address)
