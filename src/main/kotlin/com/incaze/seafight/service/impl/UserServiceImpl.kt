package com.incaze.seafight.service.impl

import com.incaze.seafight.dto.UserDTO
import com.incaze.seafight.model.User
import com.incaze.seafight.service.UserService
import com.incaze.seafight.repository.UserRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.core.Authentication
import org.springframework.stereotype.Service
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

@Service
class UserServiceImpl : UserService {

    @Autowired
    lateinit var userRepository: UserRepository

    override fun register(user: User): String {
        if (!userRepository.existsByUsername(user.username.toString())) {
            userRepository.save(user)
            return "User ${user.username} successfully registered"
        }
        return "User ${user.username} already exist"
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

    override fun changeRoleToAdmin(username: String): String {
        TODO("Not yet implemented")
    }

    override fun getUsers(): List<UserDTO> {
        TODO("Not yet implemented")
    }

}
