package com.saggy.vasukaminternship;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.saggy.vasukaminternship.adapters.Bottom_Sheet_Dialog_Fragment;
import com.saggy.vasukaminternship.adapters.Comment_Horizontal_Adapter;
import com.saggy.vasukaminternship.adapters.Comment_Vertical_Adapter;

import java.util.ArrayList;
import java.util.List;

public class Comment_Fragment extends Fragment {
    View view;

    ImageButton currencyButton;

    RecyclerView priceHorRecycleView;
    List<String> priceList = new ArrayList<>();

    RecyclerView commentVerRecycleView;
    List<String> commentList = new ArrayList<>();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.video_comment_fragment, container, false);

        priceHorRecycleView = view.findViewById(R.id.price_recycle_view);
        commentVerRecycleView = view.findViewById(R.id.comment_recycle_view);
        currencyButton= view.findViewById(R.id.currencyButton);

        addValuesToPriceList();
        addValueToCommentList();

        Comment_Horizontal_Adapter comment_horizontal_adapter = new Comment_Horizontal_Adapter(getContext(),priceList);
        priceHorRecycleView.setAdapter(comment_horizontal_adapter);
        priceHorRecycleView.setLayoutManager(new LinearLayoutManager(getContext(),LinearLayoutManager.HORIZONTAL,false));

        Comment_Vertical_Adapter comment_vertical_adapter = new Comment_Vertical_Adapter(getContext(), commentList);
        commentVerRecycleView.setAdapter(comment_vertical_adapter);
        commentVerRecycleView.setLayoutManager(new LinearLayoutManager(getContext()));

        currencyButton.setOnClickListener(view1 -> {
            Bottom_Sheet_Dialog_Fragment bottom_sheet_dialog_fragment= new Bottom_Sheet_Dialog_Fragment();
            bottom_sheet_dialog_fragment.show(getChildFragmentManager(),"TAG");
        });

        return view;


    }
    private void addValuesToPriceList() {
        priceList.add("$564");
        priceList.add("$10");
        priceList.add("$54");
        priceList.add("$68");
        priceList.add("$300");
        priceList.add("$432");
    }

    private void addValueToCommentList(){
        commentList.add("Nice Video");
        commentList.add("amazing");
        commentList.add("superb");
        commentList.add("Nice Video");
        commentList.add("incredible");
        commentList.add("nice");
        commentList.add("good");
        commentList.add("appreciable");
        commentList.add("credulous");
    }

    private void showBottomSheetDialog() {

        final BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(getContext());
        bottomSheetDialog.setContentView(R.layout.award_activity);

//        AppCompatButton currencyButton = bottomSheetDialog.findViewById(R.id.currencyButton);
//        AppCompatButton awardButton = bottomSheetDialog.findViewById(R.id.awardButton);
//        FragmentManager fragmentManager = getChildFragmentManager();
//        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
//        fragmentTransaction.replace(R.id.currencyFrameLayout,new Currency_Fragment());
//        fragmentTransaction.commit();

//            FragmentManager fragmentManager = getSupportFragmentManager();
//            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
//            fragmentTransaction.replace(R.id.frameLayoutVideo,fragment);
//            fragmentTransaction.commit();



//        LinearLayout copy = bottomSheetDialog.findViewById(R.id.copyLinearLayout);
//        TabLayout tb = bottomSheetDialog.findViewById(R.id.tab_layout);
//        ViewPager vp = bottomSheetDialog.findViewById(R.id.viewPager);
//
//        tb.addTab(tb.newTab().setText("Currency"));
//        tb.addTab(tb.newTab().setText("Awards"));
//
//        tb.setTabGravity(TabLayout.GRAVITY_FILL);
//
//        Tab_Layout_Adapter tab_layout_adapter = new Tab_Layout_Adapter(getContext(), tb.getTabCount(), getActivity().getFragmentManager() );
//        vp.setAdapter(tab_layout_adapter);
//
//        vp.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tb));
//
//        tb.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
//            @Override
//            public void onTabSelected(TabLayout.Tab tab) {
//                vp.setCurrentItem(tab.getPosition());
//            }
//
//            @Override
//            public void onTabUnselected(TabLayout.Tab tab) {
//
//            }
//
//            @Override
//            public void onTabReselected(TabLayout.Tab tab) {
//
//            }
//        });
        bottomSheetDialog.show();
    }

}
