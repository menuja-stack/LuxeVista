package com.example.luxevista.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.luxevista.R;
import com.example.luxevista.models.Room;
import com.google.android.material.chip.Chip;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class RoomAdapter extends RecyclerView.Adapter<RoomAdapter.RoomViewHolder> {
    private List<Room> rooms = new ArrayList<>();
    private OnRoomClickListener listener;

    public interface OnRoomClickListener {
        void onRoomClick(Room room);
    }

    public RoomAdapter(OnRoomClickListener listener) {
        this.listener = listener;
    }

    @NonNull
    @Override
    public RoomViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.room_item, parent, false);
        return new RoomViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RoomViewHolder holder, int position) {
        holder.bind(rooms.get(position));
    }

    @Override
    public int getItemCount() {
        return rooms.size();
    }

    public void setRooms(List<Room> rooms) {
        this.rooms = rooms;
        notifyDataSetChanged();
    }

    class RoomViewHolder extends RecyclerView.ViewHolder {
        private ImageView roomImage;
        private TextView roomNumber;
        private TextView roomType;
        private TextView roomPrice;
        private TextView roomDescription;
        private Chip availabilityChip;

        public RoomViewHolder(@NonNull View itemView) {
            super(itemView);
            roomImage = itemView.findViewById(R.id.roomImage);
            roomNumber = itemView.findViewById(R.id.roomNumber);
            roomType = itemView.findViewById(R.id.roomType);
            roomPrice = itemView.findViewById(R.id.roomPrice);
            roomDescription = itemView.findViewById(R.id.roomDescription);
            availabilityChip = itemView.findViewById(R.id.availabilityChip);

            itemView.setOnClickListener(v -> {
                int position = getAdapterPosition();
                if (position != RecyclerView.NO_POSITION && listener != null) {
                    listener.onRoomClick(rooms.get(position));
                }
            });
        }

        void bind(Room room) {
            roomNumber.setText("Room " + room.getRoomNumber());
            roomType.setText(room.getRoomType());
            roomPrice.setText(String.format(Locale.getDefault(), "$%.2f per night", room.getPrice()));
            roomDescription.setText(room.getDescription());

            availabilityChip.setText(room.isAvailable() ? "Available" : "Booked");
            availabilityChip.setChipBackgroundColorResource(
                    room.isAvailable() ? android.R.color.holo_green_light : android.R.color.holo_red_light
            );

            Glide.with(itemView.getContext())
                    .load(room.getImageUrl())
                    .placeholder(R.drawable.placeholder_room)
                    .error(R.drawable.error_room)
                    .centerCrop()
                    .into(roomImage);
        }


    }
}