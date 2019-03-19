package snaprank.example.labdadm.snaprank;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;

public class HomeFeedFragment extends Fragment {

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

        return view;

    }

    public void gotoViewPic(View view){
        Intent intent = new Intent(getContext(), ViewPic.class);
        startActivity(intent);
    }
}
