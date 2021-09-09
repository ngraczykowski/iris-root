# -*- coding: utf-8 -*-
# Generated by the protocol buffer compiler.  DO NOT EDIT!
# source: idmismatchagent/bank_identification_codes_agent.proto
"""Generated protocol buffer code."""
from google.protobuf import descriptor as _descriptor
from google.protobuf import message as _message
from google.protobuf import reflection as _reflection
from google.protobuf import symbol_database as _symbol_database

# @@protoc_insertion_point(imports)

_sym_db = _symbol_database.Default()


DESCRIPTOR = _descriptor.FileDescriptor(
    name="idmismatchagent/bank_identification_codes_agent.proto",
    package="silenteight.agent.bank_identification_codes_agent.v1.api",
    syntax="proto3",
    serialized_options=b"\nBcom.silenteight.proto.agent.bank_identification_codes_agent.v1.apiB$BankIdentificationCodesAgentApiProtoP\001",
    create_key=_descriptor._internal_create_key,
    serialized_pb=b'\n5idmismatchagent/bank_identification_codes_agent.proto\x12\x38silenteight.agent.bank_identification_codes_agent.v1.api"\xc1\x01\n#CheckBankIdentificationCodesRequest\x12$\n\x1c\x61ltered_party_matching_field\x18\x01 \x01(\t\x12\x1f\n\x17watchlist_matching_text\x18\x02 \x01(\t\x12\x16\n\x0ewatchlist_type\x18\x03 \x01(\t\x12\x1e\n\x16watchlist_search_codes\x18\x04 \x03(\t\x12\x1b\n\x13watchlist_bic_codes\x18\x05 \x03(\t"\xa6\x01\n$CheckBankIdentificationCodesResponse\x12\x10\n\x08solution\x18\x01 \x01(\t\x12l\n\x06reason\x18\x02 \x01(\x0b\x32\\.silenteight.agent.bank_identification_codes_agent.v1.api.CheckBankIdentificationCodesReason"\x98\x02\n"CheckBankIdentificationCodesReason\x12\x12\n\nconclusion\x18\x01 \x01(\t\x12\'\n\x1f\x61ltered_party_matching_sequence\x18\x02 \x01(\t\x12$\n\x1c\x61ltered_party_matching_field\x18\x03 \x01(\t\x12\x1a\n\x12partial_match_text\x18\x04 \x01(\t\x12\x1f\n\x17watchlist_matching_text\x18\x05 \x01(\t\x12\x16\n\x0ewatchlist_type\x18\x06 \x01(\t\x12\x1e\n\x16watchlist_search_codes\x18\x07 \x03(\t\x12\x1a\n\x12watchlist_bic_code\x18\x08 \x01(\t2\x80\x02\n\x1c\x42\x61nkIdentificationCodesAgent\x12\xdf\x01\n\x1c\x43heckBankIdentificationCodes\x12].silenteight.agent.bank_identification_codes_agent.v1.api.CheckBankIdentificationCodesRequest\x1a^.silenteight.agent.bank_identification_codes_agent.v1.api.CheckBankIdentificationCodesResponse"\x00\x42l\nBcom.silenteight.proto.agent.bank_identification_codes_agent.v1.apiB$BankIdentificationCodesAgentApiProtoP\x01\x62\x06proto3',
)


