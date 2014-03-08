Oz
==

Oz is a java library to simplify monitoring of long-running jobs on a webserver - look behind the curtain!

How To Use It
=============

First, add Oz to your project thru Maven like so:

<dependency>
    <groupId>com.erigir</groupId>
    <artifactId>oz</artifactId>
    <version>1.0.0-SNAPSHOT</version>
</dependency>

Second, decide which "storage" you want to use.  Default is memory based, but there is also a file-based version.  I
plan to add an S3 based version in the future as well, but that's a ways off.

File = com.erigir.oz.storage.FileStorage
Memory = com.erigir.oz.storage.MemoryStorage

Third, create a com.erigir.oz.JobService instance;

JobService service = new JobService(new FileStorage(myDirectory));

Fourth, create your "thing" that Oz is going to run and monitor.  It needs to (at least) implement Callable.  But if you
really wanna do anything useful, you should also annotate it with the things you'll need
TBD: describe the useful annotations here

Instantiate your object, but DO NOT submit it to a executor yet!
MyCallable mc = new MyCallable();

Fifth, ask JobService to create you a job that wraps your Callable

Job myJob = service.wrap(mc);

Sixth, run the job you got back, as you normally would run your callable
ExecutorService myExecutorService = Executors.fixedThreadPool(10);

Future<Blah> f = myExecutorService.submit(myJob);

Seventh, monitor your jobs status using the funtions in the job object.

Eighth, profit!!


