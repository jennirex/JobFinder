package jennirex.com.jobfinder;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.res.Resources;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

import jennirex.com.jobfinder.Helpers.ProgressDialogHelper;

public class WebActivity extends AppCompatActivity {
    WebView webView;
    ProgressDialogHelper progressDialogHelper = ProgressDialogHelper.getInstance();
    Resources resources;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web);

        webView = (WebView) findViewById(R.id.webView);
        resources = getResources();

        Intent intent = getIntent();
        String url = intent.getStringExtra("job_url");

        webView  = new WebView(this);

        progressDialogHelper.showProgressDialog(this,resources.getString(R.string.please_wait),false);

        webView.getSettings().setJavaScriptEnabled(true); // enable javascript

        final Activity activity = this;

        webView.setWebViewClient(new WebViewClient() {
            @SuppressWarnings("deprecation")
            @Override
            public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
                Toast.makeText(activity, description, Toast.LENGTH_SHORT).show();
            }
            @TargetApi(android.os.Build.VERSION_CODES.M)
            @Override
            public void onReceivedError(WebView view, WebResourceRequest req, WebResourceError rerr) {
                // Redirect to deprecated method, so you can use it in all SDK versions
                onReceivedError(view, rerr.getErrorCode(), rerr.getDescription().toString(), req.getUrl().toString());
            }

            @Override
            public void onPageFinished(WebView view, String url) {
               progressDialogHelper.hideProgressDialog();
            }
        });

        webView.loadUrl(url);
        setContentView(webView);

    }

}
