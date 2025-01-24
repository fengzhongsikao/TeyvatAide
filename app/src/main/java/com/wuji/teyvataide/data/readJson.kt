package com.wuji.teyvataide.data
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import java.io.BufferedReader

import java.io.InputStreamReader

@Composable
fun readJsonFromAssets(filePath: String): String {
    val context= LocalContext.current
    val inputStream = context.assets.open(filePath)  // 打开文件输入流
    return BufferedReader(InputStreamReader(inputStream)).use { it.readText() }
}
