# business_layer.api package

## Submodules

## business_layer.api.api module


### _class_ BaseCustomKnowledge()
Bases: `abc.ABC`


#### _abc_impl(_ = <_abc_data object_ )

#### _abstract_ run(data, knowledge, measures)

* **Return type**

    `List`[`ValueKnowledge`]



### _class_ BaseCustomMeasure()
Bases: `abc.ABC`


#### _abc_impl(_ = <_abc_data object_ )

#### _abstract_ reduce_value_measure_results(value_measures)

* **Return type**

    `str`



#### _abstract_ run(data=None, knowledge=None, measures=None)

* **Return type**

    `FieldMeasure`



### _class_ FieldMeasure(recommendation, context, results, domain_name=None)
Bases: `object`


* **Parameters**


    * **recommendation** (*str*) – The field level measure recommendation - reduced from all value results


    * **context** (*str*) – The verbal string describing the field’s meaning


    * **results** (*List**[**ValueMeasure**]*) – The value measure results for all values present in the field


    * **domain_name** (*Optional**[**str**] **= None*) –



* **Return type**

    None



#### context(_: st_ )

#### domain_name(_: Optional[str_ _ = Non_ )

#### recommendation(_: st_ )

#### results(_: List[business_layer.api.api.ValueMeasure_ )

### _class_ ValueKnowledge(original_input, results, domain_name=None)
Bases: `object`


* **Parameters**


    * **original_input** (*str*) – The exact piece of input data, which created the following results


    * **results** (*Any*) – An object containing the knowledge results acquired from the original_input


    * **domain_name** (*Optional**[**str**] **= None*) –



* **Return type**

    None



#### domain_name(_: Optional[str_ _ = Non_ )

#### original_input(_: st_ )

#### results(_: An_ )

### _class_ ValueMeasure(ap_value, wl_value, evaluation, metrics=None, ignore=False, ignore_reason='')
Bases: `object`


* **Parameters**


    * **ap_value** (*str*) – The value of a field entry corresponding to one side of the measure
    (canonically the alerted party side)


    * **wl_value** (*str*) – The value of a field entry corresponding to other side of the measure
    (canonically the watchlist party side)


    * **evaluation** (*str*) – Contains the evaluation of the measure between ap_value and wl_value


    * **metrics** (*Any = None*) – Metrics used to produce evaluation


    * **ignore** (*bool = False*) – A flag indicating whether this measure should be ignored in the analysis


    * **ignore_reason** (*str = ""*) – The reason for ignoring the measure



* **Return type**

    None



#### ap_value(_: st_ )

#### evaluation(_: st_ )

#### ignore(_: boo_ _ = Fals_ )

#### ignore_reason(_: st_ _ = '_ )

#### metrics(_: An_ _ = Non_ )

#### wl_value(_: st_ )
## Module contents
