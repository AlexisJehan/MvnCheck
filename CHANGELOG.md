# Changelog

## 1.2.0 _(unreleased)_

## [1.1.0](https://github.com/AlexisJehan/MavenCheck/releases/tag/v1.1.0) _(2023-02-09)_

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

## [1.0.1](https://github.com/AlexisJehan/MavenCheck/releases/tag/v1.0.1) _(2023-01-02)_

### Bug fixes
- Fix missing artifacts related to _Gradle_ `:dependencies` resolved versions

### Notes
- Update the `maven-core` dependency to `3.8.7`
- Update `mockito-inline` and `mockito-junit-jupiter` dependencies to `4.11.0`

## [1.0.0](https://github.com/AlexisJehan/MavenCheck/releases/tag/v1.0.0) _(2022-12-23)_
Initial release