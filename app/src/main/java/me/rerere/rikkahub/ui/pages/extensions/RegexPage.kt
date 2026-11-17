package me.rerere.rikkahub.ui.pages.extensions

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.Checkbox
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LargeFlexibleTopAppBar
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import me.rerere.hugeicons.HugeIcons
import me.rerere.hugeicons.stroke.Add01
import me.rerere.hugeicons.stroke.Delete01
import me.rerere.rikkahub.R
import me.rerere.rikkahub.data.model.AssistantAffectScope
import me.rerere.rikkahub.data.model.AssistantRegex
import me.rerere.rikkahub.ui.components.nav.BackButton
import me.rerere.rikkahub.ui.theme.CustomColors
import me.rerere.rikkahub.utils.plus
import org.koin.androidx.compose.koinViewModel
import kotlin.uuid.Uuid

@Composable
fun RegexPage(vm: PromptVM = koinViewModel()) {
    val settings by vm.settings.collectAsStateWithLifecycle()
    val scrollBehavior = TopAppBarDefaults.exitUntilCollapsedScrollBehavior()
    val assistants = settings.assistants
    var selectedId by remember { mutableStateOf<String?>(null) }
    val assistant = assistants.firstOrNull { it.id.toString() == selectedId } ?: assistants.firstOrNull()

    fun updateRegexes(list: List<AssistantRegex>) {
        val target = assistant ?: return
        vm.updateSettings { s ->
            s.copy(assistants = s.assistants.map { a ->
                if (a.id == target.id) a.copy(regexes = list) else a
            })
        }
    }

    Scaffold(
        topBar = {
            LargeFlexibleTopAppBar(
                title = { Text(stringResource(R.string.extensions_page_regexes)) },
                navigationIcon = { BackButton() },
                scrollBehavior = scrollBehavior,
                colors = CustomColors.topBarColors
            )
        },
        modifier = Modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
        containerColor = CustomColors.topBarColors.containerColor
    ) { innerPadding ->
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = innerPadding + PaddingValues(horizontal = 16.dp, vertical = 8.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp),
        ) {
            item {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                ) {
                    Text(
                        text = stringResource(R.string.regex_page_applies_to),
                        style = MaterialTheme.typography.labelLarge,
                    )
                    assistants.forEach { a ->
                        TextButton(onClick = { selectedId = a.id.toString() }) {
                            Text(
                                text = a.name.ifBlank { stringResource(R.string.assistant_page_default_assistant) },
                                color = if (a.id == assistant?.id) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurfaceVariant,
                            )
                        }
                    }
                }
            }

            if (assistant == null) {
                item { Text(stringResource(R.string.extensions_page_regexes_desc)) }
            } else {
                items(assistant.regexes) { regex ->
                    RegexCard(
                        regex = regex,
                        onUpdate = { updated ->
                            updateRegexes(assistant.regexes.map { if (it.id == updated.id) updated else it })
                        },
                        onDelete = {
                            updateRegexes(assistant.regexes.filter { it.id != regex.id })
                        },
                    )
                }

                item {
                    TextButton(onClick = {
                        updateRegexes(assistant.regexes + AssistantRegex(id = Uuid.random()))
                    }) {
                        Icon(HugeIcons.Add01, contentDescription = null)
                        Text(stringResource(R.string.assistant_page_regex_title))
                    }
                }
            }
        }
    }
}

@Composable
private fun RegexCard(
    regex: AssistantRegex,
    onUpdate: (AssistantRegex) -> Unit,
    onDelete: () -> Unit,
) {
    var expanded by remember { mutableStateOf(false) }
    Card(modifier = Modifier.fillMaxWidth()) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp),
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = regex.name.ifBlank { stringResource(R.string.assistant_page_regex_name) },
                        style = MaterialTheme.typography.titleSmall,
                    )
                    if (!expanded) {
                        Text(
                            text = regex.findRegex,
                            style = MaterialTheme.typography.bodySmall,
                            maxLines = 1,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                        )
                    }
                }
                Switch(checked = regex.enabled, onCheckedChange = { onUpdate(regex.copy(enabled = it)) })
                IconButton(onClick = onDelete) {
                    Icon(HugeIcons.Delete01, contentDescription = null)
                }
            }

            TextButton(onClick = { expanded = !expanded }) {
                Text(if (expanded) stringResource(R.string.assistant_page_regex_desc) else stringResource(R.string.assistant_page_regex_title))
            }

            if (expanded) {
                OutlinedTextField(
                    value = regex.name,
                    onValueChange = { onUpdate(regex.copy(name = it)) },
                    label = { Text(stringResource(R.string.assistant_page_regex_name)) },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true,
                )
                OutlinedTextField(
                    value = regex.findRegex,
                    onValueChange = { onUpdate(regex.copy(findRegex = it)) },
                    label = { Text(stringResource(R.string.assistant_page_regex_find_regex)) },
                    modifier = Modifier.fillMaxWidth(),
                )
                OutlinedTextField(
                    value = regex.replaceString,
                    onValueChange = { onUpdate(regex.copy(replaceString = it)) },
                    label = { Text(stringResource(R.string.assistant_page_regex_replace_string)) },
                    modifier = Modifier.fillMaxWidth(),
                )
                Text(
                    text = stringResource(R.string.assistant_page_regex_affecting_scopes),
                    style = MaterialTheme.typography.labelLarge,
                )
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Checkbox(
                        checked = regex.affectingScope.contains(AssistantAffectScope.USER),
                        onCheckedChange = { checked ->
                            val next = if (checked) regex.affectingScope + AssistantAffectScope.USER
                            else regex.affectingScope - AssistantAffectScope.USER
                            onUpdate(regex.copy(affectingScope = next))
                        },
                    )
                    Text(stringResource(R.string.regex_page_scope_user))
                }
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Checkbox(
                        checked = regex.affectingScope.contains(AssistantAffectScope.ASSISTANT),
                        onCheckedChange = { checked ->
                            val next = if (checked) regex.affectingScope + AssistantAffectScope.ASSISTANT
                            else regex.affectingScope - AssistantAffectScope.ASSISTANT
                            onUpdate(regex.copy(affectingScope = next))
                        },
                    )
                    Text(stringResource(R.string.regex_page_scope_assistant))
                }
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Switch(checked = regex.visualOnly, onCheckedChange = { onUpdate(regex.copy(visualOnly = it)) })
                    Text(stringResource(R.string.assistant_page_regex_visual_only))
                }
            }
        }
    }
}
