import dataclasses


@dataclasses.dataclass
class SSLCredentials:
    """
    ca_filename: str
    client_private_key_filename: str
    client_public_key_chain_filename: str
    """

    ca_filename: str
    client_private_key_filename: str
    client_public_key_chain_filename: str
