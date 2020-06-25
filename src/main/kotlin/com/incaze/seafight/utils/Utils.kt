package com.incaze.seafight.utils

import com.incaze.seafight.model.Game
import com.incaze.seafight.model.User
import com.incaze.seafight.repository.GameRepository

class Utils {

    fun getLastGameID(id: Long, gameRepository : GameRepository): Int {
        val createrGames = gameRepository.findAllByCreaterUserID(id)
        val partGames = gameRepository.findAllByPartUserID(id)
        val createrLastGameID = checkGames(createrGames)
        val partLastGameID = checkGames(partGames)
        if (createrLastGameID != 0) {
            return createrLastGameID
        }
        return partLastGameID
    }

    fun isCreatorUser(id: Long, gameRepository: GameRepository): Boolean? {
        val createrGames = gameRepository.findAllByCreaterUserID(id)
        val partGames = gameRepository.findAllByPartUserID(id)
        val createrLastGameID = checkGames(createrGames)
        val partLastGameID = checkGames(partGames)
        if (createrLastGameID != 0) {
            return true
        }
        if (partLastGameID != 0) {
            return false
        }
        return null
    }

    private fun checkGames(games: MutableList<Game?>): Int {
        for (game in games){
            if (game != null) {
                if(game.active!!)
                    return game.id!!.toInt()
            }
        }
        return 0
    }
}