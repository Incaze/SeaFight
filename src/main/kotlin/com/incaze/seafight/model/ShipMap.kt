package com.incaze.seafight.model

import com.incaze.seafight.repository.GameRepository
import com.incaze.seafight.utils.Utils

class ShipMap {

    private val mapSize = 160
    private val mapLen = 10

    private val errors = mapOf(
            "Can't insert ship" to 1,
            "Count of this ship type is already at max" to 2
    )

    fun tryInsertShip(xAxis: Int, yAxis: Int, shipType: String, userID: Long, game: Game, gameRepository : GameRepository, way: String) : String{

        val utils = Utils()
        val ship = Ship()
        val isCreatorUser = utils.isCreatorUser(userID, gameRepository)

        val arrayMap: Array <String?>
        val countUserShips: Array <String?>

        if (isCreatorUser!!) {
            arrayMap = utils.convertMap(game.createrMap, mapSize)
            countUserShips = utils.convertMap(game.countCreaterShips, ship.getCountShipTypesFromNull())
        } else {
            arrayMap = utils.convertMap(game.partMap, mapSize)
            countUserShips = utils.convertMap(game.countPartShips, ship.getCountShipTypesFromNull())
        }
        return insertShip(arrayMap, xAxis, yAxis, shipType, way, countUserShips)
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

    fun isMapFillFinished(countUserShips: String) : Boolean{
        val utils = Utils()
        val ship = Ship()
        val ships = utils.convertMap(countUserShips, mapSize)
        val countShips = ship.getCountShipTypesFromNull()
        for (i in 1 until countShips){
            if (ships[i]!!.toInt() != ship.getMaxCountShip(i)){
                return false
            }
        }
        return true
    }

    fun move(xAxis: Int, yAxis: Int, gameMap: String) : String{
        val utils = Utils()
        val arrayMap = utils.convertMap(gameMap, mapSize)
        val index = yAxis * mapLen + xAxis

        if (arrayMap[index] == "0") {
            return "Fail"
        }
        val tmp = arrayMap[index]

        if (arrayMap[index + 1] != tmp
                || arrayMap[index - 1] != tmp
                || arrayMap[index - mapLen] != tmp
                || arrayMap[index + mapLen] != tmp
                || arrayMap[index + 1] != "x"
                || arrayMap[index - 1] != "x"
                || arrayMap[index - mapLen] != "x"
                || arrayMap[index + mapLen] != "x"
        ){
            return "Destroyed"
        }
        return "Hit"
    }

    fun applyMove(xAxis: Int, yAxis: Int, gameMap: String) : String {
        val utils = Utils()
        val arrayMap = utils.convertMap(gameMap, mapSize)
        val index = yAxis * mapLen + xAxis
        arrayMap[index] = "x"
        return arrayMap.toString()
    }

    fun isGameOver(countUserShips: Array<String?>) : Boolean{
        for (i in countUserShips.indices) {
            if (countUserShips[i] != "0") {
                return false
            }
        }
        return true
    }

    fun getShipIdByAxis(xAxis: Int, yAxis: Int, gameMap: String) : Int {
        val utils = Utils()
        val arrayMap = utils.convertMap(gameMap, mapSize)
        return arrayMap[yAxis * mapLen + xAxis]!!.toInt()
    }

    fun isMapError(str: String) : Boolean{
        return !errors.containsKey(str)
    }
}