# auth service
The auth service handles persistence of sensitive user information, authentication and authorization
for other services. Multiple endpoints are provided to create, edit and delete entities. Most
endpoints require a user's secret to ensure that a request is not sent by a bad actor with a stolen
token.

This service provides tokens which can be used to authenticate endpoints of other services by
transparently passing this to this service on restricted endpoints.

Because only parts of other services require authentication, let alone a logged in user, no
middleware was used for routing requests. Instead services decide internally whether or not
authorization is required and communicate wiht the auth services.
