package com.example.demo.entity

import javax.persistence.*

@Entity
@Table(name = "hobby")
data class Hobby(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "hobby_id")
    val id: Long? = null,
    @Column(name = "complexity")
    val complexity: Int? = null,
    @Column(name = "hobby_name")
    val hobby_name: String? = null,
    @Column(name = "person_id")
    val person: Long? = null
)