package com.abanoubashraf.badawy.ChooseSpecialists;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.abanoubashraf.badawy.Helpers.SharedHelper;
import com.abanoubashraf.badawy.R;
import com.bumptech.glide.Glide;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.List;

public class UsersAdapter extends RecyclerView.Adapter<UsersAdapter.MyViewHolder> {
    private Context mContext;
    private List<User> users;
    private DatabaseReference databaseReference;

    public UsersAdapter(Context mContext, List<User> users) {
        this.mContext = mContext;
        this.users = users;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View row = LayoutInflater.from(mContext).inflate(R.layout.user_item,parent,false);
        return new MyViewHolder(row);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        holder.username.setText(users.get(position).getUsername());
        holder.tribe.setText(String.valueOf(users.get(position).getTribe()));
//        holder.relativeLayout.setAnimation(AnimationUtils.loadAnimation(mContext,R.anim.fade_scale_animation));

        if (users.get(position).getImage_URL().equals("default")) {
            holder.image.setImageResource(R.drawable.default_pp);
        } else {
            Glide.with(mContext).load(users.get(position).getImage_URL()).into(holder.image);
        }

        //prevent user from voting more than one time and disable voting if verified
        if (users.get(position).getVoters_ids().contains(SharedHelper.getSharedHelper(mContext).getCurrentUser().getId())
                || users.get(position).is_verified()) {
            holder.button_vote.setEnabled(false);
        }

    }

    @Override
    public int getItemCount() {
        return users.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder{
        TextView username;
        TextView tribe;
        ImageView image;
        Button button_vote;
        CardView layout;

        public MyViewHolder(@NonNull final View itemView) {
            super(itemView);
            username = itemView.findViewById(R.id.textView_username);
            tribe = itemView.findViewById(R.id.textView_tribe);
            image = itemView.findViewById(R.id.imageView_user);
            button_vote = itemView.findViewById(R.id.button_vote);
            layout = itemView.findViewById(R.id.relative_layout_users);

            button_vote.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    final User user = users.get(getAdapterPosition());

                    new AlertDialog.Builder(mContext)
                            .setMessage(mContext.getString(R.string.do_you_want_to_vote) + " " + user.getUsername() + " "
                                    + mContext.getString(R.string.to_be_a_specialist_bedouin))
                            .setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface paramDialogInterface, int paramInt) {

                                    databaseReference = FirebaseDatabase.getInstance().getReference("Users").child(user.getId());

                                    user.getVoters_ids().add(SharedHelper.getSharedHelper(mContext).getCurrentUser().getId());
                                    if (user.getVoters_ids().size() == 10) {
                                        user.setIs_verified(true);
                                    }

                                    final HashMap<String, Object> users = new HashMap<>();
                                    users.put("voters_ids", user.getVoters_ids());
                                    databaseReference.updateChildren(users);
                                    button_vote.setEnabled(false);
                                }
                            }).setNegativeButton(R.string.no, null).show();
                }
            });
        }
    }

}

