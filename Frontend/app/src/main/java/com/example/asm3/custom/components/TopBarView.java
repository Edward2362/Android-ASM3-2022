package com.example.asm3.custom.components;

import static androidx.constraintlayout.helper.widget.MotionEffect.TAG;

import android.content.Context;
import android.content.res.Configuration;
import android.os.Build;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.widget.SearchView;
import androidx.compose.foundation.interaction.DragInteraction;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.constraintlayout.widget.ConstraintSet;

import com.example.asm3.R;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textview.MaterialTextView;

public class TopBarView extends LinearLayout {
    private MaterialButton cartButton, backButton;
    private TextView titleText;
    private SearchView searchView;
    private Context context;
    LinearLayout linearLayout;
    private int viewWidth;

    public TopBarView(Context context) {
        super(context);
    }

    public TopBarView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
        linearLayout = findViewById(this.getId());
        // settings for layout
        setOrientation(HORIZONTAL);
        setGravity(Gravity.CENTER_VERTICAL);

        // inflate layout
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.top_bar, this, true);

        // find views and attach to vars
        backButton = (MaterialButton) getChildAt(0);
        searchView = (SearchView) getChildAt(1);
        titleText = (MaterialTextView) getChildAt(2);
        cartButton = (MaterialButton) getChildAt(3);

        // change color of search view
        setSearchViewColor();
    }

//    @RequiresApi(api = Build.VERSION_CODES.M)
//    @Override
//    public void onClick(View view) {
//        switch (view.getId()) {
//            case R.id.backButton:       // BACK BUTTON
//                Log.d(TAG, "onClick: test Back button was clicked!");
//                break;
//            case R.id.searchView:       // SEARCH VIEW
//                if (searchView.isIconified()) {
//                    // go back to search fragment
//                } else {
//
//                }
//                break;
//            case R.id.cartButton:       // CART BUTTON
//                Log.d(TAG, "onClick: Cart button was clicked!");
//                break;
//        }
//    }

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

    //========================== SETTING LAYOUT FUNCTIONS ==========================================

    /**
     * Set visibility and constraints for main pages' components
     *
     * @param title String holds text for title
     */
    public void setMainPage(String title) {
        // set visibility
        titleText.setVisibility(VISIBLE);
        cartButton.setVisibility(VISIBLE);
        backButton.setVisibility(GONE);
        searchView.setVisibility(GONE);

        // set title
        titleText.setText(title);
        titleText.setTextAlignment(TEXT_ALIGNMENT_VIEW_START);
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
        searchView.requestFocus();

        // set hint
        searchView.setQueryHint("What to eat, maaah?");
    }

    /**
     * Set visibility and constraints for search result page's components
     */
    public void setSearchResultPage() {
        // set visibility
        searchView.setVisibility(VISIBLE);
        backButton.setVisibility(VISIBLE);
        cartButton.setVisibility(VISIBLE);
        titleText.setVisibility(GONE);
        searchView.setIconified(false);
        searchView.clearFocus();
    }

    /**
     * Set visibility and constraints for login and register page's components
     */
    public void setAuthPage() {
        // set visibility
        searchView.setVisibility(GONE);
        backButton.setVisibility(VISIBLE);
        cartButton.setVisibility(GONE);
        titleText.setVisibility(GONE);
    }

    /**
     * Set visibility and constraints for cart, user info, post book page's components
     */
    public void setSubPage(String title) {
        // set visibility
        searchView.setVisibility(GONE);
        backButton.setVisibility(VISIBLE);
        cartButton.setVisibility(INVISIBLE);
        titleText.setVisibility(VISIBLE);

        titleText.setText(title);
        titleText.setTextAlignment(TEXT_ALIGNMENT_CENTER);
    }

    // getters
    public SearchView getSearchView() {
        return searchView;
    }

    public MaterialButton getBackButton() {
        return backButton;
    }

    public MaterialButton getCartButton() {
        return cartButton;
    }

    // setters
    public void setSearchQuery(String query) {
        searchView.setQuery(query, false);
    }

}//end of class
