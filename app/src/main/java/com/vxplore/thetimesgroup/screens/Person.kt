package com.vxplore.thetimesgroup.screens

import androidx.compose.ui.graphics.Color

data class Person(
    val name: String,
    val age: Int,
    val profilePictureUrl: String? = null
)


fun getPersonList() = listOf(
    Person("Grace Hopper", 25),
    Person("Ada Lovelace", 29),
    Person("John Smith", 28),
    Person("Elon Musk", 41),
    Person("Will Smith", 31),
    Person("Robert James", 42),
    Person("Anthony Curry", 91),
    Person("Kevin Jackson", 22),
    Person("Robert Curry", 1),
    Person("John Curry", 9),
    Person("Ada Jackson", 2),
    Person("Joe Defoe", 35)
)

fun getPersonAge() = listOf(
    Person("Grace Hopper", 50),
    Person("Ada Lovelace", 100),
    Person("John Smith", 150),
    Person("Elon Musk", 200),
    Person("Will Smith", 250),
    Person("Robert James", 300),
)

data class Paper(var name: String, val price: Int)

fun getPaperPrice() = listOf(
    Paper("Times of India", 5),
    Paper("Economics Times", 7),
    Paper("Ei Samay", 3),
    Paper("Ananda Bazar", 2),
    Paper("Bortoman", 2),
    Paper("The Telegraph", 5),
    Paper("Hindustan Times", 5),
    Paper("The Indian Express", 7),
    Paper("The Hindu", 4),
    Paper("Dainik Jagran", 2),
    Paper("Dainik Bhaskar", 2),
)

data class PaperSold(
    val name: String,
    val price: Int,
    val percentage: String,
    val floatValue: Float,
    val color: Color
)

fun getPaperSoldDetails() = listOf(
    PaperSold("TOI", 10000, "10%", 10f, Color(0xFF219C0B.toInt())),
    PaperSold("ET", 70000, "70%", 70f, Color(0xFF6A9E73.toInt())),
    PaperSold("ES", 20000, "20%", 20f, Color(0xFF477D50.toInt())),
    PaperSold("AB", 80000, "80%", 80f, Color(0xFF275E30.toInt())),
    PaperSold("Btm", 30000, "30%", 30f, Color(0xFF509944.toInt())),
    PaperSold("TTlg", 50000, "50%", 50f, Color(0xFF219C0B.toInt())),
    PaperSold("HT", 40000, "40%", 40f, Color(0xFF6A9E73.toInt())),
    PaperSold("TIE", 10000, "10%", 10f, Color(0xFF477D50.toInt())),
    PaperSold("TOI", 10000, "10%", 10f, Color(0xFF219C0B.toInt())),
    PaperSold("ET", 70000, "70%", 70f, Color(0xFF6A9E73.toInt())),
    PaperSold("ES", 20000, "20%", 20f, Color(0xFF477D50.toInt())),
    PaperSold("AB", 80000, "80%", 80f, Color(0xFF275E30.toInt())),
    PaperSold("Btm", 30000, "30%", 30f, Color(0xFF509944.toInt())),
    PaperSold("TTlg", 50000, "50%", 50f, Color(0xFF219C0B.toInt())),
    PaperSold("HT", 40000, "40%", 40f, Color(0xFF6A9E73.toInt())),
    PaperSold("TIE", 10000, "10%", 10f, Color(0xFF477D50.toInt())),
    PaperSold("DB", 60000, "60%", 60f, Color(0xFF477D50.toInt()))
)
