package com.incaze.seafight.dto

data class GameDTO (
        val id: Long,
        val active: Boolean = true,
        val moveUserID: Long,
        val createrUserID: Long,
        val partUserID: Long,
        val createrMap: String = "",
        val partMap: String = "",
        val countCreaterShips: String = "",
        val countPartShips: String = "",
        val winnerUserID: Long? = 0
)
