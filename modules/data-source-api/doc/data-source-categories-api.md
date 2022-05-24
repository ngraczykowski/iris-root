# Data Source - Categories API

The resources available in the Data Source - Categories API:

-  A collection of categories: `categories/*`. Each category has the following resources:
   -  A collection of match category values: `categories/*/matches`.

## Use Cases

### 1. Getting the list of categories

    GET categories -> [
        {
            name="categories/branch",
            display_name="Bank branch",
            type=ANY_STRING,
            multi_value=false
        },
        {
            name="categories/multi_countries",
            display_name="Multi countries",
            type=ANY_STRING,
            multi_value=true
        },
        {
            name="categories/customer_type",
            display_name="Customer type",
            type=ENUMERATED,
            allowed_values=["INDIVIDUAL", "COMPANY", "VESSEL"]
        }
    ]

### 2. Getting a single-valued category for matches

    POST categories/-/matches:batchGet [
        "categories/customer_type/matches/1342",
        "categories/customer_type/matches/1989"  
    ] -> {
        category_values=[
            {
                name="categories/customer_type/matches/1342",
                single_value="INDIVIDUAL"
            },
            {
                name="categories/customer_type/matches/1989",
                single_value="COMPANY"
            }
        ]
    }

### 3. Getting a multi-valued category for matches

    POST categories/-/matches:batchGet [
        "categories/multi_countries/matches/1342",
        "categories/multi_countries/matches/1989"  
    ] -> {
        category_values=[
            {
                name="categories/multi_countries/matches/1342",
                multi_value={
                    values=["UK", "US"]
                }
            },
            {
                name="categories/multi_countries/matches/1989",
                multi_value={
                    values=["SG"]
                }
            }
        ]
    }

### 4. Getting values from multiple categories

    POST categories/-/matches:batchGet [
        "categories/branch/matches/1342",
        "categories/branch/matches/1989"  
        "categories/customer_type/matches/1342",
        "categories/customer_type/matches/1989"  
    ] -> {
        category_values=[
            {
                name="categories/branch/matches/1342",
                single_value="SG"
            },
            {
                name="categories/branch/matches/1989",
                single_value="US"
            },
            {
                name="categories/customer_type/matches/1342",
                single_value="INDIVIDUAL"
            },
            {
                name="categories/customer_type/matches/1989",
                single_value="COMPANY"
            }
        ]
    }