_CHECKBANKIDENTIFICATIONCODESREQUEST = _descriptor.Descriptor(
    name="CheckBankIdentificationCodesRequest",
    full_name="silenteight.agent.bank_identification_codes_agent.v1.api.CheckBankIdentificationCodesRequest",
    filename=None,
    file=DESCRIPTOR,
    containing_type=None,
    create_key=_descriptor._internal_create_key,
    fields=[
        _descriptor.FieldDescriptor(
            name="altered_party_matching_field",
            full_name="silenteight.agent.bank_identification_codes_agent.v1.api.CheckBankIdentificationCodesRequest.altered_party_matching_field",
            index=0,
            number=1,
            type=9,
            cpp_type=9,
            label=1,
            has_default_value=False,
            default_value=b"".decode("utf-8"),
            message_type=None,
            enum_type=None,
            containing_type=None,
            is_extension=False,
            extension_scope=None,
            serialized_options=None,
            file=DESCRIPTOR,
            create_key=_descriptor._internal_create_key,
        ),
        _descriptor.FieldDescriptor(
            name="watchlist_matching_text",
            full_name="silenteight.agent.bank_identification_codes_agent.v1.api.CheckBankIdentificationCodesRequest.watchlist_matching_text",
            index=1,
            number=2,
            type=9,
            cpp_type=9,
            label=1,
            has_default_value=False,
            default_value=b"".decode("utf-8"),
            message_type=None,
            enum_type=None,
            containing_type=None,
            is_extension=False,
            extension_scope=None,
            serialized_options=None,
            file=DESCRIPTOR,
            create_key=_descriptor._internal_create_key,
        ),
        _descriptor.FieldDescriptor(
            name="watchlist_type",
            full_name="silenteight.agent.bank_identification_codes_agent.v1.api.CheckBankIdentificationCodesRequest.watchlist_type",
            index=2,
            number=3,
            type=9,
            cpp_type=9,
            label=1,
            has_default_value=False,
            default_value=b"".decode("utf-8"),
            message_type=None,
            enum_type=None,
            containing_type=None,
            is_extension=False,
            extension_scope=None,
            serialized_options=None,
            file=DESCRIPTOR,
            create_key=_descriptor._internal_create_key,
        ),
        _descriptor.FieldDescriptor(
            name="watchlist_search_codes",
            full_name="silenteight.agent.bank_identification_codes_agent.v1.api.CheckBankIdentificationCodesRequest.watchlist_search_codes",
            index=3,
            number=4,
            type=9,
            cpp_type=9,
            label=3,
            has_default_value=False,
            default_value=[],
            message_type=None,
            enum_type=None,
            containing_type=None,
            is_extension=False,
            extension_scope=None,
            serialized_options=None,
            file=DESCRIPTOR,
            create_key=_descriptor._internal_create_key,
        ),
        _descriptor.FieldDescriptor(
            name="watchlist_bic_codes",
            full_name="silenteight.agent.bank_identification_codes_agent.v1.api.CheckBankIdentificationCodesRequest.watchlist_bic_codes",
            index=4,
            number=5,
            type=9,
            cpp_type=9,
            label=3,
            has_default_value=False,
            default_value=[],
            message_type=None,
            enum_type=None,
            containing_type=None,
            is_extension=False,
            extension_scope=None,
            serialized_options=None,
            file=DESCRIPTOR,
            create_key=_descriptor._internal_create_key,
        ),
    ],
    extensions=[],
    nested_types=[],
    enum_types=[],
    serialized_options=None,
    is_extendable=False,
    syntax="proto3",
    extension_ranges=[],
    oneofs=[],
    serialized_start=116,
    serialized_end=309,
)


_CHECKBANKIDENTIFICATIONCODESRESPONSE = _descriptor.Descriptor(
    name="CheckBankIdentificationCodesResponse",
    full_name="silenteight.agent.bank_identification_codes_agent.v1.api.CheckBankIdentificationCodesResponse",
    filename=None,
    file=DESCRIPTOR,
    containing_type=None,
    create_key=_descriptor._internal_create_key,
    fields=[
        _descriptor.FieldDescriptor(
            name="solution",
            full_name="silenteight.agent.bank_identification_codes_agent.v1.api.CheckBankIdentificationCodesResponse.solution",
            index=0,
            number=1,
            type=9,
            cpp_type=9,
            label=1,
            has_default_value=False,
            default_value=b"".decode("utf-8"),
            message_type=None,
            enum_type=None,
            containing_type=None,
            is_extension=False,
            extension_scope=None,
            serialized_options=None,
            file=DESCRIPTOR,
            create_key=_descriptor._internal_create_key,
        ),
        _descriptor.FieldDescriptor(
            name="reason",
            full_name="silenteight.agent.bank_identification_codes_agent.v1.api.CheckBankIdentificationCodesResponse.reason",
            index=1,
            number=2,
            type=11,
            cpp_type=10,
            label=1,
            has_default_value=False,
            default_value=None,
            message_type=None,
            enum_type=None,
            containing_type=None,
            is_extension=False,
            extension_scope=None,
            serialized_options=None,
            file=DESCRIPTOR,
            create_key=_descriptor._internal_create_key,
        ),
    ],
    extensions=[],
    nested_types=[],
    enum_types=[],
    serialized_options=None,
    is_extendable=False,
    syntax="proto3",
    extension_ranges=[],
    oneofs=[],
    serialized_start=312,
    serialized_end=478,
)


