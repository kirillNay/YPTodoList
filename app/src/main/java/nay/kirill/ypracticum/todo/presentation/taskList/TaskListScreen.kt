package nay.kirill.ypracticum.todo.presentation.taskList

import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FabPosition
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import nay.kirill.ypracticum.todo.domain.ID
import nay.kirill.ypracticum.todo.domain.Task
import nay.kirill.ypracticum.todo.presentation.theme.YPTodoListTheme
import org.koin.androidx.compose.koinViewModel

@Composable
fun TaskListScreen(
    navController: NavController,
    viewModel: TaskViewModel = koinViewModel()
) {
    val state by viewModel.state.collectAsState()

    Content(
        state = state,
        navController = navController,
        accept = viewModel::accept
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun Content(
    state: TasksState,
    navController: NavController,
    accept: (Event) -> Unit
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Practicum Todo List") },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    titleContentColor = Color.White
                )
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = { navController.navigate("add_task") },
                containerColor = MaterialTheme.colorScheme.primary
            ) {
                Icon(Icons.Default.Add, contentDescription = "Add Task", tint = Color.White)
            }
        },
        floatingActionButtonPosition = FabPosition.Center
    ) { paddingValues ->
        when (state) {
            is TasksState.Loading -> Loading()
            is TasksState.Empty -> Empty()
            is TasksState.Data -> Tasks(state, paddingValues, accept)
        }
    }
}

@Composable
private fun Loading() {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        CircularProgressIndicator(
            modifier = Modifier.size(48.dp)
        )
    }
}

@Composable
private fun Empty(
) {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = "Задачи не добавлены",
            style = MaterialTheme.typography.bodyLarge,
            textAlign = TextAlign.Center,
            color = MaterialTheme.colorScheme.onPrimaryContainer
        )
    }
}

@Composable
private fun Tasks(
    state: TasksState.Data,
    paddingValues: PaddingValues,
    accept: (Event) -> Unit
) {
    LazyColumn(contentPadding = paddingValues) {
        items(state.tasks) { task ->
            TaskItem(task, onTaskComplete = {
                accept(Event.MarkTaskAsCompleted(task))
            })
        }
    }
}

@Composable
fun TaskItem(task: Task, onTaskComplete: (Task) -> Unit) {
    val elevation by animateDpAsState(
        label = "Elevation",
        targetValue = if (task.isCompleted) 0.dp else 8.dp
    )

    val alpha by animateFloatAsState(
        label = "Alpha",
        targetValue = if (task.isCompleted) 0.5f else 1f
    )

    Card(
        elevation = CardDefaults.cardElevation(defaultElevation = elevation),
        modifier = Modifier
            .fillMaxWidth()
            .alpha(alpha)
            .padding(vertical = 4.dp, horizontal = 8.dp)
            .clickable { onTaskComplete(task) },
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primaryContainer),
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = Icons.Default.CheckCircle,
                contentDescription = null,
                tint = if (task.isCompleted) Color.Gray else Color.Green,
                modifier = Modifier.size(24.dp)
            )
            Spacer(modifier = Modifier.width(16.dp))
            Column {
                Text(
                    text = task.title,
                    style = MaterialTheme.typography.bodyLarge.copy(
                        textDecoration = if (task.isCompleted) TextDecoration.LineThrough else TextDecoration.None
                    ),
                    color = MaterialTheme.colorScheme.primary
                )
                Text(
                    text = task.description,
                    style = MaterialTheme.typography.bodyMedium.copy(
                        textDecoration = if (task.isCompleted) TextDecoration.LineThrough else TextDecoration.None
                    ),
                    color = MaterialTheme.colorScheme.onPrimaryContainer
                )
            }
        }
    }
}

@Preview
@Composable
private fun ContentPreview(
    @PreviewParameter(TasksStateProvider::class) state: TasksState
) {
    YPTodoListTheme {
        Content(state = state, navController = NavController(LocalContext.current)) {}
    }
}

private class TasksStateProvider : PreviewParameterProvider<TasksState> {

    override val values: Sequence<TasksState>
        get() = sequenceOf(
            TasksState.Empty,
            TasksState.Loading,
            TasksState.Data(
                tasks = listOf(
                    Task(
                        id = ID.Undefined,
                        title = "Задача 1",
                        description = "Описание 1"
                    ),
                    Task(
                        id = ID.Undefined,
                        title = "Задача 2",
                        description = "Описание 2"
                    ),
                    Task(
                        id = ID.Undefined,
                        title = "Задача 3",
                        description = "Описание 3"
                    ),
                )
            )
        )
}
