package com.example.luxevista;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.luxevista.R;
import com.example.luxevista.adapters.BookingAdapter;
import com.example.luxevista.models.Booking;
import com.example.luxevista.service.BookingService;

import java.util.List;

public class MyBookingsActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private BookingAdapter adapter;
    private BookingService bookingService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_bookings);

        recyclerView = findViewById(R.id.recyclerViewBookings);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        bookingService = new BookingService(this);
        loadBookings();
    }

    private void loadBookings() {
        List<Booking> bookings = bookingService.getAllBookings();
        adapter = new BookingAdapter(this, bookings);
        recyclerView.setAdapter(adapter);
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadBookings(); // Refresh the list when returning to the activity
    }
}