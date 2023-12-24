package com.example.quoteapp.ModelClass

import android.os.Message

data class MOdelClassQuotesResponse(
    val result:Boolean,
    val requestId:String,
    val message: String,
    val messageLBL:String,
    val payload:ModelClassQuotePayload,

)
data class ModelClassQuotePayload(
    val id:Int,
    val date:String,
    val quote:String,
    val name:String,
    val personal_perspective:String,
    val bio:String
)

/*
{
    "result": true,
    "requestId": "0ef82a06-8baa-4333-9c99-7e05730fd659",
    "message": "",
    "messageLBL": null,
    "payload": {
        "id": 253,
         "current_page": 2,
            "from": 6,
            "last_page": 15
        "date": "2023-05-01",
        "quote": "I think one of my basic flaws, has been a lack of esteem … always feeling like I had to do more. I never could do enough or be good enough. And that was the real problem.",
        "name": "Max Robinson",
        "personal_perspective": "Admiration and respect from your peers should be important because you'll be around people who treat you fairly and with love. But looking for support shouldn't be your goal. Your life accomplishments come from your hard work, and if your focus is working to impress other people, they dictate your happiness and your life's direction. Esteem is desirable, but only to a limit. Be good enough for yourself and make self-esteem the foundation for your drive and passion.",
        "bio": "Maxie Cleveland Robinson, Jr.—broadcast journalist; served as co-anchor on ABC World News, being the first African-American broadcast news anchor (1978 -1983). 🇺🇸"
    }
}
 */