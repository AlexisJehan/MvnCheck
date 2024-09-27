<p align="center">
	<img src="logo.png" alt="Logo" width="200"/>
</p>

# MvnCheck
[![release](https://img.shields.io/github/v/release/AlexisJehan/MvnCheck?display_name=tag)](https://github.com/AlexisJehan/MvnCheck/releases/latest)
[![build](https://img.shields.io/github/actions/workflow/status/AlexisJehan/MvnCheck/ci.yml?branch=main)](https://github.com/AlexisJehan/MvnCheck/actions/workflows/ci.yml)
[![coverage](https://img.shields.io/codecov/c/github/AlexisJehan/MvnCheck)](https://codecov.io/gh/AlexisJehan/MvnCheck)
[![license](https://img.shields.io/github/license/AlexisJehan/MvnCheck)](LICENSE.txt)

A _Java 11+_ command line tool to check for artifact updates of _Maven_ and _Gradle_ projects.

## Introduction
**MvnCheck** aims to help developers to keep their projects up-to-date by checking for last available dependency and
plugin versions. Unlike some existing _Maven_ plugins, _MvnCheck_ uses an advanced filtering system to choose the
most relevant update version for an artifact.

### Features
- Standalone and portable
- Recursive build files lookup
- Compact and comprehensible output
- Smart filtering to exclude non-release versions
- Smart filtering to select versions with the same qualifier (if any)
- Possibility to filter specific artifacts or update versions using a command line option or ignore files
- Use any already installed _Maven_ configuration and repository
- Use any already installed _Gradle_ installation if available, or download a wrapper distribution

### Output example
```
2 build file(s) found, checking for artifact updates

my-gradle-project\build.gradle
[COMPILE ONLY] com.google.guava:guava 31.0-android -> 33.3.1-android
1 artifact update(s) available

my-maven-project\pom.xml
[DEPENDENCY] org.apache.commons:commons-lang3 3.10 -> 3.17.0
[BUILD PLUGIN] org.apache.maven.plugins:maven-compiler-plugin 3.10.0 -> 3.13.0
2 artifact update(s) available

2/2 build file(s) checked, 3 artifact update(s) available
```

## Getting started

### Prerequisites
_MvnCheck_ is running on most operating systems, it only requires _Java 11_ or higher to be installed.

You can check that _Java_ is available in your environment and its version using the following command:
```console
java -version
```

### Installation
You can download binaries for the latest version of _MvnCheck_ on the
[release](https://github.com/AlexisJehan/MvnCheck/releases/latest) page.

#### Linux / Mac
Execute following commands in the folder where the downloaded archive file is
located:
```console
sudo tar -xvzf mvn-check-1.7.0-bin.tar.gz --directory /opt
export PATH=$PATH:/opt/mvn-check-1.7.0/bin
```

#### Windows
Extract the content of the downloaded archive file and move the extracted folder to `C:\mvn-check-1.7.0`, then:
- Right-click on the _Windows_ icon and select "System"
- On the right, click on "Advanced system settings"
- In the new window, click on "Environment Variables..."
- Find the "Path" variable, choose "Edit", and add `;C:\mvn-check-1.7.0\bin` at the end

#### Verify
If _MvnCheck_ is correctly installed, this command should work as expected:
```console
mvnchk --version
```

### Using without installation
Alternatively, you could use _MvnCheck_ by downloading the executable JAR instead.

This way, you simply have to execute this command to run it:
```console
java -jar mvn-check-1.7.0-bin.jar
```

## Usage
```
usage: mvnchk [<path>] [-d <arg>] [-f <arg>] [-h] [-i]
       [--ignore-inherited] [-o] [-s] [-v]
 -d,--max-depth <arg>    Maximum depth of subdirectories to find build
                         files (a non-negative integer)
 -f,--filter <arg>       Filter build file artifacts with a
                         "groupId[:artifactId[:updateVersion]]" expression
                         ('?' and '*' wildcards are allowed)
 -h,--help               Display help information
 -i,--ignore-snapshots   Ignore build file artifacts with a snapshot
                         version
    --ignore-inherited   Ignore build file artifacts with an inherited
                         version
 -o,--include-output     Include build files inside output directories
 -s,--short              Only show build files with at least one artifact
                         update
 -v,--version            Display version information
```

## Ignore file
_MvnCheck_ allows to ignore artifacts or update versions by having a `.mvnchk-ignore` file:
- globally in the user home
- specifically in a project directory.

Here is an example of how to write it:
```
# Ignore a specific group
com.google.guava

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
The table below shows which version of _Maven_ and _Gradle_ is used by each _MvnCheck_ release. However, a higher
version of a build tool may still be compatible.

| MvnCheck version | Maven version | Gradle version |
|:----------------:|:-------------:|:--------------:|
|      1.7.0       |     3.9.9     |     8.10.2     |
|      1.6.2       |     3.9.8     |      8.9       |
|      1.6.1       |     3.9.7     |      8.8       |
|      1.6.0       |     3.9.6     |      8.7       |
|      1.5.0       |     3.9.6     |      8.6       |
|      1.4.2       |     3.9.5     |      8.5       |
|      1.4.1       |     3.9.5     |      8.4       |
|      1.4.0       |     3.9.4     |      8.3       |
|      1.3.2       |     3.9.3     |     8.2.1      |
|      1.3.1       |     3.9.3     |     8.1.1      |
|      1.3.0       |     3.9.2     |     8.1.1      |
|      1.2.1       |     3.9.1     |     8.1.1      |
|      1.2.0       |     3.9.1     |      8.1       |
|      1.1.3       |     3.9.1     |     8.0.2      |
|      1.1.2       |     3.9.0     |     8.0.2      |
|      1.1.1       |     3.9.0     |     8.0.1      |
|      1.1.0       |     3.9.0     |      7.6       |
|      1.0.1       |     3.8.7     |      7.6       |
|      1.0.0       |     3.8.6     |      7.6       |

Note: _Gradle_ is compatible starting with _Gradle 5.0_.

## License
This project is licensed under the [MIT License](LICENSE.txt).