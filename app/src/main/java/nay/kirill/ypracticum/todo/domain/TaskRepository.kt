package nay.kirill.ypracticum.todo.domain

import kotlinx.coroutines.flow.Flow

interface TaskRepository {

    val tasks: Flow<List<Task>>

    suspend fun insertTask(task: Task)

    suspend fun updateTask(task: Task)

}