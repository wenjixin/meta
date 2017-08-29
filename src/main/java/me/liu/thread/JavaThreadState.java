package me.liu.thread;

/**
 * Created by tongwei on 17/8/28.
 */
public class JavaThreadState {


    String NEW = "NEW";

    String RUNNABLE = "RUNNABLE";

    String RUNNING = "RUNNING";

    String BLOCKED = "BLOCKED";

    String BLOCKED_WAIT = "BLOCK_WAIT";

    String BLOCKED_HOLD = "BLOCK_HOLD";

    String DEAD = "DEAD";

    String current = NEW;


    //  NEW --start--> RUNNABLE
    //  RUNNABLE --获得cpu时间片--> RUNNING
    //  RUNNING --yield 让出cpu--> RUNNABLE
    //  RUNNING --wait-->BLOCKED_WAIT
    //  RUNNING -->sync -->BLOCKED_HOLD
    //  RUNNING -->sleep join -->BLOCKED
    //  BLOCKED_WAIT --notify/interrupt -->BLOCKED_HOLD
    //  BLOCKED_HOLD -->get lock --> RUNNABLE
    //  BLOCKED -->sleep join complement -> RUNNABLE
    //  RUNNING  -----> DEAD


    //注意：jdk的解释中，说wait()的作用是让“当前线程”等待，而“当前线程”是指正在cpu上运行的线程！

    //notify(), wait()依赖于“同步锁”，而“同步锁”是对象锁持有，并且每个对象有且仅有一个！这就是为什么notify(), wait()等函数定义在Object类，而不是Thread类中的原因。

    //join() 的作用：让“主线程”等待“子线程”结束之后才能继续运行

    //java 中的线程优先级的范围是1～10，默认的优先级是5。“高优先级线程”会优先于“低优先级线程”执行。

}
