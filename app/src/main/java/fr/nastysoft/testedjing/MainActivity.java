package fr.nastysoft.testedjing;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


public class MainActivity extends ActionBarActivity {

    private TextView textview1;
    private String urlJsonObj = "http://api.edjing.com/v1/codeTest/android";
    private JsonObjectRequest jsonObjReq;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        textview1 = (TextView) findViewById(R.id.textview1);

        jsonObjReq = new JsonObjectRequest(Request.Method.GET,
                urlJsonObj, null, new Response.Listener<JSONObject>() {

            @Override
            public void onResponse(JSONObject response) {

                try {
                    // extraction des données du JSON
                    JSONObject data = response.getJSONObject("data");

                    // on récupère le tableau JSON et on le parse dans un tableau d'int
                    JSONArray T = data.getJSONArray("T1");
                    int[] intTab = new int[3];
                    if (T != null) {
                        for (int i = 0 ; i < T.length() ; i++){
                            intTab[i] = Integer.parseInt(T.get(i).toString());
                        }
                    }

                    // on récupère la valeur de X1
                    String X = data.getString("X1");

                    // initialisation du calcul
                    int x = Integer.parseInt(X);

                    // calcul de la somme
                    int R = 0;
                    for(int j = 0 ; j < intTab.length ; j++) {
                        R += (intTab[j] * x);
                        Log.d("R", String.valueOf(R));
                    }

                    textview1.setText("Résultat : " + String.valueOf(R));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                textview1.setText("Erreur de connexion réseau.");
            }
        });
    }

    public void onButtonClick(View v) {
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        boolean isConnected = activeNetwork != null && activeNetwork.isConnectedOrConnecting();

        if(isConnected) {
            // on fait la requète
            MySingleton.getInstance(this).addToRequestQueue(jsonObjReq);
        }
        else {
            Toast.makeText(getApplicationContext(), "Pas de connexion réseau.", Toast.LENGTH_LONG).show();
        }
    }
}
