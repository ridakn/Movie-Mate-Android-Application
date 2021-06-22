package com.coen268.moviemate;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import java.util.List;

import static android.content.Context.MODE_PRIVATE;

/**
 * An {@link MovieAdapter} knows how to create a list item layout for each earthquake
 * in the data source (a list of {@link Movie} objects).
 *
 * These list item layouts will be provided to an adapter view like ListView
 * to be displayed to the user.
 */
public class MovieAdapter extends PagerAdapter {

    private static final String LOG_TAG = MovieAdapter.class.getName();

    List<String> listImages;
    List<Movie> movieList;
    Context context;
    LayoutInflater layoutInflater;
    String username;
    SharedPreferences sharedPreferences;

    public MovieAdapter(List<String> listImages, List<Movie> movies, Context context, String user) {
        this.listImages = listImages;
        this.movieList = movies;
        this.context = context;
        this.layoutInflater = LayoutInflater.from(context);
        this.username = user;
    }

    @Override
    public int getCount() {

        return listImages.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object o) {
        return view.equals(o);
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View) object);
    }



    /**fetch the images with Picasso from URL
     * */
    @Override
    public Object instantiateItem(ViewGroup container, final int position) {
        LayoutInflater inflater = (LayoutInflater) container.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.moive_list_item,null);
        ((ViewPager) container).addView(view);
        sharedPreferences =  context.getSharedPreferences("MOVIE_ID", MODE_PRIVATE);
        final SharedPreferences.Editor editor = sharedPreferences.edit();
        final ImageView img = (ImageView) view.findViewById(R.id.imageView);
        Picasso.get()
                .load(listImages.get(position))
                //.placeholder()
                .centerCrop()
                .fit()
                .into(img);

        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, MovieInfoActivity.class);
                long id = movieList.get(position).getId();
                Log.i(LOG_TAG, "id is" + id);
                editor.putString("id", String.valueOf(id));
                editor.commit();
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                String name = movieList.get(position).getTitle();
                intent.putExtra("Movie_name", name);
                intent.putExtra("user_name", username);
                context.startActivity(intent);

            }
        });
        return view;
    }
}