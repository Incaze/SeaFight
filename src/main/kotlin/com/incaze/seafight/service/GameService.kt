package com.incaze.seafight.service

import com.incaze.seafight.model.User
import org.springframework.http.ResponseEntity

interface GameService {
    fun createGame(opponent_username : String, user : User) : ResponseEntity<Any>?
    fun fillMap(id : Long, x_axis: Int, y_axis: Int, ship_type: String, way: String, user: User) : ResponseEntity<Any>?
    fun play(id : Long, x_axis : Int, y_axis : Int, user : User) : ResponseEntity<Any>?
}