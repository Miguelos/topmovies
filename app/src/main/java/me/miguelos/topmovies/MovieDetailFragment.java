package me.miguelos.topmovies;

import me.miguelgp.topmovies.R;
import me.miguelos.topmovies.data.LoadImageTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * A fragment representing a single Movie detail screen. This fragment is either
 * contained in a {@link MovieListActivity} in two-pane mode (on tablets) or a
 * {@link MovieDetailActivity} on handsets.
 */
public class MovieDetailFragment extends Fragment {
	/**
	 * The fragment argument representing the movie summary.
	 */
	public static final String ARG_MOVIE_SUMMARY = "movie_summary";

	/**
	 * The fragment argument representing the movie poster URL.
	 */
	public static final String ARG_MOVIE_POSTER = "movie_poster";

	/**
	 * The movie summary.
	 */
	private String summary;

	/**
	 * The movie poster URL.
	 */
	private String poster;

	/**
	 * Mandatory empty constructor for the fragment manager to instantiate the
	 * fragment (e.g. upon screen orientation changes).
	 */
	public MovieDetailFragment() {
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		// Load the data required from the arguments. This way is clear and
		// simple.
		// However, it must be improved using a Loader.

		// Load the summary
		if (getArguments().containsKey(ARG_MOVIE_SUMMARY)) {
			summary = getArguments().getString(ARG_MOVIE_SUMMARY);
		}

		// Load the poster URL
		if (getArguments().containsKey(ARG_MOVIE_POSTER)) {
			poster = getArguments().getString(ARG_MOVIE_POSTER);
		}
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.fragment_movie_detail,
				container, false);

		// Show the summary.
		if (summary != null) {
			((TextView) rootView.findViewById(R.id.movie_summary))
					.setText(summary);
		}
		// Load from URL and show Show the poster.
		if (poster != null) {
			// Check Internet connection
			if (Util.isNetworkConected(getActivity())) {
				new LoadImageTask(
						(ImageView) rootView.findViewById(R.id.movie_poster))
						.execute(poster);
			}
		}

		return rootView;
	}

}
