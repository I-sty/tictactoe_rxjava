package com.project.tictactoe.ui.screen

import androidx.appcompat.content.res.AppCompatResources
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.google.accompanist.drawablepainter.rememberDrawablePainter
import com.project.tictactoe.R

@Composable
fun AboutScreen(modifier: Modifier) {
    val context = LocalContext.current
    val packageInfo = context.packageManager.getPackageInfo(context.packageName, 0)
    val drawable = AppCompatResources.getDrawable(LocalContext.current, R.mipmap.ic_launcher)

    val painter: Painter = rememberDrawablePainter(drawable)

    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        AppIcon(painter, modifier = Modifier.size(128.dp))
        Spacer(modifier = Modifier.height(16.dp))
        Text("Tic Tac Toe", style = MaterialTheme.typography.titleLarge)
        Spacer(modifier = Modifier.height(8.dp))
        Text("Version ${packageInfo.versionName}")
        Spacer(modifier = Modifier.height(16.dp))
        Text(
            "A classic game of Tic Tac Toe where you can challenge your friends.",
            textAlign = TextAlign.Center
        )
    }
}

@Composable
private fun AppIcon(painter: Painter, modifier: Modifier = Modifier) {
    Image(
        painter = painter,
        contentDescription = "App Icon",
        modifier = modifier
    )
}

@Preview(showBackground = true)
@Composable
private fun AboutScreenPreview() {
    AboutScreen(modifier = Modifier)
}

