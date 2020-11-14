package ch.heigvd.iict.sym.labo2

class Person (
    val name: String,
    val firstname: String,
    val middlename: String = "",
    val gender: String,
    val phone: String,
    val phoneType: String
){
    override fun toString(): String{
        return name + ", " + firstname + ", " + gender + ", " + phone
    }
}

