package snaprank.example.labdadm.snaprank.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.Toast;

import snaprank.example.labdadm.snaprank.R;

public class RankingFragment extends Fragment implements View.OnClickListener {

    ImageButton ib_filter;

    String category="All";

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        View view = inflater.inflate(R.layout.fragment_ranking,container, false);
        ImageView image = view.findViewById(R.id.main_pic);

        Button user = (Button) view.findViewById(R.id.toUserRanking);
        user.setOnClickListener(this);

        Button photo = (Button) view.findViewById(R.id.toPhotoRanking);
        photo.setOnClickListener(this);

        ib_filter = ((AppCompatActivity)getActivity()).findViewById(R.id.custom_bar_filter);
        ib_filter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showPopUpMenuFilter(v, ib_filter);
            }
        });

        return view;

    }

    public void gotoUserRanking(View view){
        Fragment fragment = new UserRankingFragment();
        FragmentTransaction transaction = getFragmentManager().beginTransaction();
        transaction.replace(R.id.fragmentContainer, fragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }

    public void showPopUpMenuFilter(final View view, ImageButton filter){
        PopupMenu popupMenu = new PopupMenu(getActivity(), filter);
        popupMenu.getMenuInflater().inflate(R.menu.category_filter_menu, popupMenu.getMenu());

        popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            public boolean onMenuItemClick(MenuItem item) {
                category = (String) item.getTitle();
                category = translateCategory(category);
                Toast.makeText(getActivity(),"You Clicked : " + category, Toast.LENGTH_SHORT).show();
                Fragment fragment = new PhotoRankingFragment();
                FragmentTransaction transaction = getFragmentManager().beginTransaction();
                transaction.replace(R.id.fragmentContainer, fragment);
                transaction.addToBackStack(null);
                transaction.commit();
                return true;
            }
        });

        popupMenu.show();
    }

    public String translateCategory(String category){
        switch(category){
            case "Montaña": return "Mountain";
            case "Mar": return "Sea";
            case "Planetas y satélites": return "Planets";
            case "Amigos": return "Friends";
            case "Animales": return "Animales";
            case "Calles": return "Streets";
            case "Vehículos": return "Vehicles";
            case "Comida": return "Food";
            case "Gente": return "People";
            case "Música": return "Music";
            case "Festivales": return "Festivals";
            case "Cultura": return "Culture";
            case "Todo": return "All";
            default: return category;
        }
    }

    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.toUserRanking:
                gotoUserRanking(v);
                break;

            case R.id.toPhotoRanking:
                showPopUpMenuFilter(v, ib_filter);
                break;
        }
    }
}
