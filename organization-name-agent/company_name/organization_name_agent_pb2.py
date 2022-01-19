# -*- coding: utf-8 -*-
# Generated by the protocol buffer compiler.  DO NOT EDIT!
# source: organization_name_agent.proto
"""Generated protocol buffer code."""
from google.protobuf import descriptor as _descriptor
from google.protobuf import message as _message
from google.protobuf import reflection as _reflection
from google.protobuf import symbol_database as _symbol_database

# @@protoc_insertion_point(imports)

_sym_db = _symbol_database.Default()


DESCRIPTOR = _descriptor.FileDescriptor(
    name="organization_name_agent.proto",
    package="silenteight.agent.organizationname.v1.api",
    syntax="proto3",
    serialized_options=b"\n3com.silenteight.proto.agent.organizationname.v1.apiB\035OrganizationNameAgentApiProtoP\001",
    create_key=_descriptor._internal_create_key,
    serialized_pb=b'\n\x1dorganization_name_agent.proto\x12)silenteight.agent.organizationname.v1.api"]\n\x1f\x43ompareOrganizationNamesRequest\x12\x1b\n\x13\x61lerted_party_names\x18\x01 \x03(\t\x12\x1d\n\x15watchlist_party_names\x18\x02 \x03(\t"\x8f\x01\n CompareOrganizationNamesResponse\x12\x10\n\x08solution\x18\x01 \x01(\t\x12Y\n\x06reason\x18\x02 \x01(\x0b\x32I.silenteight.agent.organizationname.v1.api.CompareOrganizationNamesReason"\xea\x02\n\x1e\x43ompareOrganizationNamesReason\x12y\n\x07results\x18\x01 \x03(\x0b\x32h.silenteight.agent.organizationname.v1.api.CompareOrganizationNamesReason.CompareOrganizationNamesResult\x1a\xcc\x01\n\x1e\x43ompareOrganizationNamesResult\x12\x10\n\x08solution\x18\x01 \x01(\t\x12\x1c\n\x14solution_probability\x18\x02 \x01(\x01\x12\x1a\n\x12\x61lerted_party_name\x18\x03 \x01(\t\x12\x1c\n\x14watchlist_party_name\x18\x04 \x01(\t\x12@\n\x06scores\x18\x05 \x03(\x0b\x32\x30.silenteight.agent.organizationname.v1.api.Score"\xd7\x01\n\x05Score\x12\x0c\n\x04name\x18\x01 \x01(\t\x12\x0e\n\x06status\x18\x02 \x01(\t\x12\r\n\x05value\x18\x03 \x01(\x01\x12R\n\x08\x63ompared\x18\x04 \x01(\x0b\x32@.silenteight.agent.organizationname.v1.api.Score.ComparisonItems\x1aM\n\x0f\x43omparisonItems\x12\x1b\n\x13\x61lerted_party_items\x18\x01 \x03(\t\x12\x1d\n\x15watchlist_party_items\x18\x02 \x03(\t2\xcf\x01\n\x15OrganizationNameAgent\x12\xb5\x01\n\x18\x43ompareOrganizationNames\x12J.silenteight.agent.organizationname.v1.api.CompareOrganizationNamesRequest\x1aK.silenteight.agent.organizationname.v1.api.CompareOrganizationNamesResponse"\x00\x42V\n3com.silenteight.proto.agent.organizationname.v1.apiB\x1dOrganizationNameAgentApiProtoP\x01\x62\x06proto3',
)


