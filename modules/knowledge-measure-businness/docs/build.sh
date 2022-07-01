rm -R source documentation
sphinx-apidoc -o source ../business_layer/ ../business_layer/comment ../business_layer/config \
        ../business_layer/custom_knowledge ../../business_layer/custom_measure
make markdown
