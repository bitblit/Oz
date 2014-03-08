package com.erigir.oz;

import com.erigir.oz.storage.MemoryStorageService;
import com.erigir.oz.storage.StorageService;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.Callable;

/**
 * User: chrweiss
 * Date: 3/5/14
 * Time: 5:14 PM
 */
public class JobService {
    private StorageService storageService = new MemoryStorageService();

    public JobService()
    {
        super();
    }

    public JobService(StorageService storageService) {
        this.storageService = storageService;
    }

    public StorageService getStorageService() {
        return storageService;
    }

    public void setStorageService(StorageService storageService) {
        this.storageService = storageService;
    }

    public List<Job> getAllJobs()
    {

        return Arrays.asList(new Job(new SampleCallable(1000,1),storageService), new Job(new SampleCallable(1000,2),storageService), new Job(new SampleCallable(1000,3),storageService));
    }

    public <T> Job<T> createJob(Callable<T> callable)
    {
        return new Job(callable,storageService);
    }
}
