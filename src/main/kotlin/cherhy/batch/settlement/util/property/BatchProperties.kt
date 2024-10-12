package cherhy.batch.settlement.util.property

object BatchProperties {
    const val CHUNK_SIZE = 10

    object Job {
        const val EXAMPLE_JOB = "exampleJob"
        const val EXAMPLE_FILE_JOB = "exampleFileJob"
    }

    object Step {
        const val EXAMPLE_FIRST_STEP = "firstStep"
        const val EXAMPLE_LAST_STEP = "lastStep"
        const val EXAMPLE_FILE_FIRST_STEP = "exampleFileFirstStep"
        const val EXAMPLE_FILE_LAST_STEP = "exampleFileLastStep"
    }

    object ItemReader {
        const val EXAMPLE_ITEM_READER = "exampleItemReader"
        const val CSV_FILE_ITEM_READER = "csvFileItemReader"
        const val TEXT_FILE_ITEM_READER = "textFileItemReader"
    }

    object ItemProcessor {
        const val EXAMPLE_ITEM_PROCESSOR = "exampleItemProcessor"
        const val CSV_FILE_ITEM_PROCESSOR = "csvFileItemProcessor"
        const val TEXT_FILE_ITEM_PROCESSOR = "textFileItemProcessor"
    }

    object ItemWriter {
        const val EXAMPLE_ITEM_WRITER = "exampleItemWriter"
        const val CSV_FILE_ITEM_WRITER = "csvFileItemWriter"
        const val TEXT_FILE_ITEM_WRITER = "textFileItemWriter"
    }
}