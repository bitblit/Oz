package com.erigir.oz;

/**
 * User: chrweiss
 * Date: 3/5/14
 * Time: 5:15 PM
 */
public enum JobState {
    UNDEFINED("Job is still being constructed"),
    NOT_STARTED("Job enqueued by not started"),
    RUNNING("Job currently executing"),
    CANCELLED("Job cancelled by user"),
    SUCCESS("Job completed successfully"),
    FAILURE("Job completed unsuccessfully"),
    EXCEPTION("Exception thrown during job");

    private String description;

    JobState(String description)
    {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }
}
