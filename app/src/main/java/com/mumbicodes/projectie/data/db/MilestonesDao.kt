package com.mumbicodes.projectie.data.db

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Query
import androidx.room.Upsert
import com.mumbicodes.projectie.domain.model.Milestone
import com.mumbicodes.projectie.domain.relations.MilestoneWithTasks
import kotlinx.coroutines.flow.Flow

@Dao
interface MilestonesDao {

    @Upsert
    suspend fun insertOrUpdateMilestone(milestone: Milestone)

    /** Fetch */
    @Query("SELECT * from milestones_table WHERE milestoneId = :milestoneId")
    fun getMilestoneByIdWithTasks(milestoneId: Int): Flow<MilestoneWithTasks?>

    @Query("SELECT * from milestones_table Where projectId = :projectId")
    suspend fun getAllMilestonesBasedOnProjIdAndStatus(
        projectId: Int,
    ): List<Milestone>

    @Query("SELECT * from milestones_table")
    fun getAllMilestones(): Flow<List<MilestoneWithTasks>>

    /** Deletion */
    @Delete
    suspend fun deleteMilestone(milestone: Milestone)

    @Query("DELETE FROM milestones_table WHERE projectId = :projectId")
    suspend fun deleteMilestonesForProject(projectId: Int)

    @Query("DELETE FROM milestones_table")
    suspend fun deleteAllMilestones()
}
