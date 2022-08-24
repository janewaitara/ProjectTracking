package com.mumbicodes.domain.util

sealed class ProjectsOrder(val orderType: OrderType) {
    class Name(orderType: OrderType) : ProjectsOrder(orderType)
    class Deadline(orderType: OrderType) : ProjectsOrder(orderType)
    class DateAdded(orderType: OrderType) : ProjectsOrder(orderType)

    fun copy(orderType: OrderType): ProjectsOrder {
        return when (this) {
            is Name -> Name(orderType)
            is Deadline -> Name(orderType)
            is DateAdded -> Name(orderType)
        }
    }
}
