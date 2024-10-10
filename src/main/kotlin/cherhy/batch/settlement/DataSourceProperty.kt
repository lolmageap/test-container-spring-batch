package cherhy.batch.settlement

import org.springframework.boot.context.properties.ConfigurationProperties

@ConfigurationProperties(prefix = "spring.datasource")
data class DataSourceProperty(
    val master: Connection,
    val slave: Connection,
)

data class Connection(
    val driverClassName: String,
    val url: String,
    val username: String,
    val password: String,
)