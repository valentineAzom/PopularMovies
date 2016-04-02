package com.example.ukzn.popularmovies;

import android.content.Intent;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

/**
 * A placeholder fragment containing a simple view.
 */
public class MovieDetailsActivityFragment extends Fragment {

    TextView titleView;
    ImageView thumbView;
    TextView ReleaseView;
    TextView RatingView;
    TextView OverviewView;
    public MovieDetailsActivityFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.menu_movie_details,menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if(id == R.id.action_settings){
            Intent menuIntent = new Intent(getActivity(),SettingsActivity.class);
            startActivity(menuIntent);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View RootView = inflater.inflate(R.layout.fragment_movie_details, container, false);
        Intent selectedMovieIntent = getActivity().getIntent();
        if(selectedMovieIntent !=null && selectedMovieIntent.hasExtra("moviedata")){
             titleView = (TextView)RootView.findViewById(R.id.titletextView);
             thumbView = (ImageView)RootView.findViewById(R.id.ThumbimageView);
             ReleaseView = (TextView)RootView.findViewById(R.id.ReleasetextView);
             RatingView = (TextView)RootView.findViewById(R.id.RatingtextView);
             OverviewView = (TextView)RootView.findViewById(R.id.OverviewtextView);
             Movie ar = (Movie)selectedMovieIntent.getSerializableExtra("moviedata");
             titleView.setText("TITLE: " + ar.getTitle());
             Picasso.with(getActivity()).load(ar.getFullbackdroppath())
                    .placeholder(R.drawable.poster_place_holder)
                    .into(thumbView);
             ReleaseView.setText("RELEASE DATE: " + ar.getRelease_date());
             RatingView.setText("USER RATING: " + ar.getVote_average());
             OverviewView.setText("OVERVIEW" + "\n\n" + ar.getOverview());
        }
        return  RootView;
    }
}
