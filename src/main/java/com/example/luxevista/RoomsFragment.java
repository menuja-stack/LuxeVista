package com.example.luxevista;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.example.luxevista.adapters.RoomAdapter;
import com.example.luxevista.models.Room;
import com.example.luxevista.service.RoomService;

public class RoomsFragment extends Fragment implements RoomAdapter.OnRoomClickListener {

    private RecyclerView recyclerView;
    private RoomAdapter adapter;
    private RoomService roomService;
    private SwipeRefreshLayout swipeRefreshLayout;

    public static RoomsFragment newInstance() {
        return new RoomsFragment();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        roomService = new RoomService(requireContext());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_rooms, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        recyclerView = view.findViewById(R.id.roomsRecyclerView);
        swipeRefreshLayout = view.findViewById(R.id.swipeRefresh);

        setupRecyclerView();
        setupSwipeRefresh();
        loadRooms();
    }

    private void setupRecyclerView() {
        adapter = new RoomAdapter(this);
        recyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));
        recyclerView.setAdapter(adapter);
    }

    private void setupSwipeRefresh() {
        swipeRefreshLayout.setOnRefreshListener(this::loadRooms);
    }

    private void loadRooms() {
        // Load rooms from database
        adapter.setRooms(roomService.getAllRooms());
        swipeRefreshLayout.setRefreshing(false);
    }

    @Override
    public void onRoomClick(Room room) {
        Intent intent = new Intent(requireContext(), SingleRoomActivity.class);
        intent.putExtra("ROOM_ID", room.getId());
        startActivity(intent);
    }
}