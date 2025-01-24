package com.wuji.teyvataide.model

data class CharacterData(
    val id: Int,
    val contentId: Int,
    val dropDays: List<Int>,
    val name: String,
    val itemType: String,
    val star: Int,
    val weapon: String,
    val element: String,
    val materials: List<Material>,
    val source: Source
)


data class Material(
    val id: Long,
    val name: String,
    val star: Int
)

data class Source(
    val index: Int,
    val area: String,
    val name: String
)

