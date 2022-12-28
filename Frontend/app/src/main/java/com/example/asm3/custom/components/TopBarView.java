package com.example.asm3.custom.components;

import static androidx.constraintlayout.helper.widget.MotionEffect.TAG;

import android.content.Context;
import android.content.res.Configuration;
import android.os.Build;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.widget.SearchView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.constraintlayout.widget.ConstraintSet;

import com.example.asm3.R;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textview.MaterialTextView;

public class TopBarView extends ConstraintLayout implements View.OnClickListener {
    private MaterialButton cartButton, backButton;
    private TextView titleText;
    private SearchView searchView;
    private Context context;
    ConstraintLayout constraintLayout;
    ConstraintSet constraintSet = new ConstraintSet();

    public TopBarView(Context context) {
        super(context);
    }

    public TopBarView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
        constraintLayout = findViewById(this.getId());

        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.top_bar, this, true);

//        final int count = getChildCount();
//        for(int i = 0; i < count; i++){
//            Log.d(TAG, "TopBarView: Child #" + i + " " + getChildAt(i));
//        }

        // find views and attach to vars
        backButton = (MaterialButton) getChildAt(0);
        searchView = (SearchView) getChildAt(1);
        titleText = (MaterialTextView) getChildAt(2);
        cartButton = (MaterialButton) getChildAt(3);

        // set onClickListener
        backButton.setOnClickListener(this);
        cartButton.setOnClickListener(this);

        // change color of search view
        setSearchViewColor();
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.backButton:       // BACK BUTTON
                Log.d(TAG, "onClick: Back button was clicked!");
                break;
            case R.id.searchView:       // SEARCH VIEW
                if (searchView.isIconified()) {
                    // go back to search fragment
                } else {

                }
                break;
            case R.id.cartButton:       // CART BUTTON
                Log.d(TAG, "onClick: Cart button was clicked!");
                break;
        }
    }

    /**
     * Set color for search view's icon
     */
    private void setSearchViewColor() {
        ImageView searchIcon = searchView.findViewById(androidx.appcompat.R.id.search_mag_icon);
        int nightModeFlags = context.getResources().getConfiguration().uiMode & Configuration.UI_MODE_NIGHT_MASK;

        switch (nightModeFlags) {
            case Configuration.UI_MODE_NIGHT_YES:
                searchIcon.setColorFilter(getResources().getColor(R.color.md_theme_dark_primary),
                        android.graphics.PorterDuff.Mode.SRC_IN);
                break;

            case Configuration.UI_MODE_NIGHT_NO:
                searchIcon.setColorFilter(getResources().getColor(R.color.md_theme_light_primary),
                        android.graphics.PorterDuff.Mode.SRC_IN);
                break;
        }
    }

    /**
     * Set visibility and constraints for main pages' components
     * @param title String holds text for title
     */
    public void setMainPage(String title){
        // set visibility
        titleText.setVisibility(VISIBLE);
        cartButton.setVisibility(VISIBLE);
        backButton.setVisibility(GONE);
        searchView.setVisibility(GONE);

        // set constraints
        constraintSet.clone(constraintLayout);
        constraintSet.connect(R.id.titleText, ConstraintSet.START, ConstraintSet.PARENT_ID, ConstraintSet.START);
        constraintSet.connect(R.id.cartButton, ConstraintSet.END, ConstraintSet.PARENT_ID, ConstraintSet.END);
        constraintSet.connect(R.id.titleText, ConstraintSet.END, R.id.cartButton, ConstraintSet.START);

        // set title
        titleText.setText(title);
    }

    /**
     * Set visibility and constraints for search page's components
     */
    public void setSearchPage() {
        // set visibility
        searchView.setVisibility(VISIBLE);
        backButton.setVisibility(GONE);
        cartButton.setVisibility(GONE);
        titleText.setVisibility(GONE);

        // set constraints
        constraintSet.clone(constraintLayout);
        constraintSet.connect(R.id.searchView, ConstraintSet.START, ConstraintSet.PARENT_ID, ConstraintSet.START);
        constraintSet.connect(R.id.searchView, ConstraintSet.END, ConstraintSet.PARENT_ID, ConstraintSet.END);

        // set hint
        searchView.setQueryHint("What to eat, maaah?");
    }

    /**
     * Set visibility and constraints for search result page's components
     * @param searchQuery String holds text to be displayed in searchView
     */
    public void setSearchResultPage(String searchQuery) {
        // set visibility
        searchView.setVisibility(VISIBLE);
        backButton.setVisibility(VISIBLE);
        cartButton.setVisibility(VISIBLE);
        titleText.setVisibility(GONE);
        searchView.setIconified(false);

        // set constraints
        constraintSet.clone(constraintLayout);
        constraintSet.connect(R.id.backButton, ConstraintSet.START, ConstraintSet.PARENT_ID, ConstraintSet.START);
        constraintSet.connect(R.id.cartButton, ConstraintSet.END, ConstraintSet.PARENT_ID, ConstraintSet.END);
        constraintSet.connect(R.id.searchView, ConstraintSet.START, R.id.backButton, ConstraintSet.END);
        constraintSet.connect(R.id.searchView, ConstraintSet.END, R.id.cartButton, ConstraintSet.START);

        // set text for searchView
        searchView.setQuery(searchQuery, false);
    }

}//end of class
