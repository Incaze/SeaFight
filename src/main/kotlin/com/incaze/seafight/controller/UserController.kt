package com.incaze.seafight.controller

import com.incaze.seafight.model.Role
import com.incaze.seafight.model.User
import com.incaze.seafight.repository.UserRepository
import com.incaze.seafight.service.UserService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.core.Authentication
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.web.bind.annotation.*
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

@RestController
class UserController {

    @Autowired
    lateinit var userService: UserService

    @Autowired
    private lateinit var userRepository: UserRepository

    @Autowired
    lateinit var encoder: PasswordEncoder

    @PostMapping("/registration")
    fun registration(@RequestBody user : User) : String {
        if (userRepository.existsByUsername(user.username.toString())){
            return "User ${user.username} already exists"
        }
        user.password = encoder.encode(user.password)
        user.roles = mutableListOf(Role.USER)
        userRepository.save(user)
        return "User ${user.username} created"
    }

    @PostMapping("/login")
    fun authorization(request: HttpServletRequest?,
                      response: HttpServletResponse?,
                      authentication: Authentication?): String {
        return userService.authorization(request, response, authentication)
    }
}