_CHECKBANKIDENTIFICATIONCODESREASON = _descriptor.Descriptor(
    name="CheckBankIdentificationCodesReason",
    full_name="silenteight.agent.bank_identification_codes_agent.v1.api.CheckBankIdentificationCodesReason",
    filename=None,
    file=DESCRIPTOR,
    containing_type=None,
    create_key=_descriptor._internal_create_key,
    fields=[
        _descriptor.FieldDescriptor(
            name="conclusion",
            full_name="silenteight.agent.bank_identification_codes_agent.v1.api.CheckBankIdentificationCodesReason.conclusion",
            index=0,
            number=1,
            type=9,
            cpp_type=9,
            label=1,
            has_default_value=False,
            default_value=b"".decode("utf-8"),
            message_type=None,
            enum_type=None,
            containing_type=None,
            is_extension=False,
            extension_scope=None,
            serialized_options=None,
            file=DESCRIPTOR,
            create_key=_descriptor._internal_create_key,
        ),
        _descriptor.FieldDescriptor(
            name="altered_party_matching_sequence",
            full_name="silenteight.agent.bank_identification_codes_agent.v1.api.CheckBankIdentificationCodesReason.altered_party_matching_sequence",
            index=1,
            number=2,
            type=9,
            cpp_type=9,
            label=1,
            has_default_value=False,
            default_value=b"".decode("utf-8"),
            message_type=None,
            enum_type=None,
            containing_type=None,
            is_extension=False,
            extension_scope=None,
            serialized_options=None,
            file=DESCRIPTOR,
            create_key=_descriptor._internal_create_key,
        ),
        _descriptor.FieldDescriptor(
            name="altered_party_matching_field",
            full_name="silenteight.agent.bank_identification_codes_agent.v1.api.CheckBankIdentificationCodesReason.altered_party_matching_field",
            index=2,
            number=3,
            type=9,
            cpp_type=9,
            label=1,
            has_default_value=False,
            default_value=b"".decode("utf-8"),
            message_type=None,
            enum_type=None,
            containing_type=None,
            is_extension=False,
            extension_scope=None,
            serialized_options=None,
            file=DESCRIPTOR,
            create_key=_descriptor._internal_create_key,
        ),
        _descriptor.FieldDescriptor(
            name="partial_match_text",
            full_name="silenteight.agent.bank_identification_codes_agent.v1.api.CheckBankIdentificationCodesReason.partial_match_text",
            index=3,
            number=4,
            type=9,
            cpp_type=9,
            label=1,
            has_default_value=False,
            default_value=b"".decode("utf-8"),
            message_type=None,
            enum_type=None,
            containing_type=None,
            is_extension=False,
            extension_scope=None,
            serialized_options=None,
            file=DESCRIPTOR,
            create_key=_descriptor._internal_create_key,
        ),
        _descriptor.FieldDescriptor(
            name="watchlist_matching_text",
            full_name="silenteight.agent.bank_identification_codes_agent.v1.api.CheckBankIdentificationCodesReason.watchlist_matching_text",
            index=4,
            number=5,
            type=9,
            cpp_type=9,
            label=1,
            has_default_value=False,
            default_value=b"".decode("utf-8"),
            message_type=None,
            enum_type=None,
            containing_type=None,
            is_extension=False,
            extension_scope=None,
            serialized_options=None,
            file=DESCRIPTOR,
            create_key=_descriptor._internal_create_key,
        ),
        _descriptor.FieldDescriptor(
            name="watchlist_type",
            full_name="silenteight.agent.bank_identification_codes_agent.v1.api.CheckBankIdentificationCodesReason.watchlist_type",
            index=5,
            number=6,
            type=9,
            cpp_type=9,
            label=1,
            has_default_value=False,
            default_value=b"".decode("utf-8"),
            message_type=None,
            enum_type=None,
            containing_type=None,
            is_extension=False,
            extension_scope=None,
            serialized_options=None,
            file=DESCRIPTOR,
            create_key=_descriptor._internal_create_key,
        ),
        _descriptor.FieldDescriptor(
            name="watchlist_search_codes",
            full_name="silenteight.agent.bank_identification_codes_agent.v1.api.CheckBankIdentificationCodesReason.watchlist_search_codes",
            index=6,
            number=7,
            type=9,
            cpp_type=9,
            label=3,
            has_default_value=False,
            default_value=[],
            message_type=None,
            enum_type=None,
            containing_type=None,
            is_extension=False,
            extension_scope=None,
            serialized_options=None,
            file=DESCRIPTOR,
            create_key=_descriptor._internal_create_key,
        ),
        _descriptor.FieldDescriptor(
            name="watchlist_bic_code",
            full_name="silenteight.agent.bank_identification_codes_agent.v1.api.CheckBankIdentificationCodesReason.watchlist_bic_code",
            index=7,
            number=8,
            type=9,
            cpp_type=9,
            label=1,
            has_default_value=False,
            default_value=b"".decode("utf-8"),
            message_type=None,
            enum_type=None,
            containing_type=None,
            is_extension=False,
            extension_scope=None,
            serialized_options=None,
            file=DESCRIPTOR,
            create_key=_descriptor._internal_create_key,
        ),
    ],
    extensions=[],
    nested_types=[],
    enum_types=[],
    serialized_options=None,
    is_extendable=False,
    syntax="proto3",
    extension_ranges=[],
    oneofs=[],
    serialized_start=481,
    serialized_end=761,
)

