package com.example.dikob.app5.ui.fragment;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.arellomobile.mvp.MvpAppCompatFragment;
import com.arellomobile.mvp.presenter.InjectPresenter;
import com.arellomobile.mvp.presenter.ProvidePresenter;
import com.example.dikob.app5.App;
import com.example.dikob.app5.R;
import com.example.dikob.app5.mvp.model.image.ImageLoader;
import com.example.dikob.app5.mvp.model.image.android.ImageLoaderGlide;
import com.example.dikob.app5.mvp.presenter.MainPresenter;
import com.example.dikob.app5.mvp.view.MainView;
import com.example.dikob.app5.ui.activity.MainActivity;
import com.example.dikob.app5.ui.adapter.RepoRVAdapter;

import android.support.annotation.Nullable;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import javax.inject.Inject;
import javax.inject.Named;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.reactivex.android.schedulers.AndroidSchedulers;

public class MainFragment extends MvpAppCompatFragment implements MainView {
    @Inject @Named("glide") ImageLoader<ImageView> imageLoader;

    public static MainFragment getInstance(String arg){
        MainFragment fragment = new MainFragment();
        Bundle args = new Bundle();
        args.putString("arg", arg);
        fragment.setArguments(args);
        return fragment;
    }
    @BindView(R.id.iv_avatar)
    ImageView avatarImageView;
    @BindView(R.id.tv_error)
    TextView errorTextView;
    @BindView(R.id.tv_username)
    TextView usernameTextView;
    @BindView(R.id.pb_loading)
    ProgressBar loadingProgressBar;
    @BindView(R.id.rv_repos)
    RecyclerView reposRecyclerView;

    RepoRVAdapter adapter;

    @InjectPresenter
    MainPresenter presenter;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @android.support.annotation.Nullable ViewGroup container, @android.support.annotation.Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_main, null);
        ButterKnife.bind(this, view);
        App.getInstance().getAppComponent().inject(this);

        reposRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        reposRecyclerView.addItemDecoration(new DividerItemDecoration(getActivity(), DividerItemDecoration.VERTICAL));
        return view;
    }

    @Override
    public void init() {
        adapter = new RepoRVAdapter(presenter.repoListPresenter);
        reposRecyclerView.setAdapter(adapter);
    }

    @ProvidePresenter
    public MainPresenter provideMainPresenter() {

        String arg = getArguments().getString("arg");
        MainPresenter presenter = new MainPresenter(AndroidSchedulers.mainThread());
        App.getInstance().getAppComponent().inject(presenter);
        return presenter;
    }

    @Override
    public void showAvatar(String avatarUrl) {
        imageLoader.loadInto(avatarUrl, avatarImageView);
    }

    @Override
    public void showError(String message) {
        errorTextView.setText(message);
    }

    @Override
    public void showLoading() {
        loadingProgressBar.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideLoading() {
        loadingProgressBar.setVisibility(View.GONE);
    }

    @Override
    public void updateRepoList() {
        adapter.notifyDataSetChanged();
    }


    @Override
    public void setUsername(String username) {
        usernameTextView.setText(username);
    }
}
