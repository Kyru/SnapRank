package snaprank.example.labdadm.snaprank.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;

import snaprank.example.labdadm.snaprank.activities.ViewProfileActivity;
import snaprank.example.labdadm.snaprank.models.ImageRanking;
import snaprank.example.labdadm.snaprank.adapters.ImageRankingAdapter;
import snaprank.example.labdadm.snaprank.R;

import java.util.ArrayList;
import java.util.List;

public class UserRankingFragment extends Fragment {

    GridView gridView;
    ImageRankingAdapter imageRankingAdapter;
    List<ImageRanking> imageRankingList;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_user_ranking,container, false);

        imageRankingList = new ArrayList<ImageRanking>();

        for(int i = 0; i < 40; i++){
            ImageRanking imageRanking = new ImageRanking("id", R.drawable.taylor);
            imageRankingList.add(imageRanking);
        }

        imageRankingAdapter = new ImageRankingAdapter(getContext(), R.layout.ranking_grid_item, imageRankingList);
        gridView = view.findViewById(R.id.userRanking_grid);
        gridView.setAdapter(imageRankingAdapter);

        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                gotoProfile(view, position);
            }
        });

        return view;
    }

    public void gotoProfile(View view, int position){
        Intent intent = new Intent(getContext(), ViewProfileActivity.class);
        startActivity(intent);
    }
}
