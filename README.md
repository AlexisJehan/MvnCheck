<image src="logo.png" align="right" width="200"/>

# Maven Check
[![release](https://img.shields.io/github/v/release/AlexisJehan/MavenCheck?display_name=tag)](https://github.com/AlexisJehan/MavenCheck/releases/latest)
[![build](https://img.shields.io/github/actions/workflow/status/AlexisJehan/MavenCheck/ci.yml?branch=main)](https://github.com/AlexisJehan/MavenCheck/actions/workflows/ci.yml)
[![coverage](https://img.shields.io/codecov/c/github/AlexisJehan/MavenCheck)](https://codecov.io/gh/AlexisJehan/MavenCheck)
[![license](https://img.shields.io/github/license/AlexisJehan/MavenCheck)](LICENSE.txt)

A _Java 11+_ command line tool to check for artifact updates of _Maven_ and _Gradle_ projects.

## Introduction
**Maven Check** aims to help developers to keep their projects up-to-date by checking for last available dependency and
plugin versions. Unlike some existing _Maven_ plugins, _Maven Check_ uses an advanced filtering system to choose the
most relevant update version for an artifact.

### Features
- Standalone and portable
- Recursive build files lookup
- Compact and comprehensible output
- Smart filtering to exclude non-release versions
- Smart filtering to select versions with the same qualifier (if any)
- Possibility to filter specific artifacts or update versions using ignore files
- Use any already installed _Maven_ configuration and repository
- Use any already installed _Gradle_ installation if available, or download a wrapper distribution

### Output example
```
2 build file(s) found, checking for artifact updates

my-gradle-project\build.gradle
[COMPILE ONLY] com.google.guava:guava 31.0-android -> 31.1-android
1 artifact update(s) available

my-maven-project\pom.xml
[DEPENDENCY] org.apache.commons:commons-lang3 3.10 -> 3.12.0
[BUILD PLUGIN] org.apache.maven.plugins:maven-compiler-plugin 3.10.0 -> 3.10.1
2 artifact update(s) available

2/2 build file(s) checked, 3 artifact update(s) available
```

## Getting started

### Prerequisites
_Maven Check_ is running on most operating systems, it only requires _Java 11_ or higher to be installed.

You can check that _Java_ is available in your environment and its version using the following command:
```console
java -version
```

### Installation
You can download binaries for the latest version of _Maven Check_ on the
[release](https://github.com/AlexisJehan/MavenCheck/releases/latest) page.

#### Linux / Mac
Execute following commands in the folder where the downloaded archive file is
located:
```console
sudo tar -xvzf maven-check-1.1.0-bin.tar.gz --directory /opt
export PATH=$PATH:/opt/maven-check-1.1.0/bin
```

#### Windows
Extract the content of the downloaded archive file and move the extracted folder to `C:\maven-check-1.1.0`, then:
- Right-click on the _Windows_ icon and select "System"
- On the right, click on "Advanced system settings"
- In the new window, click on "Environment Variables..."
- Find the "Path" variable, choose "Edit", and add `;C:\maven-check-1.1.0\bin` at the end

### Verify
If _Maven Check_ is correctly installed, this command should work as expected:
```console
mvnchk --version
```

## Usage
```
usage: mvnchk [<path>] [-d <arg>] [-h] [-i] [-s] [-v]
 -d,--max-depth <arg>    Maximum depth of subdirectories to find build
                         files (a non-negative integer)
 -h,--help               Display help information
 -i,--ignore-snapshots   Ignore build file artifacts with a snapshot
                         version
 -s,--short              Only show build files with at least one artifact
                         update
 -v,--version            Display version information
```

## Ignore file
_Maven Check_ allows to ignore artifacts or update versions by having a `.mvnchk-ignore` file:
- globally in the user home
- specifically in a project directory.

Here is an example of how to write it:
```
# Ignore a specific artifact
com.google.guava:guava

# Ignore a specific artifact update version
com.google.guava:guava:30.0-android

# Ignore artifact update versions using the "any characters" wildcard
com.google.guava:guava:*-android

# Ignore artifact update versions using the "any single character" wildcard
com.google.guava:guava:30.?-android
```

## Compatibility matrix
The table below shows which version of _Maven_ and _Gradle_ is used by each _Maven Check_ release. However, a higher
version of a build tool may still be compatible.

| Maven Check | Maven version | Gradle version |
|:-----------:|:-------------:|:--------------:|
|    1.1.0    |     3.9.0     |      7.6       |
|    1.0.1    |     3.8.7     |      7.6       |
|    1.0.0    |     3.8.6     |      7.6       |

Note: _Gradle_ is compatible starting with _Gradle 4.8_.

## License
This project is licensed under the [MIT License](LICENSE.txt).