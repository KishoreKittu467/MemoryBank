package com.kkapps.backpocket.models

import androidx.room.ColumnInfo

data class Reference(
    @ColumnInfo(name = "checkList") var checkList: MutableSet<Type>? = null,
    @ColumnInfo(name = "pages") var pages: MutableSet<Type>? = null,
    @ColumnInfo(name = "memories") var events: MutableSet<Type>? = null
)