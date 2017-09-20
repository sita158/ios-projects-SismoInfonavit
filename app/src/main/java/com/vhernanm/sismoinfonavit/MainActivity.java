package com.vhernanm.sismoinfonavit;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import static android.R.attr.button;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //createRecord();
        //searchByIndex();
        deleteRecord();
    }

    public void createRecord (){

        //Construir JSON para enviar como parámetros. Debe contener todos los campos del registro.
        JSONObject cita = new JSONObject();
        try{
            cita.put("fecha", "20/09/2017");
            cita.put("hora", "19:00");
            cita.put("username", "sita158@hotmail.com");
        }catch(Exception e){
            Log.d("App", "Error: "+e);
        }

        //El URL para creación de registros se compone del URL del servicio + nombre de la BD
        String url = "https://9a677ddf-a679-46bd-a28a-e5559d80abf5-bluemix.cloudant.com:443/appointments_db";
        //El tercer parámetro es el objeto JSON que contiene los valores a almacenar
        JsonObjectRequest jsonRequest = new JsonObjectRequest(Request.Method.POST, url, cita, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                //El response exitoso es algo como: {"id":"de32d7ff78a56cf8fe6fd2a8fcea01ec","rev":"1-3a932fea0a74249d6feeaf4c75e2aefa","ok":true}
                Log.d("App", "Response: " + response);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("App", "Error: " + error.getMessage());
            }
        })
        {
            @Override
            public Map<String, String> getHeaders() {
                Map<String, String> headers = new HashMap<>();
                String credentials = "9a677ddf-a679-46bd-a28a-e5559d80abf5-bluemix:32c72a6b64dad3e62ca693f6ff18967c7ace01b9348b96b38574787a75a14968";
                String auth = "Basic " + Base64.encodeToString(credentials.getBytes(), Base64.NO_WRAP);
                headers.put("Content-Type", "application/json; charset=utf-8");
                headers.put("Authorization", auth);
                return headers;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(MainActivity.this);
        requestQueue.add(jsonRequest);

    }

    public void deleteRecord (){

        //El URL para búsqueda de registros se compone del URL del servicio + nombre de la BD + id?rev=rev del registro
        String url = "https://9a677ddf-a679-46bd-a28a-e5559d80abf5-bluemix.cloudant.com:443/appointments_db/bce8cf6401a6884d69803b8974df8ec0?rev=1-3a932fea0a74249d6feeaf4c75e2aefa";
        //El tercer parámetro es nulo porque no se requiere enviar parámetros. El método del Request debe ser "DELETE"
        JsonObjectRequest jsonRequest = new JsonObjectRequest(Request.Method.DELETE, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                //El response exitoso es un JSON que confirma que el registro con el ID y REV enviados fue exitoso. Ejemplo: {"id":"bce8cf6401a6884d69803b8974df8ec0","rev":"2-464e9ed5ad908c6f6a8205d7221121a8","ok":true}
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("App", "Error: " + error.getMessage());
            }
        })
        {
            @Override
            public Map<String, String> getHeaders() {
                Map<String, String> headers = new HashMap<>();
                String credentials = "9a677ddf-a679-46bd-a28a-e5559d80abf5-bluemix:32c72a6b64dad3e62ca693f6ff18967c7ace01b9348b96b38574787a75a14968";
                String auth = "Basic " + Base64.encodeToString(credentials.getBytes(), Base64.NO_WRAP);
                headers.put("Authorization", auth);
                return headers;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(MainActivity.this);
        requestQueue.add(jsonRequest);
    }

    public void searchByIndex (){
        //Construir JSON para enviar como parámetros. Debe contener la llave para la búsqueda en un JSON que a su vez forma parte del atributo "selector"
        //del objeto que se envía como parámetro. Ejemplo: {"selector":{"username":"username"}}
        JSONObject key = new JSONObject();
        try{
            key.put("username", "sita158@hotmail.com");
        } catch(Exception e){
            Log.d("App", "Error: "+e);
        }

        JSONObject params = new JSONObject();
        try{
            params.put("selector", key);
        }catch(Exception e){
            Log.d("App", "Error: "+e);
        }

        //El URL para búsqueda de registros se compone del URL del servicio + nombre de la BD + string "_find"
        String url = "https://9a677ddf-a679-46bd-a28a-e5559d80abf5-bluemix.cloudant.com:443/appointments_db/_find";
        //El tercer parámetro es el objeto JSON que contiene los valores a almacenar
        JsonObjectRequest jsonRequest = new JsonObjectRequest(Request.Method.POST, url, params, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                //El response exitoso es un JSON con un arreglo de registros recuperados. Ejemplo: {"docs":[{"_rev":"1-927a2a34b744c92860796de0481523d8","hora":"12:0","username":"sita158@hotmail.com","fecha":"7\/8\/2017","_id":"ab4214050d4c0cb0b995468768c34504"},{"_rev":"1-3a932fea0a74249d6feeaf4c75e2aefa","hora":"19:00","username":"sita158@hotmail.com","fecha":"20\/09\/2017","_id":"de32d7ff78a56cf8fe6fd2a8fcea01ec"}],"warning":"no matching index found, create an index to optimize query time"}
                Log.d("App", "Response: " + response);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("App", "Error: " + error.getMessage());
            }
        })
        {
            @Override
            public Map<String, String> getHeaders() {
                Map<String, String> headers = new HashMap<>();
                String credentials = "9a677ddf-a679-46bd-a28a-e5559d80abf5-bluemix:32c72a6b64dad3e62ca693f6ff18967c7ace01b9348b96b38574787a75a14968";
                String auth = "Basic " + Base64.encodeToString(credentials.getBytes(), Base64.NO_WRAP);
                headers.put("Content-Type", "application/json; charset=utf-8");
                headers.put("Authorization", auth);
                return headers;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(MainActivity.this);
        requestQueue.add(jsonRequest);
    }
}
