package com.mumbicodes.presentation.all_milestones

import android.app.Application
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mumbicodes.R
import com.mumbicodes.domain.relations.MilestoneWithTasks
import com.mumbicodes.domain.use_case.milestones.MilestonesUseCases
import com.mumbicodes.domain.util.AllMilestonesOrder
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import javax.inject.Inject

@HiltViewModel
class AllMilestonesViewModel @Inject constructor(
    private val milestonesUseCase: MilestonesUseCases,
    private val appContext: Application,
) : ViewModel() {
    private val _state = mutableStateOf(AllMilestonesStates())
    val state = _state

    private val _searchParam = mutableStateOf("")
    val searchParam = _searchParam

    private var getMilestonesJob: Job? = null

    init {
        milestonesUseCase.getAllMilestonesUseCase(state.value.milestonesOrder)
    }

    private fun getAllMilestones(milestonesOrder: AllMilestonesOrder, milestoneStatus: String) {
        getMilestonesJob?.cancel()

        getMilestonesJob = milestonesUseCase.getAllMilestonesUseCase(milestonesOrder)
            .onEach { milestonesWithTasks ->
                _state.value = _state.value.copy(
                    milestones = milestonesWithTasks,
                )
                milestonesWithTasks.filterMilestones(milestoneStatus, searchParam.value)
            }
            .launchIn(viewModelScope)
    }

    private fun List<MilestoneWithTasks>.filterMilestones(
        milestoneStatus: String,
        searchParam: String,
    ) {
        _state.value = _state.value.copy(
            filteredMilestones = if (milestoneStatus == appContext.getString(R.string.all)) {
                this.filter {
                    it.milestone.milestoneTitle.contains(searchParam)
                }
            } else {
                this.filter {
                    it.milestone.status == milestoneStatus
                }.filter {
                    it.milestone.milestoneTitle.contains(searchParam)
                }
            },
            selectedMilestoneStatus = milestoneStatus,
        )
    }
}
