package com.academy.fourtk.miles.generator.entrypoint.web.dtos

import com.fasterxml.jackson.annotation.JsonProperty
import jakarta.validation.constraints.Max
import jakarta.validation.constraints.Min
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.NotNull
import jakarta.validation.constraints.Positive
import jakarta.validation.constraints.Size

class CompanyDTO(
    @field:NotBlank(message = " Required field")
    @field:Size(min = 3, max = 30, message = " Field requires 3 to 30 characters")
    val name: String,
    @field:NotBlank(message = " Required field")
    @field:Size(min = 9, max = 11, message = " Field requires 11 characters")
    @JsonProperty("member_number")
    val memberNumber: String,
    @field:NotNull(message = " Required field")
    @field:Positive(message = " Required field positive")
    @field:Min(10, message = " Field requires values between 10 to 100")
    @field:Max(100, message = " Field requires values between 10 to 100")
    @JsonProperty("official_value_of_million")
    val officialValueOfMillion: Double,
    @field:NotNull(message = " Required field")
    @field:Positive(message = " Required field positive")
    @field:Min(10, message = " Field requires values between 10 to 100")
    @field:Max(100, message = " Field requires values between 10 to 100")
    @JsonProperty("unofficial_value_of_million")
    val unofficialValueOfMillion: Double,
    @JsonProperty("program_type")
    val programType: String
)
