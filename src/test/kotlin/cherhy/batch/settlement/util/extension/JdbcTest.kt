package cherhy.batch.settlement.util.extension

import cherhy.batch.settlement.lib.WithTestContainers
import cherhy.batch.settlement.model.Example
import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.shouldBe
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.jdbc.core.JdbcTemplate

@SpringBootTest
class JdbcTest(
    private val jdbcTemplate: JdbcTemplate,
) : WithTestContainers, StringSpec({
    "findOne" {
        jdbcTemplate.execute("INSERT INTO example (name, price) VALUES ('책', 10000)")

        val example =
            jdbcTemplate.findOne(
                Example::class,
            ) { "SELECT * FROM example WHERE name = '책'" }!!

        example.name shouldBe "책"
        example.price shouldBe 10000
    }

    "findAll" {
        jdbcTemplate.execute("INSERT INTO example (name, price) VALUES ('책', 10000)")
        jdbcTemplate.execute("INSERT INTO example (name, price) VALUES ('컴퓨터', 20000)")

        val examples =
            jdbcTemplate.findAll(
                Example::class,
            ) { "SELECT * FROM example" }

        examples.size shouldBe 2

        examples[0].name shouldBe "책"
        examples[0].price shouldBe 10000

        examples[1].name shouldBe "컴퓨터"
        examples[1].price shouldBe 20000
    }
})