package com.erigir.oz.storage;

import com.erigir.oz.Job;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

/**
 * User: chrweiss
 * Date: 3/7/14
 * Time: 11:38 AM
 */
public class MemoryStorageService implements StorageService {
    private Map<String,Job> jobs;
    private Map<String,ByteArrayOutputStream> stores = new TreeMap<String, ByteArrayOutputStream>();

    @Override
    public List<String> getAllJobIds() {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public Job getJobById(String id) {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public OutputStream createOutput(String jobId, String name) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        stores.put(jobId+"---"+name, baos);
        return baos;
    }

    @Override
    public InputStream accessInput(String jobId, String name) {
        ByteArrayOutputStream holder = stores.get(jobId+"---"+name);
        if (holder!=null)
        {
            byte[] d = holder.toByteArray();
            return new ByteArrayInputStream(d);
        }
        else
        {
            return null;
        }
    }
}
