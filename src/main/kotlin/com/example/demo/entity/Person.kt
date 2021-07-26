package com.example.demo.entity

import javax.persistence.*

@Entity
@Table(name = "persons")
data class Person(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "person_id")
    var id: Long? = null,
    @Column(name = "name")
    var name: String? = null,
    @Column(name = "birthday")
    var birthday: String? = null
)