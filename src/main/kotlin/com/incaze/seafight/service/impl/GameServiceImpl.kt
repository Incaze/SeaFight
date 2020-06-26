package com.incaze.seafight.service.impl

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
import org.springframework.stereotype.Service

@Service
class GameServiceImpl : GameService {

    @Autowired
    lateinit var userRepository: UserRepository

    @Autowired
    lateinit var gameRepository: GameRepository

    override fun createGame(opponent_username : String, user : User) : ResponseEntity<Any>?{
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
        newGame.active = false
        newGame.createrUserID = getIDNotFinishedGamesUser.toLong()
        newGame.partUserID = getIDNotFinishedGamesOpponent.toLong()
        newGame.createrMap = ""
        newGame.partMap = ""
        newGame.moveUserID = getIDNotFinishedGamesUser.toLong()
        gameRepository.save(newGame)
        return ResponseEntity.ok(newGame)
    }

    override fun fillMap(id : Long, x_axis: Int, y_axis: Int, ship_type: String, way: String, user: User): ResponseEntity<Any>? {
        val game = gameRepository.findById(id).get()
        val utils = Utils()
        val ship = Ship()
        val shipMap = ShipMap()
        if (game.active!!) {
            return ResponseEntity("Game is active", HttpStatus.BAD_REQUEST)
        }

        if (game.winnerUserID != (0).toLong()) {
            return ResponseEntity("Game Over", HttpStatus.BAD_REQUEST)
        }

        val isCreatorUser = utils.isCreatorUser(user.id!!, gameRepository)
                ?: return ResponseEntity("You are not playing in game id = ${game.id}", HttpStatus.BAD_REQUEST)

        if (!ship.isExistShipType(ship_type)) {
            return ResponseEntity("Ship $ship_type does not exist", HttpStatus.BAD_REQUEST)
        }

        val gameMap = shipMap.tryInsertShip(x_axis, y_axis, ship_type, user.id!!, game, gameRepository, way)

        if (shipMap.isMapError(gameMap)) {
            return ResponseEntity(gameMap, HttpStatus.BAD_REQUEST)
        }

        val shipID = ship.getShipID(ship_type)

        if (isCreatorUser) {
            val countUserShips = utils.convertMap(game.countCreaterShips, ship.getCountShipTypesFromNull())
            countUserShips[shipID!!] = (countUserShips[shipID]!!.toInt() + 1).toString()
            game.createrMap = gameMap
            game.countCreaterShips = utils.convertToString(countUserShips)
        } else {
            val countUserShips = utils.convertMap(game.countPartShips, ship.getCountShipTypesFromNull())
            countUserShips[shipID!!] = (countUserShips[shipID]!!.toInt() + 1).toString()
            game.partMap = gameMap
            game.countPartShips = utils.convertToString(countUserShips)
        }

        val creatorIsFinishFillMap = shipMap.isMapFillFinished(game.countCreaterShips)
        val partIsFinishFillMap = shipMap.isMapFillFinished(game.countPartShips)

        if (creatorIsFinishFillMap && partIsFinishFillMap){
            game.active = true
        }
        gameRepository.save(game)
        return ResponseEntity.ok("Ship inserted")
    }

    override fun play(id : Long, x_axis : Int, y_axis : Int, user : User): ResponseEntity<Any>? {
        val game: Game = gameRepository.findById(id).get()
        val utils = Utils()
        val ship = Ship()
        val shipMap = ShipMap()
        if(!game.active!!){
            return ResponseEntity("Not active game", HttpStatus.BAD_REQUEST)
        }
        val isCreatorUser = utils.isCreatorUser(user.id!!, gameRepository)
                ?: return ResponseEntity("You are not playing in game id = ${game.id}", HttpStatus.BAD_REQUEST)

        if (game.moveUserID != user.id) {
            return ResponseEntity("Not your move", HttpStatus.BAD_REQUEST)
        }

        var opponentMap : String
        val opponentID : Long

        if (isCreatorUser) {
            opponentMap = game.partMap
            opponentID = game.partUserID!!
        } else {
            opponentMap = game.createrMap
            opponentID = game.createrUserID!!
        }

        val shot = shipMap.move(x_axis, y_axis, opponentMap)
        val countUserShips : Array<String?>
        val shipID : Int
        var destroyed = false
        when (shot) {
            "Fail" -> {
                game.moveUserID = opponentID
                gameRepository.save(game)
                return ResponseEntity.ok(shot)
            }
            "Destroyed" -> {
                destroyed = true
            }
        }
        shipID = shipMap.getShipIdByAxis(x_axis, y_axis, opponentMap)
        countUserShips = utils.convertMap(opponentMap, ship.getCountShipTypesFromNull())
        if (destroyed) {
            countUserShips[shipID] = (countUserShips[shipID]!!.toInt() - 1).toString()
        }
        opponentMap = shipMap.applyMove(x_axis, y_axis, opponentMap)
        if (isCreatorUser) {
            game.partMap = opponentMap
            game.countPartShips = utils.convertToString(countUserShips)
        } else {
            game.createrMap = opponentMap
            game.countCreaterShips = utils.convertToString(countUserShips)
        }

        val gameOver = shipMap.isGameOver(countUserShips)

        if(gameOver){
            game.winnerUserID = user.id
            game.active = false
        }

        gameRepository.save(game)

        if(!game.active!!){
            val winnerUserID = game.winnerUserID
            return ResponseEntity("Game over - winner $winnerUserID", HttpStatus.BAD_REQUEST)
        }
        return ResponseEntity.ok(shot)
    }

}