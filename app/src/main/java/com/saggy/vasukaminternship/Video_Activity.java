package com.saggy.vasukaminternship;

import android.annotation.SuppressLint;
import android.content.pm.ActivityInfo;
import android.media.MediaPlayer;
import android.media.VolumeShaper;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatSeekBar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.saggy.vasukaminternship.adapters.Bottom_Sheet_Dialog_Fragment;
import com.saggy.vasukaminternship.fragments.Right_Slider_Landscape;

import org.jetbrains.annotations.NotNull;

import eu.okatrych.rightsheet.RightSheetBehavior;

public class Video_Activity extends AppCompatActivity {
    ImageButton chatButton, dropDownButton;
    RelativeLayout headingLayout;
    FrameLayout frameLayout;
    RelativeLayout frame_video;
    ImageButton fullscreen , exit_fullscreen;
    ImageButton backButton, pause, forward, backward, info ;
    ImageButton currency_landscape, messenger_landscape, chat_enable_landscape;
    VideoView videoView;
    View view;
    TextView live_text, viewer_text;
    ImageButton eye_icon;
    ImageView red_live;
    LinearLayout L1, L2, descriptionLinear;
    TextView headingDescription;
    TextView contentDescription;
    boolean close = true;
    int chat = 0;
    ProgressBar spinner;
    AppCompatSeekBar seekBar;
    RightSheetBehavior rightSheetBehavior;

    boolean is_icon_showing = false;
    boolean is_video_pause = false;

    String url = "http://commondatastorage.googleapis.com/gtv-videos-bucket/sample/BigBuckBunny.mp4";

//    ImageButton currencyButton;

//    RecyclerView priceHorRecycleView;
//    List<String> priceList = new ArrayList<>();
//
//    RecyclerView commentVerRecycleView;
//    List<String> commentList = new ArrayList<>();

