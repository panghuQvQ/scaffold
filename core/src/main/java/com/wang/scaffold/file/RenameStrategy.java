package com.wang.scaffold.file;

/**
 *
 * 文件重命名策略
 *
 * 函数式接口：所谓的函数式接口，当然首先是一个接口，然后就是在这个接口里面只能有一个抽象方法。
 * @FunctionalInterface：主要用于编译级错误检查，加上该注解，当你写的接口不符合函数式接口定义的时候，编译器会报错
 * 提醒：加不加@FunctionalInterface对于接口是不是函数式接口没有影响，该注解知识提醒编译器去检查该接口是否仅包含一个抽象方法
 *
 * @author zhou wei
 *
 */
@FunctionalInterface
public interface RenameStrategy {

	public String rename(String original);

}
