package com.incaze.seafight.service

import com.incaze.seafight.model.User
import org.springframework.security.core.Authentication
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

interface UserService {
    fun register(user: User): String
    fun authorization(request: HttpServletRequest?,
                      response: HttpServletResponse?,
                      authentication: Authentication?): String
}