package com.dodo.customerservice.customer.definitions

import com.dodo.customerservice.customer.repositories.Addresses
import com.dodo.customerservice.customer.repositories.Customers
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.transactions.transaction
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.core.env.Environment

@Configuration
class CustomerConfiguration(val env: Environment) {
    @Bean
    fun getDatabase(): Database {
        val hostname = env.getProperty("hostname", "db")
        val port = env.getProperty("port", "5432")
        val dbName = env.getProperty("db_name", "customer_service")
        val username = env.getProperty("username", "root")
        val password = env.getProperty("password", "root")

        val db = Database.connect("jdbc:postgresql://${hostname}:${port}/${dbName}",
                driver = "org.postgresql.Driver", user = username, password = password)

        // Throw exception in initialization
        transaction {
            SchemaUtils.create(Customers, Addresses)
        }

        return db
    }
}