    @SuppressLint("UseCompatLoadingForDrawables")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.video_activity);

        chatButton = findViewById(R.id.chatButton);
        headingLayout = findViewById(R.id.secondLayout);
        L1 = findViewById(R.id.thirdLayout);
        L2 = findViewById(R.id.fourthLayout);
        descriptionLinear = findViewById(R.id.descriptionLayout);
        headingDescription = findViewById(R.id.headingDescription);
        contentDescription = findViewById(R.id.contentDescription);
        frameLayout = findViewById(R.id.frameLayoutVideo);
        frame_video = findViewById(R.id.frame_layout);
        exit_fullscreen = findViewById(R.id.fullscreen_exit);
        fullscreen = findViewById(R.id.fullscreen);
        view = findViewById(R.id.v1);
        dropDownButton = findViewById(R.id.dropDownButton);
        spinner = findViewById(R.id.progress_bar);
        videoView = findViewById(R.id.video_view);
        pause = findViewById(R.id.pause);
        forward = findViewById(R.id.forward);
        backButton = findViewById(R.id.backbtn);
        backward = findViewById(R.id.rewind);
        info = findViewById(R.id.settingbtn);
        currency_landscape = findViewById(R.id.currency_landscape);
        seekBar = findViewById(R.id.seekbar);
        eye_icon = findViewById(R.id.eye_image);
        red_live = findViewById(R.id.red_image);
        live_text = findViewById(R.id.live_text);
        viewer_text = findViewById(R.id.total_live);
        messenger_landscape = findViewById(R.id.messenger);
        chat_enable_landscape = findViewById(R.id.chat_enable);

        getSupportFragmentManager().beginTransaction().replace(R.id.right_sheet, new Right_Slider_Landscape()).commit();

        View sheet = findViewById(R.id.right_sheet);
        rightSheetBehavior = RightSheetBehavior.from(sheet);

        loadFragment(new Video_Suggestion_Fragment());

        videoView.setVideoURI(Uri.parse(url));

        videoView.setOnPreparedListener(mediaPlayer -> {
            videoView.start();
            seekBar.setMax(videoView.getDuration());
            new MyThread().start();
            spinner.setVisibility(View.GONE);
            DisplayIconButton(true);
        });

        currency_landscape.setOnClickListener(view1 -> {
            rightSheetBehavior.setPeekWidth(120);
        });

        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
            }
            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }
            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                int progress = seekBar.getProgress();
                videoView.seekTo(progress);
            }
        });

        chatButton.setOnClickListener(view1 -> {
            if(chat == 0){
                chatButton.setImageDrawable(getResources().getDrawable(R.drawable.chat_icon_figma));
                loadFragment(new Comment_Fragment());
                chat = 1;
            }
            else{
                chatButton.setImageDrawable(getResources().getDrawable(R.drawable.chat_icon_outline_figma));
                loadFragment(new Video_Suggestion_Fragment());
                chat = 0;
            }
        });


        frame_video.setOnClickListener(view1 -> {
            DisplayIconButton(!is_icon_showing);
        });


        pause.setOnClickListener(view1 -> {
            if(is_video_pause){
                videoView.start();
                pause.setImageDrawable(getResources().getDrawable(R.drawable.pause_figma));
                is_video_pause = false;
            }
            else {
                videoView.pause();
                pause.setImageDrawable(getResources().getDrawable(R.drawable.play_figma));
                is_video_pause = true;
            }
        });

        forward.setOnClickListener(view1 -> videoView.seekTo(videoView.getCurrentPosition() + 5*1000));

        backward.setOnClickListener(view1 -> videoView.seekTo(videoView.getCurrentPosition() - 5*1000));

        backButton.setOnClickListener(view1 -> {
            onBackPressed();
        });

        fullscreen.setOnClickListener(view1 -> {
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
            frame_video.setLayoutParams(new RelativeLayout.LayoutParams(new WindowManager.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT)));
            DisplayIconButton(true);
        });

        exit_fullscreen.setOnClickListener(view -> {
            rightSheetBehavior.setPeekWidth(0);
            rightSheetBehavior.setState(RightSheetBehavior.STATE_COLLAPSED);
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
            int height = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 200, getResources().getDisplayMetrics());
            frame_video.setLayoutParams(new RelativeLayout.LayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, height)));
            DisplayIconButton(true);
        });

        //new Handler().postDelayed(() -> headingLayout.setVisibility(View.GONE), 4000);

        dropDownButton.setOnClickListener(view1 -> {
            if(close){
                close = false;
                dropDownButton.setRotation(180);
                descriptionLinear.setVisibility(View.VISIBLE);
                L1.setVisibility(View.GONE);
                L2.setVisibility(View.GONE);
                view.setVisibility(View.GONE);
                frameLayout.setVisibility(View.GONE);
                contentDescription.setText("5 October 2021\n\n\nEveryday brings new opportunities so in esports. Here with the " +
                        "best teams will fight for their survivals in the tournament.\n\n\nGive a follow for more such awesome " +
                        "and a thrilling tournaments.\n\n\nAlso follow:\n\nDiscord:(Link)\nInstagram:(Link)\nTwitter:(Link)");
            }
            else{
                close = true;
                dropDownButton.setRotation(0);
                descriptionLinear.setVisibility(View.GONE);
                L1.setVisibility(View.VISIBLE);
                L2.setVisibility(View.VISIBLE);
                view.setVisibility(View.VISIBLE);
                frameLayout.setVisibility(View.VISIBLE);
            }
        });

        rightSheetBehavior.setRightSheetCallback(new RightSheetBehavior.RightSheetCallback() {
            @Override
            public void onStateChanged(@NonNull @NotNull View rightSheet, int newState) {
                if (newState == RightSheetBehavior.STATE_DRAGGING) {
                    if(rightSheetBehavior.getPeekWidth() == 0) {
                        rightSheetBehavior.setState(RightSheetBehavior.STATE_COLLAPSED);
                    }
                }
//                switch (newState){
//                    case RightSheetBehavior.STATE_DRAGGING:
//                        if(open == 0)
//                            rightSheetBehavior.setState(RightSheetBehavior.STATE_COLLAPSED);
//                        break;
//                    case RightSheetBehavior.STATE_COLLAPSED:
//                        open = 0;
//                        break;
//                    case RightSheetBehavior.STATE_EXPANDED:
//                        break;
//                    case RightSheetBehavior.STATE_HALF_EXPANDED:
//                        break;
//                    case RightSheetBehavior.STATE_SETTLING:
//                        break;
//                    case RightSheetBehavior.STATE_HIDDEN:
//                        open = 0;
//                        break;
//                }

            }

            @Override
            public void onSlide(@NonNull @NotNull View rightSheet, float slideOffset) {

            }
        });
    }

    private void DisplayIconButton(boolean b) {
        if(b){
           // we have to show icons
           pause.setVisibility(View.VISIBLE);
           forward.setVisibility(View.VISIBLE);
           backward.setVisibility(View.VISIBLE);
           backButton.setVisibility(View.VISIBLE);
           info.setVisibility(View.VISIBLE);
           int orientation = getResources().getConfiguration().orientation;
            seekBar.setVisibility(View.VISIBLE);
            red_live.setVisibility(View.VISIBLE);
            live_text.setVisibility(View.VISIBLE);
            viewer_text.setVisibility(View.VISIBLE);
            eye_icon.setVisibility(View.VISIBLE);
           if(orientation == 2){
               //landscape
               currency_landscape.setVisibility(View.VISIBLE);
               messenger_landscape.setVisibility(View.VISIBLE);
               chat_enable_landscape.setVisibility(View.VISIBLE);
               exit_fullscreen.setVisibility(View.VISIBLE);
               fullscreen.setVisibility(View.GONE);
           }
           else if(orientation == 1){
               // portrait
               exit_fullscreen.setVisibility(View.GONE);
               fullscreen.setVisibility(View.VISIBLE);
           }

           new Handler().postDelayed(() -> DisplayIconButton(false),3500);
        }
        else{
            //we have to remove icons
            if(!is_video_pause){
                pause.setVisibility(View.GONE);
                forward.setVisibility(View.GONE);
                backward.setVisibility(View.GONE);
                backButton.setVisibility(View.GONE);
                info.setVisibility(View.GONE);
                exit_fullscreen.setVisibility(View.GONE);
                fullscreen.setVisibility(View.GONE);
                currency_landscape.setVisibility(View.GONE);
                chat_enable_landscape.setVisibility(View.GONE);
                messenger_landscape.setVisibility(View.GONE);
                seekBar.setVisibility(View.GONE);
                red_live.setVisibility(View.GONE);
                live_text.setVisibility(View.GONE);
                viewer_text.setVisibility(View.GONE);
                eye_icon.setVisibility(View.GONE);

            }
        }
    }

    public  void loadFragment(Fragment fragment){
        //create a fragment manager
        FragmentManager fragmentManager = getSupportFragmentManager();
        //create Fragment transaction
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        //replace framelayout with new fragment
        fragmentTransaction.replace(R.id.frameLayoutVideo,fragment);
        //save changes
        fragmentTransaction.commit();
    }

    @Override
    public void onBackPressed() {
        int orientation = getResources().getConfiguration().orientation;
        if(orientation == 2){
            //landscape
            rightSheetBehavior.setPeekWidth(0);
            rightSheetBehavior.setState(RightSheetBehavior.STATE_COLLAPSED);
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
            int height = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 200, getResources().getDisplayMetrics());
            frame_video.setLayoutParams(new RelativeLayout.LayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, height)));
            DisplayIconButton(true);
        }
        else if(orientation == 1){
            // portrait
            super.onBackPressed();
        }
    }

    //start a method of thread
    class MyThread extends Thread {
        @Override
        public void run() {
            super.run();
            int runtime = videoView.getDuration();
            int currentPosition = 0;
            int adv = 0;
            while ((adv = ((adv = runtime - currentPosition) < 500)?adv:500) > 2) {
                try {
                    currentPosition = videoView.getCurrentPosition();
                    if (seekBar != null) {
                        seekBar.setProgress(currentPosition);
                    }
                    sleep(adv);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } catch (IllegalStateException e) {
                    seekBar.setProgress(runtime);
                    break;
                }

            }
//            while (seekBar.getProgress() <= seekBar.getMax()) {
//                seekBar.setProgress(videoView.getCurrentPosition());
//            }
        }
    }
}
    //        priceHorRecycleView = findViewById(R.id.price_recycle_view);