_COMPAREORGANIZATIONNAMESREQUEST = _descriptor.Descriptor(
    name="CompareOrganizationNamesRequest",
    full_name="silenteight.agent.organizationname.v1.api.CompareOrganizationNamesRequest",
    filename=None,
    file=DESCRIPTOR,
    containing_type=None,
    create_key=_descriptor._internal_create_key,
    fields=[
        _descriptor.FieldDescriptor(
            name="alerted_party_names",
            full_name="silenteight.agent.organizationname.v1.api.CompareOrganizationNamesRequest.alerted_party_names",
            index=0,
            number=1,
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
            name="watchlist_party_names",
            full_name="silenteight.agent.organizationname.v1.api.CompareOrganizationNamesRequest.watchlist_party_names",
            index=1,
            number=2,
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
    serialized_start=76,
    serialized_end=169,
)


_COMPAREORGANIZATIONNAMESRESPONSE = _descriptor.Descriptor(
    name="CompareOrganizationNamesResponse",
    full_name="silenteight.agent.organizationname.v1.api.CompareOrganizationNamesResponse",
    filename=None,
    file=DESCRIPTOR,
    containing_type=None,
    create_key=_descriptor._internal_create_key,
    fields=[
        _descriptor.FieldDescriptor(
            name="solution",
            full_name="silenteight.agent.organizationname.v1.api.CompareOrganizationNamesResponse.solution",
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
            full_name="silenteight.agent.organizationname.v1.api.CompareOrganizationNamesResponse.reason",
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
    serialized_start=172,
    serialized_end=315,
)


_COMPAREORGANIZATIONNAMESREASON_COMPAREORGANIZATIONNAMESRESULT = _descriptor.Descriptor(
    name="CompareOrganizationNamesResult",
    full_name="silenteight.agent.organizationname.v1.api.CompareOrganizationNamesReason.CompareOrganizationNamesResult",
    filename=None,
    file=DESCRIPTOR,
    containing_type=None,
    create_key=_descriptor._internal_create_key,
    fields=[
        _descriptor.FieldDescriptor(
            name="solution",
            full_name="silenteight.agent.organizationname.v1.api.CompareOrganizationNamesReason.CompareOrganizationNamesResult.solution",
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
            name="solution_probability",
            full_name="silenteight.agent.organizationname.v1.api.CompareOrganizationNamesReason.CompareOrganizationNamesResult.solution_probability",
            index=1,
            number=2,
            type=1,
            cpp_type=5,
            label=1,
            has_default_value=False,
            default_value=float(0),
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
            name="alerted_party_name",
            full_name="silenteight.agent.organizationname.v1.api.CompareOrganizationNamesReason.CompareOrganizationNamesResult.alerted_party_name",
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
            name="watchlist_party_name",
            full_name="silenteight.agent.organizationname.v1.api.CompareOrganizationNamesReason.CompareOrganizationNamesResult.watchlist_party_name",
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
            name="scores",
            full_name="silenteight.agent.organizationname.v1.api.CompareOrganizationNamesReason.CompareOrganizationNamesResult.scores",
            index=4,
            number=5,
            type=11,
            cpp_type=10,
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
    serialized_start=476,
    serialized_end=680,
)

_COMPAREORGANIZATIONNAMESREASON = _descriptor.Descriptor(
    name="CompareOrganizationNamesReason",
    full_name="silenteight.agent.organizationname.v1.api.CompareOrganizationNamesReason",
    filename=None,
    file=DESCRIPTOR,
    containing_type=None,
    create_key=_descriptor._internal_create_key,
    fields=[
        _descriptor.FieldDescriptor(
            name="results",
            full_name="silenteight.agent.organizationname.v1.api.CompareOrganizationNamesReason.results",
            index=0,
            number=1,
            type=11,
            cpp_type=10,
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
    nested_types=[
        _COMPAREORGANIZATIONNAMESREASON_COMPAREORGANIZATIONNAMESRESULT,
    ],
    enum_types=[],
    serialized_options=None,
    is_extendable=False,
    syntax="proto3",
    extension_ranges=[],
    oneofs=[],
    serialized_start=318,
    serialized_end=680,
)


_SCORE_COMPARISONITEMS = _descriptor.Descriptor(
    name="ComparisonItems",
    full_name="silenteight.agent.organizationname.v1.api.Score.ComparisonItems",
    filename=None,
    file=DESCRIPTOR,
    containing_type=None,
    create_key=_descriptor._internal_create_key,
    fields=[
        _descriptor.FieldDescriptor(
            name="alerted_party_items",
            full_name="silenteight.agent.organizationname.v1.api.Score.ComparisonItems.alerted_party_items",
            index=0,
            number=1,
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
            name="watchlist_party_items",
            full_name="silenteight.agent.organizationname.v1.api.Score.ComparisonItems.watchlist_party_items",
            index=1,
            number=2,
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
    serialized_start=821,
    serialized_end=898,
)

_SCORE = _descriptor.Descriptor(
    name="Score",
    full_name="silenteight.agent.organizationname.v1.api.Score",
    filename=None,
    file=DESCRIPTOR,
    containing_type=None,
    create_key=_descriptor._internal_create_key,
    fields=[
        _descriptor.FieldDescriptor(
            name="name",
            full_name="silenteight.agent.organizationname.v1.api.Score.name",
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
            name="status",
            full_name="silenteight.agent.organizationname.v1.api.Score.status",
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
            name="value",
            full_name="silenteight.agent.organizationname.v1.api.Score.value",
            index=2,
            number=3,
            type=1,
            cpp_type=5,
            label=1,
            has_default_value=False,
            default_value=float(0),
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
            name="compared",
            full_name="silenteight.agent.organizationname.v1.api.Score.compared",
            index=3,
            number=4,
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
    nested_types=[
        _SCORE_COMPARISONITEMS,
    ],
    enum_types=[],
    serialized_options=None,
    is_extendable=False,
    syntax="proto3",
    extension_ranges=[],
    oneofs=[],
    serialized_start=683,
    serialized_end=898,
)

_COMPAREORGANIZATIONNAMESRESPONSE.fields_by_name[
    "reason"
].message_type = _COMPAREORGANIZATIONNAMESREASON
_COMPAREORGANIZATIONNAMESREASON_COMPAREORGANIZATIONNAMESRESULT.fields_by_name[
    "scores"
].message_type = _SCORE
_COMPAREORGANIZATIONNAMESREASON_COMPAREORGANIZATIONNAMESRESULT.containing_type = (
    _COMPAREORGANIZATIONNAMESREASON
)
_COMPAREORGANIZATIONNAMESREASON.fields_by_name[
    "results"
].message_type = _COMPAREORGANIZATIONNAMESREASON_COMPAREORGANIZATIONNAMESRESULT
_SCORE_COMPARISONITEMS.containing_type = _SCORE
_SCORE.fields_by_name["compared"].message_type = _SCORE_COMPARISONITEMS
DESCRIPTOR.message_types_by_name[
    "CompareOrganizationNamesRequest"
] = _COMPAREORGANIZATIONNAMESREQUEST
DESCRIPTOR.message_types_by_name[
    "CompareOrganizationNamesResponse"
] = _COMPAREORGANIZATIONNAMESRESPONSE
DESCRIPTOR.message_types_by_name["CompareOrganizationNamesReason"] = _COMPAREORGANIZATIONNAMESREASON
DESCRIPTOR.message_types_by_name["Score"] = _SCORE
_sym_db.RegisterFileDescriptor(DESCRIPTOR)

CompareOrganizationNamesRequest = _reflection.GeneratedProtocolMessageType(
    "CompareOrganizationNamesRequest",
    (_message.Message,),
    {
        "DESCRIPTOR": _COMPAREORGANIZATIONNAMESREQUEST,
        "__module__": "organization_name_agent_pb2"
        # @@protoc_insertion_point(class_scope:silenteight.agent.organizationname.v1.api.CompareOrganizationNamesRequest)
    },
)
_sym_db.RegisterMessage(CompareOrganizationNamesRequest)

CompareOrganizationNamesResponse = _reflection.GeneratedProtocolMessageType(
    "CompareOrganizationNamesResponse",
    (_message.Message,),
    {
        "DESCRIPTOR": _COMPAREORGANIZATIONNAMESRESPONSE,
        "__module__": "organization_name_agent_pb2"
        # @@protoc_insertion_point(class_scope:silenteight.agent.organizationname.v1.api.CompareOrganizationNamesResponse)
    },
)
_sym_db.RegisterMessage(CompareOrganizationNamesResponse)

CompareOrganizationNamesReason = _reflection.GeneratedProtocolMessageType(
    "CompareOrganizationNamesReason",
    (_message.Message,),
    {
        "CompareOrganizationNamesResult": _reflection.GeneratedProtocolMessageType(
            "CompareOrganizationNamesResult",
            (_message.Message,),
            {
                "DESCRIPTOR": _COMPAREORGANIZATIONNAMESREASON_COMPAREORGANIZATIONNAMESRESULT,
                "__module__": "organization_name_agent_pb2"
                # @@protoc_insertion_point(class_scope:silenteight.agent.organizationname.v1.api.CompareOrganizationNamesReason.CompareOrganizationNamesResult)
            },
        ),
        "DESCRIPTOR": _COMPAREORGANIZATIONNAMESREASON,
        "__module__": "organization_name_agent_pb2"
        # @@protoc_insertion_point(class_scope:silenteight.agent.organizationname.v1.api.CompareOrganizationNamesReason)
    },
)
_sym_db.RegisterMessage(CompareOrganizationNamesReason)
_sym_db.RegisterMessage(CompareOrganizationNamesReason.CompareOrganizationNamesResult)

Score = _reflection.GeneratedProtocolMessageType(
    "Score",
    (_message.Message,),
    {
        "ComparisonItems": _reflection.GeneratedProtocolMessageType(
            "ComparisonItems",
            (_message.Message,),
            {
                "DESCRIPTOR": _SCORE_COMPARISONITEMS,
                "__module__": "organization_name_agent_pb2"
                # @@protoc_insertion_point(class_scope:silenteight.agent.organizationname.v1.api.Score.ComparisonItems)
            },
        ),
        "DESCRIPTOR": _SCORE,
        "__module__": "organization_name_agent_pb2"
        # @@protoc_insertion_point(class_scope:silenteight.agent.organizationname.v1.api.Score)
    },
)
_sym_db.RegisterMessage(Score)
_sym_db.RegisterMessage(Score.ComparisonItems)


DESCRIPTOR._options = None

_ORGANIZATIONNAMEAGENT = _descriptor.ServiceDescriptor(
    name="OrganizationNameAgent",
    full_name="silenteight.agent.organizationname.v1.api.OrganizationNameAgent",
    file=DESCRIPTOR,
    index=0,
    serialized_options=None,
    create_key=_descriptor._internal_create_key,
    serialized_start=901,
    serialized_end=1108,
    methods=[
        _descriptor.MethodDescriptor(
            name="CompareOrganizationNames",
            full_name="silenteight.agent.organizationname.v1.api.OrganizationNameAgent.CompareOrganizationNames",
            index=0,
            containing_service=None,
            input_type=_COMPAREORGANIZATIONNAMESREQUEST,
            output_type=_COMPAREORGANIZATIONNAMESRESPONSE,
            serialized_options=None,
            create_key=_descriptor._internal_create_key,
        ),
    ],
)
_sym_db.RegisterServiceDescriptor(_ORGANIZATIONNAMEAGENT)

DESCRIPTOR.services_by_name["OrganizationNameAgent"] = _ORGANIZATIONNAMEAGENT

# @@protoc_insertion_point(module_scope)
