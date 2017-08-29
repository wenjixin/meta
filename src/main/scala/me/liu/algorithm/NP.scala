package me.liu.algorithm

/**
  *
  * P：算起来很快的问题   多项式时间内解决的问题
  * NP：算起来不一定快，但对于任何答案我们都可以快速的验证这个答案对不对      能在多项式时间验证答案正确与否的问题
  * NP-hard：比所有的NP问题都难的问题
  * 如果我可以把问题A中的一个实例转化为问题B中的一个实例，然后通过解决问题B间接解决问题A，那么就认为B比A更难。  转化问题的过程必须是多项式时间内可计算的。
  * 未必可以在多项式的时间内验证一个解的正确性。因此即使NP完全问题有多项式时间内的解（若P=NP），NP困难问题依然可能没有多项式时间内的解。
  * NP-complete：满足两点：
  *   1. 是NP hard的问题
  *   2. 是NP问题
  *
  * 3SAT 问题
  *
  * https://en.wikipedia.org/wiki/List_of_NP-complete_problems
  * Created by tongwei on 17/6/13.
  */
class NP {

}
