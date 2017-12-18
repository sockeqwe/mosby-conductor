# Mosby - Conductor
Plugin for using [Mosby](https://github.com/sockeqwe/mosby) with [Conductor](https://github.com/bluelinelabs/Conductor), a small, yet full-featured framework that allows building View-based Android applications.


## Dependencies
[![Build Status](https://travis-ci.org/sockeqwe/mosby-conductor.svg?branch=master)](https://travis-ci.org/sockeqwe/mosby-conductor)


For Conductor 2.x with Mosby 3.x use:
```groovy
dependencies {
    compile 'com.hannesdorfmann.mosby3:mvp-conductor:3.1.0'
    // or
    compile 'com.hannesdorfmann.mosby3:viewstate-conductor:3.1.0' // optional viewstate feature
    // or
    compile 'com.hannesdorfmann.mosby3:mvi-conductor:3.1.0' // Model-View-Intent
}
```

### Snapshot
Latest snapshot (from master branch):

```groovy
dependencies {
    compile 'com.hannesdorfmann.mosby3:mvi-conductor:3.1.1-SNAPSHOT' // Model-View-Intent
    // or
    compile 'com.hannesdorfmann.mosby3:mvp-conductor:3.1.1-SNAPSHOT'
    // or
    compile 'com.hannesdorfmann.mosby3:viewstate-conductor:3.1.1-SNAPSHOT' // optional viewstate feature

}
```

You also have to add the url to the snapshot repository:
```gradle
allprojects {
  repositories {
    maven { url "https://oss.sonatype.org/content/repositories/snapshots/" }
  }
}
```

## Usage
Extend your own Conductor `Controller` from `MvpController` or from `MvpViewStateController` if you want to use ViewState feature or from  or `MviController` if you want to use the Model-View-Intent feature.

There are also two base LCE (Loading-Content-Error) Controllers you can extend from: `MvpLceController` and `MvpLceViewStateController`.

If you don't want to extend from one of the base controller classes shown above, you can always bring in Mosby easily in any Conductor controller:

```java
class MyController extends Controller
                  implements MvpView, MvpConductorDelegateCallback<V, P> {

  // Initializer block (will be copied into constructor)
  {
    addLifecycleListener(new MvpConductorLifecycleListener<V, P>(this);
  }

  // implement all methods from  MvpConductorDelegateCallback like createPresenter() etc.
  ...

}
```
If you need ViewState support simply use `MvpViewStateConductorLifecycleListener` and `MvpViewStateConductorDelegateCallback`.
Do the same for  Model-View-Intent but  use `MviConductorLifecycleListener` and `MviConductorDelegateCallback`.

## Lifecycle
Presenter will be created right after `Controller.onCreateView()` but before `Controller.onAttach()`. So in `onAttach()` presenter is ready to be used.
View will be detached from Presenter in `Controller.onDestroyView()` by calling `presenter.detachView(true)` (`true` if view is just detached temporarily i.e. screen orientation change, `false` if view is detached permanently because `Controller` will be destroyed)

## Example

There is a [nice external repository](https://github.com/jshvarts/MosbyMVP) that shows how to use Mosby-Conductor. All credit goes to [James Shvarts](https://github.com/jshvarts). 

This repository contains a simple [TO DO List app](https://github.com/sockeqwe/mosby-conductor/tree/master/app) which is still work in progress.

## License

```
Copyright 2016 Hannes Dorfmann

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

   http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
```
