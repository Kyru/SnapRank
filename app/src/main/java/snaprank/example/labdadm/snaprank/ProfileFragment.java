package snaprank.example.labdadm.snaprank;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;

import java.util.ArrayList;
import java.util.List;

public class ProfileFragment extends Fragment {

        GridView gridView;
        ImagenSubidaAdapter imagenSubidaAdapter;
        List<ImagenSubida> imagenSubidaList;

        @Nullable
        @Override
        public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
            View view = inflater.inflate(R.layout.fragment_profile,container, false);

            imagenSubidaList = new ArrayList<ImagenSubida>();

            for(int i = 0; i < 10; i++){
                ImagenSubida imagenSubida = new ImagenSubida("id", R.drawable.like);
                imagenSubidaList.add(imagenSubida);
            }

            imagenSubidaAdapter = new ImagenSubidaAdapter(getContext(), R.layout.profile_grid_item, imagenSubidaList);
            gridView = view.findViewById(R.id.profile_grid);
            gridView.setAdapter(imagenSubidaAdapter);

            return view;
        }

}
