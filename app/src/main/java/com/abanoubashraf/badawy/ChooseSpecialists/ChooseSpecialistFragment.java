package com.abanoubashraf.badawy.ChooseSpecialists;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.abanoubashraf.badawy.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class ChooseSpecialistFragment extends Fragment {

    private EditText editText_search;
    private RecyclerView recyclerView;
    private UsersAdapter usersAdapter;
    private List<User> users;

    private DatabaseReference databaseReference;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_choose_specialist, container, false);
        databaseReference = FirebaseDatabase.getInstance().getReference().child("Users");

        editText_search = v.findViewById(R.id.search_users);
        recyclerView = v.findViewById(R.id.users_recycler_view);
        users = new ArrayList<User>();

        editText_search.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                searchUsers(s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        usersAdapter = new UsersAdapter(getContext(),users);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(usersAdapter);

        return v;
    }

    private void searchUsers(final String s) {
        final FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        Query query = FirebaseDatabase.getInstance().getReference("Users").orderByChild("username")
                .startAt(s)
                .endAt(s + "\uf0ff");

        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (!s.isEmpty()) {
                    users.clear();
                    for (DataSnapshot dss : dataSnapshot.getChildren()) {
                        User user = dss.getValue(User.class);
                        if (!user.getId().equals(firebaseUser.getUid()) && user.getType().equals("b")) {
                            users.add(user);
                        }
                    }

                    usersAdapter = new UsersAdapter(getContext(),users);
                    recyclerView.setAdapter(usersAdapter);
                } else {
                    users.clear();
                    usersAdapter = new UsersAdapter(getContext(),users);
                    recyclerView.setAdapter(usersAdapter);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

}
