package nay.kirill.ypracticum.todo.app

import androidx.room.Room
import nay.kirill.ypracticum.todo.data.AppDatabase
import nay.kirill.ypracticum.todo.data.TaskRepositoryImpl
import nay.kirill.ypracticum.todo.domain.TaskRepository
import nay.kirill.ypracticum.todo.presentation.addTask.AddTaskViewModel
import nay.kirill.ypracticum.todo.presentation.taskList.TaskViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val databaseModule = module {
    single { Room.databaseBuilder(get(), AppDatabase::class.java, "task_db").build() }
    single { get<AppDatabase>().taskDao() }
}

val repositoryModule = module {
    single<TaskRepository> { TaskRepositoryImpl(get()) }
}

val viewModelModule = module {
    viewModel { TaskViewModel(get()) }
    viewModel { AddTaskViewModel(get()) }
}

val appModule = listOf(databaseModule, repositoryModule, viewModelModule)
