package com.mumbicodes.projectie.domain.util

sealed class AllMilestonesOrder() {
    object MostUrgent : AllMilestonesOrder()
    object LeastUrgent : AllMilestonesOrder()
}
