package com.mumbicodes.projectie.domain.util

sealed class OrderType {
    object Ascending : OrderType()
    object Descending : OrderType()
}
