load("@bazel_tools//tools/build_defs/repo:http.bzl", "http_archive")

RULES_JVM_EXTERNAL_TAG = "3.2"
RULES_JVM_EXTERNAL_SHA = "82262ff4223c5fda6fb7ff8bd63db8131b51b413d26eb49e3131037e79e324af"
http_archive(
    name = "rules_jvm_external",
    strip_prefix = "rules_jvm_external-%s" % RULES_JVM_EXTERNAL_TAG,
    sha256 = RULES_JVM_EXTERNAL_SHA,
    url = "https://github.com/bazelbuild/rules_jvm_external/archive/%s.zip" % RULES_JVM_EXTERNAL_TAG,
)

SPRING_BOOT_VERSION = "2.1.13.RELEASE"
SPRING_SECURITY_VERSION = "5.1.8.RELEASE"
load("@rules_jvm_external//:defs.bzl", "maven_install")
maven_install(
    artifacts = [
        "org.springframework.boot:spring-boot-starter-freemarker:%s" % SPRING_BOOT_VERSION,
        "org.springframework.boot:spring-boot-starter-web:%s" % SPRING_BOOT_VERSION,
        "org.springframework.boot:spring-boot-starter-security:%s" % SPRING_BOOT_VERSION,
        "org.springframework.security:spring-security-oauth2-client:%s" % SPRING_SECURITY_VERSION,
        "org.webjars:bootstrap:4.3.1",
        "org.springframework.boot:spring-boot-starter-test:%s" % SPRING_BOOT_VERSION,
        "org.springframework.security:spring-security-test:%s" % SPRING_SECURITY_VERSION,
    ],
    repositories = [
        "https://repo1.maven.org/maven2",
    ],
)


BAZEL_RULES_DOCKER_TAG = "0.12.1"
BAZEL_RULES_DOCKER_SHA = "8137d638349b076a1462f06d33d3ec5aaef607c0944fd73478447a7bc313e918"
http_archive(
    name = "io_bazel_rules_docker",
    sha256 = BAZEL_RULES_DOCKER_SHA,
    strip_prefix = "rules_docker-%s" % BAZEL_RULES_DOCKER_TAG,
    url = "https://github.com/bazelbuild/rules_docker/archive/v%s.zip" % BAZEL_RULES_DOCKER_TAG,
)
load("@io_bazel_rules_docker//repositories:repositories.bzl", container_repositories = "repositories")
container_repositories()
load("@io_bazel_rules_docker//java:image.bzl", java_image_repos = "repositories")
java_image_repos()

load("@io_bazel_rules_docker//container:container.bzl", "container_pull")
container_pull(
    name = "adoptopenjdk-jre11-alpine",
    digest = "sha256:29f84106e954f393e24a880fe1179a60f1c9f424eeb0c2f2ccc3518b76ce59a8",
    registry = "registry.hub.docker.com",
    repository = "adoptopenjdk/openjdk11",
    tag = "jre-11.0.6_10-alpine",
)
