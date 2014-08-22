package cl.uai.uai.home;

import android.support.v4.app.Fragment;
import android.content.res.TypedArray;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;

//import com.viewpagerindicator.CirclePageIndicator;

import com.octo.android.robospice.persistence.DurationInMillis;
import com.octo.android.robospice.persistence.exception.SpiceException;
import com.octo.android.robospice.request.listener.RequestListener;
import com.viewpagerindicator.CirclePageIndicator;

import java.lang.reflect.Array;
import java.util.zip.Inflater;

import cl.uai.uai.R;
import cl.uai.uai.api.HomeImagesRequest;
import cl.uai.uai.main.BaseFragment;

/**
 * Created by nicolaslopezj on 29-07-14.
 */
public class Home extends BaseFragment {

    private String[] homeImages;
    private ViewPager mPager;
    private CirclePageIndicator mIndicator;
    private PagerAdapter mPagerAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        homeImages = new String[0];

        View layout = inflater.inflate(R.layout.home, null);


        // Instantiate a ViewPager and a PagerAdapter.
        mPager = (ViewPager) layout.findViewById(R.id.homePager);
        mPagerAdapter = new ScreenSlidePagerAdapter(getFragmentManager());
        mPager.setAdapter(mPagerAdapter);
        mPager.setOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
            @Override
            public void onPageSelected(int position) {
                //invalidateOptionsMenu();
            }
        });


        //Bind the title indicator to the adapter
        mIndicator = (CirclePageIndicator) layout.findViewById(R.id.indicator);
        mIndicator.setViewPager(mPager);

        performRequest();
        return layout;
    }

    public void setHomeImages(String[] _homeImages) {
        homeImages = _homeImages;
        mPagerAdapter.notifyDataSetChanged();
    }

    private void performRequest() {
        activity.setProgressBarIndeterminateVisibility(true);

        HomeImagesRequest request = new HomeImagesRequest();
        String lastRequestCacheKey = request.createCacheKey();

        spiceManager.execute(request, lastRequestCacheKey, DurationInMillis.ONE_MINUTE, new HomeImagesRequestListener());
    }

    private class HomeImagesRequestListener implements RequestListener<String[]> {

        @Override
        public void onRequestFailure(SpiceException e) {
            //update your UI
            Log.v("Request Error", e.toString());
            showError("Ocurrió un error al descargar los datos");
        }

        @Override
        public void onRequestSuccess(String[] response) {
            //update your UI
            setHomeImages(response);
        }
    }

    private class ScreenSlidePagerAdapter extends FragmentStatePagerAdapter {

        public ScreenSlidePagerAdapter(FragmentManager fragmentManager) {
            super(fragmentManager);

        }

        @Override
        public HomeSlideFragment getItem(int position) {
            String image = homeImages[position];
            return HomeSlideFragment.create(image);
        }

        @Override
        public int getCount() {
            return homeImages.length;
        }
    }
}
