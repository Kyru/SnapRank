package snaprank.example.labdadm.snaprank.activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;

import snaprank.example.labdadm.snaprank.R;

public class ViewPicActivity extends AppCompatActivity {

    ImageView iv_imagenSubida;
    int int_imageID;
    String str_imageID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_viewpic);
        getSupportActionBar().hide();

        // Obtener la imagen que se usa para acceder a viewpic y asignarsela
        int_imageID = getIntent().getExtras().getInt("imageID");
        iv_imagenSubida = findViewById(R.id.iv_viewPicImagen);
        iv_imagenSubida.setImageResource(int_imageID);




    }

    public void close(View view){
        onBackPressed();
    }
}
