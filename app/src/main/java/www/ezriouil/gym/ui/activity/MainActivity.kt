package www.ezriouil.gym.ui.activity

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import www.ezriouil.gym.R
import www.ezriouil.gym.databinding.ActivityMainBinding
import www.ezriouil.gym.local.model.sale_notification
import www.ezriouil.gym.ui.fragment.Home
import www.ezriouil.gym.ui.fragment.Notification
import www.ezriouil.gym.ui.fragment.User

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        binding = ActivityMainBinding.inflate(layoutInflater)
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        binding.bottomNavigationView.setItemSelected(R.id.home_nav, true)
        fragmentReplace(Home())
        binding.bottomNavigationView.setOnItemSelectedListener {
            when (it) {
                R.id.home_nav -> {
                    fragmentReplace(Home())
                    if (sale_notification.isNotEmpty()) badge()
                }
                R.id.user_nav -> {
                    fragmentReplace(User())
                    if (sale_notification.isNotEmpty()) badge()
                }
                R.id.notification_nav -> {
                    fragmentReplace(Notification())
                    clearBadge()
                }
            }
        }
    }

    private fun fragmentReplace(fragment: Fragment) {
        val supportFragment = supportFragmentManager.beginTransaction()
        supportFragment.replace(R.id.frame_layout, fragment)
        supportFragment.commitNow()
    }

    private fun badge() {
        binding.bottomNavigationView.showBadge(R.id.notification_nav, sale_notification.size)
    }

    private fun clearBadge() {
        binding.bottomNavigationView.dismissBadge(R.id.notification_nav)
    }
}