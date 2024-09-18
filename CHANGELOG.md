# Changelog

## 1.7.0 (unreleased)

### New features
- [[#5](https://github.com/AlexisJehan/MvnCheck/issues/5)] Generate an executable JAR as an extra release file (thanks
  to [TobseF](https://github.com/TobseF))
- Add the `--include-output`/`-o` option to include build files from output directories (`target` for _Maven_ and
  `build` for _Gradle_, note that this was the default behavior in previous releases)

### Bug fixes
- [[#3](https://github.com/AlexisJehan/MvnCheck/issues/3)] Fix parsing errors related to the BOM character of the UTF-8
  with BOM encoding (thanks to [TobseF](https://github.com/TobseF))

### Improvements
- Improve the _Maven Artifact Resolver_ usage by migrating from `Eclipse Sisu` to `Supplier`
- Build files from output directories (`target` for _Maven_ and `build` for _Gradle_) are now excluded by default

### Notes
- Add the `maven-resolver-supplier` dependency
- Update the `maven-core` dependency to `3.9.9`
- Update the `gradle-tooling-api` dependency to `8.10.1`
- Update the `github-api` dependency to `1.326`
- Update the `commons-cli` dependency to `1.9.0`
- Update `log4j-api`, `log4j-core`, `log4j-jul` and `log4j-slf4j-impl` dependencies to `2.24.0`
- Update the `junit-jupiter` dependency to `5.11.0`
- Update `mockito-core` and `mockito-junit-jupiter` dependencies to `5.13.0`
- Update the `maven-pmd-plugin` plugin to `3.25.0`
- Update `pmd-core` and `pmd-java` dependencies to `7.5.0`
- Update `maven-surefire-plugin` and `maven-failsafe-plugin` plugins to `3.5.0`
- Update the `maven-javadoc-plugin` plugin to `3.10.0`
- Update the `maven-gpg-plugin` plugin to `3.2.6`
- Remove `maven-resolver-connector-basic` and `maven-resolver-transport-http` dependencies

## [1.6.2](https://github.com/AlexisJehan/MvnCheck/releases/tag/v1.6.2) (2024-07-16)

### Notes
- Update the `maven-core` dependency to `3.9.8`
- Update the `gradle-tooling-api` dependency to `8.9`
- Update the `github-api` dependency to `1.323`
- Update the `spotbugs-annotations` dependency to `4.8.6`
- Update the `junit-jupiter` dependency to `5.10.3`
- Update the `assertj-core` dependency to `3.26.3`
- Update the `maven-pmd-plugin` plugin to `3.24.0`
- Update `pmd-core` and `pmd-java` dependencies to `7.3.0`
- Update `maven-surefire-plugin` and `maven-failsafe-plugin` plugins to `3.3.1`
- Update the `maven-jar-plugin` plugin to `3.4.2`

## [1.6.1](https://github.com/AlexisJehan/MvnCheck/releases/tag/v1.6.1) (2024-06-03)

### Notes
- Update the `maven-core` dependency to `3.9.7`
- Update `maven-resolver-connector-basic` and `maven-resolver-transport-http` dependencies to `1.9.20`
- Update the `gradle-tooling-api` dependency to `8.8`
- Update the `commons-cli` dependency to `1.8.0`
- Update the `assertj-core` dependency to `3.26.0`
- Update `mockito-core` and `mockito-junit-jupiter` dependencies to `5.12.0`
- Update the `license-maven-plugin` plugin to `4.5`
- Update the `maven-pmd-plugin` plugin to `3.22.0`
- Update `pmd-core` and `pmd-java` dependencies to `7.2.0`
- Update the `jacoco-maven-plugin` plugin to `0.8.12`
- Update the `maven-jar-plugin` plugin to `3.4.1`
- Update the `maven-source-plugin` plugin to `3.3.1`
- Update the `maven-javadoc-plugin` plugin to `3.7.0`
- Update the `maven-gpg-plugin` plugin to `3.2.4`

## [1.6.0](https://github.com/AlexisJehan/MvnCheck/releases/tag/v1.6.0) (2024-04-02)

### Bug fixes
- Fix missing artifacts from a _Maven_ dependency with the `import` scope

### Notes
- Update the `gradle-tooling-api` dependency to `8.7`
- Update the `github-api` dependency to `1.321`
- Update `log4j-api`, `log4j-core`, `log4j-jul` and `log4j-slf4j-impl` dependencies to `2.23.1`
- Update `mockito-core` and `mockito-junit-jupiter` dependencies to `5.11.0`
- Update the `maven-compiler-plugin` plugin to `3.13.0`
- Update the `maven-assembly-plugin` plugin to `3.7.1`
- Update the `maven-gpg-plugin` plugin to `3.2.2`

## [1.5.0](https://github.com/AlexisJehan/MvnCheck/releases/tag/v1.5.0) (2024-02-09)

### New features
- Display artifacts with an inherited version in parentheses
- Add the `--ignore-inherited` option to ignore build file artifacts with an inherited version

### Notes
- Update the `maven-core` dependency to `3.9.6`
- Update `maven-resolver-connector-basic` and `maven-resolver-transport-http` dependencies to `1.9.18`
- Update the `gradle-tooling-api` dependency to `8.6`
- Update the `bridge-method-annotation` dependency to `1.29`
- Update `log4j-api`, `log4j-core`, `log4j-jul` and `log4j-slf4j-impl` dependencies to `2.22.1`
- Update the `junit-jupiter` dependency to `5.10.2`
- Update the `assertj-core` dependency to `3.25.3`
- Update `mockito-core` and `mockito-junit-jupiter` dependencies to `5.10.0`
- Update the `maven-compiler-plugin` plugin to `3.12.1`
- Update `maven-surefire-plugin` and `maven-failsafe-plugin` plugins to `3.2.5`
- Update the `maven-javadoc-plugin` plugin to `3.6.3`

## [1.4.2](https://github.com/AlexisJehan/MvnCheck/releases/tag/v1.4.2) (2023-11-30)

### Notes
- Update the `gradle-tooling-api` dependency to `8.5`
- Update the `github-api` dependency to `1.318`
- Update the `commons-cli` dependency to `1.6.0`
- Update the `jansi` dependency to `2.4.1`
- Update `log4j-api`, `log4j-core`, `log4j-jul` and `log4j-slf4j-impl` dependencies to `2.22.0`
- Update the `junit-jupiter` dependency to `5.10.1`
- Update `mockito-core` and `mockito-junit-jupiter` dependencies to `5.7.0`
- Update the `maven-pmd-plugin` plugin to `3.21.2`
- Update `maven-surefire-plugin` and `maven-failsafe-plugin` plugins to `3.2.2`
- Update the `jacoco-maven-plugin` plugin to `0.8.11`
- Update the `maven-javadoc-plugin` plugin to `3.6.2`

## [1.4.1](https://github.com/AlexisJehan/MvnCheck/releases/tag/v1.4.1) (2023-10-10)

### Bug fixes
- Fix redirecting `java.util.logging` logs from the `github-api` dependency to `log4j`

### Notes
- Rename the project to `MvnCheck`
- Add the `log4j-jul` dependency
- Update the `maven-core` dependency to `3.9.5`
- Update `maven-resolver-connector-basic` and `maven-resolver-transport-http` dependencies to `1.9.16`
- Update the `gradle-tooling-api` dependency to `8.4`
- Update `mockito-core` and `mockito-junit-jupiter` dependencies to `5.6.0`
- Update the `license-maven-plugin` plugin to `4.3`
- Update the `maven-javadoc-plugin` plugin to `3.6.0`
- Update the XML schema of the `bin.xml` file to `2.2.0`

## [1.4.0](https://github.com/AlexisJehan/MvnCheck/releases/tag/v1.4.0) (2023-08-23)

### New features
- Check if an update is available to download on _GitHub_ at the end of the execution

### Notes
- Add the `github-api` dependency
- Add the `bridge-method-annotation` dependency
- Add the `spotbugs-annotations` dependency
- Update the `maven-core` dependency to `3.9.4`
- Update `maven-resolver-connector-basic` and `maven-resolver-transport-http` dependencies to `1.9.14`
- Update the `gradle-tooling-api` dependency to `8.3`
- Update the `junit-jupiter` dependency to `5.10.0`
- Update `mockito-core` and `mockito-junit-jupiter` dependencies to `5.5.0`

## [1.3.2](https://github.com/AlexisJehan/MvnCheck/releases/tag/v1.3.2) (2023-07-14)

### Notes
- Update the `gradle-tooling-api` dependency to `8.2.1`

## [1.3.1](https://github.com/AlexisJehan/MvnCheck/releases/tag/v1.3.1) (2023-06-27)

### Notes
- Update the `maven-core` dependency to `3.9.3`
- Update `maven-resolver-connector-basic` and `maven-resolver-transport-http` dependencies to `1.9.13`
- Update `mockito-core` and `mockito-junit-jupiter` dependencies to `5.4.0`
- Update the `maven-pmd-plugin` plugin to `3.21.0`
- Update `maven-surefire-plugin` and `maven-failsafe-plugin` plugins to `3.1.2`
- Update the `maven-source-plugin` plugin to `3.3.0`

## [1.3.0](https://github.com/AlexisJehan/MvnCheck/releases/tag/v1.3.0) (2023-05-15)

### Improvements
- Improve logs

### Notes
- Update the `maven-core` dependency to `3.9.2`
- Update `maven-resolver-connector-basic` and `maven-resolver-transport-http` dependencies to `1.9.10`
- Update `maven-surefire-plugin` and `maven-failsafe-plugin` plugins to `3.1.0`
- Update the `maven-assembly-plugin` plugin to `3.6.0`
- Update the `maven-gpg-plugin` plugin to `3.1.0`

## [1.2.1](https://github.com/AlexisJehan/MvnCheck/releases/tag/v1.2.1) (2023-04-26)

### Notes
- Update the `gradle-tooling-api` dependency to `8.1.1`
- Update the `junit-jupiter` dependency to `5.9.3`
- Update `mockito-core` and `mockito-junit-jupiter` dependencies to `5.3.1`
- Update the `jacoco-maven-plugin` plugin to `0.8.10`

## [1.2.0](https://github.com/AlexisJehan/MvnCheck/releases/tag/v1.2.0) (2023-04-13)

### Improvements
- Improve resolving _Maven_ artifact available versions by throwing an exception if no remote repository is available
- Improve resolving _Gradle_ build files by filtering to exclude local repositories

### Notes
- Update the `gradle-tooling-api` dependency to `8.1`
- Update `mockito-core` and `mockito-junit-jupiter` dependencies to `5.3.0`
- Update the `license-maven-plugin` plugin to `4.2`
- Update the `jacoco-maven-plugin` plugin to `0.8.9`

## [1.1.3](https://github.com/AlexisJehan/MvnCheck/releases/tag/v1.1.3) (2023-03-20)

### Notes
- Update the `maven-core` dependency to `3.9.1`
- Update `maven-resolver-connector-basic` and `maven-resolver-transport-http` dependencies to `1.9.7`
- Update `maven-surefire-plugin` and `maven-failsafe-plugin` plugins to `3.0.0`

## [1.1.2](https://github.com/AlexisJehan/MvnCheck/releases/tag/v1.1.2) (2023-03-10)

### Notes
- Update the `gradle-tooling-api` dependency to `8.0.2`
- Update `mockito-core` and `mockito-junit-jupiter` dependencies to `5.2.0`
- Update `pmd-core` and `pmd-java` dependencies to `6.55.0`
- Update the `maven-compiler-plugin` plugin to `3.11.0`

## [1.1.1](https://github.com/AlexisJehan/MvnCheck/releases/tag/v1.1.1) (2023-02-24)

### Notes
- Update the `gradle-tooling-api` dependency to `8.0.1`
- Update `log4j-api`, `log4j-core` and `log4j-slf4j-impl` dependencies to `2.20.0`
- Update the `maven-javadoc-plugin` plugin to `3.5.0`
- Update the `maven-assembly-plugin` plugin to `3.5.0`

## [1.1.0](https://github.com/AlexisJehan/MvnCheck/releases/tag/v1.1.0) (2023-02-09)

### New features
- Add the `--max-depth`/`-d` option to customize the maximum depth of subdirectories to find build

### Improvements
- Improve resolving the _Maven_ version
- Improve the _Maven Artifact Resolver_ usage by migrating from `ServiceLocator` to `Eclipse Sisu`
- Improve exceptions output
- Improve some log levels

### Notes
- Add the `maven-pmd-plugin` plugin with `pmd-core` and `pmd-java` dependencies
- Migrate the `mockito-inline` dependency to `mockito-core`
- Update the `maven-core` dependency to `3.9.0`
- Update `maven-resolver-connector-basic` and `maven-resolver-transport-http` dependencies to `1.9.4`
- Update the `junit-jupiter` dependency to `5.9.2`
- Update the `assertj-core` dependency to `3.24.2`
- Update `mockito-core` and `mockito-junit-jupiter` dependencies to `5.1.1`
- Remove the `maven-resolver-transport-file` dependency

## [1.0.1](https://github.com/AlexisJehan/MvnCheck/releases/tag/v1.0.1) (2023-01-02)

### Bug fixes
- Fix missing artifacts related to _Gradle_ `:dependencies` resolved versions

### Notes
- Update the `maven-core` dependency to `3.8.7`
- Update `mockito-inline` and `mockito-junit-jupiter` dependencies to `4.11.0`

## [1.0.0](https://github.com/AlexisJehan/MvnCheck/releases/tag/v1.0.0) (2022-12-23)
Initial release