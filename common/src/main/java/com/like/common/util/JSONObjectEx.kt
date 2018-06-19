package com.like.common.util

import org.json.JSONObject

fun JSONObject.optStringForEmptyString(name: String) =
        if (this.isNull(name)) {
            ""
        } else {
            this.optString(name)
        }
