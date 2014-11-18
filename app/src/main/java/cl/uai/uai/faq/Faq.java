package cl.uai.uai.faq;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import cl.uai.uai.R;
import cl.uai.uai.main.BaseFragment;

/**
 * Created by nicolaslopezj on 23-10-14.
 */
public class Faq extends BaseFragment {

    protected WebView webView;
    protected String url = "http://intranet.uai.cl/mbl_app_vws/faq.html";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View layout = inflater.inflate(R.layout.webview_main, null);

        webView = (WebView) layout.findViewById(R.id.webView);
        webView.loadUrl(url);
        webView.getSettings().setJavaScriptEnabled(true);
        webView.getSettings().setJavaScriptCanOpenWindowsAutomatically(true);
        webView.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url);
                return true;
            }
        });

        return layout;

    }

}
