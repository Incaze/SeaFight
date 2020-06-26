package com.incaze.seafight.model

import com.incaze.seafight.repository.GameRepository
import com.incaze.seafight.utils.Utils

class ShipMap {

    private val mapSize = 160
    private val mapLen = 10
    private val countShipTypesFromNull = 5

    private val errors = mapOf(
            "Can't insert ship" to 1,
            "Count of this ship type is already at max" to 2
    )

    fun tryInsertShip(xAxis: Int, yAxis: Int, shipType: String, userID: Long, game: Game, gameRepository : GameRepository, way: String) : String{

        val utils = Utils()

        val isCreatorUser = utils.isCreatorUser(userID, gameRepository)

        val arrayMap: Array <String?>
        val countUserShips: Array <String?>

        if (isCreatorUser!!) {
            arrayMap = utils.convertMap(game.createrMap, mapSize)
            countUserShips = utils.convertMap(game.countCreaterShips, countShipTypesFromNull)
        } else {
            arrayMap = utils.convertMap(game.partMap, mapSize)
            countUserShips = utils.convertMap(game.countPartShips, countShipTypesFromNull)
        }
        return insertShip(arrayMap, xAxis, yAxis, shipType, way, countUserShips)
    }

    fun getCountShipTypesFromNull() : Int{
        return countShipTypesFromNull
    }

    private fun insertShip(arrayMap : Array<String?>, xAxis: Int, yAxis: Int, shipType: String, way: String, countUserShips: Array<String?>): String{

        val ship = Ship()

        val error = "Can't insert ship"

        val shipID = ship.getShipID(shipType)

        if (countUserShips[shipID!!]!!.toInt() == ship.getMaxCountShip(shipID)) {
            return "Count of this ship type is already at max"
        }

        var wayMapLen = 0
        var anotherWay = 0

        when (way) {
            "up" -> {
                wayMapLen = mapLen
                anotherWay = 1
            }
            "down" -> {
                wayMapLen = -mapLen
                anotherWay = 1
            }
            "left" -> {
                wayMapLen = -1
                anotherWay = mapLen
            }
            "right" -> {
                wayMapLen = 1
                anotherWay = mapLen
            }
        }

        val index = yAxis * mapLen + xAxis

        if (shipID == 1) {
            if (arrayMap[index] == "0"
                    && arrayMap[index + 1] == "0"
                    && arrayMap[index - 1] == "0"
                    && arrayMap[index + mapLen] == "0"
                    && arrayMap[index - mapLen] == "0"
            ) {
                arrayMap[index] = shipID.toString()
                return arrayMap.toString()
            }
            return error
        }
        var tmp = index
            if (arrayMap[tmp + wayMapLen] != "0") {
                return error
            }
            for (i in 2 until shipID) {
                if (arrayMap[tmp] != "0"
                        || arrayMap[tmp + anotherWay] != "0"
                        || arrayMap[tmp - anotherWay] != "0"
                        || arrayMap[tmp - wayMapLen] != "0"
                ) {
                    return error
                }
                arrayMap[tmp] = shipID.toString()
                tmp += wayMapLen
            }
            return arrayMap.toString()
    }

    fun isMapError(str: String) : Boolean{
        return !errors.containsKey(str)
    }
}