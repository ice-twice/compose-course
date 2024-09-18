package com.jetreader.ui.screens.login

import com.jetreader.domain.user.User

fun User.toFirestoreDataFormat(): Map<String, String> =
    mapOf(
        "user_id" to userId,
        "display_name" to displayName,
        "quote" to quote,
        "profession" to profession,
        "avatar_url" to avatarUrl,
    )
