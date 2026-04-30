package com.fatec.todolist.ui.screens

import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.fatec.todolist.ui.components.TaskItem
import com.fatec.todolist.ui.components.TaskStatsRow
import com.fatec.todolist.viewmodel.TaskViewModel

enum class TaskFilter(val label: String) {
    ALL("Todas"), PENDING("Pendentes"), DONE("Feitas")
}

@Composable
fun TaskScreen(viewModel: TaskViewModel = viewModel()) {
    val state by viewModel.uiState.collectAsState()

    MaterialTheme {
        Surface(modifier = Modifier.fillMaxSize()) {
            Column(
                modifier = Modifier.fillMaxSize().statusBarsPadding().padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                // Header
                Text("Minhas Tarefas", fontSize = 24.sp, fontWeight = FontWeight.Bold)
                HorizontalDivider()

                // Estatísticas
                TaskStatsRow(total = state.totalTasks, done = state.doneTasks, pending = state.pendingTasks)

                // Filtros
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    TaskFilter.entries.forEach { filter ->
                        FilterChip(
                            selected = state.currentFilter == filter,
                            onClick = { viewModel.onFilterChange(filter) },
                            label = { Text(filter.label) }
                        )
                    }
                }

                // Campo + Botão
                Row(Modifier.fillMaxWidth(), Arrangement.spacedBy(8.dp), Alignment.CenterVertically) {
                    OutlinedTextField(
                        value = state.inputText,
                        onValueChange = { viewModel.onInputChange(it) },
                        modifier = Modifier.weight(1f),
                        placeholder = { Text("Nova tarefa...") },
                        singleLine = true,
                        shape = RoundedCornerShape(8.dp)
                    )
                    Button(onClick = { viewModel.addTask() }) {
                        Text("Adicionar")
                    }
                }

                // Lista
                val filtered = state.filteredTasks
                if (filtered.isEmpty()) {
                    Box(Modifier.fillMaxWidth().weight(1f), contentAlignment = Alignment.Center) {
                        Text("Nenhuma tarefa", color = MaterialTheme.colorScheme.onSurfaceVariant)
                    }
                } else {
                    LazyColumn(verticalArrangement = Arrangement.spacedBy(8.dp), modifier = Modifier.weight(1f)) {
                        items(items = filtered, key = { it.id }) { task ->
                            TaskItem(
                                task = task,
                                onToggle = { viewModel.toggleTask(task.id) },
                                onDelete = { viewModel.removeTask(task.id) },
                                modifier = Modifier.animateItem(fadeInSpec = tween(300), fadeOutSpec = tween(300))
                            )
                        }
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
private fun PreviewTaskScreen() {
    TaskScreen()
}