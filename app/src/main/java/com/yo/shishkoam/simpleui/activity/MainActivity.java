package com.yo.shishkoam.simpleui.activity;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.res.Resources;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.ImageView;

import com.yo.shishkoam.simpleui.BR;
import com.yo.shishkoam.simpleui.R;
import com.yo.shishkoam.simpleui.adapter.RecyclerBindingAdapter;
import com.yo.shishkoam.simpleui.helpers.Consts;
import com.yo.shishkoam.simpleui.managers.MovieManager;
import com.yo.shishkoam.simpleui.model.Movie;

import java.util.List;


/**
 * Created by User on 08.04.2017
 */

public class MainActivity extends AppCompatActivity implements Consts {

    private RecyclerView recyclerView;
    BroadcastReceiver broadcastReceiver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        if (savedInstanceState == null) {
            MovieManager.INSTANCE.init(this, false);
        }

        findViewById(R.id.add_button).setOnClickListener((v) -> {
            Intent intent = new Intent(MainActivity.this, EditMovieActivity.class);
            startActivity(intent);
        });

        recyclerView = (RecyclerView) findViewById(R.id.movies_recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        broadcastReceiver = new BroadcastReceiver() {
            public void onReceive(Context context, Intent intent) {
                int movieID = intent.getIntExtra(MOVIE_ID, 0);
                Movie movie = MovieManager.INSTANCE.getMovie(movieID);
                MovieManager.INSTANCE.deleteMovie(movieID);
                ((RecyclerBindingAdapter) recyclerView.getAdapter()).remove(movie);
                sendNotification();
            }
        };
        IntentFilter intFilt = new IntentFilter(BROADCAST_ACTION);
        registerReceiver(broadcastReceiver, intFilt);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(broadcastReceiver);
    }

    @Override
    protected void onResume() {
        super.onResume();
        setMoviesToRV(MovieManager.INSTANCE.getMovieList());
    }

    private void setMoviesToRV(List<Movie> movies) {
        RecyclerBindingAdapter adapter =
                new RecyclerBindingAdapter<>(R.layout.list_item_movie, BR.movie, movies, false);
        adapter.setOnItemClickListener((position, item, view) -> {
            Intent intent = new Intent(MainActivity.this, EditMovieActivity.class);
//            Intent intent = new Intent(MainActivity.this, DetailsActivity.class);
            intent.putExtra(MOVIE_ID, ((Movie) item).getId());
            startActivity(intent);
        });
        recyclerView.setAdapter(adapter);
        recyclerView.setHasFixedSize(true);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
    }

    public void sendNotification() {
        Context context = getApplicationContext();

        Intent notificationIntent = new Intent(context, MainActivity.class);
        PendingIntent contentIntent = PendingIntent.getActivity(context,
                0, notificationIntent,
                PendingIntent.FLAG_CANCEL_CURRENT);

        Resources res = context.getResources();
        Notification.Builder builder = new Notification.Builder(context);

        builder.setContentIntent(contentIntent)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setLargeIcon(BitmapFactory.decodeResource(res, android.R.drawable.ic_menu_delete))
                .setTicker(getString(R.string.element_deleted))
                .setWhen(System.currentTimeMillis())
                .setAutoCancel(true)
                .setContentTitle(getString(R.string.element_deleting))
                .setContentText(getString(R.string.element_deleted)); // Текст уведомления

        Notification notification = builder.build();
        NotificationManager notificationManager = (NotificationManager) context
                .getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(NOTIFY_ID, notification);
    }
}
