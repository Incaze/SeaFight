package com.incaze.seafight.controller

import com.incaze.seafight.model.Game
import com.incaze.seafight.model.Ship
import com.incaze.seafight.model.ShipMap
import com.incaze.seafight.model.User
import com.incaze.seafight.repository.GameRepository
import com.incaze.seafight.repository.UserRepository
import com.incaze.seafight.service.GameService
import com.incaze.seafight.utils.Utils
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/game")
class GameController {

    @Autowired
    lateinit var gameRepository: GameRepository

    @Autowired
    lateinit var gameService: GameService

    @GetMapping
    fun index (@AuthenticationPrincipal user: User): MutableList<Game?> {
        return gameRepository.findAllByActive(true)
    }

    @PostMapping
    fun createGame(
            @RequestBody opponent_username: String,
            @AuthenticationPrincipal user: User
    ): ResponseEntity<Any>? {
       return gameService.createGame(opponent_username, user)
    }

    @PutMapping("/fill_map/{id}")
    fun fillMap(
            @PathVariable id: Long,
            @RequestBody x_axis: Int,
            @RequestBody y_axis: Int,
            @RequestBody ship_type: String,
            @RequestBody way: String,
            @AuthenticationPrincipal user: User
    ): ResponseEntity<Any>? {
        return gameService.fillMap(id, x_axis, y_axis, ship_type, way, user)
    }

    @PatchMapping("{id}")
    fun play(
            @PathVariable id: Long,
            @RequestBody x_axis: Int,
            @RequestBody y_axis: Int,
            @AuthenticationPrincipal user: User
    ): ResponseEntity<Any>? {
        return gameService.play(id, x_axis, y_axis, user)
    }
}