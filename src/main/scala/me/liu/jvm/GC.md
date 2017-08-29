# 内存模型

##内存模型

1. Permanent Generation
   应用的元数据，类和方法的描述信息。Java SE的库的方法和类。full gc时会回收
  
2. Method Area
   PG的一部分，保存类的结构（常量和静态变量）
   
3. Memory Pool
   String Pool;可以归属于Heap或者pg

4. Runtime Constant Pool
   属于MA，包括常量和静态方法
   
5. Java Stack Memory
    方法相关值以及方法引用对象的引用
    
    
VM SWITCH|	VM SWITCH DESCRIPTION
| ------------- |:-------------:|
-Xms	|For setting the initial heap size when JVM starts
-Xmx	|For setting the maximum heap size.
-Xmn	|For setting the size of the Young Generation, rest of the space goes for Old Generation.
-XX:PermGen	|For setting the initial size of the Permanent Generation memory
-XX:MaxPermGen	|For setting the maximum size of Perm Gen
-XX:SurvivorRatio	|For providing ratio of Eden space and Survivor Space, for example if Young Generation size is 10m and VM switch is -XX:SurvivorRatio=2 then 5m will be reserved for Eden Space and 2.5m each for both the Survivor spaces. The default value is 8.
-XX:NewRatio	|新生代 ( Young ) 与老年代 ( Old ) 的比例的值为 1:2. The default value is 2.

## 垃圾收集问题解决
    
1. 回收哪些

  * 引用计数法
  * 可达性分析  现在常用

2. 如何回收（方法论）

   * 标记-清除   Mark-Sweep
   
       - 标记  对所有可达节点，设置为true
       ```
           Mark(root)
              If markedBit(root) = false then
                  markedBit(root) = true
                  For each v referenced by root
                       Mark(v)
        ```
       - 清除  
       ```
           Sweep()
           For each object p in heap
               If markedBit(p) = true then
                   markedBit(p) = false
               else
                   heap.release(p)
       ```
       - 优势
            + 没有额外占据空间
            + 能够处理循环依赖
       - 劣势
            + 效率
            + 内存碎片产生
   * 复制
        
            分两个区，一个区满了，标记出活着的对象，copy到另一个区，然后回收调第一个区；即S0 S1
   
   * 标志-整理   Mark-Compact
     - 标记 第一步相同
     - 整理 
            
            移动所有存活的对象，且按照内存地址次序依次排列，然后将末端内存地址以后的内存全部回收。
   
   * 分代收集算法
        
        对象分类：
        1. 朝生夕死
            新生代实行copy方法，survive代数》n到到老年代
        2. 长寿
            老年代后进行标记-整理或者清除？
        3. 近乎永生
              Perm 标记-整理或者清除？
   
   * 比较
   
   指标|mark-sweep	|mark-compact	|copying
   | ------------- |:-------------:|:-------------:|:-------------:|
   速度	|中等	|最慢	|最快
   空间开销 |少（但会堆积碎片）|少（不堆积碎片）	|通常需要活对象的2倍大小（不堆积碎片）
   移动对象？	|否	|是	|是
   
