package com.example.ukzn.popularmovies;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v4.app.SharedElementCallback;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

/**
 * A placeholder fragment containing a simple view.
 */
public class MoviesActivityFragment extends Fragment {

    private CustomGridAdapter movieDataAdapter;
    ArrayList<Movie> mDBList;
    GridView myGridView;
    public MoviesActivityFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setHasOptionsMenu(true);
    }

    @Override
    public void onStart() {
        super.onStart();
        if(isNetworkAvailable()) {
            updateMoviedata();
        }
        else{
           AlertDialog.Builder alertblock = new AlertDialog.Builder(getActivity());
            alertblock.setTitle("Network state");
            alertblock.setMessage("Not connected to the internet");
            alertblock.setCancelable(true);
            alertblock.show();
        }
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_moviefragment,menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if(id == R.id.action_refresh){
            SharedPreferences mupref = PreferenceManager.getDefaultSharedPreferences(getActivity());
            String sm = mupref.getString(getString(R.string.pref_default_key), getString(R.string.pref_default_value));
            FetchMovieTask mtask = new FetchMovieTask();
            mtask.execute(sm);
            return  true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void updateMoviedata(){
        SharedPreferences mypref = PreferenceManager.getDefaultSharedPreferences(getActivity());
        String sm = mypref.getString(getString(R.string.pref_default_key), getString(R.string.pref_default_value));
        FetchMovieTask mtask = new FetchMovieTask();
        mtask.execute(sm);
    }
    //check network connection
    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

    @Override
    public void setExitSharedElementCallback(SharedElementCallback callback) {
        super.setExitSharedElementCallback(callback);
    }

    @Override
    public String toString() {
        return super.toString();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_movies, container, false);

        myGridView = (GridView)rootView.findViewById(R.id.movieGridView);
        //mDBList = new ArrayList<Movie>();
        //movieDataAdapter = new CustomGridAdapter(getActivity(),mDBList);
        //myGridView.setAdapter(movieDataAdapter);
        myGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                Movie forecast = (Movie)movieDataAdapter.getItem(position);
                Intent ndetails = new Intent(getActivity(), MovieDetailsActivity.class)
                        .putExtra("moviedata", forecast);
                startActivity(ndetails);

                //Toast.makeText(getActivity(), forecast, Toast.LENGTH_SHORT).show();
            }
        });
        return rootView;
    }

    public class FetchMovieTask extends AsyncTask<String,Void,Movie[]>{
        String LOG_TAG = FetchMovieTask.class.getSimpleName();
        ProgressDialog Dialog;
        private Movie[] getMovieDataFromJson(String movieDataJson) throws JSONException{
            final String mRESULTS = "results";
            final String mPOSTER_PATH = "poster_path";
            final String mOVERVIEW = "overview";
            final String mRELEASE_DATE = "release_date";
            final String mID = "id";
            final String mORIGINAL_TITLE = "original_title";
            final String mtitle = "title";
            final String mBACKDROP_PATH = "backdrop_path";
            final String mPOPULARITY = "popularity";
            final String mVOTE_COUNT = "vote_count";
            final String mVOTE_AVERAGE = "vote_average";

            JSONObject Movieresults = new JSONObject(movieDataJson);
            JSONArray movieArray = Movieresults.getJSONArray(mRESULTS);
            Movie [] movieRDATA = new Movie[movieArray.length()];
            try {
                for (int i = 0; i < movieArray.length(); i++) {
                    Movie mov = new Movie();
                    JSONObject moviedata = movieArray.getJSONObject(i);
                    mov.setId(moviedata.getInt(mID));
                    mov.setFullbackdroppath("http://image.tmdb.org/t/p/w780" + moviedata.getString(mPOSTER_PATH));
                    mov.setFullposterpath("http://image.tmdb.org/t/p/w185" + moviedata.getString(mPOSTER_PATH));
                    mov.setOriginal_title(moviedata.getString(mORIGINAL_TITLE));
                    mov.setOverview(moviedata.getString(mOVERVIEW));
                    mov.setPopularity(moviedata.getDouble(mPOPULARITY));
                    mov.setRelease_date(moviedata.getString(mRELEASE_DATE));
                    mov.setTitle(moviedata.getString(mtitle));
                    mov.setVote_count(moviedata.getInt(mVOTE_COUNT));
                    mov.setVote_average(moviedata.getDouble(mVOTE_AVERAGE));

                    movieRDATA[i] = mov;

                }
                return movieRDATA;
            }
            catch (Exception ex){
                Log.e(LOG_TAG,"Error",ex);
            }
            return  null;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            Dialog = new ProgressDialog(getActivity());
            Dialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);

            Dialog.setTitle("Loading....");
            Dialog.show();
        }

        @Override
        protected Movie[] doInBackground(String... params) {

            if(params.length == 0){
                return null;
            }
            HttpURLConnection urlConnection = null;
            BufferedReader reader =null;
            String MovieJsonStr = null;

            try{
                String baseUrl = "https://api.themoviedb.org/3/discover/movie";
                final String QUERY_PARAM = "sort_by";
                final String API_KEY_PARAM ="api_key";
                Uri builtUri = Uri.parse(baseUrl).buildUpon()
                        .appendQueryParameter(QUERY_PARAM,params[0])
                        .appendQueryParameter(API_KEY_PARAM,BuildConfig.MOVIEE_API_KEY)
                        .build();
                URL url = new URL(builtUri.toString());
                Log.v(LOG_TAG,"Built Uri " + builtUri.toString());

                urlConnection = (HttpURLConnection)url.openConnection();
                urlConnection.setRequestMethod("GET");
                urlConnection.connect();

                InputStream movieStream = urlConnection.getInputStream();
                StringBuffer buffer = new StringBuffer();
                if(movieStream == null){
                    return  null;
                }
                reader = new BufferedReader(new InputStreamReader(movieStream));
                String line;
                while((line = reader.readLine()) != null){
                    buffer.append(line + "\n");
                }
                if(buffer.length() ==0){
                    return null;
                }
                MovieJsonStr = buffer.toString();

            }
            catch (Exception ex){
                Log.e(LOG_TAG,"Error",ex);
                return null;
            }
            finally {
                if(urlConnection != null){
                    urlConnection.disconnect();
                }
                if(reader !=null){
                    try{
                        reader.close();
                    }
                    catch (Exception ex){
                        Log.e(LOG_TAG,"Error closing Stream",ex);
                    }
                }
            }
            try{
               return  getMovieDataFromJson(MovieJsonStr);
            }
            catch (Exception ex){
                Log.e(LOG_TAG, ex.getMessage(), ex);
                ex.printStackTrace();
            }

            return null;
        }

        @Override
        protected void onPostExecute(Movie[] movies) {
            super.onPostExecute(movies);
            if(movies != null){

                mDBList =new ArrayList<Movie>();
                for(Movie s : movies){
                    mDBList.add(s);
                }

                movieDataAdapter = new CustomGridAdapter(getActivity(),mDBList);
                myGridView.setAdapter(movieDataAdapter);

                Dialog.cancel();


            }
        }
    }
    public class CustomGridAdapter extends BaseAdapter{
        Context context;
        ArrayList<Movie> movieList;
        public CustomGridAdapter(Context context, ArrayList<Movie> mDbList){
            this.context = context;
            this.movieList = mDbList;
        }
        @Override
        public int getCount() {
            return movieList.size();
        }

        @Override
        public Object getItem(int position) {
            return movieList.get(position);
        }

        @Override
        public long getItemId(int position) {
            return 123456000 + position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

            if(convertView == null){
                convertView = LayoutInflater.from(context).inflate(R.layout.grid_cell_list,parent,false);
            }
            Movie movieDB = (Movie)getItem(position);

            ImageView imageViewcustom = (ImageView)convertView.findViewById(R.id.imageView);
            Picasso.with(context).load(movieDB.getFullposterpath())
                    .placeholder(R.drawable.poster_place_holder)
                    .into(imageViewcustom);


            return convertView;
        }
    }
}
