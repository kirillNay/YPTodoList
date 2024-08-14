package nay.kirill.ypracticum.todo.presentation.addTask

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import nay.kirill.ypracticum.todo.domain.Task
import nay.kirill.ypracticum.todo.domain.TaskRepository

data class AddTaskState(
    val title: String,
    val description: String
)

sealed interface AddTaskEvent {

    data class ChangeTitle(val value: String) : AddTaskEvent

    data class ChangeDescription(val value: String) : AddTaskEvent

    data object Complete : AddTaskEvent

}

class AddTaskViewModel(
    private val repository: TaskRepository
): ViewModel() {

    private val _state = MutableStateFlow(AddTaskState("", ""))
    val state: StateFlow<AddTaskState> = _state.asStateFlow()

    fun accept(event: AddTaskEvent) {
        when (event) {
            is AddTaskEvent.ChangeTitle -> {
                _state.value = _state.value.copy(title = event.value)
            }
            is AddTaskEvent.ChangeDescription -> {
                _state.value = _state.value.copy(description = event.value)
            }
            is AddTaskEvent.Complete -> {
                addTask(
                    Task(
                        title = _state.value.title,
                        description = _state.value.description
                    )
                )
            }
        }
    }

    private fun addTask(task: Task) {
        viewModelScope.launch {
            repository.insertTask(task)
        }
    }

}