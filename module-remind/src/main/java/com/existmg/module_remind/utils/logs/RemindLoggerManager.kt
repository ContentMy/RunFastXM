package com.existmg.module_remind.utils.logs

/**
 * @Author ContentMy
 * @Date 2024/6/1 5:13 PM
 * @Description 这里是日志类的管理类，Main模块对于日志的调用是从这个入口进行的
 */
object RemindLoggerManager {

    /**
     * @Author: ContentMy
     * @return: RemindLoggerInterface 返回日志接口类型
     * @Description:
     * 这里是通过kotlin的内联以及泛型实化来完成对于调用方法的类名的自动获取
     * 其次返回值是一个接口类型，而真正返回的是接口对应的实现类优势如下：
     * 1.解耦
     *  回接口类型可以使调用代码依赖于抽象而不是具体实现，从而减少耦合。这意味着可以更容易地替换实现而不需要修改调用代码
     * 2.可替换性
     *  使用接口可以轻松地在不同实现之间切换。例如，可以在开发和测试阶段使用不同的日志实现，或者在不同的环境（如debug和release）中使用不同的日志策略
     * 3.单一职责
     *  每个类只做一件事。具体的实现类负责日志的具体实现，而调用代码只依赖于接口，不需要关心具体实现的细节
     * 4.依赖倒置
     *  高层模块不应该依赖低层模块，而是应该依赖于抽象。通过接口，具体实现可以独立变化而不影响依赖它们的代码
     */
    inline fun <reified T> getLogger(): RemindLoggerInterface {
        return RemindLoggerImpl(T::class.java.simpleName)
    }
}