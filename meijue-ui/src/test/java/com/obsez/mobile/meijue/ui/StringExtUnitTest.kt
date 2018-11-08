package com.obsez.mobile.meijue.ui

import com.obsez.mobile.meijue.ui.ext.ptn
import org.junit.Test

class StringExtUnitTest {
    
    @Test
    fun test1() {
        
        val s = ("{actor} pushed to {repo} at {branch}".ptn(hashMapOf(
            "actor" to "1111",
            "repo" to "2222",
            "branch" to "3333"
        )))
        
        println("RES: $s")
        
    }
}
