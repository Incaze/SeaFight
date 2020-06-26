package com.incaze.seafight.controller

import com.incaze.seafight.model.User
import com.incaze.seafight.service.UserService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.core.Authentication
import org.springframework.web.bind.annotation.*
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

@RestController
@RequestMapping("/")
class UserController {

    @Autowired
    lateinit var userService: UserService

    @PostMapping("/registration")
    fun registration(@RequestBody user : User) : String {
        return userService.register(user)
    }

    @PostMapping("/login")
    fun authorization(request: HttpServletRequest?,
                      response: HttpServletResponse?,
                      authentication: Authentication?): String {
        return userService.authorization(request, response, authentication)
    }
}