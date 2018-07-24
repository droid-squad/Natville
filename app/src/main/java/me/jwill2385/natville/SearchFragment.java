package me.jwill2385.natville;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;


public class SearchFragment extends Fragment {
    private static final String TAG = "SearchFragment";
    private EditText etSearchTab;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_search, container, false);

    }
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        etSearchTab = (EditText) view.findViewById(R.id.etSearch2);
        initSearch();

    }
    private void initSearch() {
        etSearchTab.setSingleLine();
        etSearchTab.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int actionId, KeyEvent keyEvent) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH
                        || actionId == EditorInfo.IME_ACTION_DONE
                        || keyEvent.getAction() == KeyEvent.ACTION_DOWN
                        || keyEvent.getAction() == KeyEvent.KEYCODE_ENTER) {


                    //TODO: CREATE A METHOD HERE THAT DECIDES WHAT TO DO WHEN USER PRESSES ENTER
                    filterTrails();
                }
                return false;
            }
        });
    }
    private void filterTrails(){
        Log.d(TAG, "filterTrails: filtering");
        String searched = etSearchTab.getText().toString();
        Toast.makeText(getActivity(),searched,Toast.LENGTH_SHORT).show();
    }

}
