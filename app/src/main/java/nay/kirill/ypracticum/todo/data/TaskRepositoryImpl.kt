package nay.kirill.ypracticum.todo.data

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import nay.kirill.ypracticum.todo.domain.ID
import nay.kirill.ypracticum.todo.domain.Task
import nay.kirill.ypracticum.todo.domain.TaskRepository

class TaskRepositoryImpl(
    private val taskDao: TaskDao
) : TaskRepository {

    override val tasks: Flow<List<Task>> = taskDao.getTasks()
        .map { it.map { entity -> entity.toDomain() } }

    override suspend fun insertTask(task: Task) {
        taskDao.insertTask(task.toEntity())
    }

    override suspend fun updateTask(task: Task) {
        taskDao.updateTask(task.toEntity())
    }

    private fun Task.toEntity() = TaskEntity(
        id = id.takeIf { it != ID.Undefined }?.value ?: 0,
        title = title,
        description = description,
        isCompleted = isCompleted
    )

    private fun TaskEntity.toDomain() = Task(
        id = ID(id),
        title = title,
        description = description,
        isCompleted = isCompleted
    )
}
