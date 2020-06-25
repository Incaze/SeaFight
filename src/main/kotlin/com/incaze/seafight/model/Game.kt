package com.incaze.seafight.model

import javax.persistence.*

@Entity
@Table(name = "game")
data class Game(
        @Id
        @GeneratedValue(strategy = GenerationType.SEQUENCE)
        var id: Long? = null,

        @Column(name = "active")
        var active: Boolean? = true,

        @Column(name = "move_user_id")
        var moveUserID: Long? = null,

        @Column(name = "creater_user_id")
        var createrUserID: Long? = null,

        @Column(name = "part_user_id")
        var partUserID: Long? = null,

        @Column(name = "creater_map")
        var createrMap: String = "",

        @Column(name = "part_map")
        var partMap: String = "",

        @Column(name = "winner_user_id")
        var winnerUserID: Long? = null

)