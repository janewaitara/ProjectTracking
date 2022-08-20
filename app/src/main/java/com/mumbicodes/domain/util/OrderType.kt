package com.mumbicodes.domain.util

sealed class OrderType {
    object Ascending : OrderType()
    object Descending : OrderType()
}
