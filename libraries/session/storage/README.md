# Session.storage

The `storage` module needs to know about the `session` modules DAOs and storage models to add them
to the apps database. 

However, in our app a module exposes domain-specific functionality and hides the implementation details. 
These DAOs and storage models are part of the data layer and should not be exposed in the module's public API.
So we crate a `storage` module to host these data layer objects that are needed outside the module. 
