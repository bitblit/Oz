package com.erigir.oz;

import com.erigir.oz.feature.NamedOutput;
import com.erigir.oz.feature.ProgressMonitor;
import com.erigir.oz.feature.ProgressStep;

import java.io.OutputStream;
import java.io.PrintWriter;
import java.util.concurrent.Callable;

/**
 * Simple class that waits then returns an integer
 * User: chrweiss
 * Date: 3/7/14
 * Time: 11:45 AM
 */
public class SampleCallable implements Callable<Integer> {
    private long stepWaitTime=10;
    private int rval = 1;
    private int currentStep = 0;
    private PrintWriter logger;

    public SampleCallable(long waitTime, int rval) {
        if (waitTime<100)
        {
            throw new IllegalArgumentException("Wait time must be at least 100");
        }
        this.stepWaitTime = waitTime/100;
        this.rval = rval;
    }

    @Override
    public Integer call() throws Exception {
        for (currentStep=0;currentStep<100;currentStep++)
        {
            logger.println(getCurrentStep());
            try
            {
                Thread.sleep(stepWaitTime);
            }
            catch (InterruptedException ie)
            {
                // Do nothing
            }
        }

        logger.flush();
        logger.close();

        return rval;
    }

    @ProgressMonitor
    public double getCurrentProgress()
    {
        return (double)currentStep/100.0;
    }

    @NamedOutput("logger")
    public void setLogger(OutputStream logger)
    {
        this.logger = new PrintWriter(logger);
    }

    @ProgressStep
    public String getCurrentStep()
    {
        return "Processing step "+currentStep+"\n";
    }
}
