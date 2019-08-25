load("@rules_java//java:defs.bzl", "java_binary", "java_library", "java_test")
load("@io_bazel_rules_docker//java:image.bzl", "java_image")
load("@io_bazel_rules_docker//container:container.bzl", "container_image")

package(default_visibility = ["//visibility:public"])

java_library(
    name = "percolator-lib",
    srcs = glob(["src/main/java/**/*.java"]),
    resources = glob(["src/main/resources/**/*.*"]),
    deps = [
        "@maven//:com_fasterxml_jackson_core_jackson_annotations",
        "@maven//:javax_validation_validation_api",
        "@maven//:org_apache_tomcat_embed_tomcat_embed_core",
        "@maven//:org_freemarker_freemarker",
        "@maven//:org_slf4j_slf4j_api",
        "@maven//:org_springframework_boot_spring_boot",
        "@maven//:org_springframework_boot_spring_boot_autoconfigure",
        "@maven//:org_springframework_boot_spring_boot_starter_freemarker",
        "@maven//:org_springframework_boot_spring_boot_starter_web",
        "@maven//:org_springframework_boot_spring_boot_starter_security",
        "@maven//:org_springframework_spring_context",
        "@maven//:org_springframework_spring_core",
        "@maven//:org_springframework_security_spring_security_config",
        "@maven//:org_springframework_security_spring_security_core",
        "@maven//:org_springframework_spring_web",
        "@maven//:org_springframework_spring_webmvc",
    ],
    runtime_deps = [
        "@maven//:org_springframework_security_spring_security_oauth2_client",
        "@maven//:org_webjars_bootstrap",
    ],
)

java_binary(
    name = "percolator",
    main_class = "com.cargurus.percolator.PercolatorApplication",
    runtime_deps = [":percolator-lib"],
)

container_image(
    name = "percolator-container-base",
    base = "@adoptopenjdk-jre11-alpine//image",
    ports = ["8080"],
    symlinks = {
        "/usr/bin/java": "/opt/java/openjdk/bin/java"
    },
)

java_image(
    base = ":percolator-container-base",
    main_class = "com.cargurus.percolator.PercolatorApplication",
    name = "percolator-container",
    runtime_deps = [":percolator-lib"],
)

java_library(
    name = "percolator-test",
    srcs = glob(["src/test/java/**/*.java"]),
    resources = glob(["src/test/resources/**/*.*"]),
    deps = [
        ":percolator-lib",
        "@maven//:junit_junit",
        "@maven//:org_mockito_mockito_core",
        "@maven//:org_springframework_boot_spring_boot_test",
        "@maven//:org_springframework_spring_core",
        "@maven//:org_springframework_spring_test",
    ]
)

java_test(
    name = "ConsulEnvironmentPostProcessorTest",
    runtime_deps = [":percolator-test"],
    test_class = "com.cargurus.percolator.consul.ConsulEnvironmentPostProcessorTest",
)
java_test(
    name = "PercolatorApplicationTests",
    runtime_deps = [":percolator-test"],
    test_class = "com.cargurus.percolator.PercolatorApplicationTest",
)
