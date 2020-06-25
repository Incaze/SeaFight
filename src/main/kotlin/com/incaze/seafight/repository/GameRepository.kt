package com.incaze.seafight.repository

import com.incaze.seafight.model.Game
import org.springframework.data.jpa.repository.JpaRepository

interface GameRepository: JpaRepository<Game?, Long?> {
    fun findAllByCreaterUserID(createrUserID: Long): MutableList<Game?>
    fun findAllByPartUserID(partUserID: Long): MutableList<Game?>
    fun findAllByActive(active: Boolean): MutableList<Game?>
}
