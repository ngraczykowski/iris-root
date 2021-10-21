# -*- coding: utf-8 -*-
# Generated by the protocol buffer compiler.  DO NOT EDIT!
# source: company_name_surrounding_agent.proto
"""Generated protocol buffer code."""
from google.protobuf import descriptor as _descriptor
from google.protobuf import message as _message
from google.protobuf import reflection as _reflection
from google.protobuf import symbol_database as _symbol_database

# @@protoc_insertion_point(imports)

_sym_db = _symbol_database.Default()




DESCRIPTOR = _descriptor.FileDescriptor(
  name='company_name_surrounding_agent.proto',
  package='silenteight.agent.companynamesurrounding.v1.api',
  syntax='proto3',
  serialized_options=b'\n9com.silenteight.proto.agent.companynamesurrounding.v1.apiB#CompanyNameSurroundingAgentApiProtoP\001',
  create_key=_descriptor._internal_create_key,
  serialized_pb=b'\n$company_name_surrounding_agent.proto\x12/silenteight.agent.companynamesurrounding.v1.api\"3\n\"CheckCompanyNameSurroundingRequest\x12\r\n\x05names\x18\x01 \x03(\t\"D\n#CheckCompanyNameSurroundingResponse\x12\r\n\x05names\x18\x01 \x03(\t\x12\x0e\n\x06result\x18\x02 \x01(\r2\xea\x01\n\x1b\x43ompanyNameSurroundingAgent\x12\xca\x01\n\x1b\x43heckCompanyNameSurrounding\x12S.silenteight.agent.companynamesurrounding.v1.api.CheckCompanyNameSurroundingRequest\x1aT.silenteight.agent.companynamesurrounding.v1.api.CheckCompanyNameSurroundingResponse\"\x00\x42\x62\n9com.silenteight.proto.agent.companynamesurrounding.v1.apiB#CompanyNameSurroundingAgentApiProtoP\x01\x62\x06proto3'
)




_CHECKCOMPANYNAMESURROUNDINGREQUEST = _descriptor.Descriptor(
  name='CheckCompanyNameSurroundingRequest',
  full_name='silenteight.agent.companynamesurrounding.v1.api.CheckCompanyNameSurroundingRequest',
  filename=None,
  file=DESCRIPTOR,
  containing_type=None,
  create_key=_descriptor._internal_create_key,
  fields=[
    _descriptor.FieldDescriptor(
      name='names', full_name='silenteight.agent.companynamesurrounding.v1.api.CheckCompanyNameSurroundingRequest.names', index=0,
      number=1, type=9, cpp_type=9, label=3,
      has_default_value=False, default_value=[],
      message_type=None, enum_type=None, containing_type=None,
      is_extension=False, extension_scope=None,
      serialized_options=None, file=DESCRIPTOR,  create_key=_descriptor._internal_create_key),
  ],
  extensions=[
  ],
  nested_types=[],
  enum_types=[
  ],
  serialized_options=None,
  is_extendable=False,
  syntax='proto3',
  extension_ranges=[],
  oneofs=[
  ],
  serialized_start=89,
  serialized_end=140,
)


_CHECKCOMPANYNAMESURROUNDINGRESPONSE = _descriptor.Descriptor(
  name='CheckCompanyNameSurroundingResponse',
  full_name='silenteight.agent.companynamesurrounding.v1.api.CheckCompanyNameSurroundingResponse',
  filename=None,
  file=DESCRIPTOR,
  containing_type=None,
  create_key=_descriptor._internal_create_key,
  fields=[
    _descriptor.FieldDescriptor(
      name='names', full_name='silenteight.agent.companynamesurrounding.v1.api.CheckCompanyNameSurroundingResponse.names', index=0,
      number=1, type=9, cpp_type=9, label=3,
      has_default_value=False, default_value=[],
      message_type=None, enum_type=None, containing_type=None,
      is_extension=False, extension_scope=None,
      serialized_options=None, file=DESCRIPTOR,  create_key=_descriptor._internal_create_key),
    _descriptor.FieldDescriptor(
      name='result', full_name='silenteight.agent.companynamesurrounding.v1.api.CheckCompanyNameSurroundingResponse.result', index=1,
      number=2, type=13, cpp_type=3, label=1,
      has_default_value=False, default_value=0,
      message_type=None, enum_type=None, containing_type=None,
      is_extension=False, extension_scope=None,
      serialized_options=None, file=DESCRIPTOR,  create_key=_descriptor._internal_create_key),
  ],
  extensions=[
  ],
  nested_types=[],
  enum_types=[
  ],
  serialized_options=None,
  is_extendable=False,
  syntax='proto3',
  extension_ranges=[],
  oneofs=[
  ],
  serialized_start=142,
  serialized_end=210,
)

DESCRIPTOR.message_types_by_name['CheckCompanyNameSurroundingRequest'] = _CHECKCOMPANYNAMESURROUNDINGREQUEST
DESCRIPTOR.message_types_by_name['CheckCompanyNameSurroundingResponse'] = _CHECKCOMPANYNAMESURROUNDINGRESPONSE
_sym_db.RegisterFileDescriptor(DESCRIPTOR)

CheckCompanyNameSurroundingRequest = _reflection.GeneratedProtocolMessageType('CheckCompanyNameSurroundingRequest', (_message.Message,), {
  'DESCRIPTOR' : _CHECKCOMPANYNAMESURROUNDINGREQUEST,
  '__module__' : 'company_name_surrounding_agent_pb2'
  # @@protoc_insertion_point(class_scope:silenteight.agent.companynamesurrounding.v1.api.CheckCompanyNameSurroundingRequest)
  })
_sym_db.RegisterMessage(CheckCompanyNameSurroundingRequest)

CheckCompanyNameSurroundingResponse = _reflection.GeneratedProtocolMessageType('CheckCompanyNameSurroundingResponse', (_message.Message,), {
  'DESCRIPTOR' : _CHECKCOMPANYNAMESURROUNDINGRESPONSE,
  '__module__' : 'company_name_surrounding_agent_pb2'
  # @@protoc_insertion_point(class_scope:silenteight.agent.companynamesurrounding.v1.api.CheckCompanyNameSurroundingResponse)
  })
_sym_db.RegisterMessage(CheckCompanyNameSurroundingResponse)


DESCRIPTOR._options = None

_COMPANYNAMESURROUNDINGAGENT = _descriptor.ServiceDescriptor(
  name='CompanyNameSurroundingAgent',
  full_name='silenteight.agent.companynamesurrounding.v1.api.CompanyNameSurroundingAgent',
  file=DESCRIPTOR,
  index=0,
  serialized_options=None,
  create_key=_descriptor._internal_create_key,
  serialized_start=213,
  serialized_end=447,
  methods=[
  _descriptor.MethodDescriptor(
    name='CheckCompanyNameSurrounding',
    full_name='silenteight.agent.companynamesurrounding.v1.api.CompanyNameSurroundingAgent.CheckCompanyNameSurrounding',
    index=0,
    containing_service=None,
    input_type=_CHECKCOMPANYNAMESURROUNDINGREQUEST,
    output_type=_CHECKCOMPANYNAMESURROUNDINGRESPONSE,
    serialized_options=None,
    create_key=_descriptor._internal_create_key,
  ),
])
_sym_db.RegisterServiceDescriptor(_COMPANYNAMESURROUNDINGAGENT)

DESCRIPTOR.services_by_name['CompanyNameSurroundingAgent'] = _COMPANYNAMESURROUNDINGAGENT

# @@protoc_insertion_point(module_scope)
