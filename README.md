# Welcome to Podawan!

<img src="https://github.com/user-attachments/assets/a198b47b-17b9-4dd2-a4f6-ff2fce004466" width="400"/>


## Description
This project is an exploration into white labeled apps. In this codebase I have 3 different applications
for 3 different podcasts being built from the same codebase. 

The goal is to have a single codebase that can be used to build multiple apps with different branding and features.


# How to build

```
The build will fail unless:
1. You have installed the git hooks using `./scripts/install-git-hooks.sh` (sorry, cant have any smelly stinky code getting in)
2. You have the secret files that give you access to the debug and release firebases
```

Please reach out if youd like to receive access to the secret files to build the app. 

## Architecture

The architecture of this application aims to follow recommendations outlined in the [Guide To App Architecture](https://developer.android.com/topic/architecture) by: 
- ensuring unidirectional dataflow via [SEAViewModel](https://github.com/Elijah-Dangerfield/Podawan/blob/main/libraries/flowroutines/src/main/java/com.dangerfield.libraries.coreflowroutines/SEAViewModel.kt)
- maintaining an immutable state
- maintaining a clear separation of concerns between components
- using lifecycle-aware state collection
- leveraging dependency injection with Hilt

The view level architecture aims to follow a loose MVI structure without bloat code (reducers, side effect handlers, and stores). So really its just MVVM with actions to state UDF. 

## Tech stack
- Compose 
- Firebase 
- Coroutines/flow (flowroutines) 
- Datastore 
- Timber 
- Github actions - CI/CD
- Hilt - Dependency injection

## Modularization

The modularization followed in this code base aims to encourage low coupling and high cohesion as outlined in the [Guide To App Architecture](https://developer.android.com/topic/modularization)

The code base is separated into 3 module types: `library`, `feature` and `app`. The app module acts as the glue, depending on all modules. 

All Feature and Library modules aim to expose as little as possible. Libraries and features all contain a submodule `impl` where the actual beef lives (Arbys included). Doing this keeps a neat separation of concerns and helps gradle do its job. 

```
NOTE: Other than the app module, no modules should depend on anothers **impl** module. And **common** should depend on basically nothing if possible. 
```


Additionally, I leverage a `build-logic` included build with convention plugins and convenience extensions to make the gradle setup easier.

## CI/CD

This project includes a basic yet opinionated CI/CD system leveraging Github Actions.
On every PR we check:

- **build** - ensures the app isnt broken
- **style** - static code analysis that keeps that smelly code out
- **test** - runs all tests to make sure things are smoother than a fresh jar of skippy

The workflows for these can be found [here](https://github.com/Elijah-Dangerfield/Podawan/blob/main/.github/workflows)


