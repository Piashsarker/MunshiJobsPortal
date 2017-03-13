package dcastalia.com.munshijobsportal.Util;

import com.android.volley.NetworkResponse;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.toolbox.HttpHeaderParser;

import org.json.JSONArray;
import org.json.JSONException;

import java.io.UnsupportedEncodingException;
import java.util.Map;

/**
 * Created by PT on 3/11/2017.
 */

public class JsonArrayCustomRequest extends Request<JSONArray> {

    private Response.Listener<JSONArray> listener;
    private Map<String, String> params;

    public JsonArrayCustomRequest(int method, Map<String , String> params, String url,Response.Listener<JSONArray>listener, Response.ErrorListener errorlistener) {
        super(method, url, errorlistener);
        this.listener = listener ;
        this.params = params;

    }

    @Override
    public Map<String, String> getParams() {
        return params;
    }

    @Override
    protected Response<JSONArray> parseNetworkResponse(NetworkResponse response) {
        try {
            String jsonString = new String(response.data,
                    HttpHeaderParser.parseCharset(response.headers));
            return Response.success(new JSONArray(jsonString),
                    HttpHeaderParser.parseCacheHeaders(response));
        } catch (UnsupportedEncodingException e) {
            return Response.error(new ParseError(e));
        } catch (JSONException je) {
            return Response.error(new ParseError(je));
        }
    }

    @Override
    public String getBodyContentType() {
       return  "application/x-www-form-urlencoded; charset=UTF-8" ;
    }



    @Override
    protected void deliverResponse(JSONArray response) {
        listener.onResponse(response);
    }
    }