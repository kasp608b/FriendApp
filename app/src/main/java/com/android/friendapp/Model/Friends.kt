package com.android.friendapp.Model

object Friends {
    public var nextId = 22

    public val mFriends = mutableListOf<BEFriend>(
        BEFriend(1,"Jonas", "123", true, "jonas@hotmail.com","https://www.msn.com/da-dk/", null),
        BEFriend(2, "Anders", "1234", false, "jonas@hotmail.com","https://www.msn.com/da-dk/", null),
        BEFriend(3,"Nikolaj", "12345", true, "jonas@hotmail.com","https://www.msn.com/da-dk/", null),
        BEFriend(4,"Nadia", "12345678", true, "jonas@hotmail.com","https://www.msn.com/da-dk/", null),
        BEFriend(5, "Michael", "23456789", true, "jonas@hotmail.com","https://www.msn.com/da-dk/", null),
        BEFriend(6,"Kacper", "87654321", false, "jonas@hotmail.com","https://www.msn.com/da-dk/", null),
        BEFriend(7, "Rocio", "12121212", true, "jonas@hotmail.com","https://www.msn.com/da-dk/", null),
        BEFriend(8, "Jonas", "123", true, "jonas@hotmail.com","https://www.msn.com/da-dk/", null),
        BEFriend(9,"Anders", "1234", false, "jonas@hotmail.com","https://www.msn.com/da-dk/", null),
        BEFriend(10,"Nikolaj", "12345", true, "jonas@hotmail.com","https://www.msn.com/da-dk/", null),
        BEFriend(11, "Nadia", "12345678", true, "jonas@hotmail.com","https://www.msn.com/da-dk/", null),
        BEFriend(12, "Michael", "23456789", true, "jonas@hotmail.com","https://www.msn.com/da-dk/", null),
        BEFriend(13, "Kacper", "87654321", false, "jonas@hotmail.com","https://www.msn.com/da-dk/", null),
        BEFriend(14, "Rocio", "12121212", true, "jonas@hotmail.com","https://www.msn.com/da-dk/", null),
        BEFriend(15, "Jonas", "123", true, "jonas@hotmail.com","https://www.msn.com/da-dk/", null),
        BEFriend(16,"Anders", "1234", false, "jonas@hotmail.com","https://www.msn.com/da-dk/", null),
        BEFriend(17, "Nikolaj", "12345", true, "jonas@hotmail.com","https://www.msn.com/da-dk/", null),
        BEFriend(18, "Nadia", "12345678", true, "jonas@hotmail.com","https://www.msn.com/da-dk/", null),
        BEFriend(19, "Michael", "23456789", true, "jonas@hotmail.com","https://www.msn.com/da-dk/", null),
        BEFriend(20, "Kacper", "87654321", false, "jonas@hotmail.com","https://www.msn.com/da-dk/", null),
        BEFriend(21, "Rocio", "12121212", true, "jonas@hotmail.com","https://www.msn.com/da-dk/", null)
    )

    public fun getAll(): MutableList<BEFriend> = mFriends


    public fun getAllNames(): Array<String>  =  mFriends.map { p -> p.name }.toTypedArray()



}