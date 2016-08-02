# Mosby - Conductor
This plugin for using [Mosby](https://github.com/sockeqwe/mosby) with [Conductor](https://github.com/bluelinelabs/Conductor), a small, yet full-featured framework that allows building View-based Android applications.


## Dependencies
[![Build Status](https://travis-ci.org/sockeqwe/mosby-conductor.svg?branch=master)](https://travis-ci.org/sockeqwe/mosby-conductor)

For Conductor 1.x use:
```groovy
dependencies {
    compile 'com.hannesdorfmann.mosby:mvp-conductor:0.7.0'
    compile 'com.hannesdorfmann.mosby:viewstate-conductor:0.7.0' // optional viewstate feature
}
```
Internal dependencies are Conductor 1.1.4 and Mosby 2.0.1

For Conductor 2.x use:
```groovy
dependencies {
    compile 'com.hannesdorfmann.mosby:mvp-conductor:0.8.1'
    compile 'com.hannesdorfmann.mosby:viewstate-conductor:0.8.1' // optional viewstate feature
}
```
Internal dependencies are Conductor 2.0.1 and Mosby 2.0.1

## Usage
Extend your own Conductor `Controller` from `MvpController` or from `MvpViewStateController` if you want to use ViewState feature.

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

## Lifecycle
Presenter will be created right after `Controller.onCreateView()` but before `Controller.onAttach()`. So in `onAttach()` presenter is ready to be used.
Presenter will be destroyed in `Controller.onDestroyView()` except screen orientation changes where the Presenter will not be destroyed and recreated but rather the view will only be detached (`presenter.detachView(true)`) and new view will be reattached.

## Example
This repository contains a simple [TO DO app](https://github.com/sockeqwe/mosby-conductor/tree/master/app), which makes use of Conductor's [Nested backstacks](https://github.com/bluelinelabs/Conductor/issues/27).

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
