package com.aryan.vault

import android.os.Build
import android.os.Bundle
import android.view.View
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.ProgressBar
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import java.io.UnsupportedEncodingException
import java.net.URLEncoder

class PdfShowAct : AppCompatActivity() {

    var mwb_webView: WebView? = null
    var mprogressBar: ProgressBar? = null
    var pdf:String? = null

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_pdf_show)
        mwb_webView = findViewById<View>(R.id.wb_webView) as WebView
        mprogressBar = findViewById(R.id.progressBar)
        var url: String? = intent.getStringExtra("URL")
        if (url != null) {

            // wb_webView= findViewById(R.id.wb_webView)
            mwb_webView!!.settings.javaScriptEnabled = true
            mwb_webView!!.settings.safeBrowsingEnabled = true

            mwb_webView!!.webViewClient = object : WebViewClient() {
                override fun onPageFinished(view: WebView?, url: String?) {
                    super.onPageFinished(view, url)
                    mprogressBar!!.visibility = View.GONE
                    mwb_webView!!.visibility = View.VISIBLE

                }
            }
            mwb_webView!!.settings.setSupportZoom(true)
           // mwb_webView!!.settings.displayZoomControls.

            try {
                pdf = URLEncoder.encode(url, "UTF-8")
            } catch (e: UnsupportedEncodingException) {
                e.printStackTrace()
            }
          // mwb_webView.loadUrl("http://drive.google.com/viewerng/viewer?embedded=true&url=$url")
            Toast.makeText(this,"$pdf",Toast.LENGTH_LONG).show()
            mwb_webView!!.loadUrl("http://drive.google.com/viewerng/viewer?embedded=true&url=$pdf")

        }
    }
}
/*@RequiresApi(Build.VERSION_CODES.O)
@SuppressLint("SetJavaScriptEnabled")
    private fun webViewSetup() {
    wb_webView= findViewById(R.id.wb_webView)
    wb_webView.webViewClient = WebViewClient()
    val b:Bundle = intent.extras!!
    val url = b.getString("url")!!
        wb_webView.apply{
            url?.let { loadUrl(it) }
            settings.javaScriptEnabled = true
            settings.safeBrowsingEnabled = true
        }


    }
*/