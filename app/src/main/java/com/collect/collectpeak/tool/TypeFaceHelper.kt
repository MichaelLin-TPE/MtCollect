package com.collect.collectpeak.tool

import android.content.Context
import android.graphics.Typeface
import androidx.collection.SimpleArrayMap
class TypeFaceHelper {

    companion object{
        private val TYPEFACE_CACHE = SimpleArrayMap<String, Typeface>()

        fun get(context: Context, name: String) : Typeface{

            synchronized(TYPEFACE_CACHE){

                if (!TYPEFACE_CACHE.containsKey(name)){
                    try {
                        val t = Typeface.createFromAsset(context.assets, name)
                        TYPEFACE_CACHE.put(name, t)
                    }catch (e: Exception){
                        e.printStackTrace()
                    }
                }
                return TYPEFACE_CACHE.get(name)!!
            }


        }

    }

}