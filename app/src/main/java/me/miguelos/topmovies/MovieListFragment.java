package me.miguelos.topmovies;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ListView;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.StatusLine;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;

import me.miguelos.topmovies.data.Movie;
import me.miguelos.topmovies.data.MovieArrayAdapter;
import me.miguelos.topmovies.utils.Utils;

/**
 * A list fragment representing a list of Movies. This fragment also supports
 * tablet devices by allowing list items to be given an 'activated' state upon
 * selection. This helps indicate which item is currently being viewed in a
 * {@link MovieDetailFragment}.
 * <p/>
 * Activities containing this fragment MUST implement the {@link Callbacks}
 * interface.
 */
public class MovieListFragment extends ListFragment {

    public static final String PREFS_NAME = "Prefs";
    private static final String SP_FEED = "feed";
    private static final String FEED_MOVIES = "https://itunes.apple.com/gb/rss/topmovies/limit=50/json";
    /**
     * The serialization (saved instance state) Bundle key representing the
     * activated item position. Only used on tablets.
     */
    private static final String STATE_ACTIVATED_POSITION = "activated_position";
    /**
     * A dummy implementation of the {@link Callbacks} interface that does
     * nothing. Used only when this fragment is not attached to an activity.
     */
    private static Callbacks sDummyCallbacks = new Callbacks() {
        @Override
        public void onItemSelected(String summary, String poster) {
        }
    };
    public MovieArrayAdapter mArrayAdapter;
    public ArrayList<Movie> mList;
    /**
     * The fragment's current callback object, which is notified of list item
     * clicks.
     */
    private Callbacks mCallbacks = sDummyCallbacks;
    /**
     * The current activated item position. Only used on tablets.
     */
    private int mActivatedPosition = ListView.INVALID_POSITION;

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public MovieListFragment() {
    }

    /**
     * Method to load the data from Shared Preferences and notify the adapter.
     */
    private void loadMovies() {
        try {
            SharedPreferences prefs = getActivity().getSharedPreferences(
                    PREFS_NAME, 0);
            if (prefs.contains(SP_FEED)) {

                JSONObject jsonObject = new JSONObject(prefs.getString(SP_FEED,
                        ""));

                JSONArray jsonMovieArray;
                jsonMovieArray = (JSONArray) ((JSONObject) jsonObject
                        .get("feed")).get("entry");

                if (jsonMovieArray != null) {
                    int len = jsonMovieArray.length();
                    for (int i = 0; i < len; i++) {
                        JSONObject jsonMovie = (JSONObject) jsonMovieArray
                                .get(i);
                        mList.add(new Movie(jsonMovie));
                    }
                }
            }

        } catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        // Update adapter
        mArrayAdapter.notifyDataSetChanged();
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

        // Activities containing this fragment must implement its callbacks.
        if (!(activity instanceof Callbacks)) {
            throw new IllegalStateException(
                    "Activity must implement fragment's callbacks.");
        }

        mCallbacks = (Callbacks) activity;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mList = new ArrayList<Movie>();
        mArrayAdapter = new MovieArrayAdapter(getActivity(), mList);
        setListAdapter(mArrayAdapter);

        // Check internet conection
        if (Utils.isNetworkConected(getActivity())) {
            new UpdateTask().execute(FEED_MOVIES);
        } else {
            new AlertDialog.Builder(getActivity())
                    .setTitle("Info")
                    .setMessage(
                            "No internet conection. Check your network status to update the data")
                    .setIcon(android.R.drawable.stat_notify_error)
                    .setPositiveButton("Ok",
                            new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog,
                                                    int which) {
                                    // Close dialog
                                }
                            }).show();

            // Load movies from SharedPreferences
            loadMovies();
        }

    }

    @Override
    public void onDetach() {
        super.onDetach();

        // Reset the active callbacks interface to the dummy implementation.
        mCallbacks = sDummyCallbacks;
    }

    @Override
    public void onListItemClick(ListView listView, View view, int position,
                                long id) {
        super.onListItemClick(listView, view, position, id);

        // Notify the active callbacks interface (the activity, if the
        // fragment is attached to one) that an item has been selected.
        mCallbacks.onItemSelected(mList.get(position).getSummary(),
                mList.get(position).getImage());
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        if (mActivatedPosition != AdapterView.INVALID_POSITION) {
            // Serialize and persist the activated item position.
            outState.putInt(STATE_ACTIVATED_POSITION, mActivatedPosition);
        }
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Restore the previously serialized activated item position.
        if (savedInstanceState != null
                && savedInstanceState.containsKey(STATE_ACTIVATED_POSITION)) {
            setActivatedPosition(savedInstanceState
                    .getInt(STATE_ACTIVATED_POSITION));
        }
    }

    private void setActivatedPosition(int position) {
        if (position == AdapterView.INVALID_POSITION) {
            getListView().setItemChecked(mActivatedPosition, false);
        } else {
            getListView().setItemChecked(position, true);
        }

        mActivatedPosition = position;
    }

    /**
     * Turns on activate-on-click mode. When this mode is on, list items will be
     * given the 'activated' state when touched.
     */
    public void setActivateOnItemClick(boolean activateOnItemClick) {
        // When setting CHOICE_MODE_SINGLE, ListView will automatically
        // give items the 'activated' state when touched.
        getListView().setChoiceMode(
                activateOnItemClick ? AbsListView.CHOICE_MODE_SINGLE
                        : AbsListView.CHOICE_MODE_NONE);
    }

    /**
     * A callback interface that all activities containing this fragment must
     * implement. This mechanism allows activities to be notified of item
     * selections.
     */
    public interface Callbacks {
        /**
         * Callback for when an item has been selected.
         */
        public void onItemSelected(String summary, String poster);
    }

    /**
     * AsyncTask to load the feed data and store it in shared preferences.
     */
    public class UpdateTask extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... uri) {
            String result = null;
            // Try to connect and get the data from the URL (uri[0])
            try {
                HttpClient httpclient = new DefaultHttpClient();
                HttpResponse response = httpclient.execute(new HttpGet(uri[0]));
                StatusLine statusLine = response.getStatusLine();
                if (statusLine.getStatusCode() == HttpStatus.SC_OK) {
                    ByteArrayOutputStream out = new ByteArrayOutputStream();
                    response.getEntity().writeTo(out);
                    out.close();
                    result = out.toString();
                } else {
                    // Closes the connection.
                    response.getEntity().getContent().close();
                    throw new IOException(statusLine.getReasonPhrase());
                }
            } catch (ClientProtocolException e) {
                // TODO Handle problems..
            } catch (IOException e) {
                // TODO Handle problems..
            }
            return result;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);

            // If success, store the json in shared preference and load the data
            // to the UI
            if (result != null) {
                // Save results into shared preferences
                SharedPreferences prefs = getActivity().getSharedPreferences(
                        PREFS_NAME, 0);
                SharedPreferences.Editor editor = prefs.edit();
                editor.putString(SP_FEED, result);
                editor.commit();

                // Load movies from SharedPreferences
                loadMovies();
            }

        }
    }
}
