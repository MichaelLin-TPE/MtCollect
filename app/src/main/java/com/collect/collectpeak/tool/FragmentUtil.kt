package com.collect.collectpeak.tool

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction.TRANSIT_FRAGMENT_FADE
import com.collect.collectpeak.R

class FragmentUtil {


    companion object {

        const val ANIM_LEFT_RIGHT = 1
        const val ANIM_TOP_DOWN = 2
        const val ANIM_RIGHT_LEFT = 3

        fun replace(
            container: Int,
            fragmentManager: FragmentManager,
            fragment: Fragment,
            isNeedBack: Boolean,
            tag: String,
            anim: Int
        ) {

            val ft = fragmentManager.beginTransaction()
            val f = fragmentManager.findFragmentByTag(fragment.javaClass.simpleName)

            when (anim) {
                ANIM_LEFT_RIGHT -> {
                    ft.setCustomAnimations(R.anim.slide_in_right,R.anim.slide_out_left,R.anim.slide_in_left,R.anim.slide_out_right)
                }
                ANIM_TOP_DOWN -> {
                    ft.setCustomAnimations(R.anim.bottom_in,0,0,R.anim.bottom_out)
                }
                else -> {
                    ft.setCustomAnimations(R.anim.slide_in_left,R.anim.slide_out_right,R.anim.slide_in_right,R.anim.slide_out_left)
                }
            }

            if (f != null){
                ft.remove(f)
            }
            ft.replace(container,fragment)
            if (isNeedBack){
                ft.addToBackStack(tag)
            }
            ft.setTransition(TRANSIT_FRAGMENT_FADE)
            ft.commitAllowingStateLoss()
        }

        fun popBackStackTo(supportFragmentManager: FragmentManager, simpleName: String) {
            supportFragmentManager.popBackStackImmediate(simpleName,0)
        }


    }

}