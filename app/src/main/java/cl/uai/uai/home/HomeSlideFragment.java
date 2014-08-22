package cl.uai.uai.home;

import android.graphics.Bitmap;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;

import cl.uai.uai.R;
import cl.uai.uai.main.BaseFragment;

/**
 * Created by nicolaslopezj on 30-07-14.
 */
public class HomeSlideFragment extends BaseFragment {

    protected ImageLoader imageLoader = ImageLoader.getInstance();
    DisplayImageOptions options;

    public static final String ARG_IMAGE_URL = "image";
    private String mImageUrl;

    public static HomeSlideFragment create(String imageUrl) {
        HomeSlideFragment fragment = new HomeSlideFragment();
        Bundle args = new Bundle();
        args.putString(ARG_IMAGE_URL, imageUrl);
        fragment.setArguments(args);
        return fragment;
    }

    public HomeSlideFragment() {
        options = new DisplayImageOptions.Builder()
                .cacheInMemory(true)
                .cacheOnDisk(true)
                .considerExifParams(true)
                .bitmapConfig(Bitmap.Config.RGB_565)
                .build();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mImageUrl = getArguments().getString(ARG_IMAGE_URL);
    }

    @Override
    public android.view.View onCreateView(LayoutInflater inflater, ViewGroup container,
                                          Bundle savedInstanceState) {
        // Inflate the layout containing a title and body text.
        ViewGroup rootView;

        rootView = (ViewGroup) inflater.inflate(R.layout.home_page_slide, container, false);

        ImageView imageView = (ImageView) rootView.findViewById(R.id.slideImageView);
        imageLoader.displayImage(mImageUrl, imageView, options);

        return rootView;
    }

}
