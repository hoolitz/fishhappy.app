package tz.co.fishhappy.app.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;


import com.squareup.picasso.Picasso;

import java.util.List;

import tz.co.fishhappy.app.R;
import tz.co.fishhappy.app.endpoint.EndPointsUrls;
import tz.co.fishhappy.app.model.FavoriteModel;
import tz.co.fishhappy.app.utility.Utils;

/**
 * Created by Simon on 30-Apr-17.
 */

public class FavoriteAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private Context mContext;
    private List<FavoriteModel> mList;
    private static final int HEADER = 101;
    private static final int FOOTER = 202;
    private static final int ITEM = 303;
    private FavoriteListener mListener;

    public interface FavoriteListener{
        void onRemoveFavorite(int position, int itemId);
        void onAddOrder(FavoriteModel item);
    }

    public FavoriteAdapter(Context c, List<FavoriteModel> l, FavoriteListener ls){
        this.mContext = c;
        this.mList = l;
        this.mListener = ls;
    }

    class ViewHolder extends RecyclerView.ViewHolder{

        public TextView tvName, tvPrice;
        public ImageView ivPhoto, ivFavorite;
        public LinearLayout llOrder;

        public ViewHolder(View itemView){
            super(itemView);
            tvName = (TextView) itemView.findViewById(R.id.tvListItemFishName);
            tvPrice = (TextView) itemView.findViewById(R.id.tvListItemFishPrice);
            ivFavorite = (ImageView) itemView.findViewById(R.id.ivListItemFishFavorite);
            ivPhoto = (ImageView) itemView.findViewById(R.id.ivListItemFish);
            llOrder = (LinearLayout) itemView.findViewById(R.id.llListItemFishBuy);
        }
    }

    class FooterHolder extends RecyclerView.ViewHolder {

        public FooterHolder(View v){
            super(v);
        }
    }


    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View v;

        if(viewType == FOOTER) {
            v = inflater.inflate(R.layout.item_list_fish_footer_noline, parent, false);
            return new FavoriteAdapter.FooterHolder(v);
        } else {
            v = inflater.inflate(R.layout.item_list_fish, parent, false);
            return new FavoriteAdapter.ViewHolder(v);
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder vh, final int position) {
        if(vh instanceof FavoriteAdapter.ViewHolder){
            final FavoriteModel model = mList.get(position);
            ((FavoriteAdapter.ViewHolder) vh).tvName.setText(model.getName());
            ((FavoriteAdapter.ViewHolder) vh).tvPrice.setText(Utils.formatToMoney(
                    String.valueOf(model.getPrice())) + " Tshs");
            Picasso.get()
                    .load(R.drawable.ic_action_favorite)
                    .into(((ViewHolder) vh).ivFavorite);
            Picasso.get()
                    .load(R.drawable.splash_screen)
                    .error(R.drawable.splash_screen)
                    .placeholder(R.drawable.splash_screen)
                    .into(((FavoriteAdapter.ViewHolder) vh).ivPhoto);
            ((FavoriteAdapter.ViewHolder) vh).llOrder.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mListener.onAddOrder(model);
                }
            });
            ((FavoriteAdapter.ViewHolder) vh).ivFavorite.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mListener.onRemoveFavorite(position, model.getId());
                }
            });
        }
    }

    public void removeItem(int position){
        mList.remove(position);
        this.notifyItemRemoved(position);
        this.notifyItemRangeChanged(position, mList.size());
    }

    @Override
    public int getItemViewType(int position) {
        if(position == mList.size()){
            return FOOTER;
        }
        return super.getItemViewType(position);
    }

    @Override
    public int getItemCount() {
        return mList != null ? mList.size()+1 : 0;
    }
}