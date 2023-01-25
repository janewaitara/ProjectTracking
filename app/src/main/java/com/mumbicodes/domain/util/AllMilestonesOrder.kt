package com.mumbicodes.domain.util

sealed class AllMilestonesOrder() {
    object MostUrgent : AllMilestonesOrder()
    object LeastUrgent : AllMilestonesOrder()
}
