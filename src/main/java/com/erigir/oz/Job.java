package com.erigir.oz;

import com.erigir.oz.feature.Abort;
import com.erigir.oz.feature.NamedOutput;
import com.erigir.oz.feature.ProgressMonitor;
import com.erigir.oz.feature.ProgressStep;
import com.erigir.oz.storage.StorageService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Map;
import java.util.TreeMap;
import java.util.UUID;
import java.util.concurrent.Callable;

/**
 *
 *
 * User: chrweiss
 * Date: 3/5/14
 * Time: 5:14 PM
 */
public class Job<T> implements Callable<T> {
    private static final Logger LOG = LoggerFactory.getLogger(Job.class);
    private String guid = UUID.randomUUID().toString();
    private JobState state = JobState.UNDEFINED;
    private Throwable failure;

    private transient T finalResult;
    private transient StorageService storageService;
    private transient Callable<T> wrapped;
    private transient Map<String,OutputStream> namedOutputs = new TreeMap<String,OutputStream>();

    private transient Method abortMethod;
    private transient Method progressMonitorMethod;
    private transient Method progressStepMethod;

    Job(Callable<T> wrapped, StorageService storageService) {
        this.state = JobState.NOT_STARTED;
        this.wrapped = wrapped;
        this.storageService = storageService;
        initialize();
    }

    private void initialize()
    {
        Class clazz = wrapped.getClass();

        for (Method m:clazz.getMethods())
        {
            for (Annotation a :m.getAnnotations())
            {
                if (Abort.class.equals(a.annotationType()))
                {
                    if (abortMethod==null)
                    {
                        abortMethod=m;
                    }
                    else
                    {
                        throw new IllegalStateException("Multiple abort methods found ("+m+","+abortMethod+")");
                    }
                }
                else if (NamedOutput.class.equals((a.annotationType())))
                {
                    String name = ((NamedOutput)a).value();
                    if (namedOutputs.get(name)==null)
                    {
                        Class rt = m.getReturnType();
                        if (m.getReturnType()==void.class && m.getParameterTypes().length==1 && m.getParameterTypes()[0].equals(OutputStream.class))
                        {
                            OutputStream os = storageService.createOutput(guid,name);
                            cleanInvokeOnWrapped(m, new Object[]{os});
                            namedOutputs.put(name, os);
                        }
                        else
                        {
                            throw new IllegalStateException("Method "+m+" marked NamedOutput, but returns non-null or does not accept an output stream");
                        }
                    }
                    else
                    {
                        throw new IllegalStateException("Multiple methods accept named output of name "+name);
                    }
                }
                else if (ProgressMonitor.class.equals(a.annotationType()))
                {
                    if (progressMonitorMethod==null)
                    {
                        if (m.getReturnType().equals(double.class) && m.getParameterTypes().length==0)
                        {
                            progressMonitorMethod=m;
                        }
                        else
                        {
                            throw new IllegalStateException("Progress monitor method must return double and accept no parameters");
                        }
                    }
                    else
                    {
                        throw new IllegalStateException("Multiple Progress Monitor methods found ("+m+","+progressMonitorMethod+")");
                    }
                }
                else if (ProgressStep.class.equals(a.annotationType()))
                {
                    if (progressStepMethod==null)
                    {
                        if (m.getReturnType().equals(String.class) && m.getParameterTypes().length==0)
                        {
                            progressStepMethod=m;
                        }
                        else
                        {
                            throw new IllegalStateException("Progress step method must return double and accept no parameters");
                        }
                    }
                    else
                    {
                        throw new IllegalStateException("Multiple Progress Step Method found ("+m+","+progressStepMethod+")");
                    }
                }
            }
        }
    }

    public String getGuid() {
        return guid;
    }

    public void setGuid(String guid) {
        this.guid = guid;
    }

    public JobState getState() {
        return state;
    }

    public void setState(JobState state) {
        this.state = state;
    }

    @Override
    public T call() throws Exception {
        state = JobState.RUNNING;
        try
        {
            finalResult = wrapped.call();
            state = JobState.SUCCESS;
            closeAllStreams();
        }
        catch (Throwable throwable)
        {
            this.failure = throwable;
            state = JobState.FAILURE;
            finalResult = null;
        }
        return finalResult;
    }

    private void closeAllStreams()
    {
        for (Map.Entry<String,OutputStream> e:namedOutputs.entrySet())
        {
            if (e.getValue()!=null)
            {
                try
                {
                    e.getValue().flush();
                    e.getValue().close();
                }
                catch (IOException ioe)
                {
                    LOG.warn("Error closing stream {} of job {}", new Object[]{e.getKey(), guid}, ioe);

                }
            }
        }
    }

    public void abort()
    {
        if (abortMethod==null)
        {
            throw new IllegalStateException("Can't abort - no abort method annotated");
        }
        else
        {
            cleanInvokeOnWrapped(abortMethod, new Object[0]);
            closeAllStreams();
        }
    }

    public String getCurrentStep()
    {
        String rval = (progressStepMethod==null)?(String)null:(String)cleanInvokeOnWrapped(progressStepMethod, new Object[0]);
        return rval;
    }

    public Double getCurrentProgress()
    {
        Double rval = (progressMonitorMethod==null)?(Double)null:(Double)cleanInvokeOnWrapped(progressMonitorMethod, new Object[0]);
        return rval;
    }

    public Throwable getFailure() {
        return failure;
    }

    public T getFinalResult() {
        return finalResult;
    }

    private Object cleanInvokeOnWrapped(Method m, Object[] args)
    {
        try
        {
            return m.invoke(wrapped,args);
        }
        catch (InvocationTargetException ite)
        {
            throw new IllegalStateException("Cant happen",ite);
        }
        catch (IllegalAccessException iae)
        {
            throw new IllegalStateException("Cant happen",iae);
        }
    }

    public InputStream accessStream(String name)
    {
        return storageService.accessInput(guid, name);
    }



}
