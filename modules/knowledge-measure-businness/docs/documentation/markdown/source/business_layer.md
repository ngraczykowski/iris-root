# business_layer package

## Subpackages


* [business_layer.api package](business_layer.api.md)


    * [Submodules](business_layer.api.md#submodules)


    * [business_layer.api.api module](business_layer.api.md#module-business_layer.api.api)


    * [Module contents](business_layer.api.md#module-business_layer.api)


* [business_layer.custom_measure package](business_layer.custom_measure.md)


    * [Submodules](business_layer.custom_measure.md#submodules)


    * [business_layer.custom_measure.passports module](business_layer.custom_measure.md#module-business_layer.custom_measure.passports)


    * [business_layer.custom_measure.sanctions module](business_layer.custom_measure.md#module-business_layer.custom_measure.sanctions)


    * [Module contents](business_layer.custom_measure.md#module-business_layer.custom_measure)


* [business_layer.temp_org_proto package](business_layer.temp_org_proto.md)


    * [Submodules](business_layer.temp_org_proto.md#submodules)


    * [business_layer.temp_org_proto.organization_name_agent_pb2 module](business_layer.temp_org_proto.md#module-business_layer.temp_org_proto.organization_name_agent_pb2)


    * [business_layer.temp_org_proto.organization_name_agent_pb2_grpc module](business_layer.temp_org_proto.md#module-business_layer.temp_org_proto.organization_name_agent_pb2_grpc)


    * [Module contents](business_layer.temp_org_proto.md#module-business_layer.temp_org_proto)


## Submodules

## business_layer.base_business_layer module


### _class_ BaseBusinessLayerNS()
Bases: `abc.ABC`


#### _abc_impl(_ = <_abc_data object_ )

#### _abstract_ find_matching_policy()

#### _abstract_ get_comment()

#### _abstract_ get_feature_vector()

#### _abstract_ provide_measures()

### _class_ BaseBusinessLayerTS()
Bases: `abc.ABC`


#### _abc_impl(_ = <_abc_data object_ )

#### _abstract_ apply_domain_hierarchy()

#### _abstract_ find_matching_policy()

#### _abstract_ get_comment()

#### _abstract_ get_feature_vector()

#### _abstract_ provide_knowledge()

#### _abstract_ provide_measures()
## business_layer.business_layer_ns module


### _class_ BusinessLayerNS(config)
Bases: `business_layer.base_business_layer.BaseBusinessLayerNS`


#### _abc_impl(_ = <_abc_data object_ )

#### find_matching_policy(feature_vector, policy_steps)

* **Parameters**


    * **feature_vector** (*dict*) –


    * **policy_steps** (*list*) –



#### get_comment(decision, condition, measures)

* **Return type**

    `str`



* **Parameters**


    * **decision** (*str*) –


    * **condition** (*Dict**[**str**, **List**[**Union**[**str**, *[*business_layer.api.api.FieldMeasure*](business_layer.api.md#business_layer.api.api.FieldMeasure)*]**]**]*) –



#### get_feature_vector(measures)

* **Parameters**

    **measures** (*Dict*) –



#### provide_measures(data, knowledge=None)

#### provide_service_measures(data)

#### solve_hit(data, policy_steps)

* **Return type**

    `SolvedHit`



* **Parameters**


    * **data** (*Dict**[**str**, **List**[**str**]**]*) –


    * **policy_steps** (*List**[**Dict**[**Any**, **Any**]**]*) –



### _class_ SolvedHit(feature_vector, decision, comment)
Bases: `object`


* **Parameters**


    * **feature_vector** (*str*) –


    * **decision** (*str*) –


    * **comment** (*str*) –



* **Return type**

    None



#### comment(_: st_ )

#### decision(_: st_ )

#### feature_vector(_: st_ )
## business_layer.business_layer_ts module


### _class_ BusinessLayerTS(config)
Bases: `business_layer.base_business_layer.BaseBusinessLayerTS`


#### _abc_impl(_ = <_abc_data object_ )

#### apply_domain_hierarchy(knowledge, measures)

#### find_matching_policy(feature_vector, policy_steps)

* **Parameters**


    * **feature_vector** (*dict*) –


    * **policy_steps** (*list*) –



#### get_comment(decision, condition, measures)

#### get_feature_vector(measures)

* **Parameters**

    **measures** (*dict*) –



#### provide_custom_knowledge(data)

#### provide_knowledge(data)

#### provide_measures(data, knowledge=None)

#### provide_service_knowledge(data)

#### provide_service_measures(data)

#### solve_hit(data, policy_steps)

* **Parameters**

    **policy_steps** (*list*) –


## business_layer.message_dispatchers module


### _class_ KnowledgeMessageDispatcher(stubs)
Bases: `object`


* **Parameters**

    **stubs** (*Dict**[**str**, **business_layer.service_wrapper.AbstractKnowledgeStubWrapper**]*) –



#### knowledge_call(stub_name, \*args, \*\*kwargs)

* **Parameters**

    **stub_name** (*str*) –



### _class_ MeasureMessageDispatcher(stubs)
Bases: `object`


* **Parameters**

    **stubs** (*Dict**[**str**, **business_layer.service_wrapper.AbstractMeasureStubWrapper**]*) –



#### measure_call(stub_name, \*args, \*\*kwargs)

* **Parameters**

    **stub_name** (*str*) –


## business_layer.service_wrapper module


### _class_ AbstractKnowledgeStubWrapper()
Bases: `abc.ABC`


#### _abc_impl(_ = <_abc_data object_ )

#### knowledge_call(payload)

* **Return type**

    [`ValueKnowledge`](business_layer.api.md#business_layer.api.api.ValueKnowledge)



* **Parameters**

    **payload** (*str*) –



### _class_ AbstractMeasureStubWrapper()
Bases: `abc.ABC`


#### _abc_impl(_ = <_abc_data object_ )

#### measure_call(ap_payload, wl_payload, context)

* **Return type**

    [`FieldMeasure`](business_layer.api.md#business_layer.api.api.FieldMeasure)



* **Parameters**


    * **ap_payload** (*Union**[**str**, **List**[**str**]**]*) –


    * **wl_payload** (*Union**[**str**, **List**[**str**]**]*) –


    * **context** (*str*) –



### _class_ GeoKnowledgeStubWrapper(stub)
Bases: `business_layer.service_wrapper.AbstractKnowledgeStubWrapper`


* **Parameters**

    **stub** (*silenteight.agent.geo.v1.api.geo_location_extractor_pb2_grpc.GeoLocationExtractorStub*) –



#### _abc_impl(_ = <_abc_data object_ )

#### knowledge_call(payload)

* **Parameters**

    **payload** (*str*) –



### _class_ GeoMeasureStubWrapper(stub)
Bases: `business_layer.service_wrapper.AbstractMeasureStubWrapper`


* **Parameters**

    **stub** (*silenteight.agent.geo.v1.api.geo_agent_pb2_grpc.GeoLocationAgentStub*) –



#### _abc_impl(_ = <_abc_data object_ )

#### measure_call(ap_payload, wl_payload, context)
Arguments for single (non-batch) call of this method must be strings.
Even if multiple values are to be passed on either the AP or the WL
side as lists,  they will be forcefully converted to strings.
This is necessary because of the reduction logic in the geo service.


* **Return type**

    [`FieldMeasure`](business_layer.api.md#business_layer.api.api.FieldMeasure)



* **Parameters**


    * **ap_payload** (*str*) –


    * **wl_payload** (*str*) –



### _class_ OrgNameKnowledgeStubWrapper(server, port)
Bases: `business_layer.service_wrapper.AbstractKnowledgeStubWrapper`


* **Parameters**


    * **server** (*str*) –


    * **port** (*int*) –



#### _abc_impl(_ = <_abc_data object_ )

#### knowledge_call(payload)

* **Parameters**

    **payload** (*str*) –



### _class_ OrgNameMeasureStubWrapper(stub)
Bases: `business_layer.service_wrapper.AbstractMeasureStubWrapper`


* **Parameters**

    **stub** ([*business_layer.temp_org_proto.organization_name_agent_pb2_grpc.OrganizationNameAgentStub*](business_layer.temp_org_proto.md#business_layer.temp_org_proto.organization_name_agent_pb2_grpc.OrganizationNameAgentStub)) –



#### _abc_impl(_ = <_abc_data object_ )

#### measure_call(ap_payload, wl_payload, context)

* **Return type**

    [`FieldMeasure`](business_layer.api.md#business_layer.api.api.FieldMeasure)



* **Parameters**


    * **ap_payload** (*List**[**str**]*) –


    * **wl_payload** (*List**[**str**]*) –


    * **context** (*str*) –



### _class_ ServiceWrapperFactory()
Bases: `object`


#### create_wrapper(name)

* **Return type**

    `Union`[`AbstractKnowledgeStubWrapper`, `AbstractMeasureStubWrapper`]



* **Parameters**

    **name** (*str*) –



#### create_wrappers(requested_services, service_type)

* **Return type**

    `Dict`[`str`, `Union`[`AbstractKnowledgeStubWrapper`, `AbstractMeasureStubWrapper`]]



* **Parameters**


    * **requested_services** (*List**[**str**]*) –


    * **service_type** (*str*) –


## business_layer.standard_functions module


### find_matching_policy(feature_vector, policy_steps)

* **Parameters**


    * **feature_vector** (*dict*) –


    * **policy_steps** (*list*) –



### get_feature_vector(measures)

* **Parameters**

    **measures** (*dict*) –



### provide_custom_measures(measure_tools, data=None, knowledge=None, measures=None)
## Module contents
