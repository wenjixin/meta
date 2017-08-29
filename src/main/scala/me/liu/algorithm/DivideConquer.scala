package me.liu.algorithm

/**
  *
  *
  *
  *
  *
  * Created by tongwei on 17/6/10.
  */
class DivideConquer {

  /**
    * 分治法问题的抽象，使用递归方式
    *
    * @param problem
    * @return
    */
  def solve(problem: Problem): Result = {

    //规模已经容易计算，不需要分解
    if (problem.isEasyCompute()) {
      return problem.solve()
    }

    //分解任务
    val problems = problem.divide()

    //子任务计算与合并
    problems.map((p) => solve(p)).reduce((a, b) => Result.merge(a, b))
  }


}

class Problem {
  def isEasyCompute(): Boolean = {
    return true
  }

  def solve(): Result = {
    new Result
  }

  def divide(): Array[Problem] = {
    return new Array[Problem](0)
  }
}

class Result {}

object Result {
  def merge(a: Result*): Result = {
    new Result
  }

}

object DivideConquer {
  def listExample(): Array[String] = {
    Array(
      "二分查找",
      "大整数乘法",
      "Strassen矩阵乘法",
      "棋盘覆盖",
      "合并排序",
      "快速排序",
      "线性时间选择",
      "最接近点问题",
      "循环赛日程表"
    )
  }
}
