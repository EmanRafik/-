package com.abanoubashraf.badawy.BedouinHeritage;

import android.content.Intent;
import android.media.Image;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.abanoubashraf.badawy.Activities.ShowHeritageCategoryTopicActivity;
import com.abanoubashraf.badawy.R;

import java.util.ArrayList;
import java.util.List;


public class BedouinHeritageFragment extends Fragment {

    private TextView heritage_category_headline_main;
    private ImageView heritage_image_main;
    private Button button_discover_more;
    private RecyclerView recyclerView;
    private HeritageAdapter heritageAdapter;
    private List<HeritageCategory> categories;
    private String[] categories_headlines;
    private int current_tag_index = 0;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_bedouin_heritage, container, false);

        heritage_category_headline_main = v.findViewById(R.id.heritage_category_headline_main);
        heritage_image_main = v.findViewById(R.id.heritage_image_main);
        button_discover_more = v.findViewById(R.id.button_discover_more);
        recyclerView=v.findViewById(R.id.recycler_view_bedouin_heritage);
        categories = new ArrayList<>();
        categories_headlines = getContext().getResources().getStringArray(R.array.heritage_categories);

        for (String s : categories_headlines) {
            categories.add(new HeritageCategory(s));
        }
        categories.remove(0);

        heritageAdapter = new HeritageAdapter(getContext(), categories, heritage_image_main, heritage_category_headline_main);

        heritage_category_headline_main.setText(categories.get(0).getHeadline());
        heritage_image_main.setImageResource(categories.get(0).getImage_ID());

        LinearLayoutManager layoutManager
                = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);

        recyclerView.setAdapter(heritageAdapter);
        recyclerView.setLayoutManager(layoutManager);

        button_discover_more.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), ShowHeritageCategoryTopicActivity.class);
                for (int i = 0; i < categories_headlines.length; i++) {
                    if (categories_headlines[i].equals(heritage_category_headline_main.getText().toString())) {
                        current_tag_index = i;
                        break;
                    }
                }
                intent.putExtra("tag_index", String.valueOf(current_tag_index));
                startActivity(intent);
            }
        });

        return v;
    }

}
