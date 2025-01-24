package com.wuji.teyvataide.data
import androidx.compose.runtime.Composable
import com.google.gson.Gson
import com.wuji.teyvataide.model.CharacterData

class Calendar {
    @Composable
    fun getCalendar():Array<CharacterData>{
        val jsonString = readJsonFromAssets("data/app/calendar.json")
        val gson=Gson()
        val temp=gson.fromJson(jsonString,Array<CharacterData>::class.java)
        return temp
    }
}


