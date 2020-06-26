package com.incaze.seafight.model

import javax.persistence.*

@Entity
@Table(name = "user")
data class User(
        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        var id: Long? = null,
        var username: String? = null,
        var password: String? = null,
        @ElementCollection(targetClass = Role::class, fetch = FetchType.EAGER)
        @CollectionTable(name = "user_role", joinColumns = [JoinColumn(name = "user_id")])
        @Enumerated(EnumType.STRING)
        @Column(name = "role")
        var roles: MutableList<Role>? = null
)