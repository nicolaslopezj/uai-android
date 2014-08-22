package cl.uai.uai.welcome;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import cl.uai.uai.R;
import cl.uai.uai.menu.Main;

/**
 * Created by nicolaslopezj on 31-07-14.
 */
public class WelcomeSlideViewFragment extends Fragment {

    public static final String ARG_PAGE = "page";
    private int mPageNumber;

    public static WelcomeSlideViewFragment create(int pageNumber) {
        WelcomeSlideViewFragment fragment = new WelcomeSlideViewFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_PAGE, pageNumber);
        fragment.setArguments(args);
        return fragment;
    }

    public WelcomeSlideViewFragment() {

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mPageNumber = getArguments().getInt(ARG_PAGE);
    }

    @Override
    public android.view.View onCreateView(LayoutInflater inflater, ViewGroup container,
                                          Bundle savedInstanceState) {
        // Inflate the layout containing a title and body text.
        ViewGroup rootView;

        switch (mPageNumber) {
            case 0: rootView = (ViewGroup) inflater.inflate(R.layout.welcome_screen_page_slide_1, container, false);
                break;
            case 1: rootView = (ViewGroup) inflater.inflate(R.layout.welcome_screen_page_slide_2, container, false);
                break;
            case 2: rootView = (ViewGroup) inflater.inflate(R.layout.welcome_screen_page_slide_3, container, false);
                break;
            case 3: rootView = (ViewGroup) inflater.inflate(R.layout.welcome_screen_page_slide_4, container, false);
                rootView.findViewById(R.id.loginButton).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent = new Intent(getActivity(), Main.class);
                        getActivity().startActivity(intent);
                        getActivity().finish();
                    }
                });
                break;
            default: rootView = (ViewGroup) inflater.inflate(R.layout.welcome_screen_page_slide_1, container, false);
                break;
        }

        return rootView;
    }

    public int getPageNumber() {
        return mPageNumber;
    }
}
