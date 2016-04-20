package br.edu.ufabc.mobile.spacecombat.comm;

import android.os.AsyncTask;
import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

public class RemoteTimeTask extends AsyncTask<String, Void, Date> {

    protected Date doInBackground(String... param1){

        String conteudo = "";
        String linha;

        try {
            URL url = new URL(param1[0]);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.connect();

            int response = connection.getResponseCode();
            if (response == HttpURLConnection.HTTP_OK) {

                InputStreamReader isr = new InputStreamReader(connection.getInputStream());
                BufferedReader br = new BufferedReader(isr);

                while ((linha = br.readLine()) != null) {
                    conteudo += linha;
                }
                br.close();
                isr.close();
            }else if(response == HttpURLConnection.HTTP_UNAVAILABLE){
                Log.d("HTTPCLIENT", "Service Unavailable");
            }


            JSONObject reader = new JSONObject(conteudo);

            String time = reader.getString("time");

            SimpleDateFormat printFormat = new SimpleDateFormat("HH:mm");

            printFormat.setTimeZone(TimeZone.getTimeZone("UTC"));
            Date date = printFormat.parse(time);

            Log.d("HTTPCLIENT", "Time: "+date);

            return date;

        }catch(JSONException ex){
                Log.d("HTTPCLIENT", "JSON error");
        }catch(IOException ex){
                Log.d("HTTPCLIENT", "Erro ao conectar - " + ex.getMessage());
        }catch (ParseException ex){
                Log.d("HTTPCLIENT", "Erro ao analisar json");
        }
        return null;
    }

    @Override
    protected void onPostExecute(Date date) {
        super.onPostExecute(date);
        Params.EASTEREGGED = Params.checkEasteregg(date);
    }
}
