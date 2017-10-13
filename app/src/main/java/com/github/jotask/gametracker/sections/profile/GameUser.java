package com.github.jotask.gametracker.sections.profile;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.*;
import com.github.jotask.gametracker.MainActivity;
import com.github.jotask.gametracker.R;
import com.github.jotask.gametracker.igdb.FullGame;
import com.github.jotask.gametracker.model.Game;
import com.github.jotask.gametracker.utils.LoadImage;

import java.sql.Date;
import java.text.DateFormat;
import java.text.SimpleDateFormat;

public class GameUser extends Fragment {

    public enum DATA{ ID }

    private String gameId;

    private ScrollView scrollView;
    private ImageView photo;

    public MainActivity main;
    public Handler handler;
    public boolean isLoading = false;

    private Game game;

    public GameUser() { }

    public static GameUser newInstance(String gameID) {
        GameUser fragment = new GameUser();
        Bundle args = new Bundle();
        args.putString(DATA.ID.name(), gameID);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if(getArguments() == null)
            throw new RuntimeException("gameId is undefined");
        this.gameId = getArguments().getString(DATA.ID.name());
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        getActivity().setTitle(gameId);

        this.scrollView = getActivity().findViewById(R.id.scrollview_gameuser);
        this.photo = getActivity().findViewById(R.id.contactPic);

        scrollView.getViewTreeObserver().addOnScrollChangedListener(new ScrollPositionObserver());

        this.main = ((MainActivity) getActivity());

        this.handler = new GameUser.MyHandle();

        handler.sendEmptyMessage(0);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_game_user, container, false);
    }

    public void setData(final FullGame g) {
        getActivity().setTitle(g.name);
        new LoadImage(photo, "https://" + g.getCover(FullGame.SIZE.SCREENSHOT_MED)).execute();
    }

    public void setUserData(final Game data){

        if(data == null) return;

        this.game = data;

        final ToggleButton swtch = getActivity().findViewById(R.id.game_user_iscompleted);
        final TextView startdate = getActivity().findViewById(R.id.game_user_startDate);
        final TextView enddate = getActivity().findViewById(R.id.game_user_endDate);
        final Button startBtn = getActivity().findViewById(R.id.game_user_startDate_btn);
        final Button endBtn = getActivity().findViewById(R.id.game_user_endDate_btn);
        final TextView completedIn = getActivity().findViewById(R.id.game_user_completedin);

        swtch.setTextOn("YES");
        swtch.setTextOff("NO");
        swtch.setChecked(data.completed);
        swtch.setEnabled(false);

        if(data.start > 0){
            startBtn.setVisibility(View.GONE);
            startdate.setText(getDate(data.start));
        }else{
            endBtn.setEnabled(false);
        }

        if(data.end > 0) {
            endBtn.setVisibility(View.GONE);
            enddate.setText(getDate(data.end));
        }

        startBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final long time = System.currentTimeMillis();
                game.start = time;
                String d = getDate(time);
                startdate.setText(d);
                startBtn.setVisibility(View.GONE);
                endBtn.setEnabled(true);
                save();
            }
        });

        endBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final long time = System.currentTimeMillis();
                game.end = time;
                game.completed = true;
                String d = getDate(time);
                enddate.setText(d);
                endBtn.setVisibility(View.GONE);
                swtch.setChecked(true);
                save();
            }
        });

        if(game.completed){
            completedIn.setText(getDifferenceTime(game.start, game.end));
        }else{
            completedIn.setVisibility(View.GONE);
        }

    }

    private class ScrollPositionObserver implements ViewTreeObserver.OnScrollChangedListener {

        private int mImageViewHeight;

        public ScrollPositionObserver() {
            mImageViewHeight = getResources().getDimensionPixelSize(R.dimen.game_user_cover);
        }

        @Override
        public void onScrollChanged() {
            int scrollY = Math.min(Math.max(scrollView.getScrollY(), 0), mImageViewHeight);

            // changing position of ImageView
            photo.setTranslationY(scrollY / 2);

            // alpha you could set to ActionBar background
            float alpha = scrollY / (float) mImageViewHeight;

        }
    }

    public String getGameId() { return gameId; }

    public class MyHandle extends Handler {

        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){
                case 0:
                    // add loading viewduring searching
//                    listView.addFooterView(ftView);
                    main.getApi().getGameUserData(getGameId() ,this);
                    break;
                case 1:
                    // Update data adapter and ui
                    Game game = (Game) msg.obj;
                    setUserData(game);
                    isLoading = false;
                    break;
                case 2:
                    // Received all games in library
                    FullGame fg = (FullGame) msg.obj;
                    setData(fg);
                    main.getFirebase().getGameData(fg.id, this);
                    break;
                default:
                    break;
            }
        }
    }

    public class ThreadGetModeData extends Thread{
        @Override
        public void run() {
            // add footer view after load data
            handler.sendEmptyMessage(0);

            // Search more data
//            api.searchGame(editText.getText().toString(), handler);

//            // Delay time to show loading
//            try {
//                Thread.sleep(3000);
//            } catch (InterruptedException e) {
//                e.printStackTrace();
//            }
//
//            // Send the result to handle
//            Message msg = handler.obtainMessage(1, result);
//            handler.sendMessage(msg);

        }
    }

    private String getDate(final long time){
        Date d = new Date(time);
        DateFormat f = new SimpleDateFormat("EEE, MMM d, ''yy");
        return f.format(d);
    }

    private String getDifferenceTime(final long start, final long end){
        // TODO
        long time = end - start;
        Date d = new Date(time);
        DateFormat f = new SimpleDateFormat("d:h:m:s");
        return f.format(d);
    }

    private void save(){
        main.getFirebase().updateGameUser(this.game);
    }

}
