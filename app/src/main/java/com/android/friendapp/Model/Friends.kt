package com.android.friendapp.Model

object Friends {

    val mFriends = mutableListOf<BEFriend>(
        BEFriend("Jonas", "123", true),
        BEFriend("Anders", "1234", false),
        BEFriend("Nikolaj", "12345", true),
        BEFriend("Nadia", "12345678", true),
        BEFriend("Michael", "23456789", true),
        BEFriend("Kacper", "87654321", false),
        BEFriend("Rocio", "12121212", true),
        BEFriend("Jonas", "123", true),
        BEFriend("Anders", "1234", false),
        BEFriend("Nikolaj", "12345", true),
        BEFriend("Nadia", "12345678", true),
        BEFriend("Michael", "23456789", true),
        BEFriend("Kacper", "87654321", false),
        BEFriend("Rocio", "12121212", true),
        BEFriend("Jonas", "123", true),
        BEFriend("Anders", "1234", false),
        BEFriend("Nikolaj", "12345", true),
        BEFriend("Nadia", "12345678", true),
        BEFriend("Michael", "23456789", true),
        BEFriend("Kacper", "87654321", false),
        BEFriend("Rocio", "12121212", true)
    )

    fun getAll(): MutableList<BEFriend> = mFriends


    fun getAllNames(): Array<String>  =  mFriends.map { p -> p.name }.toTypedArray()



}