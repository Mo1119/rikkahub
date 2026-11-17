package me.rerere.rikkahub.ui.pages.extensions

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.LargeFlexibleTopAppBar
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import me.rerere.hugeicons.HugeIcons
import me.rerere.rikkahub.R
import me.rerere.hugeicons.stroke.Book03
import me.rerere.hugeicons.stroke.File02
import me.rerere.hugeicons.stroke.MagicWand01
import me.rerere.hugeicons.stroke.LeftToRightListBullet
import me.rerere.hugeicons.stroke.Zap
import me.rerere.rikkahub.Screen
import me.rerere.rikkahub.ui.components.nav.BackButton
import me.rerere.rikkahub.ui.components.ui.CardGroup
import me.rerere.rikkahub.ui.components.ui.CardGroupIcon
import me.rerere.rikkahub.ui.context.LocalNavController
import me.rerere.rikkahub.ui.theme.CustomColors
import me.rerere.rikkahub.utils.plus

@Composable
fun ExtensionsPage() {
    val scrollBehavior = TopAppBarDefaults.exitUntilCollapsedScrollBehavior()
    val navController = LocalNavController.current

    Scaffold(
        topBar = {
            LargeFlexibleTopAppBar(
                title = { Text(stringResource(R.string.extensions_page_title)) },
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
            verticalArrangement = Arrangement.spacedBy(16.dp),
        ) {
            item {
                val containerColor = MaterialTheme.colorScheme.primaryContainer
                val contentColor = MaterialTheme.colorScheme.onPrimaryContainer
                CardGroup(
                    title = { Text(stringResource(R.string.extensions_page_section_extensions)) },
                ) {
                    item(
                        onClick = { navController.navigate(Screen.QuickMessages) },
                        leadingContent = { CardGroupIcon(HugeIcons.Zap, containerColor = containerColor, contentColor = contentColor) },
                        headlineContent = { Text(stringResource(R.string.assistant_page_quick_messages)) },
                        supportingContent = { Text(stringResource(R.string.extensions_page_quick_messages_desc)) },
                    )
                    item(
                        onClick = { navController.navigate(Screen.PromptInjection) },
                        leadingContent = { CardGroupIcon(HugeIcons.MagicWand01, containerColor = containerColor, contentColor = contentColor) },
                        headlineContent = { Text(stringResource(R.string.extensions_page_prompt_injection)) },
                        supportingContent = { Text(stringResource(R.string.extensions_page_prompt_injection_desc)) },
                    )
                    item(
                        onClick = { navController.navigate(Screen.Regexes) },
                        leadingContent = { CardGroupIcon(HugeIcons.LeftToRightListBullet, containerColor = containerColor, contentColor = contentColor) },
                        headlineContent = { Text(stringResource(R.string.extensions_page_regexes)) },
                        supportingContent = { Text(stringResource(R.string.extensions_page_regexes_desc)) },
                    )
                    item(
                        onClick = { navController.navigate(Screen.Lorebooks) },
                        leadingContent = { CardGroupIcon(HugeIcons.Book03, containerColor = containerColor, contentColor = contentColor) },
                        headlineContent = { Text(stringResource(R.string.extensions_page_lorebooks)) },
                        supportingContent = { Text(stringResource(R.string.extensions_page_lorebooks_desc)) },
                    )
                }
            }
        }
    }
}
