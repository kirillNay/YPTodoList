package nay.kirill.ypracticum.todo.presentation.addTask

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import nay.kirill.ypracticum.todo.domain.Task
import nay.kirill.ypracticum.todo.presentation.theme.YPTodoListTheme
import org.koin.androidx.compose.getViewModel

@Composable
fun AddTaskScreen(
    viewModel: AddTaskViewModel = getViewModel(),
    navController: NavController
) {
    val state by viewModel.state.collectAsState()

    Content(
        state = state,
        accept = viewModel::accept,
        navController = navController
    )
}

@Composable
private fun Content(
    state: AddTaskState,
    accept: (AddTaskEvent) -> Unit,
    navController: NavController
) {
    Scaffold { paddingValue ->
        Column(
            modifier = Modifier
                .padding(
                    start = 16.dp,
                    end = 16.dp,
                    top = paddingValue.calculateTopPadding() + 16.dp,
                    bottom = paddingValue.calculateBottomPadding() + 16.dp
                )
                .fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            TextField(
                value = state.title,
                onValueChange = { accept(AddTaskEvent.ChangeTitle(it)) },
                label = { Text("Task Title") },
                modifier = Modifier.fillMaxWidth(),
                keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
            )
            Spacer(modifier = Modifier.height(16.dp))
            TextField(
                value = state.description,
                onValueChange = { accept(AddTaskEvent.ChangeDescription(it)) },
                label = { Text("Description") },
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(32.dp))
            Button(
                onClick = {
                    accept(AddTaskEvent.Complete)
                    navController.popBackStack()
                },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp)
            ) {
                Text("Save Task")
            }
        }
    }
}

@Preview
@Composable
private fun ContentPreview() {
    YPTodoListTheme {
        Content(state = AddTaskState(
            title = "",
            description = ""
        ),
            accept = {},
            navController = NavController(LocalContext.current)
        )
    }
}