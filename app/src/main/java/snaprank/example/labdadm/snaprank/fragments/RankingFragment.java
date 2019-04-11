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

        return view;
    }

    public void gotoUserRanking(View view){
        Fragment fragment = new UserRankingFragment();
        FragmentTransaction transaction = getFragmentManager().beginTransaction();
        transaction.replace(R.id.fragmentContainer, fragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }

    public void gotoPhotoRanking(View view){
        Fragment fragment = new PhotoRankingFragment();
        FragmentTransaction transaction = getFragmentManager().beginTransaction();
        transaction.replace(R.id.fragmentContainer, fragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }

    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.toUserRanking:
                gotoUserRanking(v);
                break;

            case R.id.toPhotoRanking:
                gotoPhotoRanking(v);
                break;
        }
    }
}
