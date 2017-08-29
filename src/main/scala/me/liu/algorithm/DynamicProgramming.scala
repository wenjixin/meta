package me.liu.algorithm

/**
  * 适用条件：
  * 很多重合子问题情况下的最优解
  *
  * 最优子结构性质：
  * 问题的最优解包含了子问题的最优解（反证法）
  *
  * 递归关系：
  * 如何由子问题的值组合计算问题的值
  *
  * 子问题：
  * 各个子问题可能会产生重复
  *
  * Created by tongwei on 17/6/10.
  */
class DynamicProgramming {

}

object DynamicProgramming {

  /**
    *
    * 矩阵连乘算法递归式
    *
    * m(i)(j)=min{m(i)(k)+m(k+1)(j)+p(i-1)p(k)p(j)}
    *
    * @param i
    * @param matirxs
    * @return
    */
  def matrixMutiple(i: Int, matirxs: Array[Matirx]): Int = {

    val cacheResult = Array.ofDim[Int](i, i)
    for (x <- 0.to(i - 1)) {
      cacheResult(i)(i) = 0;
    }



    return 0
  }


  def main(args: Array[String]): Unit = {

    val matirxs = new Array[Matirx](100)


  }

}


class Matirx(xc: Int, yc: Int) {

  var x = xc;
  var y = yc;
}

object Matirx {
  def cost(a: Matirx, b: Matirx): Int = {
    return a.x * a.y * b.y
  }
}