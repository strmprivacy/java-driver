## [1.0.1](https://github.com/streammachineio/java-driver/compare/v1.0.0...v1.0.1) (2021-08-06)


### Bug Fixes

* set compile scope for specific transitive dependencies to fix examples ([92ae940](https://github.com/streammachineio/java-driver/commit/92ae9402237bca87d994ba3f3226a34d333b342c))

# [1.0.0](https://github.com/streammachineio/java-driver/compare/v0.1.0...v1.0.0) (2021-08-06)


### Features

* added a stop method to shut down the client ([cdcee6d](https://github.com/streammachineio/java-driver/commit/cdcee6d7a5bb1843d4cf776f7a0bad1475ff5274))
* convert to gradle; add support for non legacy schemas ([e84135f](https://github.com/streammachineio/java-driver/commit/e84135f44ff8fdbfa3fcb7b7c9fbe374284f8bbb))
* private schemas and contracts ([0b3cb35](https://github.com/streammachineio/java-driver/commit/0b3cb35f7628b114d69aaf24e362363df91b101a))


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
* Supports authentication, sending and receiving (server sent) events to/from streammachine.io
