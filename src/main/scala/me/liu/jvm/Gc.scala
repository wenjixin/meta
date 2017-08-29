package me.liu.jvm

/**
  *
  * http://www.oracle.com/technetwork/java/javase/memorymanagement-whitepaper-150215.pdf
  *
  * Created by tongwei on 17/6/27.
  */
class Gc {

  //GC的需求：1.pause time 2.吞吐量
  //HotSpot gc : 1.保证所有无法访问的对象被回收 2.所有对象都能重新分配，允许对象压缩。


  //分代复制收集   新 老复制 ，回收新
  //Parallel Young Generation Collector
  //Mark-Compact Old Object Collector
  //Mostly Concurrent Mark-Sweep Collector
  //Parallel Old Generation Collector

}

object Gc {

}
