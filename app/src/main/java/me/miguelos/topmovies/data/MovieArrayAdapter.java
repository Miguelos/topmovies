/**
 *
 */
package me.miguelos.topmovies.data;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import me.miguelgp.topmovies.R;
import me.miguelos.topmovies.utils.Utils;

/**
 * Custom Array adapter for {@link Movie}.
 *
 * @author Miguel González Pérez
 */
public class MovieArrayAdapter extends ArrayAdapter<Movie> {

    private final List<Movie> list;

    /**
     * Creates the adapter from a {@link List} of {@link Movie}.
     *
     * @param context
     * @param list
     */
    public MovieArrayAdapter(Context context, List<Movie> list) {
        super(context, R.layout.list_elem_movie, list);
        this.list = list;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder = null;
        if (convertView == null) {
            LayoutInflater inflator = (LayoutInflater) this.getContext()
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflator.inflate(R.layout.list_elem_movie, null);
            viewHolder = new ViewHolder(convertView);

            convertView.setTag(viewHolder);

        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        // Get movie from list
        Movie movie = this.list.get(position);

        viewHolder.name.setText(movie.getName());
        viewHolder.rights.setText(movie.getRights());
        // Check internet conection
        if (Utils.isNetworkConected(this.getContext())) {
            new LoadImageTask(viewHolder.thumbnail).execute(movie
                    .getImageThumbnail());
        }
        return convertView;
    }

    /**
     * ViewHolder for the list_elem_movie layout
     */
    static class ViewHolder {
        protected ImageView thumbnail = null;
        protected TextView name = null;
        protected TextView rights = null;

        ViewHolder(View convertView) {
            this.thumbnail = (ImageView) convertView
                    .findViewById(R.id.movie_thumbnail);
            this.name = (TextView) convertView.findViewById(R.id.movie_name);
            this.rights = (TextView) convertView
                    .findViewById(R.id.movie_rights);
        }
    }

}
