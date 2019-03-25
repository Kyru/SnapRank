package snaprank.example.labdadm.snaprank;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;

import java.util.ArrayList;
import java.util.List;

public class UserRankingFragment extends Fragment {

    GridView gridView;
    ImagenRankingAdapter imagenRankingAdapter;
    List<ImagenRanking> imagenRankingList;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_user_ranking,container, false);

        imagenRankingList = new ArrayList<ImagenRanking>();

        for(int i = 0; i < 40; i++){
            ImagenRanking imagenRanking = new ImagenRanking("id", R.drawable.taylor);
            imagenRankingList.add(imagenRanking);
        }

        imagenRankingAdapter = new ImagenRankingAdapter(getContext(), R.layout.ranking_grid_item, imagenRankingList);
        gridView = view.findViewById(R.id.userRanking_grid);
        gridView.setAdapter(imagenRankingAdapter);

        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                gotoProfile(view, position);
            }
        });

        return view;
    }

    public void gotoProfile(View view, int position){
        Intent intent = new Intent(getContext(), ViewProfile.class);
        startActivity(intent);
    }
}
