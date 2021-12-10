package com.boss.directchat

import android.content.DialogInterface
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.*
import androidx.appcompat.widget.Toolbar
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.AdView
import com.google.android.gms.ads.MobileAds
import com.hbb20.CountryCodePicker
import hotchemi.android.rate.AppRate


class MainActivity : AppCompatActivity() {
    private lateinit var cpp: CountryCodePicker
    private lateinit var llnumber: LinearLayout
    private lateinit var rlmain: RelativeLayout
    private lateinit var etEnterMobile: EditText
    private lateinit var imgLogo: ImageView
    private lateinit var txtEnterThePhoneNumber: TextView
    private lateinit var btnOpenChat:Button
    private lateinit var toolbar: Toolbar
    lateinit var mAdView : AdView


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        MobileAds.initialize(this) {}

        mAdView = findViewById(R.id.adView)
        val adRequest = AdRequest.Builder().build()
        mAdView.loadAd(adRequest)

        cpp = findViewById(R.id.cpp)
        llnumber=findViewById(R.id.llnumber)
        rlmain=findViewById(R.id.rlmain)
        etEnterMobile=findViewById(R.id.etEnterMobile)
        imgLogo=findViewById(R.id.imgLogo)
        txtEnterThePhoneNumber=findViewById(R.id.txtEnterThePhoneNumber)
        btnOpenChat=findViewById(R.id.btnOpenChat)
        toolbar=findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)
        supportActionBar?.title="Direct Chat For Whatsapp"

        //rating dialog box
        AppRate.with(this)
            .setInstallDays(0)
            .setLaunchTimes(3)
            .setRemindInterval(2)
            .monitor()
        AppRate.showRateDialogIfMeetsConditions(this)

        btnOpenChat.setOnClickListener {
            val phoneNumber =cpp.selectedCountryCode + etEnterMobile.text.toString()
            val url = "https://api.whatsapp.com/send?phone=$phoneNumber"
            try {
                packageManager.getPackageInfo("com.whatsapp", PackageManager.GET_ACTIVITIES)
                val i = Intent(Intent.ACTION_VIEW)
                i.data = Uri.parse(url)
                startActivity(i)
            } catch (e: PackageManager.NameNotFoundException) {
                Toast.makeText(this, "Whatsapp is not installed in your phone.", Toast.LENGTH_SHORT).show()
                e.printStackTrace()
            }
        }
    }
    //Exit dialog box
    override fun onBackPressed() {
        val builder: android.app.AlertDialog.Builder = android.app.AlertDialog.Builder(this@MainActivity)
        builder.setTitle(android.R.string.dialog_alert_title)
        builder.setIcon(android.R.drawable.ic_dialog_alert)
        builder.setMessage("Enjoying this app!!!!.Could you rate 5 star and write a review on Playstore? :)")
            .setCancelable(true)
            .setPositiveButton("Exit",
                DialogInterface.OnClickListener { dialog, id -> finish() })
            .setNegativeButton("Rate",
                DialogInterface.OnClickListener { dialog, id -> dialog.cancel()
                    val i= Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=com.boss.directchat"))
                    startActivity(i)
                })

        val alert: android.app.AlertDialog? = builder.create()
        alert?.show()
    }

    //Menu items
    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu to use in the action bar
        val inflater = menuInflater
        inflater.inflate(R.menu.menu, menu)
        return super.onCreateOptionsMenu(menu)
    }
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle presses on the action bar menu items
        when (item.itemId)  {
            R.id.shareApp -> {
                val sendIntent: Intent = Intent().apply {
                    action = Intent.ACTION_SEND
                    putExtra(Intent.EXTRA_TEXT, "Hey my friend(s) check out this amazing application. You can directly chat with any number without saving contact. https://play.google.com/store/apps/details?id=com.boss.directchat")
                    type = "text/plain"
                }

                val shareIntent = Intent.createChooser(sendIntent, null)
                startActivity(shareIntent)
                return true
            }
            R.id.rating -> {
                val i=Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=com.boss.directchat"))
                startActivity(i)
                return true
            }
        }
        return super.onOptionsItemSelected(item)
        }
}
