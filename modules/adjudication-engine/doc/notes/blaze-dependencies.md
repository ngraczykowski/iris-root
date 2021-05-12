# Blaze dependencies

To use Blaze Persistence, add this to your `build.gradle`:

    implementation libraries.blaze_persistence_core_api
    implementation libraries.blaze_persistence_entity_view_api
    implementation libraries.blaze_persistence_entity_view_spring
    implementation libraries.blaze_persistence_integration_spring_data_2_3
    runtimeOnly libraries.blaze_persistence_core_impl
    runtimeOnly libraries.blaze_persistence_entity_view_impl
    runtimeOnly libraries.blaze_persistence_integration_hibernate_5_4
    annotationProcessor libraries.blaze_persistence_entity_view_processor
