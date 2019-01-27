package com.example.dikob.app4.ui.adapter;

import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.RecyclerView.ViewHolder;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.dikob.app4.R;
import com.example.dikob.app4.mvp.model.entity.User;
import com.example.dikob.app4.mvp.presenter.IUsersPresenter;
import com.example.dikob.app4.mvp.view.item.UserItemView;
import com.jakewharton.rxbinding2.view.RxView;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class UsersAdapter extends RecyclerView.Adapter<UsersAdapter.ViewHolder> {

    IUsersPresenter presenter;

    public UsersAdapter(IUsersPresenter presenter){
        this.presenter = presenter;
    }

    @Override
    public UsersAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        return new ViewHolder(LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_user, viewGroup, false));
    }

    @Override
    public void onBindViewHolder(@NonNull UsersAdapter.ViewHolder viewHolder, int i) {
//        RxView.clicks(viewHolder.itemView).map(o -> viewHolder).subscribe(presenter.getClickSubject());
        viewHolder.pos = i;
        presenter.bindView(viewHolder);
    }
    

    @Override
    public int getItemCount() {
        return presenter.getReposCount();
    }


    public class ViewHolder extends RecyclerView.ViewHolder implements UserItemView {
        @BindView(R.id.tv_item)
        TextView tVrepo;

        int pos = 0;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        @Override
        public void setRepo(String repo) {
            tVrepo.setText(repo);
        }

        @Override
        public int getPos() {
            return pos;
        }
    }
}
