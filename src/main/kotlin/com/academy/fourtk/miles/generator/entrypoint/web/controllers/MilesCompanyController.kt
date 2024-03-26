package com.academy.fourtk.miles.generator.entrypoint.web.controllers

import com.academy.fourtk.miles.generator.entrypoint.web.dtos.CompanyDTO
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import java.util.logging.Logger

@RestController
@RequestMapping("/company")
class MilesCompanyController() {
    private lateinit var logger: Logger

    @PostMapping
    fun createCompany(@RequestBody request: CompanyDTO): ResponseEntity<String> {
        logger.info("starter create company")
        return ResponseEntity("Ola", HttpStatus.OK)
    }
}
