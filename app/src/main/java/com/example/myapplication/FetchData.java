package com.example.myapplication;

import android.os.AsyncTask;
import android.widget.ArrayAdapter;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

public class FetchData extends AsyncTask<Void,Void,Void> {
    String data="";
    ArrayList<String> dataParsed=new ArrayList<>();
    String singleParsed="";
    HashMap<String, List<String>> hmap = new HashMap<String, List<String>>();
    ExpandingListAdapter listAdapter;
    @Override
    protected Void doInBackground(Void... voids) {
        try {
            URL url = new URL("https://fetch-hiring.s3.amazonaws.com/hiring.json");
            HttpURLConnection httpURLConnection= (HttpURLConnection) url.openConnection();
            InputStream inputStream = httpURLConnection.getInputStream();
            BufferedReader bufferedReader=new BufferedReader(new InputStreamReader(inputStream));
            String line= "";
            while (line != null){
                line = bufferedReader.readLine();
                data=data+line;
            }

            JSONArray JA= null;
            try {
                JA = new JSONArray(data);
                List<JSONObject> myJsonArrayAsList = new ArrayList<JSONObject>();
                for (int i = 0; i < JA.length(); i++)
                    myJsonArrayAsList.add(JA.getJSONObject(i));
                Collections.sort(myJsonArrayAsList, new Comparator<JSONObject>() {
                    @Override
                    public int compare(JSONObject a, JSONObject b) {
                        String valA = new String();
                        String valB = new String();
                        int compare=0;
                        try {

                            int keyA = a.get("name").hashCode();
                            int keyB = b.get("name").hashCode();
                            compare = Integer.compare(keyA, keyB);
                        }
                        catch (JSONException e) {
                            e.printStackTrace();
                        }

                        return compare;
                    }
                });
                Collections.sort(myJsonArrayAsList, new Comparator<JSONObject>() {
                    @Override
                    public int compare(JSONObject jsonObjectA, JSONObject jsonObjectB) {
                        int compare = 0;
                        try
                        {
                            int keyA = jsonObjectA.getInt("listId");
                            int keyB = jsonObjectB.getInt("listId");
                            compare = Integer.compare(keyA, keyB);
                        }
                        catch(JSONException e)
                        {
                            e.printStackTrace();
                        }
                        return compare;
                    }
                });
                for(int i=0; i<myJsonArrayAsList.size(); i++){
                    try {

                        JSONObject JO=(JSONObject) myJsonArrayAsList.get(i);
                        if(!JO.get("name").equals(null)&&!JO.get("name").equals("")) {

                            singleParsed =  "List ID: " + JO.get("listId");
                            if(hmap.get(singleParsed)!=null) {
                                hmap.get(singleParsed).add("Name: " + JO.get("name") + "\n" + "ID: " + JO.get("id"));
                            }
                            else{
                                List<String> temp=new ArrayList<String>();
                                temp.add("Name: " + JO.get("name") + "\n" + "ID: " + JO.get("id"));
                                hmap.put(singleParsed, temp);
                            }

                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                }
            } catch (JSONException e) {
                e.printStackTrace();
            }

        } catch (MalformedURLException e){
            e.printStackTrace();
        } catch (IOException e){
            e.printStackTrace();
        }
        return null;
    }

    @Override
    protected void onPostExecute(Void unused) {
        super.onPostExecute(unused);
        Set<String> keyset=hmap.keySet();
        ArrayList<String> listheader= new ArrayList<String>(keyset);
        Collections.sort(listheader);
        listAdapter=new ExpandingListAdapter(MainActivity.listView.getContext(),listheader,hmap);
        MainActivity.listView.setAdapter(listAdapter);
    }
}
