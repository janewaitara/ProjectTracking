package com.mumbicodes.data.db

import androidx.room.*
import com.mumbicodes.domain.model.Milestone
import kotlinx.coroutines.flow.Flow

@Dao
interface MilestonesDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMilestone(milestone: Milestone)

    /** Fetch */
    @Query("SELECT * from milestones_table WHERE milestoneId = :milestoneId")
    suspend fun getMilestoneById(milestoneId: Int): Milestone

    @Query("SELECT * from milestones_table Where projectId = :projectId AND status = :status")
    fun getAllMilestonesBasedOnProjIdAndStatus(
        projectId: Int,
        status: String?,
    ): Flow<List<Milestone>>

    /** Deletion */
    @Delete
    suspend fun deleteMilestone(milestone: Milestone)

    @Query("DELETE FROM milestones_table WHERE projectId = :projectId")
    suspend fun deleteMilestonesForProject(projectId: Int)

    @Query("DELETE FROM milestones_table")
    suspend fun deleteAllMilestones()
}
