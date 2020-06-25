package com.incaze.seafight.controller

import com.incaze.seafight.model.Game
import com.incaze.seafight.model.ShipMap
import com.incaze.seafight.model.User
import com.incaze.seafight.repository.GameRepository
import com.incaze.seafight.repository.UserRepository
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
    lateinit var userRepository: UserRepository

    @Autowired
    lateinit var gameRepository: GameRepository

    lateinit var shipMap: ShipMap

    @GetMapping
    fun index (@AuthenticationPrincipal user: User): MutableList<Game?> {
        return gameRepository.findAllByActive(true)
    }

    @PostMapping
    fun createGame(
            @RequestBody opponent_username: String,
            @AuthenticationPrincipal user: User
    ): ResponseEntity<Any>? {

        val utils = Utils()

        val getIDNotFinishedGamesUser = utils.getLastGameID(user.id!!, gameRepository)

        if (getIDNotFinishedGamesUser != 0) {
            return ResponseEntity("You have unfinished game id = $getIDNotFinishedGamesUser", HttpStatus.BAD_REQUEST )
        }

        val opponent = userRepository.findByUsername(opponent_username)
                ?: return ResponseEntity("Opponent $opponent_username is not exist", HttpStatus.BAD_REQUEST )

        val getIDNotFinishedGamesOpponent = utils.getLastGameID(opponent.id!!, gameRepository)
        if (getIDNotFinishedGamesOpponent != 0) {
            return ResponseEntity("Your opponent have unfinished game id = $getIDNotFinishedGamesOpponent", HttpStatus.BAD_REQUEST )
        }

        val newGame = Game()
        newGame.active = true
        newGame.createrUserID = getIDNotFinishedGamesUser.toLong()
        newGame.partUserID = getIDNotFinishedGamesOpponent.toLong()
        newGame.createrMap = ""
        newGame.partMap = ""
        newGame.moveUserID = getIDNotFinishedGamesUser.toLong()
        gameRepository.save(newGame)
        return ResponseEntity.ok(newGame)
    }

    @PutMapping("/fill_map/{id}")
    fun fillMap(
            @PathVariable id: Long,
            @RequestBody x_axis: Int,
            @RequestBody y_axis: Int,
            @RequestBody ship_type: String,
            @AuthenticationPrincipal user: User
    ): ResponseEntity<Any>? {
        val game = gameRepository.findById(id).get()

        if (!game.active!!) {
            return ResponseEntity("Game over", HttpStatus.BAD_REQUEST)
        }

        val isInserted = shipMap.tryInsertShip(x_axis, y_axis, ship_type, user.id!!, game, gameRepository)



        return  ResponseEntity.ok(game)
    }


}