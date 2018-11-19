package com.obsez.mobile.meijue.ui.base.fr


/**
 * 辅助性的生命周期控制
 *
 * Fragment lifecycle during Fragment transaction：https://androidlearnersite.wordpress.com/2017/02/27/fragment-lifecycle-during-fragment-transaction/
 * FragmentTransaction与Fragment lifecycle：https://www.jianshu.com/p/b588725dcc38
 * 片段：https://developer.android.com/guide/components/fragments?hl=zh-cn
 * [整] Android Fragment 生命周期图：http://www.cnblogs.com/purediy/p/3276545.html
 *
 *
 */
interface Entering {
    
    /**
     * 提供一个机会，每个fragment被接入Activity中时，可以控制其框架元素
     */
    fun onAttachToActivity(frameElements: FrameElements)
    
    /**
     *
     */
    fun doSetFrameElements(frameElements: FrameElements)
    
    fun onConnectToTabs(frameElements: FrameElements)
    fun onDisconnectFromTabs(frameElements: FrameElements)
    
    /**
     * 指示 Owner Activity 应该将 Home 按钮显示为 后退还是汉堡包
     */
    val navigateBack: Boolean
    /**
     * 指示 Owner Activity 应该将 Home 按钮显示为 白色还是黑色
     */
    val navigateIconWhite: Boolean
    /**
     * 使用 Owner Activity 的 Tabs 还是Fragment自己定义的tabs
     */
    val useParentTabs: Boolean
    
}
