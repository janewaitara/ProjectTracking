package com.mumbicodes.data.db

import androidx.room.*
import com.mumbicodes.domain.model.Project
import com.mumbicodes.domain.relations.ProjectWithMilestones
import kotlinx.coroutines.flow.Flow

@Dao
interface ProjectsDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertProject(project: Project)

    /** Fetch */
    @Query("SELECT * from projects_table WHERE projectId = :projectId")
    suspend fun getProjectById(projectId: Int): Project

    @Transaction
    @Query("SELECT * FROM projects_table WHERE projectId = :projectId")
    fun getProjectByIdWithMilestones(
        projectId: Int,
    ): Flow<ProjectWithMilestones>

    // Used when with status and timestamp filter(recently added)
    @Query("SELECT * FROM projects_table Where projectStatus = :projectStatus")
    fun getAllProjectsBasedOnStatus(projectStatus: String?): Flow<List<Project>>

    /** Deletion */
    @Delete
    suspend fun deleteProject(project: Project)

    @Query("DELETE FROM projects_table")
    suspend fun deleteAllProjects()
}
