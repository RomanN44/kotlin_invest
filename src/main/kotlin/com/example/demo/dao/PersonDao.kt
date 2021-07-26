package com.example.demo.dao

import com.example.demo.model_xml.HobbiesXml
import com.example.demo.model_xml.HobbyXml
import com.example.demo.model_xml.PersonXml
import org.springframework.jdbc.core.JdbcTemplate
import org.springframework.stereotype.Repository
import java.sql.ResultSet

@Repository
class PersonDao(var jdbcTemplate: JdbcTemplate){
    fun loadAllPersons(): MutableList<PersonXml> {
        val list: MutableList<PersonXml> = mutableListOf()
        list.addAll(selectPersonWithHobby())
        list.addAll(selectPersonsWithoutHobby())
        return list
    }

    fun selectPersonsWithoutHobby(): MutableList<PersonXml> =
        jdbcTemplate.query(
            "SELECT * FROM persons WHERE person_id NOT IN(SELECT person_id FROM hobby)"
    ) {
        rs: ResultSet, _: Int ->
            PersonXml(rs.getString("name"), rs.getString("birthday"), null)
    }

    fun selectPersonWithHobby(): MutableList<PersonXml> {
        val list: MutableList<PersonXml> = jdbcTemplate.query(
            "SELECT * FROM persons RIGHT JOIN hobby ON persons.person_id = hobby.person_id"
        ) { rs: ResultSet, _: Int ->
            val hobbies = HobbiesXml(mutableListOf())
            hobbies.hobbyXml.add(HobbyXml(rs.getInt("complexity"), rs.getString("hobby_name")))
            PersonXml(rs.getString("name"), rs.getString("birthday"), hobbies)
        }

        val result: MutableList<PersonXml> = mutableListOf()

        var temp = PersonXml("temp", "temp", null)

        list.forEach { person1 ->
            if(!temp.equalsWithoutHobbies(person1)){
                temp = person1
                val hobbyXmlList: MutableList<HobbyXml> = mutableListOf()
                person1.hobbies?.hobbyXml?.first()?.let { hobbyXmlList.add(it) }
                list.forEach { person2 ->
                    if(person1.equalsWithoutHobbies(person2) &&
                            !(person1.hobbies?.hobbyXml?.first()?.equals(person2.hobbies?.hobbyXml?.first()))!!
                    ){
                        person2.hobbies?.hobbyXml?.first()?.let { hobbyXmlList.add(it) }
                    }
                }
                result.add(PersonXml(person1.name, person1.birthday, HobbiesXml(hobbyXmlList)))
            }
        }
        return result
    }

    private fun insertPersonGenerator(personXml: PersonXml): String {
        return "INSERT INTO persons(name, birthday) VALUES ('" + personXml.name + "', '" + personXml.birthday + "')"
    }

    private fun insertHobbyGenerator(hobbyXml: HobbyXml, personXml: PersonXml): String {
        return "INSERT INTO hobby(complexity, hobby_name, person_id) VALUES (" + hobbyXml.complexity + ", '" + hobbyXml.hobby_name + "', " + getPersonId(personXml) + ")"
    }

    private fun deleteHobbyGeneratorByPerson(personXml: PersonXml): String {
        return "DELETE FROM public.hobby WHERE person_id = " + getPersonId(personXml).toString()
    }

    private fun deleteHobbyGeneratorByHobby(hobbyXml: HobbyXml, personXml: PersonXml): String {
        return "DELETE FROM public.hobby WHERE person_id = " + getPersonId(personXml).toString() + " and complexity = " + hobbyXml.complexity + " and lower(hobby_name) like lower('" +hobbyXml.hobby_name + "')"
    }

    private fun getPersonId(personXml: PersonXml): Long? {
        val sql =
            "Select person_id From persons where lower(persons.name) Like(lower('" + personXml.name + "')) and lower(birthday) Like(lower('" + personXml.birthday + "'))"
        return jdbcTemplate.queryForObject(sql, Long::class.java)
    }

    private fun selectPersonHobbiesGenerator(personXml: PersonXml): String {
        return "SELECT complexity, hobby_name, person_id FROM hobby where person_id = " + getPersonId(personXml).toString()
    }

    private fun selectHobbyByPerson(personXml: PersonXml): MutableList<HobbyXml> =
        jdbcTemplate.query(selectPersonHobbiesGenerator(personXml)
        ) { rs: ResultSet, _: Int ->
            HobbyXml(rs.getInt("complexity"), rs.getString("hobby_name"))
        }

    fun addPersonsWithoutHobby(personXmlList: MutableList<PersonXml>) {
        val all: MutableList<PersonXml> = loadAllPersons()
        personXmlList.forEach {
            if(!it.isExistList(all)) {
                jdbcTemplate.execute(insertPersonGenerator(it))
            } else {
                if(it.isExistList(all) && it.returnClone(all)?.hobbies != null) {
                    jdbcTemplate.execute(deleteHobbyGeneratorByPerson(it))
                }
            }
        }
    }

    fun addPersonsWithHobby(personXmlList: MutableList<PersonXml>) {
        val all: MutableList<PersonXml> = loadAllPersons()
        personXmlList.forEach { person ->
            if(!person.isExistList(all)) {
                jdbcTemplate.execute(insertPersonGenerator(person))
                person.hobbies?.hobbyXml?.forEach {
                    jdbcTemplate.execute(insertHobbyGenerator(it, person))
                }
            } else {
                val db_hobbiesXml:MutableList<HobbyXml> = selectHobbyByPerson(person)
                val deleteList:MutableList<HobbyXml> = mutableListOf()
                val ignorList:MutableList<HobbyXml> = mutableListOf()
                var flag = false
                db_hobbiesXml.forEach { db_hobby ->
                    person.hobbies?.hobbyXml?.forEach { xml_hobby ->
                        if(xml_hobby.equals(db_hobby))
                            flag = true
                    }
                    if(!flag)
                        deleteList.add(db_hobby)
                    flag = false
                }
                db_hobbiesXml.forEach { db_hobby ->
                    person.hobbies?.hobbyXml?.forEach { xml_hobby ->
                        if(xml_hobby.equals(db_hobby))
                            ignorList.add(xml_hobby)
                    }
                }
                deleteList.forEach { hobby ->
                    jdbcTemplate.execute(deleteHobbyGeneratorByHobby(hobby, person))
                }
                person.hobbies?.hobbyXml?.forEach {
                    if(!it.isExistList(ignorList))
                        jdbcTemplate.execute(insertHobbyGenerator(it, person))
                }
            }
        }
    }

    fun addPersons(personXmlList: MutableList<PersonXml>) {
        val personsWithoutHobby: MutableList<PersonXml> = mutableListOf()
        val personsWithHobby: MutableList<PersonXml> = mutableListOf()
        personXmlList.forEach {
            if(it.hobbies == null)
                personsWithoutHobby.add(it)
            else
                personsWithHobby.add(it)
        }
        addPersonsWithoutHobby(personsWithoutHobby)
        addPersonsWithHobby(personsWithHobby)
    }
}
