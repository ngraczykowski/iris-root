# -*- coding: utf-8 -*-
# Generated by the protocol buffer compiler.  DO NOT EDIT!
# source: hit_type_agent.proto
"""Generated protocol buffer code."""
from google.protobuf import descriptor as _descriptor
from google.protobuf import message as _message
from google.protobuf import reflection as _reflection
from google.protobuf import symbol_database as _symbol_database

# @@protoc_insertion_point(imports)

_sym_db = _symbol_database.Default()


DESCRIPTOR = _descriptor.FileDescriptor(
    name="hit_type_agent.proto",
    package="silenteight.agent.hittype.v1.api",
    syntax="proto3",
    serialized_options=b"\n*com.silenteight.proto.agent.hittype.v1.apiB\024HitTypeAgentApiProtoP\001",
    create_key=_descriptor._internal_create_key,
    serialized_pb=b'\n\x14hit_type_agent.proto\x12 silenteight.agent.hittype.v1.api"\x1c\n\nStringList\x12\x0e\n\x06tokens\x18\x01 \x03(\t"\xbb\x01\n\tTokensMap\x12N\n\ntokens_map\x18\x01 \x03(\x0b\x32:.silenteight.agent.hittype.v1.api.TokensMap.TokensMapEntry\x1a^\n\x0eTokensMapEntry\x12\x0b\n\x03key\x18\x01 \x01(\t\x12;\n\x05value\x18\x02 \x01(\x0b\x32,.silenteight.agent.hittype.v1.api.StringList:\x02\x38\x01"\xd8\x03\n\x14\x43heckTriggersRequest\x12!\n\x19normal_trigger_categories\x18\x01 \x03(\t\x12i\n\x12trigger_categories\x18\x02 \x03(\x0b\x32M.silenteight.agent.hittype.v1.api.CheckTriggersRequest.TriggerCategoriesEntry\x12\x65\n\x10triggered_tokens\x18\x03 \x03(\x0b\x32K.silenteight.agent.hittype.v1.api.CheckTriggersRequest.TriggeredTokensEntry\x1a\x66\n\x16TriggerCategoriesEntry\x12\x0b\n\x03key\x18\x01 \x01(\t\x12;\n\x05value\x18\x02 \x01(\x0b\x32,.silenteight.agent.hittype.v1.api.StringList:\x02\x38\x01\x1a\x63\n\x14TriggeredTokensEntry\x12\x0b\n\x03key\x18\x01 \x01(\t\x12:\n\x05value\x18\x02 \x01(\x0b\x32+.silenteight.agent.hittype.v1.api.TokensMap:\x02\x38\x01"k\n\x15\x43heckTriggersResponse\x12\x10\n\x08solution\x18\x01 \x01(\t\x12@\n\x06reason\x18\x02 \x01(\x0b\x32\x30.silenteight.agent.hittype.v1.api.HitTypesReason"C\n\x0eHitTypesReason\x12\x16\n\x0ehit_categories\x18\x01 \x03(\t\x12\x19\n\x11normal_categories\x18\x02 \x03(\t2\x93\x01\n\x0cHitTypeAgent\x12\x82\x01\n\rCheckTriggers\x12\x36.silenteight.agent.hittype.v1.api.CheckTriggersRequest\x1a\x37.silenteight.agent.hittype.v1.api.CheckTriggersResponse"\x00\x42\x44\n*com.silenteight.proto.agent.hittype.v1.apiB\x14HitTypeAgentApiProtoP\x01\x62\x06proto3',
)


