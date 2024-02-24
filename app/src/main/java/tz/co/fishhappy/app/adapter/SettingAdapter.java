package tz.co.fishhappy.app.adapter;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import tz.co.fishhappy.app.R;
import tz.co.fishhappy.app.activity.AboutActivity;
import tz.co.fishhappy.app.activity.ChangePasswordActivity;
import tz.co.fishhappy.app.activity.LoginActivity;
import tz.co.fishhappy.app.activity.NotificationActivity;
import tz.co.fishhappy.app.activity.UserAccountActivity;
import tz.co.fishhappy.app.activity.WebViewActivity;
import tz.co.fishhappy.app.model.SettingsModel;

/**
 * Created by Simon on 30-Apr-17.
 */

public class SettingAdapter extends RecyclerView.Adapter<SettingAdapter.ViewHolder>{

    private List<SettingsModel> mList;
    private Context mContext;
    private SettingListener mListener;

    public interface SettingListener {
        void onLogout();
    }

    public SettingAdapter(Context c, List<SettingsModel> l, SettingListener ls){
        this.mList = l;
        this.mContext = c;
        this.mListener = ls;
    }

    class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        public TextView tvTitle;
        public ImageView ivIcon;

        public ViewHolder(View v){
            super(v);
            tvTitle = (TextView) v.findViewById(R.id.tvSettingItemTitle);
            ivIcon = (ImageView) v.findViewById(R.id.ivSettingItemIcon);
            v.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            switch (getAdapterPosition()){
                case 0:
                    mContext.startActivity(new Intent(mContext, UserAccountActivity.class));
                    break;
                case 1:
                    mContext.startActivity(new Intent(mContext, ChangePasswordActivity.class));
                    break;
                case 2:
                    mContext.startActivity(new Intent(mContext, NotificationActivity.class));
                    break;
                case 3:
                    mContext.startActivity(new Intent(mContext, WebViewActivity.class)
                            .putExtra("url", "http://http://174.138.74.195/")
                            .putExtra("title","Privacy Policy"));
                    break;
                case 4:
                    mContext.startActivity(new Intent(mContext, WebViewActivity.class)
                            .putExtra("url","http://http://174.138.74.195/")
                            .putExtra("title", "Terms of Service"));
                    break;
                case 5:
                    mContext.startActivity(new Intent(mContext, WebViewActivity.class)
                            .putExtra("url","http://http://174.138.74.195/")
                            .putExtra("title", "Frequently Asked Question"));
                    break;
                case 6:
                    mContext.startActivity(new Intent(mContext, AboutActivity.class));
                    break;
                case 7:
                    mListener.onLogout();
                    break;
            }
        }
    }

    @Override
    public SettingAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());

        View view = inflater.inflate(R.layout.item_list_setting, parent, false);

        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(SettingAdapter.ViewHolder holder, int position) {
        SettingsModel model = mList.get(position);

        holder.tvTitle.setText(model.getTitle());
        Picasso.get()
                .load(model.getIcon())
                .into(holder.ivIcon);

    }

    @Override
    public int getItemCount() {
        return mList == null ? 0 : mList.size();
    }
}