3. 具体实现

    https://blogs.oracle.com/jonthecollector/our-collectors
    
    "Serial" is a stop-the-world, copying collector which uses a single GC thread.
    
    "ParNew" is a stop-the-world, copying collector which uses multiple GC threads. It differs
    from "Parallel Scavenge" in that it has enhancements that make it usable 
    with CMS. For example, "ParNew" does the
    synchronization needed so that it can run during the 
    concurrent phases of CMS.
    
    "Parallel Scavenge" is a stop-the-world, copying collector 
    which uses multiple GC threads.
    
    "Serial Old" is a stop-the-world,
    mark-sweep-compact collector that uses a single GC thread.
    
    "CMS" is a mostly concurrent, low-pause collector.
    
    "Parallel Old" is a compacting collector that uses multiple GC threads.
    Using the -XX flags for our collectors for jdk6
    
    UseSerialGC is "Serial" + "Serial Old"
    
    UseParNewGC is "ParNew" + "Serial Old"
    
    UseConcMarkSweepGC is "ParNew" + "CMS" + "Serial Old".
     "CMS" is used most of the time to collect the tenured generation. 
     "Serial Old" is used when a concurrent mode failure occurs.
    
    UseParallelGC is "Parallel Scavenge" + "Serial Old"
    
    UseParallelOldGC is "Parallel Scavenge" + "Parallel Old"

    G1 gc 
    像CMS收集器一样，能与应用程序线程并发执行。
    整理空闲空间更快。
    需要GC停顿时间更好预测。
    不希望牺牲大量的吞吐性能。
    不需要更大的Java Heap。
    G1是一个有整理内存过程的垃圾收集器，不会产生很多内存碎片。
    G1的Stop The World(STW)更可控，G1在停顿时间上添加了预测机制，用户可以指定期望停顿时间。

    http://blog.csdn.net/renfufei/article/details/41897113
    http://www.oracle.com/webfolder/technetwork/tutorials/obe/java/G1GettingStarted/index.html
    
    * cms实现
        
        阶段	|说明
        | ----- |:-----:|
        (1) 初始标记 (Initial Mark)	|(Stop the World Event,所有应用线程暂停) 在老年代(old generation)中的对象, 如果从年轻代(young generation)中能访问到, 则被 “标记,marked” 为可达的(reachable).对象在旧一代“标志”可以包括这些对象可能可以从年轻一代。暂停时间一般持续时间较短,相对小的收集暂停时间.
        (2) 并发标记 (Concurrent Marking)	 |在Java应用程序线程运行的同时遍历老年代(tenured generation)的可达对象图。扫描从被标记的对象开始,直到遍历完从root可达的所有对象. 调整器(mutators)在并发阶段的2、3、5阶段执行,在这些阶段中新分配的所有对象(包括被提升的对象)都立刻标记为存活状态.
        (3) 再次标记(Remark)	|(Stop the World Event, 所有应用线程暂停) 查找在并发标记阶段漏过的对象，这些对象是在并发收集器完成对象跟踪之后由应用线程更新的.
        (4) 并发清理(Concurrent Sweep) |回收在标记阶段(marking phases)确定为不可及的对象. 死对象的回收将此对象占用的空间增加到一个空闲列表(free list),供以后的分配使用。死对象的合并可能在此时发生. 请注意,存活的对象并没有被移动.
        (5) 重置(Resetting) |清理数据结构,为下一个并发收集做准备.

        阶段1和3会stop the world
        
    * G1实现
        
        * young gc
        
            堆一整块内存空间,被分为多个heap区(regions).
            
            年轻代内存由一组不连续的heap区组成. 这使得在需要时很容易进行容量调整.
            
            年轻代的垃圾收集,或者叫 young GCs, 会有 stop the world 事件. 在操作时所有的应用程序线程都会被暂停(stopped).
            
            年轻代 GC 通过多线程并行进行.
            
            存活的对象被拷贝到新的 survivor 区或者老年代.
            
        * old gc
        
        阶段	|说明
        | ---- | :-----: |
        (1) 初始标记(Initial Mark)	|(Stop the World Event,所有应用线程暂停) 此时会有一次 stop the world(STW)暂停事件. 在G1中, 这附加在(piggybacked on)一次正常的年轻代GC. 标记可能有引用指向老年代对象的survivor区(根regions).
        (2) 扫描根区域(Root Region Scanning)	|扫描 survivor 区中引用到老年代的引用. 这个阶段应用程序的线程会继续运行. 在年轻代GC可能发生之前此阶段必须完成.
        (3) 并发标记(Concurrent Marking)	|在整个堆中查找活着的对象. 此阶段应用程序的线程正在运行. 此阶段可以被年轻代GC打断(interrupted).
        (4) 再次标记(Remark)	|(Stop the World Event,所有应用线程暂停) 完成堆内存中存活对象的标记. 使用一个叫做 snapshot-at-the-beginning(SATB, 起始快照)的算法, 该算法比CMS所使用的算法要快速的多.
        (5) 清理(Cleanup)	|(Stop the World Event,所有应用线程暂停,并发执行)在存活对象和完全空闲的区域上执行统计(accounting). (Stop the world)擦写 Remembered Sets. (Stop the world)重置空heap区并将他们返还给空闲列表(free list). (Concurrent, 并发)
        (*) 拷贝(Copying)	|(Stop the World Event,所有应用线程暂停) 产生STW事件来转移或拷贝存活的对象到新的未使用的heap区(new unused regions). 只在年轻代发生时日志会记录为 `[GC pause (young)]`. 如果在年轻代和老年代一起执行则会被日志记录为 `[GC Pause (mixed)]`.
