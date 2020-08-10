package com.example.sabyaarimagerecognition;

import android.content.Context;
import android.os.Bundle;
//import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.MediaController;
import android.widget.Toast;
import android.widget.VideoView;


import androidx.annotation.Nullable;

import com.google.ar.core.AugmentedImage;
import com.google.ar.core.Config;
import com.google.ar.core.Session;
import com.google.ar.sceneform.AnchorNode;
import com.google.ar.sceneform.Node;
import com.google.ar.sceneform.math.Vector3;
import com.google.ar.sceneform.rendering.ViewRenderable;
import com.google.ar.sceneform.rendering.ViewSizer;
import com.google.ar.sceneform.ux.ArFragment;
import com.google.ar.sceneform.math.Quaternion;

import java.util.concurrent.CompletableFuture;

public class AugmentedWebViewNode extends AnchorNode {
    private static final String TAG = "AugmentedImageNode";
    private Node webViewNode;
    private static CompletableFuture<ViewRenderable> webViewRenderable;
    // Added on 11 May 2020 for Container Crush {
    public String selection = "2";
    public String videoPath = null;
    // Added on 11 May 2020 for Container Crush }

    public AugmentedWebViewNode (Context context) {
        if (webViewRenderable == null)
            webViewRenderable = ViewRenderable.builder().setView(context, R.layout.web_view).build();
    }
    public void setImage(AugmentedImage image, String n) {
        ViewRenderable modelRenderable;
        selection = n;
  //      String vhtml="<!DOCTYPE html><html><body><iframe width=\"300\" height=\"315\"src=\"https://sabyas3firstbucket.s3.amazonaws.com/VID-20190304-WA0007.mp4\"></iframe></body></html>";
     //   MediaController MediaC;
        setAnchor(image.createAnchor(image.getCenterPose()));
        if (!webViewRenderable.isDone()) {
            CompletableFuture.allOf(webViewRenderable)
                    .thenAccept((Void aVoid) -> setImage(image, n))
                    .exceptionally(
                            throwable -> {
                                Log.e(TAG, "Exception Loading", throwable);
                                return null;
                            });
        } else {
        }
        webViewNode = new Node();
        webViewNode.setParent(this);
        webViewNode.setLocalPosition(new Vector3(0.0f,0.0f, 0.0f));
        webViewNode.setRenderable(webViewRenderable.getNow(null));
        webViewNode.setLocalRotation(new Quaternion(90f,0f,0f,-91f));


        if (webViewRenderable.isDone()) {
            modelRenderable = webViewRenderable.getNow(null);
            modelRenderable.setSizer(new ViewSizer() {
                @Override
                public Vector3 getSize(View view) {
                   // return new Vector3(0.25f, 0.35f, 0.0f);
                    return new Vector3(0.5f, 0.7f, 0.0f);
                }
            });
           //WebView webView = (WebView) modelRenderable.getView().findViewById(R.id.webView);
           VideoView webView = (VideoView) modelRenderable.getView().findViewById(R.id.webView);
          //  webView.setWebViewClient(new WebViewClient() {
            //   @Override
              //  public boolean shouldOverrideUrlLoading(WebView view, String url) {
              //      view.loadUrl(url);
              //     return false;
          //      }
          //  });
          //  WebSettings webSettings = webView.getSettings();
          //  webView.setWebChromeClient(new WebChromeClient());
          //  webSettings.setJavaScriptEnabled(true);
           // webView.getSettings().setPluginState(WebSettings.PluginState.ON);

         //   webView.loadUrl("file:///C:/Sabya_AR_Image_Recognition/videohtml.html");
          //  webView.loadData(vhtml, "text/html", "utf-8");
          //  webView.loadUrl("https://sabyas3firstbucket.s3.amazonaws.com/VID-20190304-WA0007.mp4");
            webView.getLayoutParams().width=1200;
            webView.getLayoutParams().height=1000;
            // Added on 11 May 2020 for Container Crush {
      // if (selection = "0") {
            if (selection.equals("0")) {
             videoPath = "android.resource://com.example.sabyaarimagerecognition/" + R.raw.video1;
            }
            else if (selection.equals("1")){
                videoPath = "android.resource://com.example.sabyaarimagerecognition/" + R.raw.video2;
            }
            else if (selection.equals("2")){
                videoPath = "android.resource://com.example.sabyaarimagerecognition/" + R.raw.video3;
            }
            else if (selection.equals("3")){
                videoPath = "android.resource://com.example.sabyaarimagerecognition/" + R.raw.video4;
            }
            else if (selection.equals("4")){
                videoPath = "android.resource://com.example.sabyaarimagerecognition/" + R.raw.video5;
            }
            else if (selection.equals("5")){
                videoPath = "android.resource://com.example.sabyaarimagerecognition/" + R.raw.video6;
            }
            else {
                videoPath = "android.resource://com.example.sabyaarimagerecognition/" + R.raw.video1;
            }
            // Added on 11 May 2020 for Container Crush }
      //  }  // Commented on 11 May for COntainer Crush
            // Commented on 11 May for COntainer Crush  String videoPath = "android.resource://com.example.sabyaarimagerecognition/" + R.raw.video;
           webView.setVideoPath(videoPath);
           webView.start();
           return;
           //MainActivity.flipVideo();
              }
        }
    }




