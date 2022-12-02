package com.harisha.hackerstories.model

data class Stories(val by: String="", val descendants: String?, val id: String="",
                   val kids : List<Int>?, val score: Int, val time: Int, val title: String ="",
                   val type: String?, val url: String?)