_STRINGLIST = _descriptor.Descriptor(
    name="StringList",
    full_name="silenteight.agent.hittype.v1.api.StringList",
    filename=None,
    file=DESCRIPTOR,
    containing_type=None,
    create_key=_descriptor._internal_create_key,
    fields=[
        _descriptor.FieldDescriptor(
            name="tokens",
            full_name="silenteight.agent.hittype.v1.api.StringList.tokens",
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
    ],
    extensions=[],
    nested_types=[],
    enum_types=[],
    serialized_options=None,
    is_extendable=False,
    syntax="proto3",
    extension_ranges=[],
    oneofs=[],
    serialized_start=58,
    serialized_end=86,
)


_TOKENSMAP_TOKENSMAPENTRY = _descriptor.Descriptor(
    name="TokensMapEntry",
    full_name="silenteight.agent.hittype.v1.api.TokensMap.TokensMapEntry",
    filename=None,
    file=DESCRIPTOR,
    containing_type=None,
    create_key=_descriptor._internal_create_key,
    fields=[
        _descriptor.FieldDescriptor(
            name="key",
            full_name="silenteight.agent.hittype.v1.api.TokensMap.TokensMapEntry.key",
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
            name="value",
            full_name="silenteight.agent.hittype.v1.api.TokensMap.TokensMapEntry.value",
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
    serialized_options=b"8\001",
    is_extendable=False,
    syntax="proto3",
    extension_ranges=[],
    oneofs=[],
    serialized_start=182,
    serialized_end=276,
)

_TOKENSMAP = _descriptor.Descriptor(
    name="TokensMap",
    full_name="silenteight.agent.hittype.v1.api.TokensMap",
    filename=None,
    file=DESCRIPTOR,
    containing_type=None,
    create_key=_descriptor._internal_create_key,
    fields=[
        _descriptor.FieldDescriptor(
            name="tokens_map",
            full_name="silenteight.agent.hittype.v1.api.TokensMap.tokens_map",
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
        _TOKENSMAP_TOKENSMAPENTRY,
    ],
    enum_types=[],
    serialized_options=None,
    is_extendable=False,
    syntax="proto3",
    extension_ranges=[],
    oneofs=[],
    serialized_start=89,
    serialized_end=276,
)


_CHECKTRIGGERSREQUEST_TRIGGERCATEGORIESENTRY = _descriptor.Descriptor(
    name="TriggerCategoriesEntry",
    full_name="silenteight.agent.hittype.v1.api.CheckTriggersRequest.TriggerCategoriesEntry",
    filename=None,
    file=DESCRIPTOR,
    containing_type=None,
    create_key=_descriptor._internal_create_key,
    fields=[
        _descriptor.FieldDescriptor(
            name="key",
            full_name="silenteight.agent.hittype.v1.api.CheckTriggersRequest.TriggerCategoriesEntry.key",
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
            name="value",
            full_name="silenteight.agent.hittype.v1.api.CheckTriggersRequest.TriggerCategoriesEntry.value",
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
    serialized_options=b"8\001",
    is_extendable=False,
    syntax="proto3",
    extension_ranges=[],
    oneofs=[],
    serialized_start=548,
    serialized_end=650,
)

_CHECKTRIGGERSREQUEST_TRIGGEREDTOKENSENTRY = _descriptor.Descriptor(
    name="TriggeredTokensEntry",
    full_name="silenteight.agent.hittype.v1.api.CheckTriggersRequest.TriggeredTokensEntry",
    filename=None,
    file=DESCRIPTOR,
    containing_type=None,
    create_key=_descriptor._internal_create_key,
    fields=[
        _descriptor.FieldDescriptor(
            name="key",
            full_name="silenteight.agent.hittype.v1.api.CheckTriggersRequest.TriggeredTokensEntry.key",
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
            name="value",
            full_name="silenteight.agent.hittype.v1.api.CheckTriggersRequest.TriggeredTokensEntry.value",
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
    serialized_options=b"8\001",
    is_extendable=False,
    syntax="proto3",
    extension_ranges=[],
    oneofs=[],
    serialized_start=652,
    serialized_end=751,
)

_CHECKTRIGGERSREQUEST = _descriptor.Descriptor(
    name="CheckTriggersRequest",
    full_name="silenteight.agent.hittype.v1.api.CheckTriggersRequest",
    filename=None,
    file=DESCRIPTOR,
    containing_type=None,
    create_key=_descriptor._internal_create_key,
    fields=[
        _descriptor.FieldDescriptor(
            name="normal_trigger_categories",
            full_name="silenteight.agent.hittype.v1.api.CheckTriggersRequest.normal_trigger_categories",
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
            name="trigger_categories",
            full_name="silenteight.agent.hittype.v1.api.CheckTriggersRequest.trigger_categories",
            index=1,
            number=2,
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
        _descriptor.FieldDescriptor(
            name="triggered_tokens",
            full_name="silenteight.agent.hittype.v1.api.CheckTriggersRequest.triggered_tokens",
            index=2,
            number=3,
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
        _CHECKTRIGGERSREQUEST_TRIGGERCATEGORIESENTRY,
        _CHECKTRIGGERSREQUEST_TRIGGEREDTOKENSENTRY,
    ],
    enum_types=[],
    serialized_options=None,
    is_extendable=False,
    syntax="proto3",
    extension_ranges=[],
    oneofs=[],
    serialized_start=279,
    serialized_end=751,
)


_CHECKTRIGGERSRESPONSE = _descriptor.Descriptor(
    name="CheckTriggersResponse",
    full_name="silenteight.agent.hittype.v1.api.CheckTriggersResponse",
    filename=None,
    file=DESCRIPTOR,
    containing_type=None,
    create_key=_descriptor._internal_create_key,
    fields=[
        _descriptor.FieldDescriptor(
            name="solution",
            full_name="silenteight.agent.hittype.v1.api.CheckTriggersResponse.solution",
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
            full_name="silenteight.agent.hittype.v1.api.CheckTriggersResponse.reason",
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
    serialized_start=753,
    serialized_end=860,
)


_HITTYPESREASON = _descriptor.Descriptor(
    name="HitTypesReason",
    full_name="silenteight.agent.hittype.v1.api.HitTypesReason",
    filename=None,
    file=DESCRIPTOR,
    containing_type=None,
    create_key=_descriptor._internal_create_key,
    fields=[
        _descriptor.FieldDescriptor(
            name="hit_categories",
            full_name="silenteight.agent.hittype.v1.api.HitTypesReason.hit_categories",
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
            name="normal_categories",
            full_name="silenteight.agent.hittype.v1.api.HitTypesReason.normal_categories",
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
    serialized_start=862,
    serialized_end=929,
)

_TOKENSMAP_TOKENSMAPENTRY.fields_by_name["value"].message_type = _STRINGLIST
_TOKENSMAP_TOKENSMAPENTRY.containing_type = _TOKENSMAP
_TOKENSMAP.fields_by_name["tokens_map"].message_type = _TOKENSMAP_TOKENSMAPENTRY
_CHECKTRIGGERSREQUEST_TRIGGERCATEGORIESENTRY.fields_by_name["value"].message_type = _STRINGLIST
_CHECKTRIGGERSREQUEST_TRIGGERCATEGORIESENTRY.containing_type = _CHECKTRIGGERSREQUEST
_CHECKTRIGGERSREQUEST_TRIGGEREDTOKENSENTRY.fields_by_name["value"].message_type = _TOKENSMAP
_CHECKTRIGGERSREQUEST_TRIGGEREDTOKENSENTRY.containing_type = _CHECKTRIGGERSREQUEST
_CHECKTRIGGERSREQUEST.fields_by_name[
    "trigger_categories"
].message_type = _CHECKTRIGGERSREQUEST_TRIGGERCATEGORIESENTRY
_CHECKTRIGGERSREQUEST.fields_by_name[
    "triggered_tokens"
].message_type = _CHECKTRIGGERSREQUEST_TRIGGEREDTOKENSENTRY
_CHECKTRIGGERSRESPONSE.fields_by_name["reason"].message_type = _HITTYPESREASON
DESCRIPTOR.message_types_by_name["StringList"] = _STRINGLIST
DESCRIPTOR.message_types_by_name["TokensMap"] = _TOKENSMAP
DESCRIPTOR.message_types_by_name["CheckTriggersRequest"] = _CHECKTRIGGERSREQUEST
DESCRIPTOR.message_types_by_name["CheckTriggersResponse"] = _CHECKTRIGGERSRESPONSE
DESCRIPTOR.message_types_by_name["HitTypesReason"] = _HITTYPESREASON
_sym_db.RegisterFileDescriptor(DESCRIPTOR)

StringList = _reflection.GeneratedProtocolMessageType(
    "StringList",
    (_message.Message,),
    {
        "DESCRIPTOR": _STRINGLIST,
        "__module__": "hit_type_agent_pb2"
        # @@protoc_insertion_point(class_scope:silenteight.agent.hittype.v1.api.StringList)
    },
)
_sym_db.RegisterMessage(StringList)

TokensMap = _reflection.GeneratedProtocolMessageType(
    "TokensMap",
    (_message.Message,),
    {
        "TokensMapEntry": _reflection.GeneratedProtocolMessageType(
            "TokensMapEntry",
            (_message.Message,),
            {
                "DESCRIPTOR": _TOKENSMAP_TOKENSMAPENTRY,
                "__module__": "hit_type_agent_pb2"
                # @@protoc_insertion_point(class_scope:silenteight.agent.hittype.v1.api.TokensMap.TokensMapEntry)
            },
        ),
        "DESCRIPTOR": _TOKENSMAP,
        "__module__": "hit_type_agent_pb2"
        # @@protoc_insertion_point(class_scope:silenteight.agent.hittype.v1.api.TokensMap)
    },
)
_sym_db.RegisterMessage(TokensMap)
_sym_db.RegisterMessage(TokensMap.TokensMapEntry)

CheckTriggersRequest = _reflection.GeneratedProtocolMessageType(
    "CheckTriggersRequest",
    (_message.Message,),
    {
        "TriggerCategoriesEntry": _reflection.GeneratedProtocolMessageType(
            "TriggerCategoriesEntry",
            (_message.Message,),
            {
                "DESCRIPTOR": _CHECKTRIGGERSREQUEST_TRIGGERCATEGORIESENTRY,
                "__module__": "hit_type_agent_pb2"
                # @@protoc_insertion_point(class_scope:silenteight.agent.hittype.v1.api.CheckTriggersRequest.TriggerCategoriesEntry)
            },
        ),
        "TriggeredTokensEntry": _reflection.GeneratedProtocolMessageType(
            "TriggeredTokensEntry",
            (_message.Message,),
            {
                "DESCRIPTOR": _CHECKTRIGGERSREQUEST_TRIGGEREDTOKENSENTRY,
                "__module__": "hit_type_agent_pb2"
                # @@protoc_insertion_point(class_scope:silenteight.agent.hittype.v1.api.CheckTriggersRequest.TriggeredTokensEntry)
            },
        ),
        "DESCRIPTOR": _CHECKTRIGGERSREQUEST,
        "__module__": "hit_type_agent_pb2"
        # @@protoc_insertion_point(class_scope:silenteight.agent.hittype.v1.api.CheckTriggersRequest)
    },
)
_sym_db.RegisterMessage(CheckTriggersRequest)
_sym_db.RegisterMessage(CheckTriggersRequest.TriggerCategoriesEntry)
_sym_db.RegisterMessage(CheckTriggersRequest.TriggeredTokensEntry)

CheckTriggersResponse = _reflection.GeneratedProtocolMessageType(
    "CheckTriggersResponse",
    (_message.Message,),
    {
        "DESCRIPTOR": _CHECKTRIGGERSRESPONSE,
        "__module__": "hit_type_agent_pb2"
        # @@protoc_insertion_point(class_scope:silenteight.agent.hittype.v1.api.CheckTriggersResponse)
    },
)
_sym_db.RegisterMessage(CheckTriggersResponse)

HitTypesReason = _reflection.GeneratedProtocolMessageType(
    "HitTypesReason",
    (_message.Message,),
    {
        "DESCRIPTOR": _HITTYPESREASON,
        "__module__": "hit_type_agent_pb2"
        # @@protoc_insertion_point(class_scope:silenteight.agent.hittype.v1.api.HitTypesReason)
    },
)
_sym_db.RegisterMessage(HitTypesReason)


DESCRIPTOR._options = None
_TOKENSMAP_TOKENSMAPENTRY._options = None
_CHECKTRIGGERSREQUEST_TRIGGERCATEGORIESENTRY._options = None
_CHECKTRIGGERSREQUEST_TRIGGEREDTOKENSENTRY._options = None

_HITTYPEAGENT = _descriptor.ServiceDescriptor(
    name="HitTypeAgent",
    full_name="silenteight.agent.hittype.v1.api.HitTypeAgent",
    file=DESCRIPTOR,
    index=0,
    serialized_options=None,
    create_key=_descriptor._internal_create_key,
    serialized_start=932,
    serialized_end=1079,
    methods=[
        _descriptor.MethodDescriptor(
            name="CheckTriggers",
            full_name="silenteight.agent.hittype.v1.api.HitTypeAgent.CheckTriggers",
            index=0,
            containing_service=None,
            input_type=_CHECKTRIGGERSREQUEST,
            output_type=_CHECKTRIGGERSRESPONSE,
            serialized_options=None,
            create_key=_descriptor._internal_create_key,
        ),
    ],
)
_sym_db.RegisterServiceDescriptor(_HITTYPEAGENT)

DESCRIPTOR.services_by_name["HitTypeAgent"] = _HITTYPEAGENT

# @@protoc_insertion_point(module_scope)
