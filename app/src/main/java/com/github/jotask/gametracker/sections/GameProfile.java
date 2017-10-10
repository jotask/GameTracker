package com.github.jotask.gametracker.sections;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import com.github.jotask.gametracker.MainActivity;
import com.github.jotask.gametracker.R;
import com.github.jotask.gametracker.utils.LoadImage;

public class GameProfile extends Fragment {

    public enum DATA { ID }

    private String gameID;

    TextView id;
    TextView name;
    ImageView cover;

    public GameProfile() { }

    public static GameProfile newInstance(String gameID) {
        GameProfile fragment = new GameProfile();
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
        this.gameID = getArguments().getString(DATA.ID.name());
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        getActivity().setTitle(getGameID());

        this.id = getView().findViewById(R.id.game_profile_id);
        this.name = getView().findViewById(R.id.game_profile_name);
        this.cover = getView().findViewById(R.id.game_profile_cover);

        ((MainActivity) getActivity()).getApi().getGameData(this);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_game_profile, container, false);
    }

    public String getGameID() { return gameID; }

    public void setProfile(String id, String name, String cover) {
        this.id.setText(id);
        this.name.setText(name);
        new LoadImage(this.cover, "https://" + cover).execute();
    }

}
