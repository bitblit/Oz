package com.erigir.oz.storage;

import com.erigir.oz.Job;

import java.io.InputStream;
import java.io.OutputStream;
import java.util.List;

/**
 * User: chrweiss
 * Date: 3/7/14
 * Time: 11:37 AM
 */
public interface StorageService {
    List<String> getAllJobIds();
    Job getJobById(String id);

    OutputStream createOutput(String jobId, String name);
    InputStream accessInput(String jobId, String name);
}
