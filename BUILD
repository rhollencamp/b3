load("@rules_java//java:defs.bzl", "java_binary", "java_library", "java_test")
load("@io_bazel_rules_docker//java:image.bzl", "java_image")
load("@io_bazel_rules_docker//container:container.bzl", "container_image")

package(default_visibility = ["//visibility:public"])

java_library(
    name = "b3-lib",
    srcs = glob(["src/main/java/**/*.java"]),
    resources = glob(["src/main/resources/**/*.*"]),
    deps = [
        "@maven//:org_freemarker_freemarker",
        "@maven//:org_slf4j_slf4j_api",
        "@maven//:org_springframework_boot_spring_boot",
        "@maven//:org_springframework_boot_spring_boot_autoconfigure",
        "@maven//:org_springframework_boot_spring_boot_starter_freemarker",
        "@maven//:org_springframework_boot_spring_boot_starter_web",
        "@maven//:org_springframework_spring_beans",
        "@maven//:org_springframework_spring_context",
        "@maven//:org_springframework_spring_core",
        "@maven//:org_springframework_spring_web",
        "@maven//:org_springframework_spring_webmvc",
        "@maven//:org_springframework_spring_websocket",
    ],
    runtime_deps = [
        "@maven//:org_webjars_bootstrap",
        "@maven//:org_webjars_jquery",
        "@maven//:org_webjars_popper_js",
        "@maven//:org_webjars_webjars_locator_core",
    ],
)

java_binary(
    name = "b3",
    main_class = "net.thewaffleshop.b3.B3Application",
    runtime_deps = [":b3-lib"],
)
