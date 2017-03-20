package com.mnf.locate.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.mnf.locate.MainActivity;
import com.mnf.locate.R;
import com.mnf.locate.model.MenuModel;

import java.util.List;

/**
 * Created by Muneef on 29/10/15.
 */
public class MainMenuAdapter extends RecyclerView.Adapter<MainMenuAdapter.ViewHolder> {

    public static class ViewHolder extends RecyclerView.ViewHolder {
        // each data item is just a string in this case
        public View viewi;
        public TextView title,ads;
        ImageView image;
        CardView menuItemCard;
        RelativeLayout rel;
        public ViewHolder(View v) {
            super(v);
            menuItemCard = (CardView) v.findViewById(R.id.menu_item_card_main);
            rel = (RelativeLayout) v.findViewById(R.id.cont_relative);
            title = (TextView) v.findViewById(R.id.title_menu);
            //ads = (TextView) v.findViewById(R.id.txtads);
            image = (ImageView) v.findViewById(R.id.item_img);
            // binding = _binding;
            //viewi = v;

        }
    }
    Context c;
    List<MenuModel> mDataset;
    public MainMenuAdapter(Context context, List<MenuModel> model){
        this.c= context;
        this.mDataset = model;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v;
        v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.main_menu_item, parent, false);
        ViewHolder vh = new ViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {



       // holder.menuItemCard.setBackgroundColor(c.getResources().getColor(android.R.color.white));
        holder.title.setText(mDataset.get(position).title);
      //  holder.ads.setText(mDataset.get(position).Ads);
       // holder.image.setImageResource(mDataset.get(position).image);

        holder.menuItemCard.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                Log.e("tagAd", "inside on touch  motion event  = " + motionEvent.getAction());
                switch (motionEvent.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                         holder.menuItemCard.setBackgroundColor(c.getResources().getColor(R.color.grey500));
                        holder.rel.setBackgroundColor(c.getResources().getColor(R.color.grey500));
                        Log.e("tagAd","inside Down press");
                        break;
                    case MotionEvent.ACTION_UP:
                        holder.menuItemCard.setBackgroundColor(c.getResources().getColor(android.R.color.white));
                        holder.rel.setBackgroundColor(c.getResources().getColor(android.R.color.white));
                        Log.e("tagAd", "inside Up press");
                        Intent mainAct = new Intent(c, MainActivity.class);
                        mainAct.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        mainAct.putExtra("bus_id", mDataset.get(position).id);
                        c.startActivity(mainAct);
                      //  ((FragmentActivity)c).getSupportFragmentManager().beginTransaction().replace(R.id.main_container,AdsListFragment.getInstance(mDataset.get(position).id)).commit();

                        break;
                    case MotionEvent.ACTION_CANCEL:
                        holder.menuItemCard.setBackgroundColor(c.getResources().getColor(android.R.color.white));
                        holder.rel.setBackgroundColor(c.getResources().getColor(android.R.color.white));
                        Log.e("tagAd", "inside cancel press");
                        break;

                }



          /*      Intent str = new Intent(c, TvDetailActivity.class);
                //str.putextra("your_extra","your_class_value");
                str.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                str.putExtra("id", mData.get(position).getId().toString());
                c.startActivity(str);
               /* if(motionEvent.getAction()==MotionEvent.ACTION_DOWN){
                    holder.menuItemCard.setBackgroundColor(c.getResources().getColor(R.color.grey500));
                    Log.e("tagAd","inside Down press");
                }else{
                    holder.menuItemCard.setBackgroundColor(c.getResources().getColor(android.R.color.white));
                    Log.e("tagAd", "inside other  press = " + motionEvent.getAction());
                }*/

                return true;
            }
        });

    }

    @Override
    public int getItemCount() {
        return mDataset.size();
    }


}
