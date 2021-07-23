package com.example.demo.Dao

import com.example.demo.Model.Hobbies
import com.example.demo.Model.Hobby
import com.example.demo.Model.Person
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.jdbc.core.JdbcTemplate
import org.springframework.stereotype.Repository
import java.sql.ResultSet


@Repository
class PersonDao(@Autowired var jdbcTemplate: JdbcTemplate){

    private val SELECT_PERSONS_WITHOUT_HOBBY: String = "SELECT * FROM persons WHERE person_id NOT IN(SELECT person_id FROM hobby)"
    private val SELECT_PERSONS_WITH_HOBBY: String = "SELECT * FROM persons RIGHT JOIN hobby ON persons.person_id = hobby.person_id"

    fun loadAllPersons(): MutableList<Person> {
        val list: MutableList<Person> = mutableListOf()
        val first = selectPersonWithHobby()
        val second = selectPersonsWithoutHobby()
        list.addAll(first)
        list.addAll(second)
        return list
    }

    fun selectPersonsWithoutHobby(): MutableList<Person> =
        jdbcTemplate.query(SELECT_PERSONS_WITHOUT_HOBBY
    ) {
        rs: ResultSet, _: Int ->
            Person(rs.getString("name"), rs.getString("birthday"), null)
    }

    fun selectPersonWithHobby(): MutableList<Person> {
        val list: MutableList<Person> = jdbcTemplate.query(SELECT_PERSONS_WITH_HOBBY
        ) { rs: ResultSet, _: Int ->
            val hobbies = Hobbies(mutableListOf())
            hobbies.hobby.add(Hobby(rs.getInt("complexity"), rs.getString("hobby_name")))
            Person(rs.getString("name"), rs.getString("birthday"), hobbies)
        }

        val result: MutableList<Person> = mutableListOf()
        var temp = Person("temp", "temp", null)

        list.forEach { person1 ->
            if(!temp.equalsWithoutHobbies(person1)){
                temp = person1
                val hobbyList: MutableList<Hobby> = mutableListOf()
                person1.hobbies?.hobby?.first()?.let { hobbyList.add(it) }
                list.forEach { person2 ->
                    if(person1.equalsWithoutHobbies(person2) &&
                            !(person1.hobbies?.hobby?.first()?.equals(person2.hobbies?.hobby?.first()))!!
                    ){
                        person2.hobbies?.hobby?.first()?.let { hobbyList.add(it) }
                    }
                }
                result.add(Person(person1.name, person1.birthday, Hobbies(hobbyList)))
            }
        }
        return result
    }

    private fun insertPersonGenerator(person: Person): String {
        return "INSERT INTO persons(name, birthday) VALUES ('" + person.name + "', '" + person.birthday + "')"
    }

    private fun insertHobbyGenerator(hobby: Hobby, person: Person): String {
        return "INSERT INTO hobby(complexity, hobby_name, person_id) VALUES (" + hobby.complexity + ", '" + hobby.hobby_name + "', " + getPersonId(person) + ")"
    }

    private fun deleteHobbyGeneratorByPerson(person: Person): String {
        return "DELETE FROM public.hobby WHERE person_id = " + getPersonId(person).toString()
    }

    private fun deleteHobbyGeneratorByHobby(hobby: Hobby, person: Person): String {
        return "DELETE FROM public.hobby WHERE person_id = " + getPersonId(person).toString() + " and complexity = " + hobby.complexity + " and lower(hobby_name) like lower('" +hobby.hobby_name + "')"
    }

    private fun getPersonId(person: Person): Long? {
        val sql =
            "Select person_id From persons where lower(persons.name) Like(lower('" + person.name + "')) and lower(birthday) Like(lower('" + person.birthday + "'))"
        return jdbcTemplate.queryForObject(sql, Long::class.java)
    }

    private fun selectPersonHobbiesGenerator(person: Person): String {
        return "SELECT complexity, hobby_name, person_id FROM hobby where person_id = " + getPersonId(person).toString()
    }

    private fun selectHobbyByPerson(person: Person): MutableList<Hobby> =
        jdbcTemplate.query(selectPersonHobbiesGenerator(person)
        ) { rs: ResultSet, _: Int ->
            Hobby(rs.getInt("complexity"), rs.getString("hobby_name"))
        }

    fun addPersonsWithoutHobby(personList: MutableList<Person>) {
        val all: MutableList<Person> = loadAllPersons()
        personList.forEach {
            if(!it.isExistList(all)) {
                jdbcTemplate.execute(insertPersonGenerator(it))
            } else {
                if(it.isExistList(all) && it.returnClone(all)?.hobbies != null) {
                    jdbcTemplate.execute(deleteHobbyGeneratorByPerson(it))
                }
            }
        }
    }

    fun addPersonsWithHobby(personList: MutableList<Person>) {
        val all: MutableList<Person> = loadAllPersons()
        personList.forEach { person ->
            if(!person.isExistList(all)) {
                jdbcTemplate.execute(insertPersonGenerator(person))
                person.hobbies?.hobby?.forEach {
                    jdbcTemplate.execute(insertHobbyGenerator(it, person))
                }
            } else {
                val db_hobbies:MutableList<Hobby> = selectHobbyByPerson(person)
                val deletList:MutableList<Hobby> = mutableListOf()
                val ignorList:MutableList<Hobby> = mutableListOf()
                var flag = false
                db_hobbies.forEach { db_hobby ->
                    person.hobbies?.hobby?.forEach { xml_hobby ->
                        if(xml_hobby.equals(db_hobby))
                            flag = true
                    }
                    if(!flag)
                        deletList.add(db_hobby)
                    flag = false
                }
                db_hobbies.forEach { db_hobby ->
                    person.hobbies?.hobby?.forEach { xml_hobby ->
                        if(xml_hobby.equals(db_hobby))
                            ignorList.add(xml_hobby)
                    }
                }
                deletList.forEach { hobby ->
                    jdbcTemplate.execute(deleteHobbyGeneratorByHobby(hobby, person))
                }
                person.hobbies?.hobby?.forEach {
                    if(!it.isExistList(ignorList))
                        jdbcTemplate.execute(insertHobbyGenerator(it, person))
                }
            }
        }
    }

    fun addPersons(personList: MutableList<Person>) {
        val personsWithoutHobby: MutableList<Person> = mutableListOf()
        val personsWithHobby: MutableList<Person> = mutableListOf()
        personList.forEach {
            if(it.hobbies == null)
                personsWithoutHobby.add(it)
            else
                personsWithHobby.add(it)
        }
        addPersonsWithoutHobby(personsWithoutHobby)
        addPersonsWithHobby(personsWithHobby)
    }
}
