NIST Data Mirror
================

A compatible branch of Steve Springett's nist-data-mirror tool.

This branch is still under development.  Use with caution!

This work adds the following features:

- can also download the NSRL hash sets
- can recursively unpack the downloaded files
- adds support for unpacking the UDF (DVD) images that the NSRL provides
- also supports using the package as a library, complete with javadoc.

Usage
----------------

### Building

```sh
mvn clean package javadoc:javadoc
```

### Running

```sh
./NistDataMirror.sh <mirror-directory>
```
or for a complete list of command line options:

```sh
./NistDataMirror.sh --help
```
nist-data-mirror is Copyright (c) Steve Springett and Brent Whitmore. All Rights Reserved.

Permission to modify and redistribute is granted under the terms of the Apache 2.0 license. See the [LICENSE] [Apache 2.0] file for the full license.

---------------------------
From Steve's original page:
---------------------------

A simple Java command-line utility to mirror the NVD (CPE/CVE XML and JSON) data from NIST.

The intended purpose of nist-data-mirror is to be able to replicate the NIST vulnerabiity data 
inside a company firewall so that local (faster) access to NIST data can be achieved.

nist-data-mirror does not rely on any third-party dependencies, only the Java SE core libraries. 
It can be used in combination with [OWASP Dependency-Check] in order to provide Dependency-Check 
a mirrored copy of NIST data.

For best results, use nist-data-mirror with cron or another scheduler to keep the mirrored data fresh.

Usage
----------------

### Building

```sh
mvn clean package
```

### Running

```sh
java -jar nist-data-mirror.jar <mirror-directory>
```

Downloading
----------------

If you do not wish to download sources and compile yourself, [pre-compiled binaries] are available 
for use. NIST Data Mirror is also available on the Maven Central Repository.

```xml
<dependency>
    <groupId>us.springett</groupId>
    <artifactId>nist-data-mirror</artifactId>
    <version>1.1.0</version>
</dependency>
```

Copyright & License
-------------------

nist-data-mirror is Copyright (c) Steve Springett. All Rights Reserved.

Dependency-Check is Copyright (c) Jeremy Long. All Rights Reserved.

Permission to modify and redistribute is granted under the terms of the Apache 2.0 license. See the [LICENSE] [Apache 2.0] file for the full license.
