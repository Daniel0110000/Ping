package com.daniel.ping.domain.utilities

import android.content.Context
import android.util.Patterns
import com.daniel.ping.R
import com.daniel.ping.domain.models.ValidationResult

// Definition of the SecurityService class with a companion object
class SecurityService {
    companion object{
        // Function that validates the email address and password
        fun emailAndPasswordValidation(email: String, password: String, context: Context): ValidationResult{
            return when{

                // Checks if the email address or password are empty, returns a false ValidationResult with an error message
                email.isEmpty() || password.isEmpty() -> ValidationResult(false, context.getString(R.string.emptyFieldsErrorMessage))

                // Checks if the email address is valid according to a predefined pattern, returns a false ValidationResult with an error message
                !Patterns.EMAIL_ADDRESS.matcher(email).matches() -> ValidationResult(false, context.getString(R.string.invalidEmailErrorMessage))

                // Checks if the password has at least 8 characters, returns a false ValidationResult with an error message
                password.length < 8 -> ValidationResult(false, context.getString(R.string.passwordTooShortErrorMessage))

                // If all validation pass, returns a true ValidationResult with a success message
                else -> ValidationResult(true, context.getString(R.string.successful))
            }
        }
    }
}