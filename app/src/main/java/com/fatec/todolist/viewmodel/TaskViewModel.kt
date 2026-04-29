package com.fatec.todolist.viewmodel

import com.fatec.todolist.model.Task
import com.fatec.todolist.ui.screens.TaskFilter

data class TaskUiState(
    val tasks: List<Task> = emptyList(),
    val inputText: String = "",
    val currentFilter: TaskFilter = TaskFilter.ALL
) {
    val totalTasks: Int get() = tasks.size
    val doneTasks: Int get() = tasks.count { it.isDone }
    val pendingTasks: Int get() = totalTasks - doneTasks

    val filteredTasks: List<Task> get() = when (currentFilter) {
        TaskFilter.ALL -> tasks
        TaskFilter.PENDING -> tasks.filter { !it.isDone }
        TaskFilter.DONE -> tasks.filter { it.isDone }
    }
}

class TaskViewModel {
}