package com.example.demo.entity

import javax.persistence.*

@Entity
@Table(name = "hobby")
data class HobbyEntity(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "hobby_id")
    var id: Long? = null,
    @Column(name = "complexity")
    var complexity: Int? = null,
    @Column(name = "hobby_name")
    var hobby_name: String? = null,
    @Column(name = "person_id")
    var person: Long? = null
)