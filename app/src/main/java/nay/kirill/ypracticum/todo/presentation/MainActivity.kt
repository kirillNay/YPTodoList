package nay.kirill.ypracticum.todo.presentation

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import nay.kirill.ypracticum.todo.presentation.addTask.AddTaskScreen
import nay.kirill.ypracticum.todo.presentation.taskList.TaskListScreen
import nay.kirill.ypracticum.todo.presentation.theme.YPTodoListTheme

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            YPTodoListTheme {
                MyApp()
            }
        }
    }
}

sealed class Routes(val route: String) {

    data object TaskList : Routes("task_list")

    data object AddTask : Routes("add_task")

}

@Composable
fun MyApp() {
    val navController = rememberNavController()

    NavHost(navController = navController, startDestination = Routes.TaskList.route) {
        composable(Routes.TaskList.route) { TaskListScreen(navController = navController) }
        composable(Routes.AddTask.route) { AddTaskScreen(navController = navController) }
    }
}
