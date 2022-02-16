package it.univaq.citydiscover.utility;


import android.content.Context;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

public class RequestVolley {

    private RequestQueue queue;

    private static volatile RequestVolley instance = null;

    public static synchronized RequestVolley getInstance(Context context) {
        if(instance == null) {
            synchronized (RequestVolley.class) {
                if(instance == null) {
                    instance = new RequestVolley(context);
                }
            }
        }
        return instance;
    }

    private RequestVolley(Context context) {

        queue = Volley.newRequestQueue(context);
    }

    public RequestQueue getQueue() {
        return this.queue;
    }
}

