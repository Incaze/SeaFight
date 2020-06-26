package com.incaze.seafight.service.impl

import com.incaze.seafight.model.Role
import com.incaze.seafight.model.User
import com.incaze.seafight.service.UserService
import com.incaze.seafight.repository.UserRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.core.Authentication
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

@Service
class UserServiceImpl : UserService {

    @Autowired
    lateinit var userRepository: UserRepository

    @Autowired
    lateinit var encoder: PasswordEncoder

    override fun register(user: User): String {
        if (userRepository.existsByUsername(user.username.toString())){
            return "User ${user.username} already exists"
        }
        user.password = encoder.encode(user.password)
        user.roles = Role.USER
        userRepository.save(user)
        return "User ${user.username} created"
    }

    override fun authorization(request: HttpServletRequest?,
                               response: HttpServletResponse?,
                               authentication: Authentication?): String {
        if (authentication?.isAuthenticated == true) {
            response?.sendRedirect("/")
            return "Already authenticated"
        }
        return "Enter username and password"
    }

}
