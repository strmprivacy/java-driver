## [5.1.1](https://github.com/strmprivacy/java-driver/compare/v5.1.0...v5.1.1) (2023-07-25)


### Bug Fixes

* **deps:** set slf4j from api to implementation ([#17](https://github.com/strmprivacy/java-driver/issues/17)) ([0fd2708](https://github.com/strmprivacy/java-driver/commit/0fd2708d1a8d581282d480d895c17a2f1017bf86))

# [5.1.0](https://github.com/strmprivacy/java-driver/compare/v5.0.1...v5.1.0) (2023-07-06)


### Features

* **deps:** upgrade dependencies ([8767141](https://github.com/strmprivacy/java-driver/commit/8767141922de25bc37cc69ee6631e542719d0d75))

## [5.0.1](https://github.com/strmprivacy/java-driver/compare/v5.0.0...v5.0.1) (2023-05-03)


### Bug Fixes

* **strm-2387:** use api instead of implementation dependency for sl4j ([#16](https://github.com/strmprivacy/java-driver/issues/16)) ([d545ecb](https://github.com/strmprivacy/java-driver/commit/d545ecb410b73f9c4bef98c0f823cc81e10c260c)), closes [#10](https://github.com/strmprivacy/java-driver/issues/10)

# [5.0.0](https://github.com/strmprivacy/java-driver/compare/v4.1.0...v5.0.0) (2023-05-03)


### Features

* **strm-2387:** upgrade to latest jetty version and remove deprecated usages ([#15](https://github.com/strmprivacy/java-driver/issues/15)) ([98cd684](https://github.com/strmprivacy/java-driver/commit/98cd68428d8742c0bcee2abc1cada250c2d90c37))


### BREAKING CHANGES

* **strm-2387:** targets Java 11 - for Java 8 support, use v4.1.0

# [4.1.0](https://github.com/strmprivacy/java-driver/compare/v4.0.0...v4.1.0) (2023-04-24)


### Features

* update dependencies ([8979e76](https://github.com/strmprivacy/java-driver/commit/8979e762fda75808e77ca007113c5774fe5a8af6))
* update dependencies ([#14](https://github.com/strmprivacy/java-driver/issues/14)) ([3b01a5b](https://github.com/strmprivacy/java-driver/commit/3b01a5bace87e4735bc65ef52f9db273eda4f776))

# [4.0.0](https://github.com/strmprivacy/java-driver/compare/v3.0.0...v4.0.0) (2022-07-12)


### Features

* **strm-1259:** remove billing id usage ([#13](https://github.com/strmprivacy/java-driver/issues/13)) ([cd08301](https://github.com/strmprivacy/java-driver/commit/cd083018186c5ca6d6c2fde2b062b62b3204fe58))


### BREAKING CHANGES

* **strm-1259:** authentication has changed

# [3.0.0](https://github.com/strmprivacy/java-driver/compare/v2.0.1...v3.0.0) (2021-12-31)


* fix!: code cleanup, refactoring ([0e7e95b](https://github.com/strmprivacy/java-driver/commit/0e7e95bccf4417216fefd43af90e5fbe50b21640))


### BREAKING CHANGES

* removed websocket receive events, remove avro_json support

## [2.0.1](https://github.com/strmprivacy/java-driver/compare/v2.0.0...v2.0.1) (2021-12-13)


### Bug Fixes

* removed unused/unwanted log4j dependency ([492240a](https://github.com/strmprivacy/java-driver/commit/492240a15fdc1ddaef0da885c268328b861de13f))
* upgraded grgit and node ([dfe7133](https://github.com/strmprivacy/java-driver/commit/dfe71336081c8fea0cc09f873a308721dac860f9))

# [2.0.0](https://github.com/streammachineio/java-driver/compare/v1.0.2...v2.0.0) (2021-11-10)


### Bug Fixes

* update configuration defaults ([13d0fc2](https://github.com/streammachineio/java-driver/commit/13d0fc2dabed0e1b46a35598323f0279fd85b30c))


### Features

* naming consistency ([f8e11c7](https://github.com/streammachineio/java-driver/commit/f8e11c71aa3a51003f9667f28e6e51200736e110))
* rename to strm privacy ([4bd77aa](https://github.com/streammachineio/java-driver/commit/4bd77aa9f8981f609dedc6495e7ce7e48ddc0844))


### BREAKING CHANGES

* StreamMachineEvent -> StrmPrivacyEvent

## [1.0.2](https://github.com/strmprivacy/java-driver/compare/v1.0.1...v1.0.2) (2021-09-24)


### Bug Fixes

* fail CompletableFuture on http errors ([#7](https://github.com/strmprivacy/java-driver/issues/7)) ([30e734c](https://github.com/strmprivacy/java-driver/commit/30e734c15b9c5cb6f43d214080a78fc32bfd6168))

## [1.0.1](https://github.com/strmprivacy/java-driver/compare/v1.0.0...v1.0.1) (2021-08-06)


### Bug Fixes

* set compile scope for specific transitive dependencies to fix examples ([92ae940](https://github.com/strmprivacy/java-driver/commit/92ae9402237bca87d994ba3f3226a34d333b342c))

# [1.0.0](https://github.com/strmprivacy/java-driver/compare/v0.1.0...v1.0.0) (2021-08-06)


### Features

* added a stop method to shut down the client ([cdcee6d](https://github.com/strmprivacy/java-driver/commit/cdcee6d7a5bb1843d4cf776f7a0bad1475ff5274))
* convert to gradle; add support for non legacy schemas ([e84135f](https://github.com/strmprivacy/java-driver/commit/e84135f44ff8fdbfa3fcb7b7c9fbe374284f8bbb))
* private schemas and contracts ([0b3cb35](https://github.com/strmprivacy/java-driver/commit/0b3cb35f7628b114d69aaf24e362363df91b101a))


### BREAKING CHANGES

* added support for new schemas and contracts

# 0.2.0 (2021-03-18)


### Features
* Replaced `asynchttpclient` with Jetty client
* Support for HTTP/2

### Deprecation
* Removed Server Sent Events support

# 0.1.0 (2020-11-16)


### Features
* Adds websocket support

### Deprecation
* Server Sent Events are deprecated in favor of websocket support

# 0.0.1 (2020-09-28)


### Features
* Initial release of Java Driver
* Supports authentication, sending and receiving (server sent) events to/from strmprivacy.io
