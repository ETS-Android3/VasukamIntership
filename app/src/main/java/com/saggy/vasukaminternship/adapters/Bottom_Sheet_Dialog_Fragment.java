package com.saggy.vasukaminternship.adapters;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.core.view.ViewCompat;
import androidx.viewpager.widget.ViewPager;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.google.android.material.tabs.TabLayout;
import com.saggy.vasukaminternship.R;

public class Bottom_Sheet_Dialog_Fragment extends BottomSheetDialogFragment {

    @SuppressLint("RestrictedApi")
    @Override
    public void setupDialog(@NonNull Dialog dialog, int style) {
        super.setupDialog(dialog, style);

        View view = LayoutInflater.from(getContext()).inflate(R.layout.award_activity, null);
        TabLayout tb = view.findViewById(R.id.tab_layout);
        ViewPager vp = view.findViewById(R.id.popupViewPager);

        tb.addTab(tb.newTab().setText("Currency"));
        tb.addTab(tb.newTab().setText("Awards"));
        //using below java code we can add margin between the tabs of tab layout
//        for(int i=0; i < tb.getTabCount(); i++) {
//            View tab = ((ViewGroup) tb.getChildAt(0)).getChildAt(i);
//            ViewGroup.MarginLayoutParams p = (ViewGroup.MarginLayoutParams) tab.getLayoutParams();
//            p.setMargins(0, 0, 50, 0);
//            tab.requestLayout();
//        }

        //using below java code in main activity we can add drawable + text in tabs of the tab layout
        tb.getTabAt(0).setIcon(R.drawable.money_figma_1);
        tb.getTabAt(1).setIcon(R.drawable.trophy_black_figma);

//        for (i in 0 until tabCount) {
//            val tabView: View = (images_videos_tab_layout.getChildAt(0) as ViewGroup).getChildAt(i)
//            tabView.requestLayout()
//            ViewCompat.setBackground(tabView,setImageButtonStateNew(requireContext()));
//            ViewCompat.setPaddingRelative(tabView, tabView.paddingStart, tabView.paddingTop, tabView.paddingEnd, tabView.paddingBottom);
//        }
//        fun setImageButtonStateNew(mContext: Context): StateListDrawable {
//            val states = StateListDrawable()
//            states.addState(intArrayOf(android.R.attr.state_selected), ContextCompat.getDrawable(mContext, R.drawable.tab_bg_normal_blue))
//            states.addState(intArrayOf(-android.R.attr.state_selected), ContextCompat.getDrawable(mContext, R.drawable.tab_bg_normal))
//            return states
//        }
        for(int j=0; j<tb.getTabCount(); j++){
            View tab = ((ViewGroup) tb.getChildAt(0)).getChildAt(j);
            tab.requestLayout();
            if(j == 0){
                ViewCompat.setBackground(tab,getResources().getDrawable(R.drawable.white_rounded));
                ViewGroup.MarginLayoutParams p = (ViewGroup.MarginLayoutParams) tab.getLayoutParams();
                p.setMargins(0, 0, 20, 0);
            }
            else{
                ViewCompat.setBackground(tab,getResources().getDrawable(R.drawable.yellow_rounded_stroke));
                ViewGroup.MarginLayoutParams p = (ViewGroup.MarginLayoutParams) tab.getLayoutParams();
                p.setMargins(20, 0, 0, 0);
            }
        }

        tb.setTabGravity(TabLayout.GRAVITY_FILL);

        Tab_Layout_Adapter tab_layout_adapter = new Tab_Layout_Adapter(getContext(), tb.getTabCount(), getChildFragmentManager());
        vp.setAdapter(tab_layout_adapter);

        vp.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tb));

        tb.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                vp.setCurrentItem(tab.getPosition());
                for(int j=0; j<tb.getTabCount(); j++){
                    View tabl = ((ViewGroup) tb.getChildAt(0)).getChildAt(j);
                    tabl.requestLayout();
                    if(j == tab.getPosition()){
                        ViewCompat.setBackground(tabl,getResources().getDrawable(R.drawable.white_rounded));
                    }
                    else{
                        ViewCompat.setBackground(tabl,getResources().getDrawable(R.drawable.yellow_rounded_stroke));
                    }
                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
        dialog.setContentView(view);
    }
}

//        CoordinatorLayout.LayoutParams layoutParams = (CoordinatorLayout.LayoutParams) ((View) view.getParent()).getLayoutParams();
//        CoordinatorLayout.Behavior behavior = layoutParams.getBehavior();
//        if(behavior != null && behavior instanceof BottomSheetBehavior){
//            ((BottomSheetBehavior) behavior).setBottomSheetCallback(new BottomSheetBehavior.BottomSheetCallback() {
//                @Override
//                public void onStateChanged(@NonNull View view, int i) {
//                    String state ="";
//                    switch (i){
//                        case BottomSheetBehavior.STATE_DRAGGING:
//                            state = "DRAGGING";
//                            break;
//                        case BottomSheetBehavior.STATE_SETTLING:
//                            state = "SETTLING";
//                            break;
//                        case BottomSheetBehavior.STATE_EXPANDED:
//                            state = "EXPANDING";
//                            break;
//                        case BottomSheetBehavior.STATE_COLLAPSED:
//                            state = "COLLAPSED";
//                            break;
//                        case BottomSheetBehavior.STATE_HIDDEN:
//                            state = "HIDDEN";
//                            dismiss();
//                            break;
//                    }
//                }
//                @Override
//                public void onSlide(@NonNull View view, float v) {
//                }
//            });
//        }
//    }

//    @Nullable
//    @Override
//    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
//        return super.onCreateView(inflater, container, savedInstanceState);
//    }
//}
/*
class BottomSheet : BottomSheetDialogFragment() {

    private lateinit var bottomSheet: ViewGroup
    private lateinit var bottomSheetBehavior: BottomSheetBehavior<*>
    private lateinit var viewPager: ViewPager
    private lateinit var appBarLayout: AppBarLayout

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val myview: View = inflater.inflate(R.layout.layout_bottom_sheet, container, false)

        // SETUP THE VIEWPAGER AND THE TABLAYOUT HERE

        val tabLayout = myview.findViewById<TabLayout>(R.id.myTabLayout)
        appBarLayout = myview.findViewById(R.id.appBarLayout)
        viewPager = myview.findViewById(R.id.myViewPager)
        tabLayout.addTab(tabLayout.newTab().setText("Info"))
        tabLayout.addTab(tabLayout.newTab().setText("Other"))
        tabLayout.tabGravity = TabLayout.GRAVITY_FILL

        // USE childFragmentManager
        val adapter = MyFragmentAdapter(childFragmentManager)
        viewPager.adapter = adapter
        viewPager.addOnPageChangeListener(TabLayoutOnPageChangeListener(tabLayout))
        tabLayout.addOnTabSelectedListener(object : OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab) {
                viewPager.setCurrentItem(tab.position)
            }

            override fun onTabUnselected(tab: TabLayout.Tab) {}
            override fun onTabReselected(tab: TabLayout.Tab) {}
        })
        return myview
    }
}
 */