package com.github.jotask.gametracker.sections;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ListView;
import com.github.jotask.gametracker.MainActivity;
import com.github.jotask.gametracker.R;
import com.github.jotask.gametracker.igdb.ApiSearch;
import com.github.jotask.gametracker.igdb.DataModel;
import com.github.jotask.gametracker.sections.profile.GameUser;
import com.github.jotask.gametracker.utils.adapters.CustomAdapter;
import com.github.jotask.gametracker.utils.adapters.GamesAdapter;

import java.util.ArrayList;

public class Library extends Fragment {

    private ListView listView;

    private CustomAdapter adapter;

    private MainActivity main;

    private ApiSearch api;

    private ArrayList<DataModel> models;

    public Handler handler;
    public View ftView;
    public boolean isLoading = false;

    public boolean isDifferent = false;

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        getActivity().setTitle("Library");

        this.main = (MainActivity) getActivity();
        this.api = main.getApi();

        this.models = new ArrayList<>();

        LayoutInflater li = (LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        ftView = li.inflate(R.layout.footer_view, null);

        this.handler = new Library.MyHandle();

        this.listView = getView().findViewById(R.id.search_result);

        this.adapter = new GamesAdapter(this.getContext(), this.models);
        this.listView.setAdapter(this.adapter);
        this.listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                final Fragment fragment = GameUser.newInstance(view.getTag().toString());
                FragmentTransaction ft = main.getSupportFragmentManager().beginTransaction();
                ft.replace(R.id.content_main, fragment);
                ft.commit();
                ft.addToBackStack(null);
            }
        });

//        this.button.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//
//                if(editText.length() > 3 && isDifferent) {
//                    isDifferent = false;
//                    api.searchGame(editText.getText().toString(), handler);
//                    adapter.clearData();
//                    adapter.addData(models);
//                }
//
//                InputMethodManager inputMethodManager = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
//                inputMethodManager.hideSoftInputFromWindow(getActivity().getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
//
//            }
//        });

        this.listView.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) { }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                // Check when scroll to last item in listview, in this
                if(view.getLastVisiblePosition() == totalItemCount - 1 && !isLoading && listView.getCount() >= 7 && isDifferent){
                    isDifferent = false;
                    isLoading = true;
                    Thread thread = new Library.ThreadGetModeData();
                    thread.start();
                }
            }
        });

        // Load all my library
        main.getFirebase().getAllGamesSubscribed(this.handler);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_library, container, false);
    }

    public class MyHandle extends Handler{
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){
                case 0:
                    // add loading view during searching
                    listView.addFooterView(ftView);
                    break;
                case 1:
                    // Update data adapter and ui
                    adapter.clearData();
                    adapter.addData((ArrayList<DataModel>) msg.obj);
                    listView.removeFooterView(ftView);

//                    int id = 0;
//                    listView.performItemClick(listView.getAdapter().getView(id, null, null), id, listView.getItemIdAtPosition(id));

                    isLoading = false;
                    break;
                case 2:
                    // Received all games in library
                    ArrayList<String> tmp = (ArrayList<String>) msg.obj;
                    main.getApi().searchGamesbyId(tmp, handler);
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

}
