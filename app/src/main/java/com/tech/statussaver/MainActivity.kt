package com.tech.statussaver

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.karumi.dexter.Dexter
import com.karumi.dexter.MultiplePermissionsReport
import com.karumi.dexter.PermissionToken
import com.karumi.dexter.listener.PermissionRequest
import com.karumi.dexter.listener.multi.MultiplePermissionsListener
import com.tech.statussaver.databinding.ActivityMainBinding


class MainActivity : AppCompatActivity() {

    private lateinit var binding:ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(findViewById(R.id.toolbar))
        checkPermission()

        binding.sharechatRL.setOnClickListener {
            startActivity(Intent(this,ShareChatActivity::class.java))
        }
        binding.whatsappRL.setOnClickListener {
            startActivity(Intent(this,WhatsappActivity::class.java))
        }
//        binding.facebookRL.setOnClickListener {
//            startActivity(Intent(this,FacebookActivity::class.java))
//        }
        binding.youtubeRL.setOnClickListener {
            startActivity(Intent(this,YoutubeActivity::class.java))
        }
        binding.instaRL.setOnClickListener {
            startActivity(Intent(this,InstagramActivity::class.java))
        }
        binding.shareRL.setOnClickListener {
            val sendIntent = Intent()
            sendIntent.action = Intent.ACTION_SEND
            sendIntent.putExtra(
                Intent.EXTRA_TEXT, "Best Video & Images downloader App " +
                        "https://play.google.com/store/apps/details?id=" + this.packageName
            )
            sendIntent.type = "text/plain"

            val shareIntent = Intent.createChooser(sendIntent, null)
            startActivity(shareIntent)
        }
        binding.rateRL.setOnClickListener {
            startActivity(Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id="+this.packageName)))
        }

    }
    fun checkPermission(){
        Dexter.withContext(this)
            .withPermissions(
                android.Manifest.permission.READ_EXTERNAL_STORAGE,
                android.Manifest.permission.WRITE_EXTERNAL_STORAGE
            ).withListener(object : MultiplePermissionsListener {
                override fun onPermissionsChecked(report: MultiplePermissionsReport) {
                    if(!report.areAllPermissionsGranted())
                        checkPermission()
                }

                override fun onPermissionRationaleShouldBeShown(
                    permissions: List<PermissionRequest?>?,
                    token: PermissionToken?
                ) { /* ... */
                }
            }).check()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu,menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId){
            R.id.about -> {
                startActivity(Intent(this,AboutActivity::class.java))
            }
            R.id.privacy -> Toast.makeText(this,"Privacy Selected",Toast.LENGTH_SHORT).show()
        }
        return super.onOptionsItemSelected(item)
    }

    @Deprecated("Deprecated in Java")
    override fun onBackPressed() {
        val a = Intent(Intent.ACTION_MAIN)
        a.addCategory(Intent.CATEGORY_HOME)
        a.flags = Intent.FLAG_ACTIVITY_NEW_TASK
        startActivity(a)
    }
}