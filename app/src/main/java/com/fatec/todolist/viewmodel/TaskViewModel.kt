package com.fatec.todolist.viewmodel

import androidx.lifecycle.ViewModel
import com.fatec.todolist.model.Task
import com.fatec.todolist.ui.screens.TaskFilter
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import java.net.IDN

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

class TaskViewModel: ViewModel() {
    private val _uiState = MutableStateFlow(TaskUiState())
    val uiState: StateFlow<TaskUiState> = _uiState.asStateFlow()

    fun onInputChange(newText: String) {
        _uiState.value = _uiState.value.copy(inputText = newText)
    }

    fun addTask() {
        val current = _uiState.value
        if (current.inputText.isBlank()) return

        _uiState.value = current.copy(
            tasks = current.tasks + Task(title = current.inputText.trim()),
            inputText = ""
        )
    }

    fun toggleTask(taskId: Long) {
        _uiState.value = _uiState.value.copy(
            tasks = _uiState.value.tasks.map { task ->
                if(task.id == taskId) task.copy(isDone = !task.isDone) else task
            }
        )
    }

    fun removeTask(taskId: Long) {
        _uiState.value = _uiState.value.copy(
            tasks = _uiState.value.tasks.filter { it.id != taskId }
        )
    }

    fun onFilterChange(filter: TaskFilter){
        _uiState.value = _uiState.value.copy(currentFilter = filter)
    }

}