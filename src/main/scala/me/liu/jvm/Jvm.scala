package me.liu.jvm

/**
  *
  *
  * Created by tongwei on 17/6/27.
  */
class Jvm {


}


/**
  * 统一对象模型
  */
class UniformObjectModel {

  //1.对象使用直接指针  当内存重新分配是，由垃圾回收器负责查找和更新对象地址。
  //2.对象使用双机器字长，与class vm相比，能少占用8%的。第一个字长包含了身份hash和gc的状态信息；第二个是指向class的引用；只用array有三个头，第三个用来保存array的size
  //3.类、方法和其他反射数据被表示成object在heap上。
  //4.本地线程支持，包含抢占和并行。每个线程的方法栈使用本机的栈和线程模型；java方法和本地方法使用相同的栈空间；Java线程的抢占式实现是依托于本地操作系统的调度策略。

}