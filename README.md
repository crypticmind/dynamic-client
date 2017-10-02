
# Dynamic Client Example

This is an example project where a service consumer, instead of sending HTTP 
requests to the server itself, uses a library provided by the service 
developer to make the HTTP calls. The library code is just a service 
interface and a factory that pulls the interface implementation jar file from 
the server and loads it at runtime.

## Structure

The project contains four modules:

### `client-lib`

Includes the service interface and factory. 

The factory requires the service base `URL` to pull the implementation jar file from. 

### `client-impl`

Contains the service implementation. 

The implementation is initialized with the service base `URL`, which is passed on by the factory.  

### `server`

Contains the server implementation with at least three operations:

* `/dynamic-client/version`: Returns the server and implementation version.
* `/dynamic-client/library`: Returns the implementation jar file.
* `/dynamic-client/sum/{a}/{b}`: Returns the sum of `a` and `b` integers. This
is the example operation that is called by the client library.

### `consumer`

Contains the consumer, which has a dependency on `client-lib` only. 

The consumer asks the factory for a client, and the uses it to perform an example call.

## Implementation Details

The `server` is coded in Scala, but all the other components are code in Java
to provide a library that has as few dependencies as possible. Even though 
the client may depend on a logging library or any other utilities, coding it 
in any other language than Java would have required a dependency on that 
language's runtime library.

