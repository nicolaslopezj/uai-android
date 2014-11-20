package cl.uai.uai.welcome;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

import com.dd.processbutton.iml.ActionProcessButton;
import com.octo.android.robospice.persistence.DurationInMillis;
import com.octo.android.robospice.persistence.exception.SpiceException;
import com.octo.android.robospice.request.listener.RequestListener;

import cl.uai.uai.R;
import cl.uai.uai.api.LoginRequest;
import cl.uai.uai.api.SportActionRequest;
import cl.uai.uai.api.json.LoginResponse;
import cl.uai.uai.api.json.Success;
import cl.uai.uai.main.BaseFragment;
import cl.uai.uai.main.Helper;
import cl.uai.uai.menu.Main;

/**
 * Created by nicolaslopezj on 31-07-14.
 */
public class WelcomeSlideViewFragment extends BaseFragment {

    public static final String ARG_PAGE = "page";
    public EditText inputEmail;
    private int mPageNumber;

    public ActionProcessButton actionButton;

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

    private void hideKeyboard() {
        InputMethodManager inputManager = (InputMethodManager) getActivity().getSystemService(getActivity().getApplicationContext().INPUT_METHOD_SERVICE);

        // check if no view has focus:
        View view = getActivity().getCurrentFocus();
        if (view != null) {
            inputManager.hideSoftInputFromWindow(view.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
        }
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
                inputEmail = (EditText) rootView.findViewById(R.id.inputEmail);
                inputEmail.setOnFocusChangeListener(new View.OnFocusChangeListener() {

                    @Override
                    public void onFocusChange(View view, boolean b) {
                        if (!b) {
                            if (!inputEmail.getText().toString().matches(".+?@.*") && !inputEmail.getText().toString().equals("")) {
                                inputEmail.setText(inputEmail.getText() + "@alumnos.uai.cl");
                            }
                        }
                    }

                });

                final EditText inputPassword = (EditText) rootView.findViewById(R.id.inputPassword);

                actionButton = (ActionProcessButton) rootView.findViewById(R.id.actionButton);
                actionButton.setMode(ActionProcessButton.Mode.ENDLESS);
                actionButton.setText("Ingresar");

                actionButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        hideKeyboard();
                        ActionProcessButton button = (ActionProcessButton) view;
                        if (button.getProgress() == 0) {
                            button.setProgress(1);
                            performRequest(inputEmail.getText().toString(), inputPassword.getText().toString());
                        }
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


    private void performRequest(String username, String password) {

        LoginRequest request = new LoginRequest(username, password);
        String lastRequestCacheKey = request.createCacheKey();

        spiceManager.execute(request, lastRequestCacheKey, DurationInMillis.ALWAYS_EXPIRED, new LoginRequestListener());
    }

    private class LoginRequestListener implements RequestListener<LoginResponse> {

        @Override
        public void onRequestFailure(SpiceException e) {
            //update your UI
            Log.v("Request Error", e.toString());
            actionButton.setProgress(0);
            showError("Ocurrió un error al ingresar");
        }

        @Override
        public void onRequestSuccess(LoginResponse response) {
            //update your UI
            Log.v("Request Success", "login success: " + response.token);
            if (response.success) {
                actionButton.setProgress(100);
                Helper.setToken(response.token);
                Helper.setUsername(inputEmail.getText().toString());
                Intent intent = new Intent(getActivity(), Main.class);
                getActivity().startActivity(intent);
                getActivity().finish();
            } else {
                showError("Email o contaseña incorrecta");
                actionButton.setProgress(0);
            }

        }
    }
}
