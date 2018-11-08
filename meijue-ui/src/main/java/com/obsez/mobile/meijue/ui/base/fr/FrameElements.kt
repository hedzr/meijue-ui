package com.obsez.mobile.meijue.ui.base.fr


/**
 *
 * https://stackoverflow.com/questions/49249802/substitute-toolbar-with-collapsing-toolbar-in-different-fragment
 * https://stackoverflow.com/questions/32183381/collapsing-toolbar-layout-using-same-toolbar-on-differents-fragments-depending-o
 *
 */
interface FrameElements {
    val drawerLayoutUi: androidx.drawerlayout.widget.DrawerLayout?
    val navDrawerUi: com.google.android.material.navigation.NavigationView?
    val actionBarDrawerToggleUi: androidx.appcompat.app.ActionBarDrawerToggle?
    
    val toolbarUi: androidx.appcompat.widget.Toolbar?
    val collapsingToolbarLayoutUi: com.google.android.material.appbar.CollapsingToolbarLayout?
    val tabLayoutUi: com.google.android.material.tabs.TabLayout?
    val viewPagerUi: androidx.viewpager.widget.ViewPager?
    
    val bottomNavigationViewUi: com.google.android.material.bottomnavigation.BottomNavigationView?
    val fabUi: com.google.android.material.floatingactionbutton.FloatingActionButton?
}


