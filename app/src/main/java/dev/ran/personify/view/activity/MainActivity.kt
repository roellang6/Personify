package dev.ran.personify.view.activity

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.widget.FrameLayout
import android.widget.ProgressBar
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import dev.ran.personify.R
import dev.ran.personify.util.ErrorListener
import dev.ran.personify.util.NetworkCheck
import dev.ran.personify.view.fragment.DetailPersonFragment
import dev.ran.personify.view.fragment.NoInternetFragment
import dev.ran.personify.view.fragment.PersonFragment

class MainActivity : AppCompatActivity(), ErrorListener {
    private lateinit var networkCheck: NetworkCheck
    private lateinit var loading : ProgressBar
    private lateinit var frame : FrameLayout
    private var doubleBack: Long = 0
    private val intervals = 2000
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        loading = findViewById(R.id.pbLoad)
        frame = findViewById(R.id.frameId)
        networkCheck = NetworkCheck(this)
        networkCheck.checkNetwork()

        NetworkCheck.networkState.observe(this) { isConnected ->
            if (isConnected) {
                goToFragment(PersonFragment())
            } else {
                onNoInternet()
            }
        }
    }

    override  fun onNoInternet() {
        goToFragment(NoInternetFragment())
    }

    private fun goToFragment(fragment : Fragment){
        Handler(Looper.getMainLooper()).postDelayed({
            loading.isVisible = false
            frame.isVisible = true
            supportFragmentManager.beginTransaction().replace(R.id.frameId, fragment).addToBackStack(null).commit()
        }, 1200)
    }

    @Deprecated("Deprecated in Java")
    override fun onBackPressed() {
        val currentFragment = supportFragmentManager.findFragmentById(R.id.frameId)
        if (currentFragment is DetailPersonFragment) {
            goToFragment(PersonFragment())
        } else {
            if (System.currentTimeMillis() - doubleBack < intervals) {
                super.onBackPressed()
                finishAffinity()
            } else {
                Toast.makeText(applicationContext, "Back again to exit", Toast.LENGTH_SHORT).show()
                doubleBack = System.currentTimeMillis()
            }
        }
    }
}