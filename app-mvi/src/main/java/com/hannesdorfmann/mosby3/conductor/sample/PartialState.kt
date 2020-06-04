package com.hannesdorfmann.mosby3.conductor.sample

sealed class PartialState {

    object LoadingDataPartial:PartialState()

    data class DataPartial(val data:String):PartialState()

}