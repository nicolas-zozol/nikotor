Concepts
====

### Command


### Event



### Aggregate

In terms of Domain-Driven Design Aggregate is a logical group of Entities and Value Objects that are treated as a single unit (OOP, Composition). 
Aggregate Root is a single one Entity that all others are bound to.

The aggregate Root just kills the concept that DDD has invented microservices before everybody

Aggregate is just a link of objects, not necessarily isolated from other objects.


### Process Manager

Process Manager is a machine that takes events as input and produces commands as output.

Problem is that a race could happen, leaving two differents states with two different rebuild

### Subscriber





Exemple
====

Simple
---



With Subscriber
----




Complex
-----

### Login process

Tradition: pair login+password => User

Option 1

CQRSlight: Getting User with good login/pass is a query. 
However we could save the fact that someone did logged.
React makes Query. Just before returning the result, or with a @after action create a DidLoginCommand


Option 2

Getting User with good login/pass is a query, but in a domain driven manner, the user did make a action.
It should be a LoginCommand, generating a LoginEvent.
The LoginCommand has a UUID. If success, a LoginAttempt is stored including the User and a hash of login/pass. It could also store a JWT token. 

The front can query the User associated with the UUID and a hash of login/password.

That's pure CQRS.

It looks correct, but hey... That could be a huge security hole as I'm not a specialist. Obviously also depending on your Authentication system.


Option 2 is pure CQRS. The authentication procedure makes this surely possible but unknown as of today by your humble coder.


So how to do it ?

A react application has a LoginForm, and sends a LoginPayload to the cqrs server.
LoginPayload has a: email/password/attemptToken ; this token is a random uuid token. If email & password are ok, then email/attemptToken/now() is stored in the LoginAttempts dataset.
 Not the clear password obviously.

The client knows the attemptToken and should be the only one to know it in the next 2 minutes. So it can query for the User, providing the attemptToken: queryUserByAttemptToken(token)

Is it secure ?


 






Option - my preferred - 3 

The LoginCommand returns a value. That value depends on your system. it can be a JwtToken or the User object. And it's not CQRS.

   



  









