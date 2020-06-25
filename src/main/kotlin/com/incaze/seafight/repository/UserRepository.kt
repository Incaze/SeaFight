package com.incaze.seafight.repository

import com.incaze.seafight.model.User
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface UserRepository : JpaRepository<User, Long> {
    fun findByUsername(Username: String): User?
    fun existsByUsername(Username: String): Boolean
}