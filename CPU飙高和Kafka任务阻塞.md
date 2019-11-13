#### CPU飙高问题

     背景
        某客户现场任务监控系统出现CPU使用率持续在50%,以前基本在20%多，而且和该任务相关的页面打开都是要数十秒。
        
     问题定位：
        先是通过arthas工具调出所有的线程信息，发现有个线程池创建的任务都200左右了，这个使用的是spring内置的线程池，还好是
        控制了大小，不然程序直接挂掉很难定位。
        看了下正常情况下该定时任务只能执行完一次才执行下一次，为什么会存在多个未执行完的程序？然后去review代码，发现罪魁
        是因为开发的同事在定时任务类上加了@Asyn注解，这个注解会导致执行定时任务的时候将任务放到线程中执行，而定时任务15s
        一次，那再15s内没完成任务，那下一次任务也会启动！！！！导致任务堆积，因为该现场这个任务耗时还是蛮大的，所以任务
        处理较慢，而又不停的创建线程，所以CPU身高。
      解决办法：
        去掉@Asyn注解即可
        
        
#### Kafka定时任务挂起

      背景
        在测试环境有个kafka定时按需扩容的功能，但是需要获取offset信息变化。
      
      现象
         该定时任务没有执行，没有发现打印的日志。对此感到比较奇怪，但是在只是看到四天前kafka的异常信息，看到这个觉得和
         该任务执行应该没有关系！！！
      
      问题定位：
         定位这个系统性的问题就上arthas获取线程信息，发现该线程信息如下：
         没保留arthas的信息，囧，这个是jstack的信息，也是一样
         "Schedule Task" #76 prio=5 os_prio=0 tid=0x00007fbb34b8f000 nid=0x90ce runnable [0x00007fbabeded000]
            java.lang.Thread.State: RUNNABLE
         	at sun.nio.ch.EPollArrayWrapper.epollWait(Native Method)
         	at sun.nio.ch.EPollArrayWrapper.poll(EPollArrayWrapper.java:269)
         	at sun.nio.ch.EPollSelectorImpl.doSelect(EPollSelectorImpl.java:79)
         	at sun.nio.ch.SelectorImpl.lockAndDoSelect(SelectorImpl.java:86)
         	- locked <0x00000000e0f4e6b8> (a sun.nio.ch.Util$2)
         	- locked <0x00000000e0f4e6c8> (a java.util.Collections$UnmodifiableSet)
         	- locked <0x00000000e0f4e670> (a sun.nio.ch.EPollSelectorImpl)
         	at sun.nio.ch.SelectorImpl.select(SelectorImpl.java:97)
         	at org.apache.kafka.common.network.Selector.select(Selector.java:454)
         	at org.apache.kafka.common.network.Selector.poll(Selector.java:277)
         	at org.apache.kafka.clients.NetworkClient.poll(NetworkClient.java:260)
         	at org.apache.kafka.clients.consumer.internals.ConsumerNetworkClient.clientPoll(ConsumerNetworkClient.java:360)
         	at org.apache.kafka.clients.consumer.internals.ConsumerNetworkClient.poll(ConsumerNetworkClient.java:224)
         	at org.apache.kafka.clients.consumer.internals.ConsumerNetworkClient.poll(ConsumerNetworkClient.java:192)
         	at org.apache.kafka.clients.consumer.internals.ConsumerNetworkClient.awaitMetadataUpdate(ConsumerNetworkClient.java:134)
         	at org.apache.kafka.clients.consumer.internals.Fetcher.listOffset(Fetcher.java:320)
         	at org.apache.kafka.clients.consumer.internals.Fetcher.resetOffset(Fetcher.java:294)
         	at org.apache.kafka.clients.consumer.internals.Fetcher.updateFetchPositions(Fetcher.java:166)
         	at org.apache.kafka.clients.consumer.KafkaConsumer.updateFetchPositions(KafkaConsumer.java:1408)
         	at org.apache.kafka.clients.consumer.KafkaConsumer.position(KafkaConsumer.java:1196)
         	at com.basp.monitor.kafka.utils.KafkaInfoService.getLatestOffsetFromTopicPartition(KafkaInfoService.java:135)
         	at com.basp.monitor.kafka.utils.KafkaInfoService.getPartitionOffsetInfosByTopics(KafkaInfoService.java:115)
         	at com.basp.monitor.kafka.utils.KafkaInfoService.getTopicOffsetInfosApi(KafkaInfoService.java:102)
         	at com.basp.monitor.kafka.task.DefaultKafkaPartitionExpandStrategyV2.getTopicOffsetInfoOnCurrentTime(DefaultKafkaPartitionExpandStrategyV2.java:181)
         	at com.basp.monitor.kafka.task.DefaultKafkaPartitionExpandStrategyV2.expand(DefaultKafkaPartitionExpandStrategyV2.java:58)
         	at com.basp.monitor.kafka.task.KafkaPartitionExpandTask.run(KafkaPartitionExpandTask.java:26)
         	at java.util.concurrent.Executors$RunnableAdapter.call(Executors.java:511)
         	at java.util.concurrent.FutureTask.runAndReset(FutureTask.java:308)
         	at java.util.concurrent.ScheduledThreadPoolExecutor$ScheduledFutureTask.access$301(ScheduledThreadPoolExecutor.java:180)
         	at java.util.concurrent.ScheduledThreadPoolExecutor$ScheduledFutureTask.run(ScheduledThreadPoolExecutor.java:294)
         	at java.util.concurrent.ThreadPoolExecutor.runWorker(ThreadPoolExecutor.java:1142)
         	at java.util.concurrent.ThreadPoolExecutor$Worker.run(ThreadPoolExecutor.java:617)
         	at java.lang.Thread.run(Thread.java:745)
         
            Locked ownable synchronizers:
         	- <0x00000000e001e9f8> (a java.util.concurrent.ThreadPoolExecutor$Worker)   
         	
         分析上面的日志走过一些弯路，知道意思是大概是锁住了，认为是死锁了，其实不是。先是以为	
         at sun.nio.ch.EPollArrayWrapper.epollWait(Native Method)
         这个存在问题，jdk6确实存在空转阻塞的问题，但我们的环境是1.8的为什么还有这个问题呢？百度一番，无果。
         
         后来是想从业务代码中去寻找线索，顺着线程堆栈信息往下找：
         at org.apache.kafka.clients.NetworkClient.poll(NetworkClient.java:260)
                  	at org.apache.kafka.clients.consumer.internals.ConsumerNetworkClient.clientPoll(ConsumerNetworkClient.java:360)
                  	at org.apache.kafka.clients.consumer.internals.ConsumerNetworkClient.poll(ConsumerNetworkClient.java:224)
                  	at org.apache.kafka.clients.consumer.internals.ConsumerNetworkClient.poll(ConsumerNetworkClient.java:192)
                  	at org.apache.kafka.clients.consumer.internals.ConsumerNetworkClient.awaitMetadataUpdate(ConsumerNetworkClient.java:134)
                  	
          对应kafka的代码：        	
              private long listOffset(TopicPartition partition, long timestamp) {
                  while (true) {
                      RequestFuture<Long> future = sendListOffsetRequest(partition, timestamp);
                      client.poll(future);
          
                      if (future.succeeded())
                          return future.value();
          
                      if (!future.isRetriable())
                          throw future.exception();
          
                      if (future.exception() instanceof InvalidMetadataException)
                      //	at org.apache.kafka.clients.consumer.internals.ConsumerNetworkClient.awaitMetadataUpdate(ConsumerNetworkClient.java:134)
                          client.awaitMetadataUpdate();
                      else
                          time.sleep(retryBackoffMs);
                  }
              }
              
           public void awaitMetadataUpdate() {
                      int version = this.metadata.requestUpdate();
                      do {
                          poll(Long.MAX_VALUE);
                      } while (this.metadata.version() == version);
               }
               
               问题真相浮出水面了，因为在获取Offset信息的时候，可能因为InvalidMetadataException导致程序 poll(Long.MAX_VALUE);
               无限阻塞知道元数据信息刷新！！！这里会将线程阻塞Long.MAX_VALUE，一下子联想到四天前UNKNOWN_TOPIC_OR_PARTITION
               出现的日志，原来这里一直阻塞住了。
               
        总结
        
               