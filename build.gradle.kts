// Top-level build file where you can add configuration options common to all sub-projects/modules.
plugins {
    alias(libs.plugins.android.application) apply false
    alias(libs.plugins.kotlin.android) apply false
    alias(libs.plugins.kotlin.compose) apply false
    alias(libs.plugins.kotlin.serialization) apply false
    alias(libs.plugins.kotlin.parcelize) apply false
    id("vkid.manifest.placeholders") version "1.1.0" apply true
}

vkidManifestPlaceholders {
    // Добавьте плейсхолдеры сокращенным способом. Например, vkidRedirectHost будет "vk.com", а vkidRedirectScheme будет "vk$clientId".
    // Или укажите значения явно через properties, если не хотите использовать плейсхолдеры.
    vkidRedirectHost = "vk.com" // Обычно vk.com.
    vkidRedirectScheme = "vk53393600" // Строго в формате vk{ID приложения}.
    vkidClientId = "53393600"
    vkidClientSecret = "KqEy0byDBGHrY4MwBG90"
}