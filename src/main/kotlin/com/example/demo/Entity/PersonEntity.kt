package com.example.demo.Entity

import com.example.demo.Model.Person
import javax.persistence.*

@Entity
@Table(name = "persons")
data class PersonEntity(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "person_id")
    val id: Long? = null,
    @Column(name = "name")
    var name: String? = null,
    @Column(name = "birthday")
    var birthday: String? = null
) {
    fun isExistListWithoutId(list: MutableList<Person>): Boolean {
        list.forEach { person ->
            if (name.equals(person.name) && birthday.equals(person.birthday)) {
                return true
            }
        }
        return false
    }
}