_CHECKBANKIDENTIFICATIONCODESRESPONSE.fields_by_name[
    "reason"
].message_type = _CHECKBANKIDENTIFICATIONCODESREASON
DESCRIPTOR.message_types_by_name[
    "CheckBankIdentificationCodesRequest"
] = _CHECKBANKIDENTIFICATIONCODESREQUEST
DESCRIPTOR.message_types_by_name[
    "CheckBankIdentificationCodesResponse"
] = _CHECKBANKIDENTIFICATIONCODESRESPONSE
DESCRIPTOR.message_types_by_name[
    "CheckBankIdentificationCodesReason"
] = _CHECKBANKIDENTIFICATIONCODESREASON
_sym_db.RegisterFileDescriptor(DESCRIPTOR)

CheckBankIdentificationCodesRequest = _reflection.GeneratedProtocolMessageType(
    "CheckBankIdentificationCodesRequest",
    (_message.Message,),
    {
        "DESCRIPTOR": _CHECKBANKIDENTIFICATIONCODESREQUEST,
        "__module__": "idmismatchagent.bank_identification_codes_agent_pb2"
        # @@protoc_insertion_point(class_scope:silenteight.agent.bank_identification_codes_agent.v1.api.CheckBankIdentificationCodesRequest)
    },
)
_sym_db.RegisterMessage(CheckBankIdentificationCodesRequest)

CheckBankIdentificationCodesResponse = _reflection.GeneratedProtocolMessageType(
    "CheckBankIdentificationCodesResponse",
    (_message.Message,),
    {
        "DESCRIPTOR": _CHECKBANKIDENTIFICATIONCODESRESPONSE,
        "__module__": "idmismatchagent.bank_identification_codes_agent_pb2"
        # @@protoc_insertion_point(class_scope:silenteight.agent.bank_identification_codes_agent.v1.api.CheckBankIdentificationCodesResponse)
    },
)
_sym_db.RegisterMessage(CheckBankIdentificationCodesResponse)

CheckBankIdentificationCodesReason = _reflection.GeneratedProtocolMessageType(
    "CheckBankIdentificationCodesReason",
    (_message.Message,),
    {
        "DESCRIPTOR": _CHECKBANKIDENTIFICATIONCODESREASON,
        "__module__": "idmismatchagent.bank_identification_codes_agent_pb2"
        # @@protoc_insertion_point(class_scope:silenteight.agent.bank_identification_codes_agent.v1.api.CheckBankIdentificationCodesReason)
    },
)
_sym_db.RegisterMessage(CheckBankIdentificationCodesReason)


DESCRIPTOR._options = None

_BANKIDENTIFICATIONCODESAGENT = _descriptor.ServiceDescriptor(
    name="BankIdentificationCodesAgent",
    full_name="silenteight.agent.bank_identification_codes_agent.v1.api.BankIdentificationCodesAgent",
    file=DESCRIPTOR,
    index=0,
    serialized_options=None,
    create_key=_descriptor._internal_create_key,
    serialized_start=764,
    serialized_end=1020,
    methods=[
        _descriptor.MethodDescriptor(
            name="CheckBankIdentificationCodes",
            full_name="silenteight.agent.bank_identification_codes_agent.v1.api.BankIdentificationCodesAgent.CheckBankIdentificationCodes",
            index=0,
            containing_service=None,
            input_type=_CHECKBANKIDENTIFICATIONCODESREQUEST,
            output_type=_CHECKBANKIDENTIFICATIONCODESRESPONSE,
            serialized_options=None,
            create_key=_descriptor._internal_create_key,
        ),
    ],
)
_sym_db.RegisterServiceDescriptor(_BANKIDENTIFICATIONCODESAGENT)

DESCRIPTOR.services_by_name["BankIdentificationCodesAgent"] = _BANKIDENTIFICATIONCODESAGENT

# @@protoc_insertion_point(module_scope)
