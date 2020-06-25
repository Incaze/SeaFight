package com.incaze.seafight.model

import com.incaze.seafight.repository.GameRepository
import com.incaze.seafight.utils.Utils

class ShipMap {

    val mapSize = 111
    val mapLen = 10

    fun tryInsertShip(xAxis: Int, yAxis: Int, shipType: String, userID: Long, game: Game, gameRepository : GameRepository) : String{

        val utils = Utils()

        val isCreatorUser = utils.isCreatorUser(userID, gameRepository)
                ?: return "You are not playing in game id = ${game.id}"

        if (isCreatorUser) {
            val arrayMap = convertMap(game.createrMap)
            return insertShip(arrayMap, xAxis, yAxis, shipType)
        }

        return "ss"
    }

    private fun insertShip(arrayMap : Array<String?>, xAxis: Int, yAxis: Int, shipType: String): String{

        if ((arrayMap[yAxis * mapLen + xAxis] != "0")
                && (arrayMap[yAxis * mapLen + xAxis + 1] != "0")
                && (arrayMap[yAxis * mapLen + xAxis - 1] != "0")
                && (arrayMap[yAxis * mapLen + xAxis + mapLen] != "0")
                && (arrayMap[yAxis * mapLen + xAxis - mapLen] != "0")
        )
        {

            return ""
        }

        return "Error"
    }

    private fun convertMap(createrMap: String): Array<String?> {
        if (createrMap == "") {
            val arrayMap = arrayOfNulls<String>(mapSize)
            for (i in arrayMap.indices) {
                arrayMap[i] = "0"
            }
            return arrayMap
        }
        return createrMap.split(" ").toTypedArray()
    }
}