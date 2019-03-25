package snaprank.example.labdadm.snaprank;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
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

public class HomeFeedFragment extends Fragment {

    ImageButton ib_filter;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home,container, false);
        ImageView image = view.findViewById(R.id.main_pic);
        image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                gotoViewPic(v);
            }
        });


        ib_filter = ((AppCompatActivity)getActivity()).findViewById(R.id.custom_bar_filter);
        ib_filter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //showPopUpMenuFilter(v, ib_filter);
                showPopUpFilter(v);
            }
        });

        return view;

    }

    public void gotoViewPic(View view){
        Intent intent = new Intent(getContext(), ViewPic.class);
        startActivity(intent);
    }

    // Metodo hecho cuando funcionaba con el popupmenu, ahora trabajará con un popup window custom
    public void showPopUpMenuFilter(View view, ImageButton filter){
        PopupMenu popupMenu = new PopupMenu(getActivity(), filter);
        popupMenu.getMenuInflater().inflate(R.menu.category_filter_menu, popupMenu.getMenu());

        popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            public boolean onMenuItemClick(MenuItem item) {
                Toast.makeText(getActivity(),"You Clicked : " + item.getTitle(), Toast.LENGTH_SHORT).show();
                return true;
            }
        });

        popupMenu.show();
    }

    public void showPopUpFilter(View view){
        Intent popUpFilter = new Intent(getActivity(), PopUpFilter.class);
        startActivity(popUpFilter);
    }
}
