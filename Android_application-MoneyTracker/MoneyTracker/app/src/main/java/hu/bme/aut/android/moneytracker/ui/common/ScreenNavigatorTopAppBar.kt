package hu.bme.aut.android.moneytracker.ui.common

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import hu.bme.aut.android.moneytracker.R

@Composable
@OptIn(ExperimentalMaterial3Api::class)
fun ScreenNavigatorTopAppBar(
    title: String,
    onSettingsClicked: () -> Unit,
    onNavigateToOtherScreen: () -> Unit,
    otherScreenIcon: ImageVector,
    otherScreenDescription: String? = null,
    optionalIconButton: @Composable (() -> Unit) = {}
) {
    TopAppBar(
        title = {
            Text(
                text = title,
                maxLines = 1,
                color = MaterialTheme.colorScheme.primary,
                fontStyle = MaterialTheme.typography.titleLarge.fontStyle,
                fontSize = MaterialTheme.typography.titleLarge.fontSize,
                fontWeight = MaterialTheme.typography.titleLarge.fontWeight,
            )
        },
        actions = {
            optionalIconButton()
            IconButton(onClick = onNavigateToOtherScreen) {
                Icon(
                    imageVector = otherScreenIcon,
                    contentDescription = otherScreenDescription
                )
            }
            IconButton(onClick = onSettingsClicked) {
                Icon(
                    imageVector = Icons.Outlined.Settings,
                    contentDescription = stringResource(R.string.transaction_settings_title)
                )
            }
        }
    )
}