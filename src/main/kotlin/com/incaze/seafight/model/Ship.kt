package com.incaze.seafight.model


class Ship {

    private val shipMap = mapOf(
            "boats" to 1,
            "destroyer" to 2,
            "cruiser" to 3,
            "battleship" to 4
    )

    private val maxCountShip = mapOf(
            1 to 4,
            2 to 3,
            3 to 2,
            4 to 1
    )

    fun getMaxCountShip(shipID : Int) : Int{
        return maxCountShip[shipID]!!
    }

    fun getShipID(ShipName : String) : Int?{
        return shipMap[ShipName]
    }

    fun isExistShipType(shipName : String) : Boolean{
        return shipMap.containsKey(shipName)
    }
}