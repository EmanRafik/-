package com.abanoubashraf.badawy.BedouinHeritage;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.abanoubashraf.badawy.R;

import java.util.List;


public class HeritageAdapter extends RecyclerView.Adapter<HeritageAdapter.MyViewHolder> {
    private Context mContext;
    private List<HeritageCategory> categpries;
    private ImageView imageView_main;
    private TextView textView_headline_main;

    public HeritageAdapter(Context mContext, List<HeritageCategory> categpries,
                           ImageView imageView_main, TextView textView_headline_main) {
        this.mContext = mContext;
        this.categpries = categpries;
        this.imageView_main = imageView_main;
        this.textView_headline_main = textView_headline_main;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View row = LayoutInflater.from(mContext).inflate(R.layout.heritage_category_item,parent,false);
        return new MyViewHolder(row);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        holder.headline.setText(categpries.get(position).getHeadline());
        holder.image.setImageResource(categpries.get(position).getImage_ID());
    }

    @Override
    public int getItemCount() {
        return categpries.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder{
        TextView headline;
        ImageView image,imageView_clickable;

        public MyViewHolder(@NonNull final View itemView) {
            super(itemView);
            headline = itemView.findViewById(R.id.heritage_category_headline);
            image = itemView.findViewById(R.id.heritage_image);
            imageView_clickable = itemView.findViewById(R.id.tf_overlay);

            imageView_clickable.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
//                    Toast.makeText(mContext, "ok", Toast.LENGTH_SHORT).show();
                    imageView_main.setImageDrawable(image.getDrawable());
                    textView_headline_main.setText(headline.getText());
                }
            });
        }
    }

}

