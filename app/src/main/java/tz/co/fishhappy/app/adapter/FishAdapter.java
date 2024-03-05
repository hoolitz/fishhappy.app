package tz.co.fishhappy.app.adapter;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.util.List;

import retrofit2.Call;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import tz.co.fishhappy.app.R;
import tz.co.fishhappy.app.activity.CartActivity;
import tz.co.fishhappy.app.activity.MainActivity;
import tz.co.fishhappy.app.endpoint.CartService;
import tz.co.fishhappy.app.endpoint.EndPointsUrls;
import tz.co.fishhappy.app.endpoint.FavoriteAddService;
import tz.co.fishhappy.app.model.CartModel;
import tz.co.fishhappy.app.model.CartRequestModel;
import tz.co.fishhappy.app.model.CartResponseModel;
import tz.co.fishhappy.app.model.FavoriteAddModel;
import tz.co.fishhappy.app.model.FishModel;
import tz.co.fishhappy.app.utility.Utils;

/**
 * Created by Simon on 30-Apr-17.
 */

public class FishAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private Context mContext;
    private List<FishModel> mList;
    private static final int HEADER = 101;
    private static final int FOOTER = 202;
    private static final int ITEM = 303;
    private FishListener mListener;
    private String mToken;

    public interface FishListener{
        void onAddFavorite(int position, int itemId);
        void onRemoveFavorite(int position, int itemId);
        void onAddOrder(FishModel item);
    }


    public FishAdapter(Context c, List<FishModel> l, FishListener ls, String token){
        this.mContext = c;
        this.mList = l;
        this.mListener = ls;
        this.mToken = token;
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
            return new FishAdapter.FooterHolder(v);
        } else {
            v = inflater.inflate(R.layout.item_list_fish, parent, false);
            return new FishAdapter.ViewHolder(v);
        }
    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder vh, int position) {
            if(vh instanceof FishAdapter.ViewHolder){

                final FishModel model = mList.get(position);
                ((ViewHolder) vh).tvName.setText(model.getName());
                ((ViewHolder) vh).tvPrice.setText(Utils.formatToMoney(
                        String.valueOf(model.getPrice())) + " Tshs");
                        Picasso.get()
//                        .load(model.getPhoto().getUrl()) // This will be used to retrieve image of the fish.
                        .load(Uri.parse("https://hooli.bongomusicawards.co.tz/storage/images/fish%201.jpg"))
                        .error(R.drawable.splash_screen)
                        .placeholder(R.drawable.splash_screen)
                        .into(((ViewHolder) vh).ivPhoto);
                ((ViewHolder) vh).llOrder.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        //TODO add listener for order clicked
                        mListener.onAddOrder(model);
                    }
                });
                ((ViewHolder) vh).ivFavorite.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Picasso.get()
                                .load(R.drawable.ic_action_favorite)
                                .into(((ViewHolder) vh).ivFavorite);
                        //TODO add listener for favorite
                        Gson gson = new GsonBuilder().create();
                        Retrofit retrofit = new Retrofit.Builder()
                                .baseUrl(EndPointsUrls.BASE_URL)
                                .addConverterFactory(GsonConverterFactory.create(gson))
                                .build();
                        FavoriteAddService service = retrofit.create(FavoriteAddService.class);
                        Call call = service.addFavorite("Bearer " + mToken, model.getId());

                        call.enqueue(new retrofit2.Callback<FavoriteAddModel>(){

                            @Override
                            public void onResponse(Call<FavoriteAddModel> call, Response<FavoriteAddModel> response) {
                                if(response.isSuccessful()) {
                                    //System.out.println("Liked Success::"+response.body().getMessage());
                                } else {
                                    Picasso.get()
                                            .load(R.drawable.ic_action_favorite_not)
                                            .into(((ViewHolder) vh).ivFavorite);
                                    /*try {
                                        System.out.println(response.errorBody().string());
                                    } catch (IOException e) {
                                        e.printStackTrace();
                                    }*/
                                }
                            }

                            @Override
                            public void onFailure(Call<FavoriteAddModel> call, Throwable t) {
//                                System.out.println("Liked fail");
                                Toast.makeText(mContext, "Ooops! something went wrong.",Toast.LENGTH_LONG).show();
                                Picasso.get()
                                        .load(R.drawable.ic_action_favorite_not)
                                        .into(((ViewHolder) vh).ivFavorite);
                            }
                        });
                    }
                });
            }
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