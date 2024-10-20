package cherhy.batch.settlement.util

import java.io.File

object ExampleTempFileGenerator {
    fun createTempFile(
        jobId: Long,
    ) {
        File("temp-example-${jobId}.txt").createNewFile()
    }

    fun deleteTempFile(
        jobId: Long,
    ) {
        File("temp-example-${jobId}.txt").delete()
    }

    fun getTempFile(
        jobId: Long,
    ) =
        File("temp-example-${jobId}.txt")
}