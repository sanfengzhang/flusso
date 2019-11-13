##### Flink App开发运维设计思路

###### 背景
        一直想对Flink数据计算做一些应用性的工作，趁着最近这段时间劲头比较足，开始做一些基本的设计和开发。
 
        首先想定义一下基本方向：   
        1.Flink CP功能
        2.对Flink在计算资源（合理分配资源）      
        3.对每一条数据可以进行血缘回溯
        4.将实时特征和Flink融合起来
        5.对大数据系统尽量在内存、硬盘等方面进行稳定性检查:例如调度任务时对磁盘健康检查
        6.Flink数据源限速消费（Flink kafka本身支持限速，本地限速和全局限速）   
        
             
###### Flink CP
       在做Flink CP状态监控需要做一下事情：
       1.整理出每个Job所有Task的分布，是如何分布在不同的运行节点上的。
       2.在获取Job运行分布图，那么需要监控jobManager中出现的异常日志,Received late message for now expired checkpoint 
          attempt {} from task {} of job {} at {}.
          Received message for an unknown checkpoint {} from task {} of job {} at {}.
          等这些日志的收集，来判断是哪个任务对应的哪个节点上的CP出现问题，然后去对应节点分析具体的问题。
        3.从数据源节点检查是否发出CP消息(若没有则检查Lock或者JVM状态)
        4.关注作业积压的节点任务
        5.在每一个子任务节点需要关注CP对齐的速度，那这样就可以追溯到是哪个inputChannel barrier慢或者是为什么没有发送barrier
        6.在节点CP的时候同步执行
        