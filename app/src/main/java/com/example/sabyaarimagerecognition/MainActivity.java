package com.example.sabyaarimagerecognition;

import android.os.Bundle;
//import android.support.v7.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatActivity;

import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Context;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.google.ar.core.AugmentedImage;
import com.google.ar.core.Frame;
import com.google.ar.core.TrackingState;
import com.google.ar.sceneform.FrameTime;
//import com.google.ar.sceneform.
import com.google.ar.sceneform.ux.ArFragment;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import android.os.Handler;
import com.android.volley.toolbox.Volley;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONObject;
import org.json.JSONException;


public class MainActivity extends AppCompatActivity {
    private ArFragment arFragment;
    private ImageView fitToScanView;
    String  n = "0";
    String n1 = "9";
    // Augmented image and its associated center pose anchor, keyed by the augmented image in
    // the database.
    private final Map<AugmentedImage, AugmentedWebViewNode> augmentedImageMap = new HashMap<>();
  //  Frame frame = null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        arFragment = (ArFragment) getSupportFragmentManager().findFragmentById(R.id.ux_fragment);
        fitToScanView = findViewById(R.id.image_view_fit_to_scan);
        arFragment.getArSceneView().getScene().addOnUpdateListener(this::onUpdateFrame);
    }
    @Override
    protected void onResume() {
        super.onResume();

        if (augmentedImageMap.isEmpty()) {
            fitToScanView.setVisibility(View.VISIBLE);
         }
    }

    private void onUpdateFrame(FrameTime frameTime) {
        Frame frame = arFragment.getArSceneView().getArFrame();
       // String oldRestResponse = null;
       // String newRestResponse = null;
        //int n = 2;
        // If there is no frame, just return.
        if (frame == null || frame.getCamera().getTrackingState() != TrackingState.TRACKING) {
            return;
        }
        Collection<AugmentedImage> updatedAugmentedImages =
                frame.getUpdatedTrackables(AugmentedImage.class);
        for (AugmentedImage augmentedImage : updatedAugmentedImages) {
            switch (augmentedImage.getTrackingState()) {
                case PAUSED:
                    // When an image is in PAUSED state, but the camera is not PAUSED, it has been detected,
                    // but not yet tracked.
                    String text = "Detected Image";

                    //Toast.makeText(getContext(),"Could not set up Augmented Image database", Toast.LENGTH_LONG);
                  //  SnackbarHelper.getInstance().showMessage(this, text);
                    break;
                case TRACKING:
                    // Have to switch to UI Thread to update View.
                    // INFINITE LOOP
                     fitToScanView.setVisibility(View.GONE);
                   // n = n+1;

                       // oldRestResponse = "1";
                       // newRestResponse = "2";
                        String URL = "https://updated-nodejs-cc-uc-3-industrial-oracle-team-a.container-crush-02-4044f3a4e314f4bcb433696c70d13be9-0000.eu-de.containers.appdomain.cloud/getselecteditem/";
                        RequestQueue requestQueue=Volley.newRequestQueue(this);

                        JsonObjectRequest objectRequest=new JsonObjectRequest(
                                Request.Method.GET,
                                URL,
                                null,
                                new Response.Listener<JSONObject>() {
                                    @Override
                                    public void onResponse(JSONObject response){
                                       // JSONObject response1 = response;
                                        Log.e("Response",response.toString());
                                        try {
                                            n1 = response.getString("index");
                                        } catch (JSONException e) {
                                            e.printStackTrace();
                                        }
                                    }
                                },
                                new Response.ErrorListener() {
                                    @Override
                                    public void onErrorResponse(VolleyError error){

                                    }

                                }
                        );
                        requestQueue.add(objectRequest);


                      //  if (oldRestResponse != newRestResponse) {
                       //  if (n1 != n)
                            if (!n1.equals(n)) {
                                n = n1;
                            // fitToScanView.setVisibility(View.GONE);
                            // Create a new anchor for newly found images.
                            if (!augmentedImageMap.containsKey(augmentedImage)) {
                                AugmentedWebViewNode node = new AugmentedWebViewNode(this);
                                node.setImage(augmentedImage, n);
                                augmentedImageMap.put(augmentedImage, node);
                                arFragment.getArSceneView().getScene().addChild(node);
                              //  node = null;

                                augmentedImageMap.remove(augmentedImage);

                                }
                          /*  else {
                                AugmentedWebViewNode node = augmentedImageMap.get(augmentedImage);
                                augmentedImageMap.remove(augmentedImage);
                                arFragment.getArSceneView().getScene().removeChild(node);
                                augmentedImageMap.remove(augmentedImage);
                            }*/

                            }
                         else {
                            return;
                        }

                        break;

                case STOPPED:
                    augmentedImageMap.remove(augmentedImage);
                    break;
            }
        }
    }

}