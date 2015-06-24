package com.erigir.oz;

import org.junit.Test;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import static org.junit.Assert.assertEquals;

/**
 * User: chrweiss
 * Date: 3/7/14
 * Time: 11:34 AM
 */
public class TestJobService {

    @Test
    public void testCreateJob()
            throws Exception
    {
        Integer value = 1;

        JobService js = new JobService();
        SampleCallable sc = new SampleCallable(5000,value);

        Job<Integer> job = js.createJob(sc);
        ExecutorService es = Executors.newCachedThreadPool();

        Future<Integer> f = es.submit(job);

        while (!f.isDone())
        {
            System.out.println("Step: "+job.getCurrentStep());
            Double pct = job.getCurrentProgress();
            if (pct!=null)
            {
                System.out.println("Progress: "+(pct*100)+"%");
            }
            try
            {
                Thread.sleep(100);
            }
            catch (InterruptedException ie)
            {
                // Do nothing
            }
        }

        if (job.getState()==JobState.SUCCESS)
        {
            Integer i = f.get();
            assertEquals(i, value);

            // Dump the contents of the input stream
            InputStream ios = job.accessStream("logger");
            BufferedReader br = new BufferedReader(new InputStreamReader(ios));
            String line = br.readLine();
            while (line!=null)
            {
                System.out.println("Read: "+line);
                line = br.readLine();
            }

        }
        else
        {
            System.out.println("Failed with : "+job.getFailure());
        }


    }



}
