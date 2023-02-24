# Changelog

## [v1.1.1 (2023-02-24)](https://github.com/AlexisJehan/MavenCheck/releases/tag/v1.1.1)

### Notes
- Update the `gradle-tooling-api` dependency to `8.0.1`
- Update `log4j-api`, `log4j-core` and `log4j-slf4j-impl` dependencies to `2.20.0`
- Update the `maven-assembly-plugin` plugin to `3.5.0`

## [v1.1.0 (2023-02-09)](https://github.com/AlexisJehan/MavenCheck/releases/tag/v1.1.0)

### New features
- Add the `--max-depth`/`-d` option to customize the maximum depth of subdirectories to find build

### Improvements
- Improve resolving the _Maven_ version
- Improve _Maven_ dependency injection by migrating from `ServiceLocator` to `Eclipse Sisu`
- Improve exceptions output
- Improve some log levels

### Notes
- Include the `maven-pmd-plugin` plugin with `pmd-core` and `pmd-java` dependencies
- Migrate the `mockito-inline` dependency to `mockito-core`
- Update the `maven-core` dependency to `3.9.0`
- Update `maven-resolver-connector-basic` and `maven-resolver-transport-http` dependencies to `1.9.4`
- Update the `junit-jupiter` dependency to `5.9.2`
- Update the `assertj-core` dependency to `3.24.2`
- Update `mockito-core` and `mockito-junit-jupiter` dependencies to `5.1.1`
- Remove the `maven-resolver-transport-file` dependency

## [v1.0.1 (2023-01-02)](https://github.com/AlexisJehan/MavenCheck/releases/tag/v1.0.1)

### Bug fixes
- Fix missing artifacts related to _Gradle_ `:dependencies` resolved versions

### Notes
- Update the `maven-core` dependency to `3.8.7`
- Update `mockito-inline` and `mockito-junit-jupiter` dependencies to `4.11.0`

## [v1.0.0 (2022-12-23)](https://github.com/AlexisJehan/MavenCheck/releases/tag/v1.0.0)
Initial release