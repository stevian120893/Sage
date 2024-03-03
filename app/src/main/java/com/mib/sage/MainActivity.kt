package com.mib.sage

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.fragment.app.FragmentActivity
import androidx.navigation.fragment.NavHostFragment
import com.mib.lib_auth.repository.SessionRepository
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : FragmentActivity() {

    @Inject
    lateinit var sessionRepository: SessionRepository

    private val requestPermissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestPermission(),
    ) { isGranted: Boolean ->
        if (isGranted) {
            // FCM SDK (and your app) can post notifications.
        } else {
            // TODO: Inform user that that your app will not show notifications.
        }
    }

    private fun askNotificationPermission() {
        // This is only necessary for API level >= 33 (TIRAMISU)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.POST_NOTIFICATIONS) ==
                PackageManager.PERMISSION_GRANTED
            ) {
                // FCM SDK (and your app) can post notifications.
            } else if (shouldShowRequestPermissionRationale(Manifest.permission.POST_NOTIFICATIONS)) {
                // TODO: display an educational UI explaining to the user the features that will be enabled
                //       by them granting the POST_NOTIFICATION permission. This UI should provide the user
                //       "OK" and "No thanks" buttons. If the user selects "OK," directly request the permission.
                //       If the user selects "No thanks," allow the user to continue without notifications.
            } else {
                // Directly ask for the permission
                requestPermissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        // Handle the splash screen transition (Android 12)
        installSplashScreen()

        super.onCreate(savedInstanceState)
        window.statusBarColor = ContextCompat.getColor(this, R.color.colorPrimaryDark)
        setContentView(R.layout.activity_main)
        setupNavGraph()
        askNotificationPermission()
    }

    override fun onNewIntent(intent: Intent?) {
        super.onNewIntent(intent)
//        if (intent != null) {
//            val deeplinkResult = deeplinkDelegate.dispatchFrom(this, intent)
//            intent.data?.let { adjustTracker.trackDeeplink(it, this) }
//            if (!deeplinkResult.isSuccessful) {
//                intent.data?.toString()?.let {
//                    browserNavigation.launchBrowser(this, it, false)
//                }
//            }
//        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        // to check the status of in app update result. when the result is cancelled
        // and update type is force update, then should finish the activity
//        if (requestCode == AppUpdate.UPDATE_REQUEST_CODE) {
            val isCancelled = resultCode != Activity.RESULT_OK
//            val isForceUpdate = appUpdatePref.lastUpdateType == AppUpdate.TYPE_FORCE
//            if (isCancelled && isForceUpdate) {
//                finish()
//            }
//        }
    }

    private fun setupNavGraph() {
        val navHostFragment = supportFragmentManager.findFragmentById(R.id.fragmentContainerView)
                as NavHostFragment
        val navInflater = navHostFragment.navController.navInflater
        val navGraph = navInflater.inflate(R.navigation.nav_graph)

        val isLoggedIn = !sessionRepository.getAccessToken().isNullOrBlank()
//        val skipValueProposition = !homePref.shouldShowValueProposition
        navGraph.startDestination = if (isLoggedIn) {
            R.id.homeFragment
        } else {
            R.id.loginFragment
        }

        navHostFragment.navController.graph = navGraph
    }

    public override fun onResume() {
        super.onResume()
    }
    public override fun onPause() {
        super.onPause()
    }
}
