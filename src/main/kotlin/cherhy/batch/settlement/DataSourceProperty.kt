package cherhy.batch.settlement

import org.springframework.boot.context.properties.ConfigurationProperties

@ConfigurationProperties(prefix = "spring.datasource")
data class DataSourceProperty(
    val url: String,
    val username: String,
    val password: String,
)