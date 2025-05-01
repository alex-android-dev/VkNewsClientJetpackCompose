package com.example.vknewsclient.data.repository

import com.vk.id.VKID

class AuthRepository {

    fun refreshToken() {
        VKID.instance.refreshToken
    }

}