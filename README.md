# Playpen: A way to Play! safely

[![Latest Version](https://maven-badges.herokuapp.com/maven-central/com.beamly.playpen/playpen_2.11/badge.svg)](https://maven-badges.herokuapp.com/maven-central/com.beamly.playpen/playpen_2.11)
[![License](http://img.shields.io/:license-Apache%202-red.svg)](http://www.apache.org/licenses/LICENSE-2.0.txt)
[![Scaladoc](http://img.shields.io/:docs-Scaladoc-orange.svg)](http://beamly.github.io/playpen/latest/api)

[![Build Status](https://travis-ci.org/beamly/playpen.svg?branch=master)](https://travis-ci.org/beamly/playpen)
[![Dependency Status](https://www.versioneye.com/user/projects/54534f3730a8fef29200000a/badge.svg)](https://www.versioneye.com/user/projects/54534f3730a8fef29200000a)
[![Repo Size](https://reposs.herokuapp.com/?path=beamly/playpen)](http://github.com/beamly/playpen)

A general suite of utilities and other common code when building Play apps.

## How to Use

#### Add playpen to your build system.

For _sbt_:

```"com.beamly.playpen" %% "playpen" % "0.2.0"```

For _maven_:

```
<dependency>
  <groupId>com.beamly.playpen</groupId>
  <artifactId>playpen_${scala.binary}</artifactId>
  <version>0.2.0</version>
</dependency>
```
(where `scala.binary` is defined somewhere as `2.11`)

#### Configure playpen.HttpAccessLoggingFilter

Add HttpAccessLoggingFilter to the list of http filters.

If you are using runtime dependency injection, follow the Play documentation https://www.playframework.com/documentation/2.4.0/ScalaHttpFilters#Using-filters.

If you are using compile time dependency injection, add the `HttpAccessLoggingFilter` to `httpFilters` as in:
```scala
import playpen.HttpAccessLoggingFilter

class MyComponents(context: Context) extends BuiltInComponentsFromContext(context) {
  lazy val router = Router.empty
  override lazy val httpFilters = Seq(HttpAccessLoggingFilter)
}
```

## Dependencies

* Scala 2.11.x
* JodaTime 2.x
* Play 2.4.x
* Slf4J 1.7.x

## How to Release

* Bump version in README
* `sbt release`, responding to prompt as such (for example):
    Release (relative) version: 3
    Next release series [0.1]: [CTRL-D]
    [info] Not bumping release series
    Next (relative) version: 4-SNAPSHOT
* `sbt sonatypeRelease`
* `git push --follow-tags`
* git checkout the tag again
* `sbt ghpagesPushSite`
