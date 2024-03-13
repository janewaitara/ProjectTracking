package com.mumbicodes.projectie.data.db

import androidx.room.*
import com.mumbicodes.projectie.domain.model.Project
import com.mumbicodes.projectie.domain.model.ProjectName
import com.mumbicodes.projectie.domain.relations.ProjectWithMilestones
import kotlinx.coroutines.flow.Flow

@Dao
interface ProjectsDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertProject(project: Project)

    @Update
    suspend fun updateProject(project: Project)

    /** Fetch */
    @Query("SELECT * from projects_table WHERE projectId = :projectId")
    fun getProjectById(projectId: Int): Flow<Project>

    @Transaction
    @Query("SELECT * FROM projects_table WHERE projectId = :projectId")
    fun getProjectByIdWithMilestones(
        projectId: Int,
    ): Flow<ProjectWithMilestones?>

    // Used when with status and timestamp filter(recently added)
    @Query("SELECT * FROM projects_table")
    fun getAllProjects(): Flow<List<Project>>

    // Used when fetching project name for milestones
    @Query("SELECT projectId, projectName FROM projects_table")
    fun getProjectNameAndId(): Flow<List<ProjectName>>

    /** Deletion */
    @Delete
    suspend fun deleteProject(project: Project)

    @Query("DELETE FROM projects_table")
    suspend fun deleteAllProjects()
}
