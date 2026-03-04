package com.otg.bingo.repository.internal

import com.otg.bingo.model.UserProfile

interface UserProfileStore {

    suspend fun setUserProfile(userProfile: UserProfile?)

    suspend fun loadUserProfile(): UserProfile?
}