//        commentVerRecycleView = findViewById(R.id.comment_recycle_view);
//        currencyButton = findViewById(R.id.currencyButton);
//
//        addValuesToPriceList();
//        addValueToCommentList();

//        Comment_Horizontal_Adapter comment_horizontal_adapter = new Comment_Horizontal_Adapter(Video_Activity.this, priceList);
//        priceHorRecycleView.setAdapter(comment_horizontal_adapter);
//        priceHorRecycleView.setLayoutManager(new LinearLayoutManager(Video_Activity.this, LinearLayoutManager.HORIZONTAL, false));
//
//        Comment_Vertical_Adapter comment_vertical_adapter = new Comment_Vertical_Adapter(Video_Activity.this, commentList);
//        commentVerRecycleView.setAdapter(comment_vertical_adapter);
//        commentVerRecycleView.setLayoutManager(new LinearLayoutManager(Video_Activity.this));


//        currencyButton.setOnClickListener(view -> {
//            //showBottomSheetDialog();
//            Bottom_Sheet_Dialog_Fragment bottom_sheet_dialog_fragment= new Bottom_Sheet_Dialog_Fragment();
//            bottom_sheet_dialog_fragment.show(getSupportFragmentManager(),"TAG");
//        });

//    private void showBottomSheetDialog() {
//
//        final BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(Video_Activity.this);
//        bottomSheetDialog.setContentView(R.layout.award_activity);

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
//        ViewPager vp = bottomSheetDialog.findViewById(R.id.popupViewPager);
//
//        tb.addTab(tb.newTab().setText("Currency"));
//        tb.addTab(tb.newTab().setText("Awards"));
//
//        tb.setTabGravity(TabLayout.GRAVITY_FILL);
//
//        Tab_Layout_Adapter tab_layout_adapter = new Tab_Layout_Adapter(Video_Activity.this, tb.getTabCount(), getSupportFragmentManager());
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
//        bottomSheetDialog.show();
   // }

//        chatButton.setOnClickListener(view -> {
//            loadFragment(new Chat_Fragment());
//        });

