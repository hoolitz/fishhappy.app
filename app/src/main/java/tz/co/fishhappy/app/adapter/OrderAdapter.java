package tz.co.fishhappy.app.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import java.util.List;

import tz.co.fishhappy.app.R;
import tz.co.fishhappy.app.model.CartResponseModel;
import tz.co.fishhappy.app.model.FavoriteModel;
import tz.co.fishhappy.app.model.OrderModel;
import tz.co.fishhappy.app.model.Orders;
import tz.co.fishhappy.app.utility.Utils;

/**
 * Created by Simon on 30-Apr-17.
 */

public class OrderAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private List<Orders> mList;
    private Context mContext;
    private OrderListener mListener;
    private static final int HEADER = 101;
    private static final int FOOTER = 202;
    private static final int ITEM = 303;

    public interface OrderListener{
        void onCancelOrder(int position, int itemId);
    }

    public OrderAdapter(Context c, List<Orders> l, OrderListener ls){
        this.mList = l;
        this.mContext = c;
        this.mListener = ls;
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        public TextView tvOrderStatus, tvOrderItems, tvOrderTotal;
        public Button bnCancelOrder;
        public ViewHolder(View itemView){
            super(itemView);
            tvOrderItems = (TextView) itemView.findViewById(R.id.tvOrderItem);
            tvOrderStatus = (TextView) itemView.findViewById(R.id.tvOrderStatus);
            tvOrderTotal = (TextView) itemView.findViewById(R.id.tvOrderTotal);
            bnCancelOrder = (Button) itemView.findViewById(R.id.bnCancelOrder);
        }
    }

    class FooterHolder extends RecyclerView.ViewHolder {

        public FooterHolder(View v){
            super(v);
        }
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType){
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View v;

        if(viewType == FOOTER) {
            v = inflater.inflate(R.layout.item_list_fish_footer_noline, parent, false);
            return new OrderAdapter.FooterHolder(v);
        } else {
            v = inflater.inflate(R.layout.item_list_order, parent, false);
            return new OrderAdapter.ViewHolder(v);
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder vh, final int position){
        if(vh instanceof OrderAdapter.ViewHolder) {
            final Orders model = mList.get(position);

            int total = 0;
            String items = "";
            for (int i = 0; i < model.getProducts().size(); i++) {
                int price = model.getProducts().get(i).getPrice();
                int qty = model.getProducts().get(i).getPivot().getQuantity();
                total += price * qty;

                items += model.getProducts().get(i).getName() + "~"
                        + model.getProducts().get(i).getPivot().getQuantity();
                int j = i;
                if (j + 1 != model.getProducts().size()) {
                    items += ", ";
                }
            }

            ((ViewHolder) vh).tvOrderTotal.setText(Utils.formatToMoney(String.valueOf(total)) + " Tshs");
            ((ViewHolder) vh).tvOrderItems.setText(items);
            ((ViewHolder) vh).tvOrderStatus.setText(model.getStatus());
            ((ViewHolder) vh).bnCancelOrder.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mListener.onCancelOrder(position, model.getId());
                }
            });

            switch (model.getStatus()) {
                case "cancelled":
                    ((ViewHolder) vh).tvOrderStatus.setTextColor(mContext.getResources().getColor(R.color.brown_300));
                    break;
                case "pending":
                    ((ViewHolder) vh).tvOrderStatus.setTextColor(mContext.getResources().getColor(R.color.orange_500));
                    break;
            }
        }
    }

    public void removeItem(int position){
        mList.remove(position);
        this.notifyItemRemoved(position);
        this.notifyItemRangeChanged(position, mList.size());
    }

    @Override
    public int getItemCount(){
        return mList != null ? mList.size() : 0;
    }
}
