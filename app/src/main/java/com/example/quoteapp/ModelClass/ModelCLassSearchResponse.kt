package com.example.quoteapp.ModelClass

import android.support.v4.app.INotificationSideChannel
import com.google.gson.JsonElement

data class ModelCLassSearchResponse(
    val result:Boolean,
    val requestId:String,
    val message: String,
    val messageLBL:String,
    val payload:ModelClassData
)
data class ModelClassData(
    val data: ArrayList<ModelClassSearchPayload>,
    val meta:MeteData
)
data class ModelClassSearchPayload(
    val response_type:String,
    val id:Int,
    val date :String,
    val quote:String,
    val name:String,
    val personal_perspective:String,
    val bio:String,
    val fact:String

) :java.io.Serializable
data class MeteData(
    val current_page:Int,
    val from:Int,
    val last_page:Int,
    val links:ArrayList<JsonElement>,
    val per_page:Int,
    val to:Int,
    val total:Int
)

/*
* "current_page": 2,
            "from": 6,
            "last_page": 15,
            "links": [

            ],
            "path": "https://shopblack366.com/daily_quotes_mobile_app/api/search",
            "per_page": 5,
            "to": 10,
            "total": 75
* */
/*
"response_type": "quotes",
                "id": 100,
                "date": "2023-02-01",
                "quote": "If you read the life of great men and women who made important changes in history, there are two common features: One, they are angry at the state of affairs, and two, they were people of faith.",
                "name": "Leymah Gbowee",
                "personal_perspective": "Faith doesn't have to be ascribed to a higher power. Faith can simply involve knowing there is more than the surface of what your five senses can experience. You can be faithful to ideologies, systems, and even your mission to be part of change. \r\n\r\nFaith is hope and knowing that what's currently going on isn't right or enough. Faith is knowing that your planned work will bring change that matters.",
                "bio": "Leymah Roberta Gbowee—peace activist responsible for leading a women's nonviolent peace movement (Women of Liberia Mass Action for Peace) that helped bring an end to the second Liberian civil War (2003); co-awarded the Nobel Peace Prize for \"nonviolent struggle for the safety of women and for women's rights to full participation in peace-building work\" (2011). 🇱🇷"
 */