package jennirex.com.jobfinder.Helpers;

import android.content.Context;

import com.android.volley.Request;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Tupelo on 2/17/2019.
 */

public class ApiRequest {

    public static void GetProviderRequest(Context context){
        Map<String,String> requestParams = new HashMap<String, String>();
        Map<String,String> headerParams = new HashMap<String, String>();
        headerParams.put("Content-Type","application/json");
        headerParams.put("Accept","application/json");

        JSONObject bodyParams = new JSONObject();
        VolleyHelper.volleyRequest(Constants.request_type.GETPROVIDERS,Constants.DOMAIN, Request.Method.POST,requestParams,headerParams,context,bodyParams.toString());
    }

    public static void GetJobsRequest(Context context, String api_url){
        Map<String,String> requestParams = new HashMap<String, String>();
        Map<String,String> headerParams = new HashMap<String, String>();
        headerParams.put("Content-Type","application/json");
        headerParams.put("Accept","application/json");

        JSONObject bodyParams = new JSONObject();
        VolleyHelper.volleyRequest(Constants.request_type.GETJOBS,api_url, Request.Method.GET,requestParams,headerParams,context,bodyParams.toString());
    }
}
