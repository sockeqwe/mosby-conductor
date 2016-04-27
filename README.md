# Mosby - Conductor
This plugin for using [Mosby](https://github.com/sockeqwe/mosby) with [Conductor](https://github.com/bluelinelabs/Conductor), a small, yet full-featured framework that allows building View-based Android applications.


## Dependencies
```groovy
dependencies {
    compile 'com.hannesdorfmann.mosby:mvp-conductor:0.7.0'
    compile 'com.hannesdorfmann.mosby:viewstate.conductor:0.7.0' // optional viewstate feature
}
```

Internal dependencies are Conductor 1.1.4 and Mosby 2.0.1

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


## Example
This repository contains a simple TO DO app, which is not implemented yet, because I'm waiting for some Conductor to implement some changes [#27](https://github.com/bluelinelabs/Conductor/issues/27).