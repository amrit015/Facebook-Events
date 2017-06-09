package com.amrit.facebook_events

import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.app.AppCompatActivity

class MainActivity : AppCompatActivity() {
    private val TAG = "MainActivity"
//    var hashkey = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        //          for hashKey
//                var key: String? = printHashKey(applicationContext)
//                Log.i(TAG, "printHashKey() Hash Key: " + key)

        val fm = supportFragmentManager
        var fragment: Fragment? = fm.findFragmentById(R.id.fragment_container)

        if (fragment == null) {
            fragment = FacebookFragment()
            fm.beginTransaction()
                    .add(R.id.fragment_container, fragment)
                    .commit()
        }

    }

    /*
    fun printHashKey(pContext: Context?): String? {
        try {
            val packageName: String? = pContext?.getPackageName();
            val info = packageManager.getPackageInfo(packageName, PackageManager.GET_SIGNATURES)
            for (signature in info.signatures) {
                val md = MessageDigest.getInstance("SHA")
                md.update(signature.toByteArray())
                hashkey = String(Base64.encode(md.digest(), 0))
            }
        } catch (e: NoSuchAlgorithmException) {
            Log.e(TAG, "printHashKey()", e)
        } catch (e: Exception) {
            Log.e(TAG, "printHashKey()", e)
        }
        return hashkey;
    }
    */
}
