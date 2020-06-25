package com.incaze.seafight.service

import com.incaze.seafight.dto.GameDTO
import com.incaze.seafight.model.User
import org.springframework.security.core.Authentication
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

interface GameService {

    fun getActualGame(id: Long): GameDTO

}