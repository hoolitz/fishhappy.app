package tz.co.fishhappy.app.adapter;

import android.content.Context;
import android.os.AsyncTask;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import io.realm.Realm;
import io.realm.RealmResults;
import tz.co.fishhappy.app.R;
import tz.co.fishhappy.app.data.CartRealmObject;
import tz.co.fishhappy.app.model.CartModel;
import tz.co.fishhappy.app.utility.Utils;

/**
 * Created by Simon on 04-May-17.
 */

public class CartAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private Context mContext;
    private RealmResults<CartRealmObject> mList;
    private static final int HEADER = 101;
    private static final int FOOTER = 202;
    private static final int ITEM = 303;
    private CartListener mListener;
    private Realm mRealm;

    public interface CartListener {
        void quantityChange();
        void removeItem(int position, int itemId);
    }

    public CartAdapter(Context c, RealmResults<CartRealmObject> l, CartListener ls){
        this.mContext = c;
        this.mList = l;
        this.mListener = ls;
        mRealm = Realm.getDefaultInstance();
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        public TextView tvName, tvPrice, tvSubTotal, tvQuantity;
        public ImageView ivAddQty, ivReduceQty, ivRemove;

        public ViewHolder(View v){
            super(v);
            tvName = (TextView) v.findViewById(R.id.tvCartItemName);
            tvPrice = (TextView) v.findViewById(R.id.tvCartItemPrice);
            tvSubTotal = (TextView) v.findViewById(R.id.tvCartItemSubTotal);
            tvQuantity = (TextView) v.findViewById(R.id.tvCartItemQuantity);
            ivAddQty = (ImageView) v.findViewById(R.id.ivCartItemAddQty);
            ivReduceQty = (ImageView) v.findViewById(R.id.ivCartItemReduceQty);
            ivRemove = (ImageView) v.findViewById(R.id.ivCartItemRemove);
        }
    }

    class FooterHolder extends RecyclerView.ViewHolder {

        public FooterHolder(View v){
            super(v);
        }
    }

    class HeaderHolder extends RecyclerView.ViewHolder {

        public HeaderHolder(View v){
            super(v);
        }
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType){
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View v;

        if(viewType == FOOTER) {
            v = inflater.inflate(R.layout.item_list_cart_footer, parent, false);
            return new FooterHolder(v);
        } else if(viewType == HEADER) {
            v = inflater.inflate(R.layout.item_list_cart_header, parent, false);
            return new HeaderHolder(v);
        } else {
            v = inflater.inflate(R.layout.item_list_cart, parent, false);
            return new ViewHolder(v);
        }

    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder vh, final int position){

        if( vh instanceof ViewHolder){
            try {
                final CartRealmObject model = mList.get(position-1);

                ((ViewHolder) vh).tvName.setText(model.getName());
                ((ViewHolder) vh).tvPrice.setText(Utils.formatToMoney(String.valueOf(model.getPrice())) + " Tshs");
                ((ViewHolder) vh).tvQuantity.setText(String.valueOf(model.getQuantity()));

                int subTotal = model.getPrice() * model.getQuantity();

                ((ViewHolder) vh).tvSubTotal.setText(Utils.formatToMoney(String.valueOf(subTotal))+" Tshs");

                ((ViewHolder) vh).ivAddQty.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String quantity = ((ViewHolder) vh).tvQuantity.getText().toString();
                        int qty = Integer.valueOf(quantity);
                        if(qty == 999){
                            return;
                        }
                        if(qty > 1){
                            qty++;
                        }
                        int subTotal = model.getPrice() * qty;
                        ((ViewHolder) vh).tvSubTotal.setText(Utils.formatToMoney(
                                String.valueOf(subTotal)) + " Tshs");
                        ((ViewHolder) vh).tvQuantity.setText(String.valueOf(qty));

                        mRealm.executeTransaction(new Realm.Transaction() {
                            @Override
                            public void execute(Realm realm) {
                                CartRealmObject order = new CartRealmObject();
                                if (model != null) {
                                    order.setQuantity(model.getQuantity()+1);
                                    order.setId(model.getId());
                                    order.setCreatedAt(model.getCreatedAt());
                                    order.setPrice(model.getPrice());
                                    order.setName(model.getName());
                                    order.setUpdatedAt(System.currentTimeMillis());
                                }
                                realm.insertOrUpdate(order);
                            }
                        });
                        mListener.quantityChange();
                    }
                });
                ((ViewHolder) vh).ivReduceQty.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String quantity = ((ViewHolder) vh).tvQuantity.getText().toString();
                        int qty = Integer.valueOf(quantity);
                        if(qty == 1){
                            return;
                        }
                        if(qty > 1){
                            qty--;
                        }
                        int subTotal = model.getPrice() * qty;
                        ((ViewHolder) vh).tvSubTotal.setText(Utils.formatToMoney(
                                String.valueOf(subTotal)) + " Tshs");
                        ((ViewHolder) vh).tvQuantity.setText(String.valueOf(qty));

                        mRealm.executeTransaction(new Realm.Transaction() {
                            @Override
                            public void execute(Realm realm) {
                                CartRealmObject order = new CartRealmObject();
                                if (model != null) {
                                    order.setQuantity(model.getQuantity()-1);
                                    order.setId(model.getId());
                                    order.setCreatedAt(model.getCreatedAt());
                                    order.setPrice(model.getPrice());
                                    order.setName(model.getName());
                                    order.setUpdatedAt(System.currentTimeMillis());
                                }
                                realm.insertOrUpdate(order);
                            }
                        });
                        mListener.quantityChange();
                    }
                });
                ((ViewHolder) vh).ivRemove.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mListener.removeItem(position, model.getId());
                    }
                });

            } catch (Exception e){
                e.printStackTrace();
            }
        }
    }

    public void removeItem(int position){
        this.notifyItemRemoved(position);
        this.notifyItemRangeChanged(position, mList.size());
    }

    @Override
    public int getItemViewType(int position) {

        if(position == mList.size()+1){
            return FOOTER;
        } else if (position == 0){
            return HEADER;
        } else {
            return ITEM;
        }
    }

    @Override
    public int getItemCount(){
        return mList != null ? (mList.size()+2) : 0;
    }
}
