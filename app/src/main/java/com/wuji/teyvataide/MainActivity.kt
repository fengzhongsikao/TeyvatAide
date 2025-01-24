package com.wuji.teyvataide
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.annotation.RequiresApi
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.PagerState
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.outlined.DateRange
import androidx.compose.material.icons.outlined.Favorite
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material.icons.outlined.Star
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.viewmodel.compose.viewModel
import com.wuji.teyvataide.page.Home
import com.wuji.teyvataide.ui.theme.TeyvatAideTheme
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

data class NavigationItem(val label: String, val selectIcon: ImageVector,val unSelectIcon:ImageVector)

class MainActivity : ComponentActivity() {

    @RequiresApi(Build.VERSION_CODES.O)
    @OptIn(ExperimentalFoundationApi::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContent {
            val navigationItems = listOf(
                NavigationItem("首页", Icons.Filled.Home,Icons.Outlined.Home),
                NavigationItem("未知1", Icons.Filled.DateRange,Icons.Outlined.DateRange),
                NavigationItem("未知2", Icons.Filled.DateRange,Icons.Outlined.DateRange),
                NavigationItem("我的", Icons.Filled.Person,Icons.Outlined.Person)
            )
            val viewModel: WeViewModel = viewModel()
            TeyvatAideTheme {
                @OptIn(ExperimentalFoundationApi::class)
                val pagerState = rememberPagerState(pageCount = {
                    4
                });
                Scaffold(
                    modifier = Modifier.fillMaxSize(),
                    bottomBar = {
                        val coroutineScope = rememberCoroutineScope()
                        MyBottomBar(navigationItems, viewModel,coroutineScope,pagerState)
                    }
                ) {
                        innerPadding -> MyHorizontalPager(innerPadding,pagerState)
                }
            }


        }
    }

    @OptIn(ExperimentalFoundationApi::class)
    @Composable
    private fun MyBottomBar(
        navigationItems: List<NavigationItem>,
        viewModel: WeViewModel,
        coroutineScope: CoroutineScope,
        pagerState: PagerState
    ) {
        NavigationBar(
            containerColor = Color.White
        ) {
            navigationItems.forEachIndexed { index, navigationItem ->
                NavigationBarItem(
                    selected = viewModel.selectTab == index,
                    onClick = {
                        viewModel.selectTab = index
                        coroutineScope.launch {
                            pagerState.scrollToPage(viewModel.selectTab)
                        }
                    },
                    icon = { // 图标
                        Icon(
                            imageVector = if (index == viewModel.selectTab) navigationItem.selectIcon else navigationItem.unSelectIcon,
                            tint = if (index == viewModel.selectTab) Color.Green else Color.Black,
                            contentDescription = null
                        )
                    },
                    label = { // 文字
                        Text(
                            text = navigationItem.label,
                            color = if (index == viewModel.selectTab) Color.Green else Color.Black
                        )
                    },
                    alwaysShowLabel = false,
                    colors = NavigationBarItemDefaults.colors( // 颜色配置
                        selectedIconColor = Color(0xff149ee7),
                        selectedTextColor = Color(0xff149ee7),
                        unselectedIconColor = Color(0xff999999),
                        unselectedTextColor = Color(0xff999999)
                    )
                )
            }
        }
    }


    @RequiresApi(Build.VERSION_CODES.O)
    @Composable
    @OptIn(ExperimentalFoundationApi::class)
    private fun MyHorizontalPager(innerPadding: PaddingValues,pagerState: PagerState) {
        return HorizontalPager(state = pagerState, modifier = Modifier.padding(innerPadding)) { page ->
            when (page) {
                0 -> Home()
                1 -> Box(modifier = Modifier
                    .fillMaxSize()
                    .background(color = Color.Green))
                2 -> Box(modifier = Modifier
                    .fillMaxSize()
                    .background(color = Color.Blue))
                3 -> Box(modifier = Modifier
                    .fillMaxSize()
                    .background(color = Color.Yellow))
            }
        }
    }



}

@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    Text(
        text = "Hello $name!",
        modifier = modifier
    )
}


@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    TeyvatAideTheme {
        Greeting("Android")
    }
}