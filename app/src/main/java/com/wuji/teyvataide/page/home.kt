package com.wuji.teyvataide.page

import android.annotation.SuppressLint
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.staggeredgrid.LazyVerticalStaggeredGrid
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridCells
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import coil.compose.rememberAsyncImagePainter
import coil.request.ImageRequest
import com.wuji.teyvataide.R
import com.wuji.teyvataide.data.Calendar
import com.wuji.teyvataide.model.CharacterData
import com.wuji.teyvataide.model.Material
import com.wuji.teyvataide.model.Source
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.time.format.TextStyle
import java.util.Locale


var showDialog by mutableStateOf(false)

var characterItem = CharacterData(
    id = 0,
    contentId = 0,
    dropDays = emptyList(),
    name = "",
    itemType = "",
    star = 0,
    weapon = "",
    element = "",
    materials = listOf(
        Material(
            id = 0,
            name = "",
            star = 0
        )
    ),
    source = Source(
        index = 0,
        area = "",
        name = ""
    )
)

val selectedType = mutableStateOf("character")

@SuppressLint("RememberReturnType")
@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun Home() {
    val calendar = Calendar()

    val calendarList = calendar.getCalendar()

    val btnNow = remember { mutableStateOf(LocalDate.now().dayOfWeek.value); }

    var checked by remember { mutableStateOf(true) }

    val date = LocalDate.now()
    val formattedDate = date.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"))
    val dayOfWeek = date.dayOfWeek.getDisplayName(TextStyle.FULL, Locale.CHINESE)

    val currentDate = remember { mutableStateOf("") }



    currentDate.value = "$formattedDate $dayOfWeek"


    Box(
        Modifier
            .fillMaxSize()
            .background(Color(249, 249, 249, 0xFF))
            .padding(10.dp)
    ) {

        Box(
            Modifier
                .clip(RoundedCornerShape(6.dp))
                .background(Color.White)
                .fillMaxWidth()
                .padding(10.dp)
        ) {
            Column {

                Row(verticalAlignment = Alignment.CenterVertically) {
                    Switch(
                        checked = checked,
                        onCheckedChange = {
                            checked = it
                            if (checked) {
                                selectedType.value = "character"
                            } else {
                                selectedType.value = "weapon"
                            }
                        }
                    )
                    Spacer(modifier = Modifier.size(10.dp))
                    Text(text = if (checked) "角色" else "武器")
                }

                Text(text = "今日素材 ${currentDate.value}")
                ButtonList(btnNow.value) {
                    btnNow.value = it
                }

                val calendarTotal = calendarList.filter {
                    it.dropDays.contains(btnNow.value) && it.itemType == selectedType.value
                }
                val imageBgcId by remember { mutableIntStateOf(R.drawable.star0) }
                LazyVerticalStaggeredGrid(
                    columns = StaggeredGridCells.Adaptive(100.dp),
                    verticalItemSpacing = 10.dp,
                ) {
                    items(calendarTotal.size) { calIndex ->
                        if (checked) CharacterItem(
                            calendarTotal,
                            calIndex,
                            imageBgcId,
                            selectedType
                        ) else WeaponItem(calendarTotal, calIndex, imageBgcId, selectedType)
                    }
                }
            }
        }

        // 如果 showDialog 为 true，则显示对话框
        if (showDialog) {
            ToCalendarDialog(onDismissRequest = { showDialog = false })
        }
    }
}

