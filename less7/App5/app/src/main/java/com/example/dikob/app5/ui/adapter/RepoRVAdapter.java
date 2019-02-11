package com.example.dikob.app5.ui.adapter;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import com.jakewharton.rxbinding2.view.RxView;
import com.example.dikob.app5.R;
import com.example.dikob.app5.mvp.presenter.list.IRepoListPresenter;
import com.example.dikob.app5.mvp.view.item.RepoItemView;


public class RepoRVAdapter extends RecyclerView.Adapter<RepoRVAdapter.ViewHolder>
{
    IRepoListPresenter presenter;

    public RepoRVAdapter(IRepoListPresenter presenter)
    {
        this.presenter = presenter;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType)
    {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_repo, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position)
    {
        RxView.clicks(holder.itemView).map(obj -> holder).subscribe(presenter.getClickSubject());
        holder.pos = position;
        presenter.bindView(holder);
    }

    @Override
    public int getItemCount()
    {
        return presenter.getRepoCount();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements RepoItemView {

        int pos = 0;

        @BindView(R.id.tv_title) TextView titleTextView;

        public ViewHolder(View itemView)
        {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        @Override
        public int getPos() {
            return pos;
        }

        @Override
        public void setTitle(String title)
        {
            titleTextView.setText(title);
        }
    }
}
