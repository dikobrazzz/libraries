package com.example.dikob.app4.ui;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.arellomobile.mvp.MvpAppCompatActivity;
import com.arellomobile.mvp.presenter.InjectPresenter;
import com.arellomobile.mvp.presenter.ProvidePresenter;
import com.bumptech.glide.Glide;
import com.example.dikob.app4.R;
import com.example.dikob.app4.mvp.model.image.IImageLoader;
import com.example.dikob.app4.mvp.presenter.MainPresenter;
import com.example.dikob.app4.mvp.view.MainView;
import com.example.dikob.app4.ui.adapter.UsersAdapter;
import com.example.dikob.app4.ui.image.GlideImageLoader;
import com.squareup.picasso.Picasso;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.reactivex.Single;
import io.reactivex.android.schedulers.AndroidSchedulers;
import okhttp3.OkHttpClient;
import okhttp3.Request;

public class MainActivity extends MvpAppCompatActivity implements MainView {
    private String text;
    UsersAdapter adapter;

    @BindView(R.id.recycler)
    RecyclerView recyclerView;

    @BindView(R.id.iv_avatar)
    ImageView avatar;

    @BindView(R.id.tv_username)
    TextView username;

    @InjectPresenter
    MainPresenter presenter;

    IImageLoader<ImageView> loader = new GlideImageLoader();

    @ProvidePresenter
    public MainPresenter provideMainPresenter(){
        return new MainPresenter(AndroidSchedulers.mainThread());
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        init();
    }

    @Override
    public void init() {
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));
        adapter = new UsersAdapter(presenter.getListPresenter());
        recyclerView.setAdapter(adapter);
    }

    @Override
    public void setImageUrl(String url) {
        loader.loadInto(url, avatar);
    }

    @Override
    public void setUsersText(String text) {
        username.setText(text);
    }

    @Override
    public void showRepos(List<String> repos) {
        adapter.notifyDataSetChanged();
    }

    @Override
    public void updateUsersList() {
        adapter.notifyDataSetChanged();
    }

}
