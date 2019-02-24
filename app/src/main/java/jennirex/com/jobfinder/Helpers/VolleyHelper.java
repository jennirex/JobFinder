package jennirex.com.jobfinder.Helpers;

import android.content.Context;
import android.content.res.Resources;
import android.util.Log;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NetworkError;
import com.android.volley.NoConnectionError;
import com.android.volley.ParseError;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.ServerError;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.io.UnsupportedEncodingException;
import java.util.Map;

import jennirex.com.jobfinder.Interfaces.GetJobsInterface;
import jennirex.com.jobfinder.Interfaces.GetProvidersInterface;
import jennirex.com.jobfinder.R;

/**
 * Created by Tupelo on 2/17/2019.
 */

public class VolleyHelper {

    static Resources resources;
    static RequestQueue MyRequestQueue;

    public static void volleyRequest(final Constants.request_type rtype, String api_url, Integer method, final Map<String,String> requestParams, final Map<String,String> headerParams, final Context context, final String bodyParams)
    {
        MyRequestQueue = Volley.newRequestQueue(context);
        resources = context.getResources();
        final GetProvidersInterface getProvidersInterface = (GetProvidersInterface) context;
        final GetJobsInterface getJobsInterface = (GetJobsInterface) context;
        StringRequest MyStringRequest = new StringRequest(method, api_url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                Log.d("VolleyHelper1",response);
                switch (rtype){
                    case GETPROVIDERS:{
                        getProvidersInterface.onGetResponse(response);
                    }
                    break;
                    case GETJOBS:{
                        getJobsInterface.onGetJobsResponse(response);
                    }
                    break;
                    default:
                        break;
                }


            }
        }, new Response.ErrorListener() { //Create an error listener to handle errors appropriately.
            @Override
            public void onErrorResponse(VolleyError error) {
                String err="";

                if (error instanceof TimeoutError ) {
                    //This indicates that the reuest has timed out
                    err = resources.getString(R.string.timeout_error);

                } else if (error instanceof NoConnectionError) {
                    //This indicates that  there is no connection
                    err = resources.getString(R.string.noconnection_error);

                } else if (error instanceof AuthFailureError) {
                    // Error indicating that there was an Authentication Failure while performing the request
                    err = resources.getString(R.string.auth_error);

                } else if (error instanceof ServerError) {
                    //Indicates that the server responded with a error response
                    err = resources.getString(R.string.server_error);

                } else if (error instanceof NetworkError) {
                    //Indicates that there was network error while performing the request
                    err = resources.getString(R.string.network_error);

                } else if (error instanceof ParseError) {
                    // Indicates that the server response could not be parsed
                    err = resources.getString(R.string.parse_error);
                }
                switch (rtype){
                    case GETPROVIDERS:{
                        getProvidersInterface.onGetResponse(err);
                    }
                    break;
                    case GETJOBS:{
                        getJobsInterface.onGetJobsResponse(err);
                    }
                    break;
                    default:
                        break;
                }
            }
        }) {
            @Override
            protected Map<String,String> getParams(){
                return requestParams;
            }

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                return headerParams;
            }

            @Override
            public byte[] getBody() throws AuthFailureError {

                byte[] body = new byte[0];
                try {
                    body = bodyParams.getBytes("UTF-8");
                } catch (UnsupportedEncodingException e) {
                    Log.e("VolleyHelper2", "Unable to gets bytes from JSON", e.fillInStackTrace());
                }
                return body;
            }


            @Override
            public String getBodyContentType() {
                return "application/json";
            }
        };

        int socketTimeout = 5000;//5 seconds
        RetryPolicy policy = new DefaultRetryPolicy(socketTimeout, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
        MyStringRequest.setRetryPolicy(policy);
        MyRequestQueue.add(MyStringRequest);
    }
}