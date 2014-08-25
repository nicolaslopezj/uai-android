package cl.uai.uai.buses;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;

import cl.uai.uai.R;
import cl.uai.uai.main.BaseFragment;

/**
 * Created by nicolaslopezj on 24-08-14.
 */
public class BusesSlideFragment extends BaseFragment {


    public static BusesSlideFragment create() {
        BusesSlideFragment fragment = new BusesSlideFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    public BusesSlideFragment() {

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public android.view.View onCreateView(LayoutInflater inflater, ViewGroup container,
                                          Bundle savedInstanceState) {
        // Inflate the layout containing a title and body text.
        ViewGroup rootView;

        rootView = (ViewGroup) inflater.inflate(R.layout.buses_detail, container, false);

        return rootView;
    }

}