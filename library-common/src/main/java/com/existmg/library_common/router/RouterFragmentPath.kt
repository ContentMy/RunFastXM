package com.existmg.library_common.router

/**
 * @Author ContentMy
 * @Date 2024/4/7 12:39 上午
 * @Description 这里是阿里ARouter的路由地址类，统一封装了各个模块入口的地址
 */
class RouterFragmentPath {
    /** 首页组件（已废弃） */
    object Home {
        private const val HOME = "/home"

        /** 首页 （已废弃）*/
        const val PAGER_HOME = "$HOME/Home"
    }

    /** 提醒倒计时组件  */
    object Remind {
        private const val REMIND = "/remind"

        /** 提醒倒计时  */
        const val PAGER_REMIND = "$REMIND/Remind"
    }

    /** 目标组件  */
    object Target {
        private const val TARGET = "/target"

        /** 目标页  */
        const val PAGER_TARGET = "$TARGET/Target"
    }

    /** 记录生活组件  */
    object Memorandum {
        private const val MEMORANDUM = "/memorandum"

        /** 记录生活页面  */
        const val PAGER_MEMORANDUM = "$MEMORANDUM/Memorandum"
    }
    /** 个人设置组件 */
    object User {
        private const val USER = "/user"

        /** 个人设置页面  */
        const val PAGER_USER = "$USER/User"
        const val OPTIMIZATION_USER = "$USER/User/Optimization"
    }

}