@Composable
private fun CharacterItem(
    calendarTotal: List<CharacterData>,
    calIndex: Int,
    imageBgcId: Int,
    selectedType: MutableState<String>
) {
    var imageBgcId1 = imageBgcId
    Box {
        when (calendarTotal[calIndex].star) {
            0 -> imageBgcId1 = R.drawable.star0
            1 -> imageBgcId1 = R.drawable.star1
            2 -> imageBgcId1 = R.drawable.star2
            3 -> imageBgcId1 = R.drawable.star3
            4 -> imageBgcId1 = R.drawable.star4
            5 -> imageBgcId1 = R.drawable.star5
        }

        val imagePainter = rememberAsyncImagePainter(
            model = ImageRequest.Builder(LocalContext.current)
                .data("file:///android_asset/image/WIKI/${selectedType.value}/${calendarTotal[calIndex].id}.webp")
                .build()
        )

        val elementPainter = rememberAsyncImagePainter(
            model = ImageRequest.Builder(LocalContext.current)
                .data("file:///android_asset/image/icon/element/${calendarTotal[calIndex].element}元素.webp")
                .build()
        )
        val weaponTypePainter = rememberAsyncImagePainter(
            model = ImageRequest.Builder(LocalContext.current)
                .data("file:///android_asset/image/icon/weapon/${calendarTotal[calIndex].weapon}.webp")
                .build()
        )

        Image(
            painter = painterResource(id = imageBgcId1),
            contentDescription = "图片",
            modifier = Modifier.size(100.dp)
        )
        Image(
            painter = imagePainter,
            contentDescription = "Local WebP Image",
            modifier = Modifier
                .clickable {
                    showDialog = true;
                    characterItem = calendarTotal[calIndex];
                }
                .size(100.dp),
        )

        Image(
            painter = elementPainter,
            contentDescription = "Local WebP Image",
            modifier = Modifier
                .size(30.dp)
                .padding(start = 5.dp, top = 5.dp)
        )
        Box(
            modifier = Modifier
                .size(width = 100.dp, height = 35.dp)
                .align(Alignment.BottomStart)
                .background(Color(20, 20, 20, 80)),
            contentAlignment = Alignment.Center
        ) {
            Row {
                Image(
                    painter = weaponTypePainter,
                    contentDescription = "Local WebP Image",
                    modifier = Modifier.size(30.dp)
                )
                Text(
                    text = calendarTotal[calIndex].name,
                    color = Color.White,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}


@Composable
private fun WeaponItem(
    calendarTotal: List<CharacterData>,
    calIndex: Int,
    imageBgcId: Int,
    selectedType: MutableState<String>
) {
    var imageBgcId1 = imageBgcId
    Box {
        when (calendarTotal[calIndex].star) {
            0 -> imageBgcId1 = R.drawable.star0
            1 -> imageBgcId1 = R.drawable.star1
            2 -> imageBgcId1 = R.drawable.star2
            3 -> imageBgcId1 = R.drawable.star3
            4 -> imageBgcId1 = R.drawable.star4
            5 -> imageBgcId1 = R.drawable.star5
        }

        val imagePainter = rememberAsyncImagePainter(
            model = ImageRequest.Builder(LocalContext.current)
                .data("file:///android_asset/image/WIKI/${selectedType.value}/${calendarTotal[calIndex].id}.webp")
                .build()
        )

        val weaponTypePainter = rememberAsyncImagePainter(
            model = ImageRequest.Builder(LocalContext.current)
                .data("file:///android_asset/image/icon/weapon/${calendarTotal[calIndex].weapon}.webp")
                .build()
        )


        Image(
            painter = painterResource(id = imageBgcId1),
            contentDescription = "图片",
            modifier = Modifier.size(100.dp)
        )
        Image(
            painter = imagePainter,
            contentDescription = "Local WebP Image",
            modifier = Modifier
                .clickable {
                    showDialog = true;
                    characterItem = calendarTotal[calIndex];
                }
                .size(100.dp)
        )

        Image(
            painter = weaponTypePainter,
            contentDescription = "Local WebP Image",
            modifier = Modifier
                .size(30.dp)
        )
        Box(
            modifier = Modifier
                .size(width = 100.dp, height = 35.dp)
                .background(Color(20, 20, 20, 80))
                .align(Alignment.BottomStart),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = calendarTotal[calIndex].name,
                color = Color.White,
                fontSize = 12.sp,
                fontWeight = FontWeight.Bold
            )
        }
    }
}


@Composable
@OptIn(ExperimentalLayoutApi::class)
private fun ButtonList(selectId: Int, onClick: (Int) -> Unit) {
    FlowRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
        Button(
            onClick = { onClick(1) }, colors = ButtonDefaults.buttonColors(
                containerColor = if (selectId == 1) Color(0xFFFFcd0c) else Color(0XFF393b40),
            )
        ) {
            Text("周一", color = if (selectId == 1) Color(0XFF393b40) else Color(0xFFf4d8a8))
        }
        Button(
            onClick = { onClick(2) }, colors = ButtonDefaults.buttonColors(
                containerColor = if (selectId == 2) Color(0xFFFFcd0c) else Color(0XFF393b40),
            )
        ) {
            Text("周二", color = if (selectId == 2) Color(0XFF393b40) else Color(0xFFf4d8a8))
        }
        Button(
            onClick = { onClick(3) }, colors = ButtonDefaults.buttonColors(
                containerColor = if (selectId == 3) Color(0xFFFFcd0c) else Color(0XFF393b40),
            )
        ) {
            Text("周三", color = if (selectId == 3) Color(0XFF393b40) else Color(0xFFf4d8a8))
        }
        Button(
            onClick = { onClick(4) }, colors = ButtonDefaults.buttonColors(
                containerColor = if (selectId == 4) Color(0xFFFFcd0c) else Color(0XFF393b40),
            )
        ) {
            Text("周四", color = if (selectId == 4) Color(0XFF393b40) else Color(0xFFf4d8a8))
        }
        Button(
            onClick = { onClick(5) }, colors = ButtonDefaults.buttonColors(
                containerColor = if (selectId == 5) Color(0xFFFFcd0c) else Color(0XFF393b40),
            )
        ) {
            Text("周五", color = if (selectId == 5) Color(0XFF393b40) else Color(0xFFf4d8a8))
        }
        Button(
            onClick = { onClick(6) }, colors = ButtonDefaults.buttonColors(
                containerColor = if (selectId == 6) Color(0xFFFFcd0c) else Color(0XFF393b40),
            )
        ) {
            Text("周六", color = if (selectId == 6) Color(0XFF393b40) else Color(0xFFf4d8a8))
        }
        Button(
            onClick = { onClick(7) }, colors = ButtonDefaults.buttonColors(
                containerColor = if (selectId == 7) Color(0xFFFFcd0c) else Color(0XFF393b40),
            )
        ) {
            Text("周日", color = if (selectId == 7) Color(0XFF393b40) else Color(0xFFf4d8a8))
        }
    }
}


@OptIn(ExperimentalLayoutApi::class)
@Composable
fun ToCalendarDialog(onDismissRequest: () -> Unit) {
    Dialog(onDismissRequest = { onDismissRequest() }) {
        Box(
            modifier = Modifier
                .clip(RoundedCornerShape(6.dp))
                .fillMaxWidth()
                .background(Color.White)
                .height(300.dp)
                .padding(10.dp),
        ) {

           Column {
               Row {
                   if(selectedType.value=="character")PersonImageItem() else WeaponImageItem()
                   Spacer(modifier = Modifier.width(10.dp))
                   FlowRow(horizontalArrangement = Arrangement.spacedBy(8.dp), verticalArrangement = Arrangement.spacedBy(8.dp)){
                       for (item in characterItem.materials){
                           Row {
                               var imageBgcId by remember { mutableIntStateOf(R.drawable.star0) }
                               when (characterItem.star) {
                                   0 -> imageBgcId = R.drawable.star0
                                   1 -> imageBgcId = R.drawable.star1
                                   2 -> imageBgcId = R.drawable.star2
                                   3 -> imageBgcId = R.drawable.star3
                                   4 -> imageBgcId = R.drawable.star4
                                   5 -> imageBgcId = R.drawable.star5
                               }

                               val tempMaterialPainter= rememberAsyncImagePainter(
                                   model = ImageRequest.Builder(LocalContext.current)
                                       .data("file:///android_asset/image/icon/material/${item.id}.webp")
                                       .build()
                               )
                               val starPainter=rememberAsyncImagePainter(
                                   model = ImageRequest.Builder(LocalContext.current)
                                       .data("file:///android_asset/image/icon/star/${item.star}.webp")
                                       .build()
                               )

                               Box{
                                   Image(
                                       painter = painterResource(id = imageBgcId),
                                       contentDescription = "图片",
                                       modifier = Modifier.size(45.dp)
                                   )
                                   Image(
                                       painter = tempMaterialPainter,
                                       contentDescription = "Local WebP Image",
                                       modifier = Modifier
                                           .size(45.dp)
                                   )
                                   Image(
                                       painter = starPainter,
                                       contentDescription = "Local WebP Image",
                                       modifier = Modifier.align(Alignment.BottomCenter)
                                   )
                               }
                               Text(text = item.name, fontSize = 12.sp)
                           }
                       }
                   }
               }


               Spacer(modifier = Modifier.height(10.dp))
               LineItem()
               Spacer(modifier = Modifier.height(10.dp))

               BottomItem()
           }
        }
    }
}

@Composable
private fun BottomItem() {
    Box(
        modifier = Modifier
            .clip(RoundedCornerShape(6.dp))
            .fillMaxWidth()
            .background(Color(0XFFf9f6f2))
            .padding(10.dp)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(text = "来源：", fontSize = 12.sp)

            val area = characterItem.source.area + " - " + characterItem.source.name

            val areaPainter = rememberAsyncImagePainter(
                model = ImageRequest.Builder(LocalContext.current)
                    .data("file:///android_asset/image/icon/nation/${characterItem.source.area}.webp")
                    .build()
            )

            Image(
                painter = areaPainter,
                contentDescription = "Local WebP Image",
            )
            Text(text = area, fontSize = 12.sp)
            Spacer(modifier = Modifier.weight(1f))
            Box(
                modifier = Modifier
                    .size(width = 60.dp, height = 30.dp)
                    .border(
                        1.dp,
                        color = Color.Black, // 边框颜色
                        shape = RoundedCornerShape(5.dp)
                    ),
                contentAlignment = Alignment.Center
            ) {
                Text("详情", color = Color.Black, fontSize = 12.sp)
            }

        }
    }
}

@Composable
private fun LineItem() {
    val linePainter = rememberAsyncImagePainter(
        model = ImageRequest.Builder(LocalContext.current)
            .data("file:///android_asset/image/UI/item-line.webp")
            .build()
    )

    Image(
        painter = linePainter,
        contentDescription = "Local WebP Image",
        modifier = Modifier.fillMaxWidth(),
        contentScale = ContentScale.FillWidth
    )
}

@Composable
private fun PersonImageItem() {
    Box {
        var imageBgcId by remember { mutableIntStateOf(R.drawable.star0) }
        when (characterItem.star) {
            0 -> imageBgcId = R.drawable.star0
            1 -> imageBgcId = R.drawable.star1
            2 -> imageBgcId = R.drawable.star2
            3 -> imageBgcId = R.drawable.star3
            4 -> imageBgcId = R.drawable.star4
            5 -> imageBgcId = R.drawable.star5
        }

        val imagePainter = rememberAsyncImagePainter(
            model = ImageRequest.Builder(LocalContext.current)
                .data("file:///android_asset/image/WIKI/${selectedType.value}/${characterItem.id}.webp")
                .build()
        )
        val elementPainter = rememberAsyncImagePainter(
            model = ImageRequest.Builder(LocalContext.current)
                .data("file:///android_asset/image/icon/element/${characterItem.element}元素.webp")
                .build()
        )

        val weaponTypePainter = rememberAsyncImagePainter(
            model = ImageRequest.Builder(LocalContext.current)
                .data("file:///android_asset/image/icon/weapon/${characterItem.weapon}.webp")
                .build()
        )
        Image(
            painter = painterResource(id = imageBgcId),
            contentDescription = "图片",
            modifier = Modifier.size(100.dp)
        )
        Image(
            painter = imagePainter,
            contentDescription = "Local WebP Image",
            modifier = Modifier
                .size(100.dp)
        )
        Image(
            painter = elementPainter,
            contentDescription = "Local WebP Image",
            modifier = Modifier
                .size(30.dp)
                .padding(start = 5.dp, top = 5.dp)
        )
        Box(
            modifier = Modifier
                .size(width = 100.dp, height = 35.dp)
                .align(Alignment.BottomStart)
                .background(Color(20, 20, 20, 80)),
            contentAlignment = Alignment.Center
        ) {
            Row {
                Image(
                    painter = weaponTypePainter,
                    contentDescription = "Local WebP Image",
                    modifier = Modifier.size(30.dp)
                )
                Text(
                    text = characterItem.name,
                    color = Color.White,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}

@Composable
private fun WeaponImageItem() {
    Box {
        var imageBgcId by remember { mutableIntStateOf(R.drawable.star0) }
        when (characterItem.star) {
            0 -> imageBgcId = R.drawable.star0
            1 -> imageBgcId = R.drawable.star1
            2 -> imageBgcId = R.drawable.star2
            3 -> imageBgcId = R.drawable.star3
            4 -> imageBgcId = R.drawable.star4
            5 -> imageBgcId = R.drawable.star5
        }

        val imagePainter = rememberAsyncImagePainter(
            model = ImageRequest.Builder(LocalContext.current)
                .data("file:///android_asset/image/WIKI/${selectedType.value}/${characterItem.id}.webp")
                .build()
        )

        val weaponTypePainter = rememberAsyncImagePainter(
            model = ImageRequest.Builder(LocalContext.current)
                .data("file:///android_asset/image/icon/weapon/${characterItem.weapon}.webp")
                .build()
        )
        Image(
            painter = painterResource(id = imageBgcId),
            contentDescription = "图片",
            modifier = Modifier.size(100.dp)
        )
        Image(
            painter = imagePainter,
            contentDescription = "Local WebP Image",
            modifier = Modifier
                .size(100.dp)
        )
        Image(
            painter = weaponTypePainter,
            contentDescription = "Local WebP Image",
            modifier = Modifier.size(30.dp)
        )
        Box(
            modifier = Modifier
                .size(width = 100.dp, height = 35.dp)
                .align(Alignment.BottomStart)
                .background(Color(20, 20, 20, 80)),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = characterItem.name,
                color = Color.White,
                fontSize = 12.sp,
                fontWeight = FontWeight.Bold
            )
        }
    }
}


