package com.kkapps.memorybank.home.models

import androidx.room.ColumnInfo

data class Reference(
    @ColumnInfo(name = "checklist") var checkList: MutableSet<Entity>? = null,
    @ColumnInfo(name = "pages") var pages: MutableSet<Entity>? = null,
    @ColumnInfo(name = "memories") var events: MutableSet<Entity>? = null,
    @ColumnInfo(name = "transactions") var transactions: MutableSet<Entity>? = null
)