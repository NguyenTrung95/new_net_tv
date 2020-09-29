package net.sunniwell.download.http;

import android.os.Message;
import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;
import org.xmlpull.v1.XmlPullParser;

public class JsonHttpResponseHandler extends AsyncHttpResponseHandler {
    protected static final int SUCCESS_JSON_MESSAGE = 100;

    public void onSuccess(JSONObject response) {
    }

    public void onSuccess(JSONArray response) {
    }

    public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
        onSuccess(statusCode, response);
    }

    public void onSuccess(int statusCode, JSONObject response) {
        onSuccess(response);
    }

    public void onSuccess(int statusCode, Header[] headers, JSONArray response) {
        onSuccess(statusCode, response);
    }

    public void onSuccess(int statusCode, JSONArray response) {
        onSuccess(response);
    }

    public void onFailure(Throwable e, JSONObject errorResponse) {
    }

    public void onFailure(Throwable e, JSONArray errorResponse) {
    }

    /* access modifiers changed from: protected */
    public void sendSuccessMessage(int statusCode, Header[] headers, String responseBody) {
        if (statusCode != 204) {
            try {
                sendMessage(obtainMessage(100, new Object[]{Integer.valueOf(statusCode), headers, parseResponse(responseBody)}));
            } catch (JSONException e) {
                sendFailureMessage((Throwable) e, responseBody);
            }
        } else {
            sendMessage(obtainMessage(100, new Object[]{Integer.valueOf(statusCode), new JSONObject()}));
        }
    }

    /* access modifiers changed from: protected */
    public void handleMessage(Message msg) {
        switch (msg.what) {
            case 100:
                Object[] response = (Object[]) msg.obj;
                handleSuccessJsonMessage(((Integer) response[0]).intValue(), (Header[]) response[1], response[2]);
                return;
            default:
                super.handleMessage(msg);
                return;
        }
    }

    /* access modifiers changed from: protected */
    public void handleSuccessJsonMessage(int statusCode, Header[] headers, Object jsonResponse) {
        if (jsonResponse instanceof JSONObject) {
            onSuccess(statusCode, headers, (JSONObject) jsonResponse);
        } else if (jsonResponse instanceof JSONArray) {
            onSuccess(statusCode, headers, (JSONArray) jsonResponse);
        } else {
            onFailure((Throwable) new JSONException("Unexpected type " + jsonResponse.getClass().getName()), (JSONObject) null);
        }
    }

    /* access modifiers changed from: protected */
    public Object parseResponse(String responseBody) throws JSONException {
        Object result = null;
        String responseBody2 = responseBody.trim();
        if (responseBody2.startsWith("{") || responseBody2.startsWith("[")) {
            result = new JSONTokener(responseBody2).nextValue();
        }
        if (result == null) {
            return responseBody2;
        }
        return result;
    }

    /* access modifiers changed from: protected */
    public void handleFailureMessage(Throwable e, String responseBody) {
        if (responseBody != null) {
            try {
                Object jsonResponse = parseResponse(responseBody);
                if (jsonResponse instanceof JSONObject) {
                    onFailure(e, (JSONObject) jsonResponse);
                } else if (jsonResponse instanceof JSONArray) {
                    onFailure(e, (JSONArray) jsonResponse);
                } else {
                    onFailure(e, responseBody);
                }
            } catch (JSONException e2) {
                onFailure(e, responseBody);
            }
        } else {
            onFailure(e, XmlPullParser.NO_NAMESPACE);
        }
    }
}
