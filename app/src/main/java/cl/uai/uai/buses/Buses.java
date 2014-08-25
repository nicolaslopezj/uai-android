package cl.uai.uai.buses;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.actionbarsherlock.app.ActionBar;
import com.octo.android.robospice.persistence.DurationInMillis;
import com.octo.android.robospice.persistence.exception.SpiceException;
import com.octo.android.robospice.request.listener.RequestListener;
import com.viewpagerindicator.CirclePageIndicator;
import com.viewpagerindicator.TabPageIndicator;
import com.viewpagerindicator.TitlePageIndicator;

import cl.uai.uai.R;
import cl.uai.uai.api.HomeImagesRequest;
import cl.uai.uai.home.HomeSlideFragment;
import cl.uai.uai.main.BaseFragment;

/**
 * Created by nicolaslopezj on 24-08-14.
 */
public class Buses extends BaseFragment {

    private ViewPager mPager;
    private TitlePageIndicator mIndicator;
    private PagerAdapter mPagerAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View layout = inflater.inflate(R.layout.buses_main, null);


        // Instantiate a ViewPager and a PagerAdapter.
        mPager = (ViewPager) layout.findViewById(R.id.busesPager);
        mPagerAdapter = new ScreenSlidePagerAdapter(getFragmentManager());
        mPager.setAdapter(mPagerAdapter);
        mPager.setOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
            @Override
            public void onPageSelected(int position) {
                //invalidateOptionsMenu();
            }
        });

        //Bind the title indicator to the adapter
        mIndicator = (TitlePageIndicator) layout.findViewById(R.id.indicator);
        mIndicator.setViewPager(mPager);

        return layout;
    }

    private class ScreenSlidePagerAdapter extends FragmentStatePagerAdapter {

        public ScreenSlidePagerAdapter(FragmentManager fragmentManager) {
            super(fragmentManager);

        }

        @Override
        public BusesSlideFragment getItem(int position) {
            return BusesSlideFragment.create();
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return position == 0 ? "IDA" : "VUELTA";
        }


        @Override
        public int getCount() {
            return 2;
        }
    }
}
