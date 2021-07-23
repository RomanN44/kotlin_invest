package com.example.demo.Entity

import javax.persistence.*

@Entity
@Table(name = "hobby")
class HobbyEntity(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "hobby_id")
    val id: Int,

    @Column(name = "complexity")
    val complexity: Int,

    @Column(name = "hobby_name")
    val hobby_name: String,

    @Column(name = "person_id")
    val person_id: Long
) {
    constructor() : this(0, 0, "No hobby name", 0)

    override fun toString(): String {
        return "HobbyEntity(id=$id, complexity=$complexity, hobby_name='$hobby_name', person_id=$person_id)"
    }
}