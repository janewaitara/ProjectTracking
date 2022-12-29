package com.mumbicodes.data.db

import androidx.room.*
import com.mumbicodes.domain.model.Milestone
import com.mumbicodes.domain.relations.MilestoneWithTasks
import kotlinx.coroutines.flow.Flow

@Dao
interface MilestonesDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMilestone(milestone: Milestone)

    /** Fetch */
    @Query("SELECT * from milestones_table WHERE milestoneId = :milestoneId")
    fun getMilestoneByIdWithTasks(milestoneId: Int): Flow<MilestoneWithTasks?>

    @Query("SELECT * from milestones_table Where projectId = :projectId")
    suspend fun getAllMilestonesBasedOnProjIdAndStatus(
        projectId: Int,
    ): List<Milestone>

    /** Deletion */
    @Delete
    suspend fun deleteMilestone(milestone: Milestone)

    @Query("DELETE FROM milestones_table WHERE projectId = :projectId")
    suspend fun deleteMilestonesForProject(projectId: Int)

    @Query("DELETE FROM milestones_table")
    suspend fun deleteAllMilestones()
}
