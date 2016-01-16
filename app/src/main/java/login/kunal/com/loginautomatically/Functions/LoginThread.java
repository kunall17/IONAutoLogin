package login.kunal.com.loginautomatically.Functions;

import android.content.Context;
import android.net.Network;
import android.os.AsyncTask;
import android.util.Log;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

import com.squareup.okhttp.Connection;

import org.w3c.dom.Document;
import org.xml.sax.InputSource;

import java.io.StringReader;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpression;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;

import login.kunal.com.loginautomatically.MainActivity;
import login.kunal.com.loginautomatically.R;

/**
 * Created by kunall17 on 12/21/15.
 */
public class LoginThread {
    private static final String URL_CLIENTLOGIN = "http://172.16.16.16/24online/webpages/clientlogin.jsp";
    private static final String URL_AFTERLOGIN = "http://172.16.16.16/24online/servlet/E24onlineHTTPClient";
    Context context;
    WebView webView;
    differentFunctions dfListener;
    Logger log;
    private String savedPassword;
    private String savedUsername;


    public void attemptToLogin() {

        dfListener.updateStatus(context.getString(R.string.status_login));
        webView.loadUrl(URL_CLIENTLOGIN);
    }

    public void changeCred(String username, String password) {
        this.savedUsername = username;
        this.savedPassword = password;
    }

    public LoginThread(Context cont, String Username, String Password, differentFunctions listener) {
        {
            this.dfListener = listener;
            this.context = cont;
            this.log = Logger.getInstance();
            this.savedPassword = Password;
            this.savedUsername = Username;

            webView = new WebView(context);
            webView.getSettings().setBlockNetworkImage(true);
            webView.getSettings().setLoadsImagesAutomatically(false);
            webView.setWebChromeClient(new WebChromeClient() {
                public void onProgressChanged(WebView view, int progress) {
                    // Activities and WebViews measure progress with different scales.
                    // The progress meter will automatically disappear when we reach 100%
//                activity.setProgress(progress * 1000);
                }
            });

            webView.getSettings().setSupportMultipleWindows(false);
            webView.getSettings().setJavaScriptCanOpenWindowsAutomatically(false);
            webView.setWebViewClient(new WebViewClient() {
                public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
                    Toast.makeText(context, "Oh no! " + description, Toast.LENGTH_SHORT).show();
                }


                @Override
                public void onPageFinished(WebView view, String url) {
                    Log.d("urlOnPageFinished", url);
                    if (url.equals(URL_CLIENTLOGIN)) {
                        //TODO Search for same user different IP and all And wrong username passwords


                        log.addToLog("tried to login " + savedUsername + "-" + savedPassword);
                        webView.loadUrl("javascript:document.getElementsByName(\"username\")[0].setAttribute('value','" + savedUsername + "'); document.getElementsByName(\"password\")[0].setAttribute('value','" + savedPassword + "'); document.getElementsByName(\"login\")[0].click();");
                        log.addToLog("Login Attempted");

                        dfListener.updateStatus(context.getString(R.string.status_idle));
                    }

                    if (url.equals(URL_AFTERLOGIN)) {
                        Log.d("we got here", "zxzc");
                        dfListener.LoginDone(LoginConstants.LOGIN_DONE);
                    }
                }
            });
            webView.getSettings().setCacheMode(WebSettings.LOAD_NO_CACHE);
            webView.getSettings().setAppCacheEnabled(false);
            webView.clearCache(true);
            webView.getSettings().setAppCacheEnabled(false);
            webView.getSettings().setCacheMode(WebSettings.LOAD_NO_CACHE);
            webView.getSettings().setJavaScriptEnabled(true);
            webView.getSettings().setJavaScriptCanOpenWindowsAutomatically(true);

        }

    }
}