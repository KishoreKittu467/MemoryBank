package com.kkapps.memorybank.utils

object Constants {

    enum class Field {
        EVENT, GROUP, TxTYPE, TxMODE, TxCAT, TxRESULT, SOCIAL, FEELING, RELATION, UNKNOWN
    }

    const val MY_ID = "12JH1A0467"
    const val MY_NAME = "Kishore Kittu"
    const val CAN_I_BE_STARRED = true
    const val MY_DP_1 = "https://scontent.fhyd5-1.fna.fbcdn.net/v/t1.0-9/13015221_519940101527294_3903543322646941533_n.jpg?_nc_cat=103&_nc_sid=174925&_nc_ohc=TLiXBxhK6DsAX9DePUB&_nc_ht=scontent.fhyd5-1.fna&oh=a672d01e791f2a730b076561fd7e9265&oe=5F0B0ACE"
    const val MY_DP_2 = "https://scontent-maa2-1.xx.fbcdn.net/v/t1.0-9/70236206_1201642006690430_5397452329635020800_o.jpg?_nc_cat=106&_nc_sid=09cbfe&_nc_ohc=DpiWf2WfdpUAX9ikESd&_nc_ht=scontent-maa2-1.xx&oh=8b35aa32d4b70e20e58ec4996bca4138&oe=5F09BAE6"

    //EVENT_FIELDS
    const val EVENT_PMNT = "payment"
    private const val EVENT_TODO = "todo"
    private const val EVENT_NOTES = "notes"
    private const val EVENT_DIARY = "diary"
    private const val EVENT_MOOD = "mood"
    private const val EVENT_OTHER = "others" //reminders

    //GROUP_FIELDS
    private const val GROUP_FMLY = "Family"
    private const val GROUP_FRNDS = "Friends"
    private const val GROUP_COLL = "Colleagues"
    private const val GROUP_BANK = "Bank"

    //TxTYPE_FIELDS
    const val TxTYPE_PAID = "PAID"
    const val TxTYPE_SPENT = "SPENT"
    const val TxTYPE_GAVE = "GAVE"

    //TxMODE_FIELDS
    const val TxMODE_CASH = "Cash"
    const val TxMODE_SBI_CC = "SBI Credit Card"

    //TxCAT_FIELDS
    const val TxCAT_FOOD = "Food"
    const val TxCAT_OTHER = "Other"

    //TxRESULT_FIELDS
    const val TxRESULT_CLEAR = "Clear"
    const val TxRESULT_EXPENSE = "Expense"
    const val TxRESULT_INCOME = "Income"
    const val TxRESULT_ASSET = "Asset"
    const val TxRESULT_LIABILITY = "Liability"
    const val TxRESULT_INFLOW = "Inflow"
    const val TxRESULT_OUTFLOW = "Outflow"
    const val TxRESULT_MISC = "Miscellaneous"

    //SOCIAL_FIELDS
    private const val SOCIAL_FB = "Fb"

    //FEELING_FIELDS
    const val FEELING_HPY = "Happy"

    //RELATION_FIELDS
    private const val RELATION_SIS = "Sister"

    val EVENT_FIELDS = mutableSetOf(EVENT_PMNT, EVENT_TODO, EVENT_NOTES, EVENT_DIARY, EVENT_MOOD, EVENT_OTHER)
    val GROUP_FIELDS = mutableSetOf(GROUP_FMLY, GROUP_FRNDS, GROUP_COLL, GROUP_BANK)
    val TxTYPE_FIELDS = mutableSetOf(TxTYPE_PAID)
    val TxMODE_FIELDS = mutableSetOf(TxMODE_CASH, TxMODE_SBI_CC)
    val TxCAT_FIELDS = mutableSetOf(TxCAT_FOOD)
    val TxRESULT_FIELDS = mutableSetOf(TxRESULT_EXPENSE)
    val SOCIAL_FIELDS = mutableSetOf(SOCIAL_FB)
    val FEELING_FIELDS = mutableSetOf(FEELING_HPY)
    val RELATION_FIELDS = mutableSetOf(RELATION_SIS)

    enum class GENDER {
        MALE, FEMALE
    }
}