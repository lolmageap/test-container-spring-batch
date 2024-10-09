package cherhy.batch.settlement

object BatchProperties {
    const val CHUNK_SIZE = 10

    object Job {
        const val EXAMPLE_JOB = "exampleJob"
    }

    object Step {
        const val EXAMPLE_FIRST_STEP = "firstStep"
        const val EXAMPLE_LAST_STEP = "lastStep"
    }

    object ItemReader {
        const val EXAMPLE_ITEM_READER = "exampleItemReader"
    }

    object ItemProcessor {
        const val EXAMPLE_ITEM_PROCESSOR = "exampleItemProcessor"
    }

    object ItemWriter {
        const val EXAMPLE_ITEM_WRITER = "exampleItemWriter"
    }
}