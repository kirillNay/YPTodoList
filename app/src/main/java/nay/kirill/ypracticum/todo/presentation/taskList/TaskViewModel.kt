package nay.kirill.ypracticum.todo.presentation.taskList

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import nay.kirill.ypracticum.todo.domain.Task
import nay.kirill.ypracticum.todo.domain.TaskRepository

sealed interface TasksState {

    data class Data(
        val tasks: List<Task>
    ) : TasksState

    data object Loading : TasksState

    data object Empty : TasksState

}

sealed interface Event {

    data class MarkTaskAsCompleted(val task: Task) : Event

}

class TaskViewModel(
    private val repository: TaskRepository
) : ViewModel() {

    val state: StateFlow<TasksState> = repository.tasks
        .map { tasks ->
            if (tasks.isEmpty()) {
                TasksState.Empty
            } else {
                TasksState.Data(tasks)
            }
        }
        .stateIn(viewModelScope, SharingStarted.Eagerly, TasksState.Loading)

    fun accept(event: Event) {
        when (event) {
            is Event.MarkTaskAsCompleted -> markTaskAsCompleted(task = event.task)
        }
    }

    private fun markTaskAsCompleted(task: Task) {
        viewModelScope.launch {
            repository.updateTask(task.copy(isCompleted = true))
        }
    }
}
